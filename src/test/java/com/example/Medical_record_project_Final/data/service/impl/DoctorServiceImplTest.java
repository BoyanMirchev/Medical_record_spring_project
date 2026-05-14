package com.example.Medical_record_project_Final.data.service.impl;

import com.example.Medical_record_project_Final.data.entity.Doctor;
import com.example.Medical_record_project_Final.data.repo.DoctorRepository;
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
class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    @Test
    void getByIdShouldReturnDoctorWhenExists() {
        Doctor doctor = new Doctor();
        doctor.setId(1);
        doctor.setFirstName("Ivan");
        doctor.setLastName("Petrov");

        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));

        Doctor result = doctorService.getById(1);

        assertEquals(1, result.getId());
        assertEquals("Ivan", result.getFirstName());
        assertEquals("Petrov", result.getLastName());
    }

    @Test
    void getByIdShouldThrowWhenDoctorDoesNotExist() {
        when(doctorRepository.findById(100)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> doctorService.getById(100));
    }

    @Test
    void deleteByIdShouldDeleteExistingDoctor() {
        Doctor doctor = new Doctor();
        doctor.setId(1);

        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));

        doctorService.deleteById(1);

        verify(doctorRepository, times(1)).delete(doctor);
    }
}