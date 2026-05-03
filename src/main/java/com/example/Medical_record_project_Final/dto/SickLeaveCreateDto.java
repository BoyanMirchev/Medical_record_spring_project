package com.example.Medical_record_project_Final.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class SickLeaveCreateDto {

    @NotNull(message = "Examination id is required.")
    private Integer examinationId;

    @NotNull(message = "Start date is required.")
    private LocalDate startDate;

    @NotNull(message = "Days count is required.")
    @Min(value = 1, message = "Days count must be at least 1.")
    private Integer daysCount;

    public SickLeaveCreateDto() {
    }

    public Integer getExaminationId() {
        return examinationId;
    }

    public void setExaminationId(Integer examinationId) {
        this.examinationId = examinationId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Integer getDaysCount() {
        return daysCount;
    }

    public void setDaysCount(Integer daysCount) {
        this.daysCount = daysCount;
    }
}