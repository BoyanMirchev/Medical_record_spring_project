package com.example.Medical_record_project_Final.data.service.impl;

import com.example.Medical_record_project_Final.data.entity.Doctor;
import com.example.Medical_record_project_Final.data.entity.Specialty;
import com.example.Medical_record_project_Final.data.repo.DoctorRepository;
import com.example.Medical_record_project_Final.data.repo.ExaminationRepository;
import com.example.Medical_record_project_Final.data.repo.PatientRepository;
import com.example.Medical_record_project_Final.data.repo.SpecialtyRepository;
import com.example.Medical_record_project_Final.data.service.DoctorService;
import com.example.Medical_record_project_Final.exception.DuplicateEntityException;
import com.example.Medical_record_project_Final.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecialtyRepository specialtyRepository;
    private final PatientRepository patientRepository;
    private final ExaminationRepository examinationRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository,
                             SpecialtyRepository specialtyRepository,
                             PatientRepository patientRepository,
                             ExaminationRepository examinationRepository) {
        this.doctorRepository = doctorRepository;
        this.specialtyRepository = specialtyRepository;
        this.patientRepository = patientRepository;
        this.examinationRepository = examinationRepository;
    }

    @Override
    public Doctor create(Doctor doctor) {
        if (doctorRepository.existsByDoctorIdentifier(doctor.getDoctorIdentifier())) {
            throw new DuplicateEntityException("Doctor with identifier " + doctor.getDoctorIdentifier() + " already exists.");
        }

        validateSpecialty(doctor.getSpecialty());

        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor update(Integer id, Doctor doctor) {
        Doctor existing = getById(id);

        if (!existing.getDoctorIdentifier().equals(doctor.getDoctorIdentifier()) &&
                doctorRepository.existsByDoctorIdentifier(doctor.getDoctorIdentifier())) {
            throw new DuplicateEntityException("Doctor with identifier " + doctor.getDoctorIdentifier() + " already exists.");
        }

        validateSpecialty(doctor.getSpecialty());

        existing.setUser(doctor.getUser());
        existing.setDoctorIdentifier(doctor.getDoctorIdentifier());
        existing.setFirstName(doctor.getFirstName());
        existing.setLastName(doctor.getLastName());
        existing.setSpecialty(doctor.getSpecialty());
        existing.setCanBePersonalDoctor(doctor.isCanBePersonalDoctor());

        return doctorRepository.save(existing);
    }

    @Override
    public void deleteById(Integer id) {
        Doctor doctor = getById(id);
        doctorRepository.delete(doctor);
    }

    @Override
    public Doctor getById(Integer id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor with id " + id + " not found."));
    }

    @Override
    public List<Doctor> getAll() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor getByDoctorIdentifier(String doctorIdentifier) {
        return doctorRepository.findByDoctorIdentifier(doctorIdentifier)
                .orElseThrow(() -> new EntityNotFoundException("Doctor with identifier " + doctorIdentifier + " not found."));
    }

    @Override
    public List<Doctor> getAllPersonalDoctors() {
        return doctorRepository.findAllByCanBePersonalDoctorTrue();
    }

    @Override
    public List<Doctor> getBySpecialty(Integer specialtyId) {
        Specialty specialty = specialtyRepository.findById(specialtyId)
                .orElseThrow(() -> new EntityNotFoundException("Specialty with id " + specialtyId + " not found."));
        return doctorRepository.findAllBySpecialty(specialty);
    }

    @Override
    public long getPatientCountByDoctor(Integer doctorId) {
        Doctor doctor = getById(doctorId);
        return patientRepository.countByPersonalDoctor(doctor);
    }

    @Override
    public long getVisitCountByDoctor(Integer doctorId) {
        Doctor doctor = getById(doctorId);
        return examinationRepository.countByDoctor(doctor);
    }

    private void validateSpecialty(Specialty specialty) {
        if (specialty == null || specialty.getId() == null) {
            throw new EntityNotFoundException("Specialty is required.");
        }

        specialtyRepository.findById(specialty.getId())
                .orElseThrow(() -> new EntityNotFoundException("Specialty with id " + specialty.getId() + " not found."));
    }
}