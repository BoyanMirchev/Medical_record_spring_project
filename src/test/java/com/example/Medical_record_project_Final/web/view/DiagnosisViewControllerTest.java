package com.example.Medical_record_project_Final.web.view;

import com.example.Medical_record_project_Final.data.entity.Diagnosis;
import com.example.Medical_record_project_Final.data.service.DiagnosisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DiagnosisViewController.class)
class DiagnosisViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DiagnosisService diagnosisService;

    @Test
    void getAllShouldRedirectToLoginWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/diagnoses"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void getAllShouldReturnListView() throws Exception {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setId(1);
        diagnosis.setCode("J10");
        diagnosis.setName("Influenza");

        when(diagnosisService.getAll()).thenReturn(List.of(diagnosis));

        mockMvc.perform(get("/diagnoses"))
                .andExpect(status().isOk())
                .andExpect(view().name("diagnosis/list"))
                .andExpect(model().attributeExists("diagnoses"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createFormShouldReturnCreateView() throws Exception {
        mockMvc.perform(get("/diagnoses/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("diagnosis/create"))
                .andExpect(model().attributeExists("diagnosisCreateDto"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void editFormShouldReturnEditViewWithDto() throws Exception {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setId(1);
        diagnosis.setCode("J10");
        diagnosis.setName("Influenza");
        diagnosis.setDescription("Seasonal");

        when(diagnosisService.getById(1)).thenReturn(diagnosis);

        mockMvc.perform(get("/diagnoses/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("diagnosis/edit"))
                .andExpect(model().attributeExists("diagnosisEditDto"));
    }
}
