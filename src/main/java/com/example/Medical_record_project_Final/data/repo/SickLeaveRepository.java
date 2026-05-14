package com.example.Medical_record_project_Final.data.repo;

import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.SickLeave;
import com.example.Medical_record_project_Final.dto.report.DoctorCountReportDto;
import com.example.Medical_record_project_Final.dto.report.MonthSickLeaveReportDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SickLeaveRepository extends JpaRepository<SickLeave, Integer> {

    Optional<SickLeave> findByExamination(Examination examination);

    List<SickLeave> findAllByStartDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("""
        SELECT new com.example.Medical_record_project_Final.dto.report.MonthSickLeaveReportDto(
            MONTH(s.startDate),
            COUNT(s.id)
        )
        FROM SickLeave s
        GROUP BY MONTH(s.startDate)
        ORDER BY COUNT(s.id) DESC
        """)
    List<MonthSickLeaveReportDto> findSickLeaveCountByMonth();

    @Query("""
        SELECT new com.example.Medical_record_project_Final.dto.report.DoctorCountReportDto(
            d.id,
            CONCAT(d.firstName, ' ', d.lastName),
            COUNT(s.id)
        )
        FROM SickLeave s
        JOIN s.examination e
        JOIN e.doctor d
        GROUP BY d.id, d.firstName, d.lastName
        ORDER BY COUNT(s.id) DESC
        """)
    List<DoctorCountReportDto> findSickLeaveCountByDoctor();
}