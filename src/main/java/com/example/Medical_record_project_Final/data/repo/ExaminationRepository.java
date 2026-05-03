package com.example.Medical_record_project_Final.data.repo;

import com.example.Medical_record_project_Final.data.entity.Diagnosis;
import com.example.Medical_record_project_Final.data.entity.Doctor;
import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.data.entity.enums.PaidBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExaminationRepository extends JpaRepository<Examination, Integer> {

    List<Examination> findAllByDoctor(Doctor doctor);

    List<Examination> findAllByPatient(Patient patient);

    List<Examination> findAllByDiagnosis(Diagnosis diagnosis);

    List<Examination> findAllByDoctorAndExamDateBetween(
            Doctor doctor,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<Examination> findAllByPatientAndExamDateBetween(
            Patient patient,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<Examination> findAllByExamDateBetween(
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<Examination> findAllByPaidBy(PaidBy paidBy);

    long countByDoctor(Doctor doctor);

    long countByPatient(Patient patient);
}