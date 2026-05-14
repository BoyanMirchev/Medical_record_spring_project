package com.example.Medical_record_project_Final.data.service.report;

import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.dto.report.DiagnosisCountReportDto;
import com.example.Medical_record_project_Final.dto.report.DoctorCountReportDto;
import com.example.Medical_record_project_Final.dto.report.MonthSickLeaveReportDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {

    List<Patient> getPatientsByDiagnosis(Integer diagnosisId);

    DiagnosisCountReportDto getMostCommonDiagnosis();

    List<Patient> getPatientsByPersonalDoctor(Integer doctorId);

    BigDecimal getTotalPaidByPatients();

    BigDecimal getTotalPaidByPatientsByDoctor(Integer doctorId);

    List<DoctorCountReportDto> getPatientCountByPersonalDoctor();

    List<DoctorCountReportDto> getVisitCountByDoctor();

    List<Examination> getExaminationsByDoctorAndPeriod(Integer doctorId, LocalDateTime start, LocalDateTime end);

    List<Examination> getExaminationsByPeriod(LocalDateTime start, LocalDateTime end);

    MonthSickLeaveReportDto getMonthWithMostSickLeaves();

    List<DoctorCountReportDto> getDoctorsWithMostSickLeaves();
}