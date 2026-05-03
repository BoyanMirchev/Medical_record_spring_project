package com.example.Medical_record_project_Final.web.api;

import com.example.Medical_record_project_Final.data.entity.Doctor;
import com.example.Medical_record_project_Final.data.entity.Specialty;
import com.example.Medical_record_project_Final.data.entity.User;
import com.example.Medical_record_project_Final.data.repo.SpecialtyRepository;
import com.example.Medical_record_project_Final.data.service.DoctorService;
import com.example.Medical_record_project_Final.data.service.UserService;
import com.example.Medical_record_project_Final.dto.DoctorCreateDto;
import com.example.Medical_record_project_Final.dto.DoctorEditDto;
import com.example.Medical_record_project_Final.exception.EntityNotFoundException;
import com.example.Medical_record_project_Final.mapper.DoctorMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorApiController {

    private final DoctorService doctorService;
    private final UserService userService;
    private final SpecialtyRepository specialtyRepository;

    public DoctorApiController(DoctorService doctorService,
                               UserService userService,
                               SpecialtyRepository specialtyRepository) {
        this.doctorService = doctorService;
        this.userService = userService;
        this.specialtyRepository = specialtyRepository;
    }

    @GetMapping
    public List<Doctor> getAll() {
        return doctorService.getAll();
    }

    @GetMapping("/{id}")
    public Doctor getById(@PathVariable Integer id) {
        return doctorService.getById(id);
    }

    @GetMapping("/personal-doctors")
    public List<Doctor> getAllPersonalDoctors() {
        return doctorService.getAllPersonalDoctors();
    }

    @GetMapping("/specialty/{specialtyId}")
    public List<Doctor> getBySpecialty(@PathVariable Integer specialtyId) {
        return doctorService.getBySpecialty(specialtyId);
    }

    @GetMapping("/{doctorId}/patients-count")
    public long getPatientCount(@PathVariable Integer doctorId) {
        return doctorService.getPatientCountByDoctor(doctorId);
    }

    @GetMapping("/{doctorId}/visits-count")
    public long getVisitCount(@PathVariable Integer doctorId) {
        return doctorService.getVisitCountByDoctor(doctorId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Doctor create(@Valid @RequestBody DoctorCreateDto dto) {
        User user = userService.getById(dto.getUserId());

        Specialty specialty = specialtyRepository.findById(dto.getSpecialtyId())
                .orElseThrow(() -> new EntityNotFoundException("Specialty with id " + dto.getSpecialtyId() + " not found."));

        Doctor doctor = DoctorMapper.toEntity(dto, user, specialty);
        return doctorService.create(doctor);
    }

    @PutMapping("/{id}")
    public Doctor update(@PathVariable Integer id, @Valid @RequestBody DoctorEditDto dto) {
        Doctor existingDoctor = doctorService.getById(id);
        User user = userService.getById(dto.getUserId());

        Specialty specialty = specialtyRepository.findById(dto.getSpecialtyId())
                .orElseThrow(() -> new EntityNotFoundException("Specialty with id " + dto.getSpecialtyId() + " not found."));

        DoctorMapper.updateEntity(existingDoctor, dto, user, specialty);
        return doctorService.update(id, existingDoctor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        doctorService.deleteById(id);
    }
}