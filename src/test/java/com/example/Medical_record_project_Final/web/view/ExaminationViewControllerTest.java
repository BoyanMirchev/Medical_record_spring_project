package com.example.Medical_record_project_Final.web.view;

import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.data.entity.enums.PaidBy;
import com.example.Medical_record_project_Final.data.repo.PatientRepository;
import com.example.Medical_record_project_Final.data.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExaminationViewController.class)
class ExaminationViewControllerTest {

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

    @MockitoBean
    private PatientRepository patientRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllShouldReturnAllExaminationsForAdmin() throws Exception {
        when(accessService.isPatient()).thenReturn(false);
        when(examinationService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/examinations"))
                .andExpect(status().isOk())
                .andExpect(view().name("examination/list"));

        verify(examinationService).getAll();
    }

    @Test
    @WithMockUser(roles = "PATIENT")
    void getAllShouldReturnPatientExaminationsForPatientRole() throws Exception {
        Patient patient = new Patient();
        patient.setId(4);

        Examination examination = new Examination();
        examination.setId(1);

        when(accessService.isPatient()).thenReturn(true);
        when(accessService.getLoggedUserId()).thenReturn(50);
        when(patientRepository.findByUserId(50)).thenReturn(Optional.of(patient));
        when(examinationService.getByPatient(4)).thenReturn(List.of(examination));

        mockMvc.perform(get("/examinations"))
                .andExpect(status().isOk())
                .andExpect(view().name("examination/list"));

        verify(examinationService).getByPatient(4);
        verify(examinationService, never()).getAll();
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void detailsShouldReturnDetailsView() throws Exception {
        Examination examination = new Examination();
        examination.setId(1);

        doNothing().when(accessService).checkCanAccessExamination(1);
        when(examinationService.getById(1)).thenReturn(examination);

        mockMvc.perform(get("/examinations/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("examination/details"))
                .andExpect(model().attributeExists("examination"));
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void createFormShouldReturnCreateView() throws Exception {
        when(doctorService.getAll()).thenReturn(List.of());
        when(patientService.getAll()).thenReturn(List.of());
        when(diagnosisService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/examinations/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("examination/create"))
                .andExpect(model().attributeExists("examinationCreateDto", "doctors", "patients", "diagnoses"));
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void editFormShouldReturnEditView() throws Exception {
        Examination examination = new Examination();
        examination.setId(1);
        examination.setExamDate(LocalDateTime.of(2026, 1, 10, 9, 0));
        examination.setPrice(BigDecimal.TEN);
        examination.setPaidBy(PaidBy.PATIENT);
        examination.setTreatmentText("Rest");

        doNothing().when(accessService).checkCanEditExamination(1);
        when(examinationService.getById(1)).thenReturn(examination);
        when(doctorService.getAll()).thenReturn(List.of());
        when(patientService.getAll()).thenReturn(List.of());
        when(diagnosisService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/examinations/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("examination/edit"))
                .andExpect(model().attributeExists("examinationEditDto"));
    }
}
