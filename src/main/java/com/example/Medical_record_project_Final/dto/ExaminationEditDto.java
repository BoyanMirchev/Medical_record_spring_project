package com.example.Medical_record_project_Final.dto;

import com.example.Medical_record_project_Final.data.entity.enums.PaidBy;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExaminationEditDto {

    @NotNull(message = "Id is required.")
    private Integer id;

    @NotNull(message = "Exam date is required.")
    @PastOrPresent(message = "Exam date cannot be in the future.")
    private LocalDateTime examDate;

    @NotNull(message = "Doctor id is required.")
    private Integer doctorId;

    @NotNull(message = "Patient id is required.")
    private Integer patientId;

    @NotNull(message = "Diagnosis id is required.")
    private Integer diagnosisId;

    @NotBlank(message = "Treatment text is required.")
    private String treatmentText;

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.00", message = "Price cannot be negative.")
    private BigDecimal price;

    @NotNull(message = "Paid by is required.")
    private PaidBy paidBy;

    private String notes;

    public ExaminationEditDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDateTime examDate) {
        this.examDate = examDate;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(Integer diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public String getTreatmentText() {
        return treatmentText;
    }

    public void setTreatmentText(String treatmentText) {
        this.treatmentText = treatmentText;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public PaidBy getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(PaidBy paidBy) {
        this.paidBy = paidBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}