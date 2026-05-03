package com.example.Medical_record_project_Final.mapper;

import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.SickLeave;
import com.example.Medical_record_project_Final.dto.SickLeaveCreateDto;
import com.example.Medical_record_project_Final.dto.SickLeaveEditDto;

public final class SickLeaveMapper {

    private SickLeaveMapper() {
    }

    public static SickLeave toEntity(SickLeaveCreateDto dto, Examination examination) {
        SickLeave sickLeave = new SickLeave();
        sickLeave.setExamination(examination);
        sickLeave.setStartDate(dto.getStartDate());
        sickLeave.setDaysCount(dto.getDaysCount());
        return sickLeave;
    }

    public static void updateEntity(SickLeave sickLeave, SickLeaveEditDto dto, Examination examination) {
        sickLeave.setExamination(examination);
        sickLeave.setStartDate(dto.getStartDate());
        sickLeave.setDaysCount(dto.getDaysCount());
    }
}