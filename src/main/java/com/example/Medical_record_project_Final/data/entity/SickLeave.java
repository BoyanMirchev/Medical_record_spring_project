package com.example.Medical_record_project_Final.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "sick_leaves")
public class SickLeave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sick_leave_id")
    private Integer id;

    @OneToOne(optional = false)
    @JoinColumn(name = "exam_id", nullable = false, unique = true)
    private Examination examination;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Min(1)
    @Column(name = "days_count", nullable = false)
    private Integer daysCount;

    public SickLeave() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Examination getExamination() {
        return examination;
    }

    public void setExamination(Examination examination) {
        this.examination = examination;
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