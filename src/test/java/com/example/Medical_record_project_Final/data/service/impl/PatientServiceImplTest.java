package com.example.Medical_record_project_Final.data.service.impl;

import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.data.repo.PatientRepository;
import com.example.Medical_record_project_Final.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    void getByIdShouldReturnPatientWhenExists() {
        Patient patient = new Patient();
        patient.setId(1);
        patient.setEgn("0123456789");
        patient.setFirstName("Boyan");

        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));

        Patient result = patientService.getById(1);

        assertEquals(1, result.getId());
        assertEquals("0123456789", result.getEgn());
        assertEquals("Boyan", result.getFirstName());
    }

    @Test
    void getByIdShouldThrowWhenPatientDoesNotExist() {
        when(patientRepository.findById(77)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> patientService.getById(77));
    }

    @Test
    void deleteByIdShouldDeleteExistingPatient() {
        Patient patient = new Patient();
        patient.setId(1);

        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));

        patientService.deleteById(1);

        verify(patientRepository, times(1)).delete(patient);
    }
}