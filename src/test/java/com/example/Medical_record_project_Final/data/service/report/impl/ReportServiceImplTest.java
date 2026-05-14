package com.example.Medical_record_project_Final.data.service.report.impl;

import com.example.Medical_record_project_Final.data.repo.ExaminationRepository;
import com.example.Medical_record_project_Final.data.repo.PatientRepository;
import com.example.Medical_record_project_Final.data.repo.SickLeaveRepository;
import com.example.Medical_record_project_Final.dto.report.DiagnosisCountReportDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private ExaminationRepository examinationRepository;


    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    void getMostCommonDiagnosisShouldReturnFirstDiagnosisFromStatistics() {
        DiagnosisCountReportDto flu = new DiagnosisCountReportDto(1, "Flu", 10L);
        DiagnosisCountReportDto cold = new DiagnosisCountReportDto(2, "Cold", 5L);

        when(examinationRepository.findDiagnosisStatistics())
                .thenReturn(List.of(flu, cold));

        DiagnosisCountReportDto result = reportService.getMostCommonDiagnosis();

        assertNotNull(result);
        assertEquals("Flu", result.getDiagnosisName());
        assertEquals(10L, result.getCount());
    }

    @Test
    void getMostCommonDiagnosisShouldReturnNullWhenNoData() {
        when(examinationRepository.findDiagnosisStatistics())
                .thenReturn(List.of());

        DiagnosisCountReportDto result = reportService.getMostCommonDiagnosis();

        assertNull(result);
    }
}