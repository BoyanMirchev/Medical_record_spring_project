package com.example.Medical_record_project_Final.data.service.impl;

import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.repo.ExaminationRepository;
import com.example.Medical_record_project_Final.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExaminationServiceImplTest {

    @Mock
    private ExaminationRepository examinationRepository;

    @InjectMocks
    private ExaminationServiceImpl examinationService;

    @Test
    void getByIdShouldReturnExaminationWhenExists() {
        Examination examination = new Examination();
        examination.setId(1);
        examination.setExamDate(LocalDateTime.now());
        examination.setPrice(BigDecimal.valueOf(50));

        when(examinationRepository.findById(1)).thenReturn(Optional.of(examination));

        Examination result = examinationService.getById(1);

        assertEquals(1, result.getId());
        assertEquals(BigDecimal.valueOf(50), result.getPrice());
    }

    @Test
    void getByIdShouldThrowWhenExaminationDoesNotExist() {
        when(examinationRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> examinationService.getById(99));
    }


    @Test
    void deleteByIdShouldDeleteExistingExamination() {
        Examination examination = new Examination();
        examination.setId(1);

        when(examinationRepository.findById(1)).thenReturn(Optional.of(examination));

        examinationService.deleteById(1);

        verify(examinationRepository, times(1)).delete(examination);
    }
}