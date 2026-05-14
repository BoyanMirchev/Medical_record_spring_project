package com.example.Medical_record_project_Final.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class PatientEditDto {

    @NotNull(message = "Id is required.")
    private Integer id;

    @NotNull(message = "User id is required.")
    private Integer userId;

    @NotBlank(message = "EGN is required.")
    @Pattern(regexp = "\\d{10}", message = "EGN must contain exactly 10 digits.")
    private String egn;

    @NotBlank(message = "First name is required.")
    @Size(max = 50, message = "First name must be up to 50 characters.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(max = 50, message = "Last name must be up to 50 characters.")
    private String lastName;

    @NotBlank(message = "Date of birth is required")
    @PastOrPresent(message = "Date of birth cannot be in the future")
    private LocalDate dateOfBirth;

    @Size(max = 10, message = "Gender must be up to 10 characters.")
    private String gender;

    @NotNull(message = "Personal doctor id is required.")
    private Integer personalDoctorId;

    public PatientEditDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getPersonalDoctorId() {
        return personalDoctorId;
    }

    public void setPersonalDoctorId(Integer personalDoctorId) {
        this.personalDoctorId = personalDoctorId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}