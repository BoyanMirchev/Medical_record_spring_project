package com.example.Medical_record_project_Final.data.service;

import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.Patient;

import java.util.List;

public interface PatientService {

    Patient create(Patient patient);

    Patient update(Integer id, Patient patient);

    void deleteById(Integer id);

    Patient getById(Integer id);

    List<Patient> getAll();

    Patient getByEgn(String egn);

    List<Patient> getByPersonalDoctor(Integer doctorId);

    List<Examination> getPatientHistory(Integer patientId);
}