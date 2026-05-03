package com.example.Medical_record_project_Final.web.api;

import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.SickLeave;
import com.example.Medical_record_project_Final.data.service.ExaminationService;
import com.example.Medical_record_project_Final.data.service.SickLeaveService;
import com.example.Medical_record_project_Final.dto.SickLeaveCreateDto;
import com.example.Medical_record_project_Final.dto.SickLeaveEditDto;
import com.example.Medical_record_project_Final.mapper.SickLeaveMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sick-leaves")
public class SickLeaveApiController {

    private final SickLeaveService sickLeaveService;
    private final ExaminationService examinationService;

    public SickLeaveApiController(SickLeaveService sickLeaveService,
                                  ExaminationService examinationService) {
        this.sickLeaveService = sickLeaveService;
        this.examinationService = examinationService;
    }

    @GetMapping
    public List<SickLeave> getAll() {
        return sickLeaveService.getAll();
    }

    @GetMapping("/{id}")
    public SickLeave getById(@PathVariable Integer id) {
        return sickLeaveService.getById(id);
    }

    @GetMapping("/examination/{examinationId}")
    public SickLeave getByExamination(@PathVariable Integer examinationId) {
        return sickLeaveService.getByExamination(examinationId);
    }

    @GetMapping("/period")
    public List<SickLeave> getByPeriod(@RequestParam LocalDate startDate,
                                       @RequestParam LocalDate endDate) {
        return sickLeaveService.getByPeriod(startDate, endDate);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SickLeave create(@Valid @RequestBody SickLeaveCreateDto dto) {
        Examination examination = examinationService.getById(dto.getExaminationId());

        SickLeave sickLeave = SickLeaveMapper.toEntity(dto, examination);
        return sickLeaveService.create(sickLeave);
    }

    @PutMapping("/{id}")
    public SickLeave update(@PathVariable Integer id, @Valid @RequestBody SickLeaveEditDto dto) {
        SickLeave existingSickLeave = sickLeaveService.getById(id);
        Examination examination = examinationService.getById(dto.getExaminationId());

        SickLeaveMapper.updateEntity(existingSickLeave, dto, examination);
        return sickLeaveService.update(id, existingSickLeave);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        sickLeaveService.deleteById(id);
    }
}