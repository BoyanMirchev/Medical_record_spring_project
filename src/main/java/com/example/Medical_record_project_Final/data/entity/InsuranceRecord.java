package com.example.Medical_record_project_Final.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(
        name = "insurance_records",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_patient_month", columnNames = {"patient_id", "record_month"})
        }
)
public class InsuranceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "insurance_record_id")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull
    @Column(name = "record_month", nullable = false)
    private LocalDate recordMonth;

    @Column(name = "is_insured", nullable = false)
    private boolean insured;

    @Size(max = 255)
    @Column(name = "note", length = 255)
    private String note;

    public InsuranceRecord() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotNull LocalDate getRecordMonth() {
        return recordMonth;
    }

    public void setRecordMonth(@NotNull LocalDate recordMonth) {
        this.recordMonth = recordMonth;
    }

    public boolean isInsured() {
        return insured;
    }

    public void setInsured(boolean insured) {
        this.insured = insured;
    }

    public @Size(max = 255) String getNote() {
        return note;
    }

    public void setNote(@Size(max = 255) String note) {
        this.note = note;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}