package com.example.Medical_record_project_Final.web.view;

import com.example.Medical_record_project_Final.data.entity.Doctor;
import com.example.Medical_record_project_Final.data.entity.Specialty;
import com.example.Medical_record_project_Final.data.entity.User;
import com.example.Medical_record_project_Final.data.repo.SpecialtyRepository;
import com.example.Medical_record_project_Final.data.service.DoctorService;
import com.example.Medical_record_project_Final.data.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorViewController.class)
class DoctorViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DoctorService doctorService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private SpecialtyRepository specialtyRepository;

    @Test
    @WithMockUser(roles = "PATIENT")
    void getAllShouldReturnForbiddenForPatient() throws Exception {
        mockMvc.perform(get("/doctors"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void getAllShouldReturnListView() throws Exception {
        when(doctorService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctor/list"))
                .andExpect(model().attributeExists("doctors"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createFormShouldReturnCreateViewWithLookups() throws Exception {
        when(userService.getAll()).thenReturn(List.of());
        when(specialtyRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/doctors/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctor/create"))
                .andExpect(model().attributeExists("doctorCreateDto", "users", "specialties"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createShouldRedirectWhenValid() throws Exception {
        User user = new User();
        user.setId(1);

        Specialty specialty = new Specialty();
        specialty.setId(2);

        when(userService.getById(1)).thenReturn(user);
        when(specialtyRepository.findById(2)).thenReturn(Optional.of(specialty));
        when(doctorService.create(any())).thenReturn(new Doctor());

        mockMvc.perform(post("/doctors/create")
                        .with(csrf())
                        .param("userId", "1")
                        .param("doctorIdentifier", "DOC-100")
                        .param("firstName", "New")
                        .param("lastName", "Doctor")
                        .param("specialtyId", "2")
                        .param("canBePersonalDoctor", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doctors"));

        verify(doctorService).create(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void editFormShouldReturnEditView() throws Exception {
        Specialty specialty = new Specialty();
        specialty.setId(2);

        User user = new User();
        user.setId(5);

        Doctor doctor = new Doctor();
        doctor.setId(1);
        doctor.setUser(user);
        doctor.setDoctorIdentifier("DOC-001");
        doctor.setFirstName("Anna");
        doctor.setLastName("Ivanova");
        doctor.setSpecialty(specialty);
        doctor.setCanBePersonalDoctor(true);

        when(doctorService.getById(1)).thenReturn(doctor);
        when(userService.getAll()).thenReturn(List.of(user));
        when(specialtyRepository.findAll()).thenReturn(List.of(specialty));

        mockMvc.perform(get("/doctors/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctor/edit"))
                .andExpect(model().attributeExists("doctorEditDto"));
    }
}
