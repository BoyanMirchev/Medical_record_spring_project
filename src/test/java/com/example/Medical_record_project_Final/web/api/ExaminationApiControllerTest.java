package com.example.Medical_record_project_Final.web.api;

import com.example.Medical_record_project_Final.data.entity.*;
import com.example.Medical_record_project_Final.data.entity.enums.PaidBy;
import com.example.Medical_record_project_Final.data.service.*;
import com.example.Medical_record_project_Final.exception.RestResponseEntityExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExaminationApiController.class)
@Import(RestResponseEntityExceptionHandler.class)
class ExaminationApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExaminationService examinationService;

    @MockitoBean
    private DoctorService doctorService;

    @MockitoBean
    private PatientService patientService;

    @MockitoBean
    private DiagnosisService diagnosisService;

    @MockitoBean
    private AccessService accessService;

    @Test
    @WithMockUser(roles = "DOCTOR")
    void getAllShouldReturnExaminations() throws Exception {
        Examination examination = new Examination();
        examination.setId(1);
        examination.setExamDate(LocalDateTime.of(2026, 1, 10, 9, 0));
        examination.setPrice(BigDecimal.TEN);

        when(examinationService.getAll()).thenReturn(List.of(examination));

        mockMvc.perform(get("/api/examinations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @WithMockUser(roles = "PATIENT")
    void getByIdShouldCheckAccess() throws Exception {
        Examination examination = new Examination();
        examination.setId(1);

        doNothing().when(accessService).checkCanAccessExamination(1);
        when(examinationService.getById(1)).thenReturn(examination);

        mockMvc.perform(get("/api/examinations/1"))
                .andExpect(status().isOk());

        verify(accessService).checkCanAccessExamination(1);
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void deleteShouldReturnForbiddenWhenNotAdmin() throws Exception {
        when(accessService.isAdmin()).thenReturn(false);

        mockMvc.perform(delete("/api/examinations/1"))
                .andExpect(status().isForbidden());

        verify(examinationService, never()).deleteById(any());
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void getTotalPaidByPatientsShouldReturnAmount() throws Exception {
        when(examinationService.getTotalPaidByPatients()).thenReturn(new BigDecimal("100.00"));

        mockMvc.perform(get("/api/examinations/paid-by-patients/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(100.00));
    }
}
