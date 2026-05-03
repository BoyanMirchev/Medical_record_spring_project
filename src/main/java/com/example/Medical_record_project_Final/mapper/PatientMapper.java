package com.example.Medical_record_project_Final.mapper;

import com.example.Medical_record_project_Final.data.entity.Doctor;
import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.data.entity.User;
import com.example.Medical_record_project_Final.dto.PatientCreateDto;
import com.example.Medical_record_project_Final.dto.PatientEditDto;

public final class PatientMapper {

    private PatientMapper() {
    }

    public static Patient toEntity(PatientCreateDto dto, User user, Doctor personalDoctor) {
        Patient patient = new Patient();
        patient.setUser(user);
        patient.setEgn(dto.getEgn());
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setGender(dto.getGender());
        patient.setPersonalDoctor(personalDoctor);
        return patient;
    }

    public static void updateEntity(Patient patient, PatientEditDto dto, User user, Doctor personalDoctor) {
        patient.setUser(user);
        patient.setEgn(dto.getEgn());
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setGender(dto.getGender());
        patient.setPersonalDoctor(personalDoctor);
    }
}