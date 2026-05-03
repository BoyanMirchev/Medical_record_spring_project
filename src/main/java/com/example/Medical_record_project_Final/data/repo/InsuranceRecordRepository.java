package com.example.Medical_record_project_Final.data.repo;

import com.example.Medical_record_project_Final.data.entity.InsuranceRecord;
import com.example.Medical_record_project_Final.data.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InsuranceRecordRepository extends JpaRepository<InsuranceRecord, Integer> {

    List<InsuranceRecord> findAllByPatient(Patient patient);

    List<InsuranceRecord> findAllByPatientOrderByRecordMonthDesc(Patient patient);

    Optional<InsuranceRecord> findByPatientAndRecordMonth(Patient patient, LocalDate recordMonth);

    boolean existsByPatientAndRecordMonth(Patient patient, LocalDate recordMonth);
}