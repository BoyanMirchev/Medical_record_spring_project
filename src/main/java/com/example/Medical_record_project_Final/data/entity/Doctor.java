package com.example.Medical_record_project_Final.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Integer id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @NotBlank
    @Size(max = 50)
    @Column(name = "doctor_identifier", nullable = false, unique = true, length = 50)
    private String doctorIdentifier;

    @NotBlank
    @Size(max = 50)
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "specialty_id", nullable = false)
    private Specialty specialty;

    @Column(name = "can_be_personal_doctor", nullable = false)
    private boolean canBePersonalDoctor = false;

    @OneToMany(mappedBy = "personalDoctor")
    @JsonIgnore
    private Set<Patient> personalDoctorPatients = new LinkedHashSet<>();

    @OneToMany(mappedBy = "doctor")
    @JsonIgnore
    private Set<Examination> examinations = new LinkedHashSet<>();

    public Doctor() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Set<Patient> getPersonalDoctorPatients() {
        return personalDoctorPatients;
    }

    public void setPersonalDoctorPatients(Set<Patient> personalDoctorPatients) {
        this.personalDoctorPatients = personalDoctorPatients;
    }

    public Set<Examination> getExaminations() {
        return examinations;
    }

    public void setExaminations(Set<Examination> examinations) {
        this.examinations = examinations;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public boolean isCanBePersonalDoctor() {
        return canBePersonalDoctor;
    }

    public void setCanBePersonalDoctor(boolean canBePersonalDoctor) {
        this.canBePersonalDoctor = canBePersonalDoctor;
    }
}