package com.example.Medical_record_project_Final.data.repo;

import com.example.Medical_record_project_Final.data.entity.Doctor;
import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.dto.report.DoctorCountReportDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    Optional<Patient> findByEgn(String egn);

    boolean existsByEgn(String egn);

    List<Patient> findAllByPersonalDoctor(Doctor personalDoctor);

    long countByPersonalDoctor(Doctor personalDoctor);

    List<Patient> findAllByPersonalDoctorId(Integer doctorId);

    @Query("""
        SELECT new com.example.Medical_record_project_Final.dto.report.DoctorCountReportDto(
            d.id,
            CONCAT(d.firstName, ' ', d.lastName),
            COUNT(p.id)
        )
        FROM Patient p
        JOIN p.personalDoctor d
        GROUP BY d.id, d.firstName, d.lastName
        ORDER BY COUNT(p.id) DESC
        """)
    List<DoctorCountReportDto> findPatientCountByPersonalDoctor();
}