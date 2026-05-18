package com.example.Medical_record_project_Final.web.view;

import com.example.Medical_record_project_Final.data.entity.Doctor;
import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.data.entity.User;
import com.example.Medical_record_project_Final.data.repo.PatientRepository;
import com.example.Medical_record_project_Final.data.service.AccessService;
import com.example.Medical_record_project_Final.data.service.DoctorService;
import com.example.Medical_record_project_Final.data.service.PatientService;
import com.example.Medical_record_project_Final.data.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientViewController.class)
class PatientViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientService patientService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private DoctorService doctorService;

    @MockitoBean
    private AccessService accessService;

    @MockitoBean
    private PatientRepository patientRepository;


    @Test
    @WithMockUser(roles = "ADMIN")
    void createFormShouldReturnCreateView() throws Exception {
        when(userService.getAll()).thenReturn(List.of());
        when(doctorService.getAllPersonalDoctors()).thenReturn(List.of());

        mockMvc.perform(get("/patients/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/create"))
                .andExpect(model().attributeExists("patientCreateDto", "users", "personalDoctors"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void editFormShouldReturnEditView() throws Exception {
        User user = new User();
        user.setId(2);

        Doctor doctor = new Doctor();
        doctor.setId(3);

        Patient patient = new Patient();
        patient.setId(1);
        patient.setUser(user);
        patient.setEgn("1234567890");
        patient.setFirstName("Maria");
        patient.setLastName("Dimitrova");
        patient.setPersonalDoctor(doctor);

        doNothing().when(accessService).checkCanAccessPatient(1);
        when(patientService.getById(1)).thenReturn(patient);
        when(userService.getAll()).thenReturn(List.of(user));
        when(doctorService.getAllPersonalDoctors()).thenReturn(List.of(doctor));

        mockMvc.perform(get("/patients/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/edit"))
                .andExpect(model().attributeExists("patientEditDto"));
    }
}
