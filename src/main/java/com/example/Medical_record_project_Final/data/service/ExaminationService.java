package com.example.Medical_record_project_Final.data.service;

import com.example.Medical_record_project_Final.data.entity.Examination;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ExaminationService {

    Examination create(Examination examination);

    Examination update(Integer id, Examination examination);

    void deleteById(Integer id);

    Examination getById(Integer id);

    List<Examination> getAll();

    List<Examination> getByDoctor(Integer doctorId);

    List<Examination> getByPatient(Integer patientId);

    List<Examination> getByDiagnosis(Integer diagnosisId);

    List<Examination> getByPeriod(LocalDateTime startDate, LocalDateTime endDate);

    List<Examination> getByDoctorAndPeriod(Integer doctorId, LocalDateTime startDate, LocalDateTime endDate);

    BigDecimal getTotalPaidByPatients();

    BigDecimal getTotalPaidByPatientsByDoctor(Integer doctorId);
}