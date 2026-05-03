package com.example.Medical_record_project_Final.mapper;

import com.example.Medical_record_project_Final.data.entity.Doctor;
import com.example.Medical_record_project_Final.data.entity.Specialty;
import com.example.Medical_record_project_Final.data.entity.User;
import com.example.Medical_record_project_Final.dto.DoctorCreateDto;
import com.example.Medical_record_project_Final.dto.DoctorEditDto;

public final class DoctorMapper {

    private DoctorMapper() {
    }

    public static Doctor toEntity(DoctorCreateDto dto, User user, Specialty specialty) {
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setDoctorIdentifier(dto.getDoctorIdentifier());
        doctor.setFirstName(dto.getFirstName());
        doctor.setLastName(dto.getLastName());
        doctor.setSpecialty(specialty);
        doctor.setCanBePersonalDoctor(dto.isCanBePersonalDoctor());
        return doctor;
    }

    public static void updateEntity(Doctor doctor, DoctorEditDto dto, User user, Specialty specialty) {
        doctor.setUser(user);
        doctor.setDoctorIdentifier(dto.getDoctorIdentifier());
        doctor.setFirstName(dto.getFirstName());
        doctor.setLastName(dto.getLastName());
        doctor.setSpecialty(specialty);
        doctor.setCanBePersonalDoctor(dto.isCanBePersonalDoctor());
    }
}