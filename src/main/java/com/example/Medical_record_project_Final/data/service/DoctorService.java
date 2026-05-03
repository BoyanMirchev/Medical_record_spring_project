package com.example.Medical_record_project_Final.data.service;

import com.example.Medical_record_project_Final.data.entity.Doctor;

import java.util.List;

public interface DoctorService {

    Doctor create(Doctor doctor);

    Doctor update(Integer id, Doctor doctor);

    void deleteById(Integer id);

    Doctor getById(Integer id);

    List<Doctor> getAll();

    Doctor getByDoctorIdentifier(String doctorIdentifier);

    List<Doctor> getAllPersonalDoctors();

    List<Doctor> getBySpecialty(Integer specialtyId);

    long getPatientCountByDoctor(Integer doctorId);

    long getVisitCountByDoctor(Integer doctorId);
}