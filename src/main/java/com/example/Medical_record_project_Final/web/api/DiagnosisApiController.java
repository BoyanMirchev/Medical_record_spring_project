package com.example.Medical_record_project_Final.web.api;

import com.example.Medical_record_project_Final.data.entity.Diagnosis;
import com.example.Medical_record_project_Final.data.service.DiagnosisService;
import com.example.Medical_record_project_Final.dto.DiagnosisCreateDto;
import com.example.Medical_record_project_Final.dto.DiagnosisEditDto;
import com.example.Medical_record_project_Final.mapper.DiagnosisMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diagnoses")
public class DiagnosisApiController {

    private final DiagnosisService diagnosisService;

    public DiagnosisApiController(DiagnosisService diagnosisService) {
        this.diagnosisService = diagnosisService;
    }

    @GetMapping
    public List<Diagnosis> getAll() {
        return diagnosisService.getAll();
    }

    @GetMapping("/{id}")
    public Diagnosis getById(@PathVariable Integer id) {
        return diagnosisService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Diagnosis create(@Valid @RequestBody DiagnosisCreateDto dto) {
        Diagnosis diagnosis = DiagnosisMapper.toEntity(dto);
        return diagnosisService.create(diagnosis);
    }

    @PutMapping("/{id}")
    public Diagnosis update(@PathVariable Integer id, @Valid @RequestBody DiagnosisEditDto dto) {
        Diagnosis existingDiagnosis = diagnosisService.getById(id);
        DiagnosisMapper.updateEntity(existingDiagnosis, dto);
        return diagnosisService.update(id, existingDiagnosis);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        diagnosisService.deleteById(id);
    }
}