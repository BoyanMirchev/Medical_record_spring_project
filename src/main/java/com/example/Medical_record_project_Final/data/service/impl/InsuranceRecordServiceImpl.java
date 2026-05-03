package com.example.Medical_record_project_Final.data.service.impl;

import com.example.Medical_record_project_Final.data.entity.InsuranceRecord;
import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.data.repo.InsuranceRecordRepository;
import com.example.Medical_record_project_Final.data.repo.PatientRepository;
import com.example.Medical_record_project_Final.data.service.InsuranceRecordService;
import com.example.Medical_record_project_Final.exception.DuplicateEntityException;
import com.example.Medical_record_project_Final.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InsuranceRecordServiceImpl implements InsuranceRecordService {

    private final InsuranceRecordRepository insuranceRecordRepository;
    private final PatientRepository patientRepository;

    public InsuranceRecordServiceImpl(InsuranceRecordRepository insuranceRecordRepository,
                                      PatientRepository patientRepository) {
        this.insuranceRecordRepository = insuranceRecordRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public InsuranceRecord create(InsuranceRecord insuranceRecord) {
        validatePatient(insuranceRecord);

        if (insuranceRecordRepository.existsByPatientAndRecordMonth(
                insuranceRecord.getPatient(),
                insuranceRecord.getRecordMonth())) {
            throw new DuplicateEntityException("Insurance record for this patient and month already exists.");
        }

        return insuranceRecordRepository.save(insuranceRecord);
    }

    @Override
    public InsuranceRecord update(Integer id, InsuranceRecord insuranceRecord) {
        InsuranceRecord existing = getById(id);

        validatePatient(insuranceRecord);

        boolean monthChanged = !existing.getRecordMonth().equals(insuranceRecord.getRecordMonth());
        boolean patientChanged = !existing.getPatient().getId().equals(insuranceRecord.getPatient().getId());

        if ((monthChanged || patientChanged) &&
                insuranceRecordRepository.existsByPatientAndRecordMonth(
                        insuranceRecord.getPatient(),
                        insuranceRecord.getRecordMonth())) {
            throw new DuplicateEntityException("Insurance record for this patient and month already exists.");
        }

        existing.setPatient(insuranceRecord.getPatient());
        existing.setRecordMonth(insuranceRecord.getRecordMonth());
        existing.setInsured(insuranceRecord.isInsured());
        existing.setNote(insuranceRecord.getNote());

        return insuranceRecordRepository.save(existing);
    }

    @Override
    public void deleteById(Integer id) {
        InsuranceRecord insuranceRecord = getById(id);
        insuranceRecordRepository.delete(insuranceRecord);
    }

    @Override
    public InsuranceRecord getById(Integer id) {
        return insuranceRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Insurance record with id " + id + " not found."));
    }

    @Override
    public List<InsuranceRecord> getAll() {
        return insuranceRecordRepository.findAll();
    }

    @Override
    public List<InsuranceRecord> getByPatient(Integer patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient with id " + patientId + " not found."));
        return insuranceRecordRepository.findAllByPatientOrderByRecordMonthDesc(patient);
    }

    @Override
    public InsuranceRecord getByPatientAndMonth(Integer patientId, LocalDate recordMonth) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient with id " + patientId + " not found."));

        return insuranceRecordRepository.findByPatientAndRecordMonth(patient, recordMonth)
                .orElseThrow(() -> new EntityNotFoundException("Insurance record not found for patient id " + patientId + " and month " + recordMonth + "."));
    }

    @Override
    public boolean isPatientInsuredForMonth(Integer patientId, LocalDate recordMonth) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient with id " + patientId + " not found."));

        return insuranceRecordRepository.findByPatientAndRecordMonth(patient, recordMonth)
                .map(InsuranceRecord::isInsured)
                .orElse(false);
    }

    private void validatePatient(InsuranceRecord insuranceRecord) {
        if (insuranceRecord.getPatient() == null || insuranceRecord.getPatient().getId() == null) {
            throw new EntityNotFoundException("Patient is required.");
        }

        patientRepository.findById(insuranceRecord.getPatient().getId())
                .orElseThrow(() -> new EntityNotFoundException("Patient with id " + insuranceRecord.getPatient().getId() + " not found."));
    }
}