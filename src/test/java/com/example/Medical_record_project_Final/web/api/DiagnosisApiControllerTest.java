package com.example.Medical_record_project_Final.web.api;

import com.example.Medical_record_project_Final.data.entity.Diagnosis;
import com.example.Medical_record_project_Final.data.service.DiagnosisService;
import com.example.Medical_record_project_Final.exception.EntityNotFoundException;
import com.example.Medical_record_project_Final.exception.RestResponseEntityExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DiagnosisApiController.class)
@Import(RestResponseEntityExceptionHandler.class)
class DiagnosisApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DiagnosisService diagnosisService;

    @Test
    void getAllShouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/diagnoses"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "PATIENT")
    void getAllShouldReturnDiagnosesWhenAuthenticated() throws Exception {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setId(1);
        diagnosis.setCode("J10");
        diagnosis.setName("Influenza");

        when(diagnosisService.getAll()).thenReturn(List.of(diagnosis));

        mockMvc.perform(get("/api/diagnoses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("J10"))
                .andExpect(jsonPath("$[0].name").value("Influenza"));
    }

    @Test
    @WithMockUser(roles = "PATIENT")
    void getByIdShouldReturnDiagnosis() throws Exception {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setId(1);
        diagnosis.setCode("J00");
        diagnosis.setName("Cold");

        when(diagnosisService.getById(1)).thenReturn(diagnosis);

        mockMvc.perform(get("/api/diagnoses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("J00"));
    }

    @Test
    @WithMockUser(roles = "PATIENT")
    void createShouldReturnForbiddenForNonAdmin() throws Exception {
        String body = """
                {"code":"J11","name":"New diagnosis","description":"Test"}
                """;

        mockMvc.perform(post("/api/diagnoses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());

        verify(diagnosisService, never()).create(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createShouldReturnCreatedWhenAdmin() throws Exception {
        Diagnosis saved = new Diagnosis();
        saved.setId(5);
        saved.setCode("J11");
        saved.setName("New diagnosis");

        when(diagnosisService.create(any())).thenReturn(saved);

        String body = """
                {"code":"J11","name":"New diagnosis","description":"Test"}
                """;

        mockMvc.perform(post("/api/diagnoses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.code").value("J11"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateShouldReturnUpdatedDiagnosis() throws Exception {
        Diagnosis existing = new Diagnosis();
        existing.setId(1);
        existing.setCode("J10");
        existing.setName("Old");

        Diagnosis updated = new Diagnosis();
        updated.setId(1);
        updated.setCode("J10");
        updated.setName("Updated");

        when(diagnosisService.getById(1)).thenReturn(existing);
        when(diagnosisService.update(eq(1), any())).thenReturn(updated);

        String body = """
                {"id":1,"code":"J10","name":"Updated","description":"Desc"}
                """;

        mockMvc.perform(put("/api/diagnoses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/diagnoses/1"))
                .andExpect(status().isNoContent());

        verify(diagnosisService).deleteById(1);
    }

    @Test
    @WithMockUser(roles = "PATIENT")
    void getByIdShouldReturnNotFoundWhenMissing() throws Exception {
        when(diagnosisService.getById(99)).thenThrow(new EntityNotFoundException("Not found"));

        mockMvc.perform(get("/api/diagnoses/99"))
                .andExpect(status().isNotFound());
    }
}
