package com.example.Medical_record_project_Final.data.service.impl;

import com.example.Medical_record_project_Final.data.entity.Diagnosis;
import com.example.Medical_record_project_Final.data.entity.Doctor;
import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.data.entity.enums.PaidBy;
import com.example.Medical_record_project_Final.data.repo.DiagnosisRepository;
import com.example.Medical_record_project_Final.data.repo.DoctorRepository;
import com.example.Medical_record_project_Final.data.repo.ExaminationRepository;
import com.example.Medical_record_project_Final.data.repo.PatientRepository;
import com.example.Medical_record_project_Final.data.service.ExaminationService;
import com.example.Medical_record_project_Final.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExaminationServiceImpl implements ExaminationService {

    private final ExaminationRepository examinationRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DiagnosisRepository diagnosisRepository;

    public ExaminationServiceImpl(ExaminationRepository examinationRepository,
                                  DoctorRepository doctorRepository,
                                  PatientRepository patientRepository,
                                  DiagnosisRepository diagnosisRepository) {
        this.examinationRepository = examinationRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.diagnosisRepository = diagnosisRepository;
    }

    @Override
    public Examination create(Examination examination) {
        validateRelations(examination);
        return examinationRepository.save(examination);
    }

    @Override
    public Examination update(Integer id, Examination examination) {
        Examination existing = getById(id);

        validateRelations(examination);

        existing.setExamDate(examination.getExamDate());
        existing.setDoctor(examination.getDoctor());
        existing.setPatient(examination.getPatient());
        existing.setDiagnosis(examination.getDiagnosis());
        existing.setTreatmentText(examination.getTreatmentText());
        existing.setPrice(examination.getPrice());
        existing.setPaidBy(examination.getPaidBy());
        existing.setNotes(examination.getNotes());

        return examinationRepository.save(existing);
    }

    @Override
    public void deleteById(Integer id) {
        Examination examination = getById(id);
        examinationRepository.delete(examination);
    }

    @Override
    public Examination getById(Integer id) {
        return examinationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Examination with id " + id + " not found."));
    }

    @Override
    public List<Examination> getAll() {
        return examinationRepository.findAll();
    }

    @Override
    public List<Examination> getByDoctor(Integer doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor with id " + doctorId + " not found."));
        return examinationRepository.findAllByDoctor(doctor);
    }

    @Override
    public List<Examination> getByPatient(Integer patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient with id " + patientId + " not found."));
        return examinationRepository.findAllByPatient(patient);
    }

    @Override
    public List<Examination> getByDiagnosis(Integer diagnosisId) {
        Diagnosis diagnosis = diagnosisRepository.findById(diagnosisId)
                .orElseThrow(() -> new EntityNotFoundException("Diagnosis with id " + diagnosisId + " not found."));
        return examinationRepository.findAllByDiagnosis(diagnosis);
    }

    @Override
    public List<Examination> getByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return examinationRepository.findAllByExamDateBetween(startDate, endDate);
    }

    @Override
    public List<Examination> getByDoctorAndPeriod(Integer doctorId, LocalDateTime startDate, LocalDateTime endDate) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor with id " + doctorId + " not found."));
        return examinationRepository.findAllByDoctorAndExamDateBetween(doctor, startDate, endDate);
    }

    @Override
    public BigDecimal getTotalPaidByPatients() {
        return examinationRepository.findAllByPaidBy(PaidBy.PATIENT)
                .stream()
                .map(Examination::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalPaidByPatientsByDoctor(Integer doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor with id " + doctorId + " not found."));

        return examinationRepository.findAllByDoctor(doctor)
                .stream()
                .filter(examination -> examination.getPaidBy() == PaidBy.PATIENT)
                .map(Examination::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateRelations(Examination examination) {
        if (examination.getDoctor() == null || examination.getDoctor().getId() == null) {
            throw new EntityNotFoundException("Doctor is required.");
        }

        if (examination.getPatient() == null || examination.getPatient().getId() == null) {
            throw new EntityNotFoundException("Patient is required.");
        }

        if (examination.getDiagnosis() == null || examination.getDiagnosis().getId() == null) {
            throw new EntityNotFoundException("Diagnosis is required.");
        }

        doctorRepository.findById(examination.getDoctor().getId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor with id " + examination.getDoctor().getId() + " not found."));

        patientRepository.findById(examination.getPatient().getId())
                .orElseThrow(() -> new EntityNotFoundException("Patient with id " + examination.getPatient().getId() + " not found."));

        diagnosisRepository.findById(examination.getDiagnosis().getId())
                .orElseThrow(() -> new EntityNotFoundException("Diagnosis with id " + examination.getDiagnosis().getId() + " not found."));
    }
}