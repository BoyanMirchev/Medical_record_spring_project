package com.example.Medical_record_project_Final.data.service.impl;

import com.example.Medical_record_project_Final.config.MedicalRecordUserDetails;
import com.example.Medical_record_project_Final.data.entity.Doctor;
import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.data.repo.DoctorRepository;
import com.example.Medical_record_project_Final.data.repo.ExaminationRepository;
import com.example.Medical_record_project_Final.data.repo.PatientRepository;
import com.example.Medical_record_project_Final.data.service.AccessService;
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
    public boolean canAccessPatient(Integer patientId) {
        MedicalRecordUserDetails loggedUser = LoggedUserUtil.getLoggedUser();

        if (loggedUser == null) {
            return false;
        }

        if ("ADMIN".equalsIgnoreCase(loggedUser.getRoleName()) || "DOCTOR".equalsIgnoreCase(loggedUser.getRoleName())) {
            return true;
        }

        if ("PATIENT".equalsIgnoreCase(loggedUser.getRoleName())) {
            Patient patient = patientRepository.findById(patientId).orElse(null);
            return patient != null && patient.getUser().getId().equals(loggedUser.getId());
        }

        return false;
    }

    @Override
    public boolean canAccessExamination(Integer examinationId) {
        MedicalRecordUserDetails loggedUser = LoggedUserUtil.getLoggedUser();

        if (loggedUser == null) {
            return false;
        }

        Examination examination = examinationRepository.findById(examinationId).orElse(null);

        if (examination == null) {
            return false;
        }

        if ("ADMIN".equalsIgnoreCase(loggedUser.getRoleName())) {
            return true;
        }

        if ("DOCTOR".equalsIgnoreCase(loggedUser.getRoleName())) {
            Doctor doctor = doctorRepository.findById(examination.getDoctor().getId()).orElse(null);
            return doctor != null && doctor.getUser().getId().equals(loggedUser.getId());
        }

        if ("PATIENT".equalsIgnoreCase(loggedUser.getRoleName())) {
            return examination.getPatient().getUser().getId().equals(loggedUser.getId());
        }

        return false;
    }

    @Override
    public boolean canEditExamination(Integer examinationId) {
        MedicalRecordUserDetails loggedUser = LoggedUserUtil.getLoggedUser();

        if (loggedUser == null) {
            return false;
        }

        Examination examination = examinationRepository.findById(examinationId).orElse(null);

        if (examination == null) {
            return false;
        }

        if ("ADMIN".equalsIgnoreCase(loggedUser.getRoleName())) {
            return true;
        }

        if ("DOCTOR".equalsIgnoreCase(loggedUser.getRoleName())) {
            return examination.getDoctor().getUser().getId().equals(loggedUser.getId());
        }

        return false;
    }

    @Override
    public boolean isAdmin() {
        MedicalRecordUserDetails loggedUser = LoggedUserUtil.getLoggedUser();
        return loggedUser != null && "ADMIN".equalsIgnoreCase(loggedUser.getRoleName());
    }
}