package com.example.Medical_record_project_Final.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class InsuranceRecordCreateDto {

    @NotNull(message = "Patient id is required.")
    private Integer patientId;

    @NotNull(message = "Record month is required.")
    private LocalDate recordMonth;

    private boolean insured;

    @Size(max = 255, message = "Note must be up to 255 characters.")
    private String note;

    public InsuranceRecordCreateDto() {
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public LocalDate getRecordMonth() {
        return recordMonth;
    }

    public void setRecordMonth(LocalDate recordMonth) {
        this.recordMonth = recordMonth;
    }

    public boolean isInsured() {
        return insured;
    }

    public void setInsured(boolean insured) {
        this.insured = insured;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}