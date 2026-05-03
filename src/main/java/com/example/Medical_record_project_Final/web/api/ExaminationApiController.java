package com.example.Medical_record_project_Final.web.api;

import com.example.Medical_record_project_Final.data.entity.Diagnosis;
import com.example.Medical_record_project_Final.data.entity.Doctor;
import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.data.service.DiagnosisService;
import com.example.Medical_record_project_Final.data.service.DoctorService;
import com.example.Medical_record_project_Final.data.service.ExaminationService;
import com.example.Medical_record_project_Final.data.service.PatientService;
import com.example.Medical_record_project_Final.dto.ExaminationCreateDto;
import com.example.Medical_record_project_Final.dto.ExaminationEditDto;
import com.example.Medical_record_project_Final.mapper.ExaminationMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/examinations")
public class ExaminationApiController {

    private final ExaminationService examinationService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final DiagnosisService diagnosisService;

    public ExaminationApiController(ExaminationService examinationService,
                                    DoctorService doctorService,
                                    PatientService patientService,
                                    DiagnosisService diagnosisService) {
        this.examinationService = examinationService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.diagnosisService = diagnosisService;
    }

    @GetMapping
    public List<Examination> getAll() {
        return examinationService.getAll();
    }

    @GetMapping("/{id}")
    public Examination getById(@PathVariable Integer id) {
        return examinationService.getById(id);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Examination> getByDoctor(@PathVariable Integer doctorId) {
        return examinationService.getByDoctor(doctorId);
    }

    @GetMapping("/patient/{patientId}")
    public List<Examination> getByPatient(@PathVariable Integer patientId) {
        return examinationService.getByPatient(patientId);
    }

    @GetMapping("/diagnosis/{diagnosisId}")
    public List<Examination> getByDiagnosis(@PathVariable Integer diagnosisId) {
        return examinationService.getByDiagnosis(diagnosisId);
    }

    @GetMapping("/period")
    public List<Examination> getByPeriod(@RequestParam LocalDateTime startDate,
                                         @RequestParam LocalDateTime endDate) {
        return examinationService.getByPeriod(startDate, endDate);
    }

    @GetMapping("/doctor/{doctorId}/period")
    public List<Examination> getByDoctorAndPeriod(@PathVariable Integer doctorId,
                                                  @RequestParam LocalDateTime startDate,
                                                  @RequestParam LocalDateTime endDate) {
        return examinationService.getByDoctorAndPeriod(doctorId, startDate, endDate);
    }

    @GetMapping("/paid-by-patients/total")
    public BigDecimal getTotalPaidByPatients() {
        return examinationService.getTotalPaidByPatients();
    }

    @GetMapping("/paid-by-patients/doctor/{doctorId}")
    public BigDecimal getTotalPaidByPatientsByDoctor(@PathVariable Integer doctorId) {
        return examinationService.getTotalPaidByPatientsByDoctor(doctorId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Examination create(@Valid @RequestBody ExaminationCreateDto dto) {
        Doctor doctor = doctorService.getById(dto.getDoctorId());
        Patient patient = patientService.getById(dto.getPatientId());
        Diagnosis diagnosis = diagnosisService.getById(dto.getDiagnosisId());

        Examination examination = ExaminationMapper.toEntity(dto, doctor, patient, diagnosis);
        return examinationService.create(examination);
    }

    @PutMapping("/{id}")
    public Examination update(@PathVariable Integer id, @Valid @RequestBody ExaminationEditDto dto) {
        Examination existingExamination = examinationService.getById(id);
        Doctor doctor = doctorService.getById(dto.getDoctorId());
        Patient patient = patientService.getById(dto.getPatientId());
        Diagnosis diagnosis = diagnosisService.getById(dto.getDiagnosisId());

        ExaminationMapper.updateEntity(existingExamination, dto, doctor, patient, diagnosis);
        return examinationService.update(id, existingExamination);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        examinationService.deleteById(id);
    }
}