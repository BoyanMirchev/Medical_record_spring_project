package com.example.Medical_record_project_Final.data.service.report.impl;

import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.data.repo.ExaminationRepository;
import com.example.Medical_record_project_Final.data.repo.PatientRepository;
import com.example.Medical_record_project_Final.data.repo.SickLeaveRepository;
import com.example.Medical_record_project_Final.data.service.report.ReportService;
import com.example.Medical_record_project_Final.dto.report.DiagnosisCountReportDto;
import com.example.Medical_record_project_Final.dto.report.DoctorCountReportDto;
import com.example.Medical_record_project_Final.dto.report.MonthSickLeaveReportDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final ExaminationRepository examinationRepository;
    private final PatientRepository patientRepository;
    private final SickLeaveRepository sickLeaveRepository;

    public ReportServiceImpl(ExaminationRepository examinationRepository,
                             PatientRepository patientRepository,
                             SickLeaveRepository sickLeaveRepository) {
        this.examinationRepository = examinationRepository;
        this.patientRepository = patientRepository;
        this.sickLeaveRepository = sickLeaveRepository;
    }

    @Override
    public List<Patient> getPatientsByDiagnosis(Integer diagnosisId) {
        return examinationRepository.findAllByDiagnosisId(diagnosisId)
                .stream()
                .map(Examination::getPatient)
                .distinct()
                .toList();
    }

    @Override
    public DiagnosisCountReportDto getMostCommonDiagnosis() {
        return examinationRepository.findDiagnosisStatistics()
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Patient> getPatientsByPersonalDoctor(Integer doctorId) {
        return patientRepository.findAllByPersonalDoctorId(doctorId);
    }

    @Override
    public BigDecimal getTotalPaidByPatients() {
        return examinationRepository.getTotalPaidByPatients();
    }

    @Override
    public BigDecimal getTotalPaidByPatientsByDoctor(Integer doctorId) {
        return examinationRepository.getTotalPaidByPatientsByDoctor(doctorId);
    }

    @Override
    public List<DoctorCountReportDto> getPatientCountByPersonalDoctor() {
        return patientRepository.findPatientCountByPersonalDoctor();
    }

    @Override
    public List<DoctorCountReportDto> getVisitCountByDoctor() {
        return examinationRepository.findDoctorVisitStatistics();
    }

    @Override
    public List<Examination> getExaminationsByDoctorAndPeriod(Integer doctorId, LocalDateTime start, LocalDateTime end) {
        return examinationRepository.findAllByDoctorIdAndExamDateBetween(doctorId, start, end);
    }

    @Override
    public List<Examination> getExaminationsByPeriod(LocalDateTime start, LocalDateTime end) {
        return examinationRepository.findAllByExamDateBetween(start, end);
    }

    @Override
    public MonthSickLeaveReportDto getMonthWithMostSickLeaves() {
        return sickLeaveRepository.findSickLeaveCountByMonth()
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<DoctorCountReportDto> getDoctorsWithMostSickLeaves() {
        List<DoctorCountReportDto> result = sickLeaveRepository.findSickLeaveCountByDoctor();

        if (result.isEmpty()) {
            return result;
        }

        Long max = result.stream()
                .map(DoctorCountReportDto::getCount)
                .max(Comparator.naturalOrder())
                .orElse(0L);

        return result.stream()
                .filter(r -> r.getCount().equals(max))
                .toList();
    }
}