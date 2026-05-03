package com.example.Medical_record_project_Final.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Integer id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @NotBlank(message = "EGN is required")
    @Pattern(regexp = "\\d{10}", message = "EGN must contain exactly 10 digits")
    @Column(name = "egn", nullable = false, unique = true, columnDefinition = "CHAR(10)")
    private String egn;

    @NotBlank
    @Size(max = 50)
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;


    // Валидации на String-a, за упражнение

    @NotBlank
    @Size(max = 50)
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Size(max = 10)
    @Column(name = "gender", length = 10)
    private String gender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "personal_doctor_id", nullable = false)
    private Doctor personalDoctor;

    @OneToMany(mappedBy = "patient")
    @JsonIgnore
    private Set<Examination> examinations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<InsuranceRecord> insuranceRecords = new LinkedHashSet<>();

    public Patient() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<InsuranceRecord> getInsuranceRecords() {
        return insuranceRecords;
    }

    public void setInsuranceRecords(Set<InsuranceRecord> insuranceRecords) {
        this.insuranceRecords = insuranceRecords;
    }

    public Set<Examination> getExaminations() {
        return examinations;
    }

    public void setExaminations(Set<Examination> examinations) {
        this.examinations = examinations;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEgn() {
        return egn;
    }

    public void setEgn(String egn) {
        this.egn = egn;
    }

    public String getFirstName() {
        return firstName;
    }

    public Doctor getPersonalDoctor() {
        return personalDoctor;
    }

    public void setPersonalDoctor(Doctor personalDoctor) {
        this.personalDoctor = personalDoctor;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank @Size(max = 50) String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank @Size(max = 50) String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public @Size(max = 10) String getGender() {
        return gender;
    }

    public void setGender(@Size(max = 10) String gender) {
        this.gender = gender;
    }
}