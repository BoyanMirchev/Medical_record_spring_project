package com.example.Medical_record_project_Final.data.service.impl;

import com.example.Medical_record_project_Final.config.MedicalRecordUserDetails;
import com.example.Medical_record_project_Final.data.entity.Doctor;
import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.data.repo.DoctorRepository;
import com.example.Medical_record_project_Final.data.repo.ExaminationRepository;
import com.example.Medical_record_project_Final.data.repo.PatientRepository;
import com.example.Medical_record_project_Final.data.service.AccessService;
import com.example.Medical_record_project_Final.exception.AccessDeniedException;
import com.example.Medical_record_project_Final.exception.DoctorExaminationAccessDeniedException;
import com.example.Medical_record_project_Final.util.LoggedUserUtil;
import org.springframework.stereotype.Service;

@Service
public class AccessServiceImpl implements AccessService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ExaminationRepository examinationRepository;

    public AccessServiceImpl(PatientRepository patientRepository,
                             DoctorRepository doctorRepository,
                             ExaminationRepository examinationRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.examinationRepository = examinationRepository;
    }

    @Override
    public boolean isAdmin() {
        MedicalRecordUserDetails user = LoggedUserUtil.getLoggedUser();
        return user != null && "ADMIN".equalsIgnoreCase(user.getRoleName());
    }

    @Override
    public boolean isDoctor() {
        MedicalRecordUserDetails user = LoggedUserUtil.getLoggedUser();
        return user != null && "DOCTOR".equalsIgnoreCase(user.getRoleName());
    }

    @Override
    public boolean isPatient() {
        MedicalRecordUserDetails user = LoggedUserUtil.getLoggedUser();
        return user != null && "PATIENT".equalsIgnoreCase(user.getRoleName());
    }

    @Override
    public Integer getLoggedUserId() {
        MedicalRecordUserDetails user = LoggedUserUtil.getLoggedUser();
        return user != null ? user.getId() : null;
    }

    @Override
    public boolean canAccessPatient(Integer patientId) {
        if (isAdmin() || isDoctor()) {
            return true;
        }

        if (isPatient()) {
            Integer loggedUserId = getLoggedUserId();

            return patientRepository.findById(patientId)
                    .map(patient -> patient.getUser().getId().equals(loggedUserId))
                    .orElse(false);
        }

        return false;
    }

    @Override
    public boolean canAccessExamination(Integer examinationId) {
        if (isAdmin() || isDoctor()) {
            return true;
        }

        if (isPatient()) {
            Integer loggedUserId = getLoggedUserId();

            return examinationRepository.findById(examinationId)
                    .map(exam -> exam.getPatient().getUser().getId().equals(loggedUserId))
                    .orElse(false);
        }

        return false;
    }

    @Override
    public boolean canEditExamination(Integer examinationId) {
        if (isAdmin()) {
            return true;
        }

        if (isDoctor()) {
            Integer loggedUserId = getLoggedUserId();

            return examinationRepository.findById(examinationId)
                    .map(exam -> exam.getDoctor().getUser().getId().equals(loggedUserId))
                    .orElse(false);
        }

        return false;
    }

    @Override
    public void checkCanAccessPatient(Integer patientId) {
        if (!canAccessPatient(patientId)) {
            throw new AccessDeniedException("You do not have access to this patient.");
        }
    }

    @Override
    public void checkCanAccessExamination(Integer examinationId) {
        if (!canAccessExamination(examinationId)) {
            throw new AccessDeniedException("You do not have access to this examination.");
        }
    }

    @Override
    public void checkCanEditExamination(Integer examinationId) {
        if (!canEditExamination(examinationId)) {
            throw new DoctorExaminationAccessDeniedException();
        }
    }
}
