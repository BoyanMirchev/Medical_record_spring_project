package com.example.Medical_record_project_Final.web.api;

import com.example.Medical_record_project_Final.data.entity.Doctor;
import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.data.entity.User;
import com.example.Medical_record_project_Final.data.service.AccessService;
import com.example.Medical_record_project_Final.data.service.DoctorService;
import com.example.Medical_record_project_Final.data.service.PatientService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientApiController.class)
@Import(RestResponseEntityExceptionHandler.class)
class PatientApiControllerTest {

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

    @Test
    @WithMockUser(roles = "DOCTOR")
    void getAllShouldReturnPatients() throws Exception {
        Patient patient = new Patient();
        patient.setId(1);
        patient.setEgn("1234567890");
        patient.setFirstName("Maria");
        patient.setLastName("Dimitrova");

        when(patientService.getAll()).thenReturn(List.of(patient));

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].egn").value("1234567890"));
    }

    @Test
    @WithMockUser(roles = "PATIENT")
    void getByIdShouldCheckAccessAndReturnPatient() throws Exception {
        Patient patient = new Patient();
        patient.setId(1);
        patient.setEgn("1234567890");

        doNothing().when(accessService).checkCanAccessPatient(1);
        when(patientService.getById(1)).thenReturn(patient);

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(accessService).checkCanAccessPatient(1);
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void getHistoryShouldReturnExaminations() throws Exception {
        Examination examination = new Examination();
        examination.setId(5);

        doNothing().when(accessService).checkCanAccessPatient(1);
        when(patientService.getPatientHistory(1)).thenReturn(List.of(examination));

        mockMvc.perform(get("/api/patients/1/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(5));
    }

    @Test
    @WithMockUser(roles = "PATIENT")
    void createShouldReturnForbiddenForPatientRole() throws Exception {
        String body = """
                {
                  "userId": 2,
                  "egn": "1234567890",
                  "firstName": "Maria",
                  "lastName": "Dimitrova",
                  "personalDoctorId": 3
                }
                """;

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }
}
