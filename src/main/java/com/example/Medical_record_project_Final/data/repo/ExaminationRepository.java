package com.example.Medical_record_project_Final.data.repo;

import com.example.Medical_record_project_Final.data.entity.Diagnosis;
import com.example.Medical_record_project_Final.data.entity.Doctor;
import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.data.entity.enums.PaidBy;
import com.example.Medical_record_project_Final.dto.report.DiagnosisCountReportDto;
import com.example.Medical_record_project_Final.dto.report.DoctorCountReportDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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

    @Query("""
        SELECT new com.example.Medical_record_project_Final.dto.report.DiagnosisCountReportDto(
            d.id,
            d.name,
            COUNT(e.id)
        )
        FROM Examination e
        JOIN e.diagnosis d
        GROUP BY d.id, d.name
        ORDER BY COUNT(e.id) DESC
        """)
    List<DiagnosisCountReportDto> findDiagnosisStatistics();

    @Query("""
        SELECT new com.example.Medical_record_project_Final.dto.report.DoctorCountReportDto(
            d.id,
            CONCAT(d.firstName, ' ', d.lastName),
            COUNT(e.id)
        )
        FROM Examination e
        JOIN e.doctor d
        GROUP BY d.id, d.firstName, d.lastName
        ORDER BY COUNT(e.id) DESC
        """)
    List<DoctorCountReportDto> findDoctorVisitStatistics();

    @Query("""
        SELECT COALESCE(SUM(e.price), 0)
        FROM Examination e
        WHERE e.paidBy = com.example.Medical_record_project_Final.data.entity.enums.PaidBy.PATIENT
        """)
    BigDecimal getTotalPaidByPatients();

    @Query("""
        SELECT COALESCE(SUM(e.price), 0)
        FROM Examination e
        WHERE e.paidBy = com.example.Medical_record_project_Final.data.entity.enums.PaidBy.PATIENT
        AND e.doctor.id = :doctorId
        """)
    BigDecimal getTotalPaidByPatientsByDoctor(Integer doctorId);

    List<Examination> findAllByDoctorIdAndExamDateBetween(
            Integer doctorId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<Examination> findAllByDiagnosisId(Integer diagnosisId);


}