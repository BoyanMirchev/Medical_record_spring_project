package com.example.Medical_record_project_Final.web.api;

import com.example.Medical_record_project_Final.data.entity.Doctor;
import com.example.Medical_record_project_Final.data.entity.Specialty;
import com.example.Medical_record_project_Final.data.entity.User;
import com.example.Medical_record_project_Final.data.repo.SpecialtyRepository;
import com.example.Medical_record_project_Final.data.service.DoctorService;
import com.example.Medical_record_project_Final.data.service.UserService;
import com.example.Medical_record_project_Final.exception.RestResponseEntityExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorApiController.class)
@Import(RestResponseEntityExceptionHandler.class)
class DoctorApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DoctorService doctorService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private SpecialtyRepository specialtyRepository;

    @Test
    @WithMockUser(roles = "DOCTOR")
    void getAllShouldReturnDoctors() throws Exception {
        Doctor doctor = new Doctor();
        doctor.setId(1);
        doctor.setDoctorIdentifier("DOC-001");
        doctor.setFirstName("Anna");
        doctor.setLastName("Ivanova");

        when(doctorService.getAll()).thenReturn(List.of(doctor));

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorIdentifier").value("DOC-001"));
    }



    @Test
    @WithMockUser(roles = "DOCTOR")
    void getPersonalDoctorsShouldReturnList() throws Exception {
        when(doctorService.getAllPersonalDoctors()).thenReturn(List.of());

        mockMvc.perform(get("/api/doctors/personal-doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void getPatientCountShouldReturnCount() throws Exception {
        when(doctorService.getPatientCountByDoctor(1)).thenReturn(3L);

        mockMvc.perform(get("/api/doctors/1/patients-count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(3));
    }
}
