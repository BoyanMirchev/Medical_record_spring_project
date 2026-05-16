package com.example.Medical_record_project_Final.web.api.report;

import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.data.service.report.ReportService;
import com.example.Medical_record_project_Final.dto.report.DiagnosisCountReportDto;
import com.example.Medical_record_project_Final.dto.report.DoctorCountReportDto;
import com.example.Medical_record_project_Final.dto.report.MonthSickLeaveReportDto;
import com.example.Medical_record_project_Final.exception.RestResponseEntityExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportApiController.class)
@Import(RestResponseEntityExceptionHandler.class)
class ReportApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReportService reportService;

    @Test
    void getMostCommonDiagnosisShouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/reports/most-common-diagnosis"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void getMostCommonDiagnosisShouldReturnReport() throws Exception {
        DiagnosisCountReportDto dto = new DiagnosisCountReportDto(1, "Influenza", 5L);
        when(reportService.getMostCommonDiagnosis()).thenReturn(dto);

        mockMvc.perform(get("/api/reports/most-common-diagnosis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diagnosisName").value("Influenza"))
                .andExpect(jsonPath("$.count").value(5));
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void getPatientsByDiagnosisShouldReturnPatients() throws Exception {
        Patient patient = new Patient();
        patient.setId(1);
        patient.setEgn("1234567890");

        when(reportService.getPatientsByDiagnosis(3)).thenReturn(List.of(patient));

        mockMvc.perform(get("/api/reports/patients-by-diagnosis/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].egn").value("1234567890"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getTotalPaidByPatientsShouldReturnSum() throws Exception {
        when(reportService.getTotalPaidByPatients()).thenReturn(new BigDecimal("250.50"));

        mockMvc.perform(get("/api/reports/total-paid-by-patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(250.50));
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void getExaminationsByPeriodShouldReturnList() throws Exception {
        Examination examination = new Examination();
        examination.setId(7);

        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 31, 23, 59);

        when(reportService.getExaminationsByPeriod(start, end)).thenReturn(List.of(examination));

        mockMvc.perform(get("/api/reports/examinations-by-period")
                        .param("start", "2026-01-01T00:00:00")
                        .param("end", "2026-01-31T23:59:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(7));
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void getMonthWithMostSickLeavesShouldReturnReport() throws Exception {
        MonthSickLeaveReportDto dto = new MonthSickLeaveReportDto(3, 4L);
        when(reportService.getMonthWithMostSickLeaves()).thenReturn(dto);

        mockMvc.perform(get("/api/reports/month-with-most-sick-leaves"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.month").value(3))
                .andExpect(jsonPath("$.count").value(4));
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void getVisitCountByDoctorShouldReturnStatistics() throws Exception {
        DoctorCountReportDto dto = new DoctorCountReportDto(1, "Anna Ivanova", 10L);
        when(reportService.getVisitCountByDoctor()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/reports/visit-count-by-doctor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorName").value("Anna Ivanova"))
                .andExpect(jsonPath("$[0].count").value(10));
    }
}
