package com.example.Medical_record_project_Final.data.service.impl;

import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.SickLeave;
import com.example.Medical_record_project_Final.data.repo.ExaminationRepository;
import com.example.Medical_record_project_Final.data.repo.SickLeaveRepository;
import com.example.Medical_record_project_Final.data.service.SickLeaveService;
import com.example.Medical_record_project_Final.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SickLeaveServiceImpl implements SickLeaveService {

    private final SickLeaveRepository sickLeaveRepository;
    private final ExaminationRepository examinationRepository;

    public SickLeaveServiceImpl(SickLeaveRepository sickLeaveRepository,
                                ExaminationRepository examinationRepository) {
        this.sickLeaveRepository = sickLeaveRepository;
        this.examinationRepository = examinationRepository;
    }

    @Override
    public SickLeave create(SickLeave sickLeave) {
        validateExamination(sickLeave);
        return sickLeaveRepository.save(sickLeave);
    }

    @Override
    public SickLeave update(Integer id, SickLeave sickLeave) {
        SickLeave existing = getById(id);

        validateExamination(sickLeave);

        existing.setExamination(sickLeave.getExamination());
        existing.setStartDate(sickLeave.getStartDate());
        existing.setDaysCount(sickLeave.getDaysCount());

        return sickLeaveRepository.save(existing);
    }

    @Override
    public void deleteById(Integer id) {
        SickLeave sickLeave = getById(id);
        sickLeaveRepository.delete(sickLeave);
    }

    @Override
    public SickLeave getById(Integer id) {
        return sickLeaveRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sick leave with id " + id + " not found."));
    }

    @Override
    public List<SickLeave> getAll() {
        return sickLeaveRepository.findAll();
    }

    @Override
    public SickLeave getByExamination(Integer examinationId) {
        Examination examination = examinationRepository.findById(examinationId)
                .orElseThrow(() -> new EntityNotFoundException("Examination with id " + examinationId + " not found."));

        return sickLeaveRepository.findByExamination(examination)
                .orElseThrow(() -> new EntityNotFoundException("Sick leave for examination id " + examinationId + " not found."));
    }

    @Override
    public List<SickLeave> getByPeriod(LocalDate startDate, LocalDate endDate) {
        return sickLeaveRepository.findAllByStartDateBetween(startDate, endDate);
    }

    private void validateExamination(SickLeave sickLeave) {
        if (sickLeave.getExamination() == null || sickLeave.getExamination().getId() == null) {
            throw new EntityNotFoundException("Examination is required.");
        }

        examinationRepository.findById(sickLeave.getExamination().getId())
                .orElseThrow(() -> new EntityNotFoundException("Examination with id " + sickLeave.getExamination().getId() + " not found."));
    }
}