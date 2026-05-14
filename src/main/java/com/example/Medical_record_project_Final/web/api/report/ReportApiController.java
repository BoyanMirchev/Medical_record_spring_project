package com.example.Medical_record_project_Final.web.api.report;

import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.data.service.report.ReportService;
import com.example.Medical_record_project_Final.dto.report.DiagnosisCountReportDto;
import com.example.Medical_record_project_Final.dto.report.DoctorCountReportDto;
import com.example.Medical_record_project_Final.dto.report.MonthSickLeaveReportDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportApiController {

    private final ReportService reportService;

    public ReportApiController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/patients-by-diagnosis/{diagnosisId}")
    public List<Patient> getPatientsByDiagnosis(@PathVariable Integer diagnosisId) {
        return reportService.getPatientsByDiagnosis(diagnosisId);
    }

    @GetMapping("/most-common-diagnosis")
    public DiagnosisCountReportDto getMostCommonDiagnosis() {
        return reportService.getMostCommonDiagnosis();
    }

    @GetMapping("/patients-by-personal-doctor/{doctorId}")
    public List<Patient> getPatientsByPersonalDoctor(@PathVariable Integer doctorId) {
        return reportService.getPatientsByPersonalDoctor(doctorId);
    }

    @GetMapping("/total-paid-by-patients")
    public BigDecimal getTotalPaidByPatients() {
        return reportService.getTotalPaidByPatients();
    }

    @GetMapping("/total-paid-by-patients/doctor/{doctorId}")
    public BigDecimal getTotalPaidByPatientsByDoctor(@PathVariable Integer doctorId) {
        return reportService.getTotalPaidByPatientsByDoctor(doctorId);
    }

    @GetMapping("/patient-count-by-personal-doctor")
    public List<DoctorCountReportDto> getPatientCountByPersonalDoctor() {
        return reportService.getPatientCountByPersonalDoctor();
    }

    @GetMapping("/visit-count-by-doctor")
    public List<DoctorCountReportDto> getVisitCountByDoctor() {
        return reportService.getVisitCountByDoctor();
    }

    @GetMapping("/examinations-by-period")
    public List<Examination> getExaminationsByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return reportService.getExaminationsByPeriod(start, end);
    }

    @GetMapping("/examinations-by-doctor/{doctorId}")
    public List<Examination> getExaminationsByDoctorAndPeriod(
            @PathVariable Integer doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return reportService.getExaminationsByDoctorAndPeriod(doctorId, start, end);
    }

    @GetMapping("/month-with-most-sick-leaves")
    public MonthSickLeaveReportDto getMonthWithMostSickLeaves() {
        return reportService.getMonthWithMostSickLeaves();
    }

    @GetMapping("/doctors-with-most-sick-leaves")
    public List<DoctorCountReportDto> getDoctorsWithMostSickLeaves() {
        return reportService.getDoctorsWithMostSickLeaves();
    }
}