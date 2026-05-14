package com.example.Medical_record_project_Final.data.service.impl;

import com.example.Medical_record_project_Final.data.entity.Diagnosis;
import com.example.Medical_record_project_Final.data.repo.DiagnosisRepository;
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
class DiagnosisServiceImplTest {

    @Mock
    private DiagnosisRepository diagnosisRepository;

    @InjectMocks
    private DiagnosisServiceImpl diagnosisService;

    @Test
    void getByIdShouldReturnDiagnosisWhenExists() {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setId(1);
        diagnosis.setCode("A00");
        diagnosis.setName("Test diagnosis");

        when(diagnosisRepository.findById(1)).thenReturn(Optional.of(diagnosis));

        Diagnosis result = diagnosisService.getById(1);

        assertEquals(1, result.getId());
        assertEquals("A00", result.getCode());
        assertEquals("Test diagnosis", result.getName());
    }

    @Test
    void getByIdShouldThrowExceptionWhenDiagnosisDoesNotExist() {
        when(diagnosisRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> diagnosisService.getById(99));
    }

    @Test
    void createShouldSaveDiagnosis() {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setCode("B00");
        diagnosis.setName("New diagnosis");

        when(diagnosisRepository.save(diagnosis)).thenReturn(diagnosis);

        Diagnosis result = diagnosisService.create(diagnosis);

        assertEquals("B00", result.getCode());
        verify(diagnosisRepository, times(1)).save(diagnosis);
    }
}