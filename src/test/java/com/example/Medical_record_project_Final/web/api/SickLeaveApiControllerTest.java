package com.example.Medical_record_project_Final.web.api;

import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.SickLeave;
import com.example.Medical_record_project_Final.data.service.ExaminationService;
import com.example.Medical_record_project_Final.data.service.SickLeaveService;
import com.example.Medical_record_project_Final.exception.RestResponseEntityExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SickLeaveApiController.class)
@Import(RestResponseEntityExceptionHandler.class)
class SickLeaveApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SickLeaveService sickLeaveService;

    @MockitoBean
    private ExaminationService examinationService;

    @Test
    void getAllShouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/sick-leaves"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void getAllShouldReturnSickLeaves() throws Exception {
        SickLeave sickLeave = new SickLeave();
        sickLeave.setId(1);
        sickLeave.setStartDate(LocalDate.of(2026, 1, 11));
        sickLeave.setDaysCount(5);

        when(sickLeaveService.getAll()).thenReturn(List.of(sickLeave));

        mockMvc.perform(get("/api/sick-leaves"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].daysCount").value(5));
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void getByExaminationShouldReturnSickLeave() throws Exception {
        SickLeave sickLeave = new SickLeave();
        sickLeave.setId(2);

        when(sickLeaveService.getByExamination(10)).thenReturn(sickLeave);

        mockMvc.perform(get("/api/sick-leaves/examination/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void createShouldReturnCreated() throws Exception {
        Examination examination = new Examination();
        examination.setId(10);

        SickLeave saved = new SickLeave();
        saved.setId(1);
        saved.setStartDate(LocalDate.of(2026, 2, 1));
        saved.setDaysCount(3);

        when(examinationService.getById(10)).thenReturn(examination);
        when(sickLeaveService.create(any())).thenReturn(saved);

        String body = """
                {"examinationId":10,"startDate":"2026-02-01","daysCount":3}
                """;

        mockMvc.perform(post("/api/sick-leaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.daysCount").value(3));
    }

    @Test
    @WithMockUser(roles = "PATIENT")
    void createShouldReturnForbiddenForPatient() throws Exception {
        String body = """
                {"examinationId":10,"startDate":"2026-02-01","daysCount":3}
                """;

        mockMvc.perform(post("/api/sick-leaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/sick-leaves/1"))
                .andExpect(status().isNoContent());

        verify(sickLeaveService).deleteById(1);
    }
}
