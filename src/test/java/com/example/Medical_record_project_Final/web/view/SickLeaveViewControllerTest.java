package com.example.Medical_record_project_Final.web.view;

import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.SickLeave;
import com.example.Medical_record_project_Final.data.service.ExaminationService;
import com.example.Medical_record_project_Final.data.service.SickLeaveService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SickLeaveViewController.class)
class SickLeaveViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SickLeaveService sickLeaveService;

    @MockitoBean
    private ExaminationService examinationService;

    @Test
    void getAllShouldRedirectToLoginWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/sick-leaves"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void getAllShouldReturnListView() throws Exception {
        SickLeave sickLeave = new SickLeave();
        sickLeave.setId(1);
        sickLeave.setDaysCount(5);

        when(sickLeaveService.getAll()).thenReturn(List.of(sickLeave));

        mockMvc.perform(get("/sick-leaves"))
                .andExpect(status().isOk())
                .andExpect(view().name("sick-leave/list"))
                .andExpect(model().attributeExists("sickLeaves"));
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void createFormShouldReturnCreateView() throws Exception {
        when(examinationService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/sick-leaves/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("sick-leave/create"))
                .andExpect(model().attributeExists("sickLeaveCreateDto", "examinations"));
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void editFormShouldReturnEditViewWithDto() throws Exception {
        Examination examination = new Examination();
        examination.setId(10);

        SickLeave sickLeave = new SickLeave();
        sickLeave.setId(1);
        sickLeave.setExamination(examination);
        sickLeave.setStartDate(LocalDate.of(2026, 2, 1));
        sickLeave.setDaysCount(3);

        when(sickLeaveService.getById(1)).thenReturn(sickLeave);
        when(examinationService.getAll()).thenReturn(List.of(examination));

        mockMvc.perform(get("/sick-leaves/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("sick-leave/edit"))
                .andExpect(model().attributeExists("sickLeaveEditDto", "examinations"));
    }
}
