package com.example.Medical_record_project_Final.data.service;

import com.example.Medical_record_project_Final.data.entity.InsuranceRecord;

import java.time.LocalDate;
import java.util.List;

public interface InsuranceRecordService {

    InsuranceRecord create(InsuranceRecord insuranceRecord);

    InsuranceRecord update(Integer id, InsuranceRecord insuranceRecord);

    void deleteById(Integer id);

    InsuranceRecord getById(Integer id);

    List<InsuranceRecord> getAll();

    List<InsuranceRecord> getByPatient(Integer patientId);

    InsuranceRecord getByPatientAndMonth(Integer patientId, LocalDate recordMonth);

    boolean isPatientInsuredForMonth(Integer patientId, LocalDate recordMonth);
}