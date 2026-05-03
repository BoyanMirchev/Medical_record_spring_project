package com.example.Medical_record_project_Final.data.service;

import com.example.Medical_record_project_Final.data.entity.SickLeave;

import java.time.LocalDate;
import java.util.List;

public interface SickLeaveService {

    SickLeave create(SickLeave sickLeave);

    SickLeave update(Integer id, SickLeave sickLeave);

    void deleteById(Integer id);

    SickLeave getById(Integer id);

    List<SickLeave> getAll();

    SickLeave getByExamination(Integer examinationId);

    List<SickLeave> getByPeriod(LocalDate startDate, LocalDate endDate);
}