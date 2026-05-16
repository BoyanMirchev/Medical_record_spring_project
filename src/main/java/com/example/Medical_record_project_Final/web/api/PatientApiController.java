package com.example.Medical_record_project_Final.web.api;

import com.example.Medical_record_project_Final.data.entity.Doctor;
import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.data.entity.User;
import com.example.Medical_record_project_Final.data.service.AccessService;
import com.example.Medical_record_project_Final.data.service.DoctorService;
import com.example.Medical_record_project_Final.data.service.PatientService;
import com.example.Medical_record_project_Final.data.service.UserService;
import com.example.Medical_record_project_Final.dto.PatientCreateDto;
import com.example.Medical_record_project_Final.dto.PatientEditDto;
import com.example.Medical_record_project_Final.mapper.PatientMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientApiController {

    private final PatientService patientService;
    private final UserService userService;
    private final DoctorService doctorService;
    private final AccessService accessService;

    public PatientApiController(PatientService patientService,
                                UserService userService,
                                DoctorService doctorService,
                                AccessService accessService) {
        this.patientService = patientService;
        this.userService = userService;
        this.doctorService = doctorService;
        this.accessService = accessService;
    }

    @GetMapping
    public List<Patient> getAll() {
        return patientService.getAll();
    }

    @GetMapping("/{id}")
    public Patient getById(@PathVariable Integer id) {
        accessService.checkCanAccessPatient(id);
        return patientService.getById(id);
    }

    @GetMapping("/egn/{egn}")
    public Patient getByEgn(@PathVariable String egn) {
        return patientService.getByEgn(egn);
    }

    @GetMapping("/personal-doctor/{doctorId}")
    public List<Patient> getByPersonalDoctor(@PathVariable Integer doctorId) {
        return patientService.getByPersonalDoctor(doctorId);
    }

    @GetMapping("/{patientId}/history")
    public List<Examination> getHistory(@PathVariable Integer patientId) {
        accessService.checkCanAccessPatient(patientId);
        return patientService.getPatientHistory(patientId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Patient create(@Valid @RequestBody PatientCreateDto dto) {
        User user = userService.getById(dto.getUserId());
        Doctor personalDoctor = doctorService.getById(dto.getPersonalDoctorId());

        Patient patient = PatientMapper.toEntity(dto, user, personalDoctor);
        return patientService.create(patient);
    }

    @PutMapping("/{id}")
    public Patient update(@PathVariable Integer id, @Valid @RequestBody PatientEditDto dto) {
        accessService.checkCanAccessPatient(id);

        Patient existingPatient = patientService.getById(id);
        User user = userService.getById(dto.getUserId());
        Doctor personalDoctor = doctorService.getById(dto.getPersonalDoctorId());

        PatientMapper.updateEntity(existingPatient, dto, user, personalDoctor);
        return patientService.update(id, existingPatient);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        patientService.deleteById(id);
    }
}