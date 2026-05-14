package com.example.Medical_record_project_Final.data.service.impl;

import com.example.Medical_record_project_Final.data.entity.SickLeave;
import com.example.Medical_record_project_Final.data.repo.SickLeaveRepository;
import com.example.Medical_record_project_Final.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SickLeaveServiceImplTest {

    @Mock
    private SickLeaveRepository sickLeaveRepository;

    @InjectMocks
    private SickLeaveServiceImpl sickLeaveService;

    @Test
    void getByIdShouldReturnSickLeaveWhenExists() {
        SickLeave sickLeave = new SickLeave();
        sickLeave.setId(1);
        sickLeave.setStartDate(LocalDate.now());
        sickLeave.setDaysCount(5);

        when(sickLeaveRepository.findById(1)).thenReturn(Optional.of(sickLeave));

        SickLeave result = sickLeaveService.getById(1);

        assertEquals(1, result.getId());
        assertEquals(5, result.getDaysCount());
    }

    @Test
    void getByIdShouldThrowWhenSickLeaveDoesNotExist() {
        when(sickLeaveRepository.findById(100)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> sickLeaveService.getById(100));
    }

    @Test
    void deleteByIdShouldDeleteExistingSickLeave() {
        SickLeave sickLeave = new SickLeave();
        sickLeave.setId(1);

        when(sickLeaveRepository.findById(1)).thenReturn(Optional.of(sickLeave));

        sickLeaveService.deleteById(1);

        verify(sickLeaveRepository, times(1)).delete(sickLeave);
    }
}