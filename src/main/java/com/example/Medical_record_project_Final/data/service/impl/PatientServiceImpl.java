package com.example.Medical_record_project_Final.data.service.impl;

import com.example.Medical_record_project_Final.data.entity.Doctor;
import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.data.repo.DoctorRepository;
import com.example.Medical_record_project_Final.data.repo.ExaminationRepository;
import com.example.Medical_record_project_Final.data.repo.PatientRepository;
import com.example.Medical_record_project_Final.data.service.PatientService;
import com.example.Medical_record_project_Final.exception.DuplicateEntityException;
import com.example.Medical_record_project_Final.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ExaminationRepository examinationRepository;

    public PatientServiceImpl(PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              ExaminationRepository examinationRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.examinationRepository = examinationRepository;
    }

    @Override
    public Patient create(Patient patient) {
        if (patientRepository.existsByEgn(patient.getEgn())) {
            throw new DuplicateEntityException("Patient with EGN " + patient.getEgn() + " already exists.");
        }

        validatePersonalDoctor(patient.getPersonalDoctor());

        return patientRepository.save(patient);
    }

    @Override
    public Patient update(Integer id, Patient patient) {
        Patient existing = getById(id);

        if (!existing.getEgn().equals(patient.getEgn()) &&
                patientRepository.existsByEgn(patient.getEgn())) {
            throw new DuplicateEntityException("Patient with EGN " + patient.getEgn() + " already exists.");
        }

        validatePersonalDoctor(patient.getPersonalDoctor());

        existing.setUser(patient.getUser());
        existing.setEgn(patient.getEgn());
        existing.setFirstName(patient.getFirstName());
        existing.setLastName(patient.getLastName());
        existing.setDateOfBirth(patient.getDateOfBirth());
        existing.setGender(patient.getGender());
        existing.setPersonalDoctor(patient.getPersonalDoctor());

        return patientRepository.save(existing);
    }

    @Override
    public void deleteById(Integer id) {
        Patient patient = getById(id);
        patientRepository.delete(patient);
    }

    @Override
    public Patient getById(Integer id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient with id " + id + " not found."));
    }

    @Override
    public List<Patient> getAll() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getByEgn(String egn) {
        return patientRepository.findByEgn(egn)
                .orElseThrow(() -> new EntityNotFoundException("Patient with EGN " + egn + " not found."));
    }

    @Override
    public List<Patient> getByPersonalDoctor(Integer doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor with id " + doctorId + " not found."));
        return patientRepository.findAllByPersonalDoctor(doctor);
    }

    @Override
    public List<Examination> getPatientHistory(Integer patientId) {
        Patient patient = getById(patientId);
        return examinationRepository.findAllByPatient(patient);
    }

    private void validatePersonalDoctor(Doctor doctor) {
        if (doctor == null || doctor.getId() == null) {
            throw new EntityNotFoundException("Personal doctor is required.");
        }

        Doctor existingDoctor = doctorRepository.findById(doctor.getId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor with id " + doctor.getId() + " not found."));

        if (!existingDoctor.isCanBePersonalDoctor()) {
            throw new IllegalArgumentException("Selected doctor cannot be a personal doctor.");
        }
    }
}