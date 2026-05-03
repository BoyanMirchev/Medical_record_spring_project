package com.example.Medical_record_project_Final.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DoctorEditDto {

    @NotNull(message = "Id is required.")
    private Integer id;

    @NotNull(message = "User id is required.")
    private Integer userId;

    @NotBlank(message = "Doctor identifier is required.")
    @Size(max = 50, message = "Doctor identifier must be up to 50 characters.")
    private String doctorIdentifier;

    @NotBlank(message = "First name is required.")
    @Size(max = 50, message = "First name must be up to 50 characters.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(max = 50, message = "Last name must be up to 50 characters.")
    private String lastName;

    @NotNull(message = "Specialty id is required.")
    private Integer specialtyId;

    private boolean canBePersonalDoctor;

    public DoctorEditDto() {
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

    public String getDoctorIdentifier() {
        return doctorIdentifier;
    }

    public void setDoctorIdentifier(String doctorIdentifier) {
        this.doctorIdentifier = doctorIdentifier;
    }

    public String getFirstName() {
        return firstName;
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

    public Integer getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(Integer specialtyId) {
        this.specialtyId = specialtyId;
    }

    public boolean isCanBePersonalDoctor() {
        return canBePersonalDoctor;
    }

    public void setCanBePersonalDoctor(boolean canBePersonalDoctor) {
        this.canBePersonalDoctor = canBePersonalDoctor;
    }
}