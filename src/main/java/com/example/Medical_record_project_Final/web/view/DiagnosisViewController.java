package com.example.Medical_record_project_Final.web.view;

import com.example.Medical_record_project_Final.data.service.DiagnosisService;
import com.example.Medical_record_project_Final.dto.DiagnosisCreateDto;
import com.example.Medical_record_project_Final.dto.DiagnosisEditDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DiagnosisViewController {

    private final DiagnosisService diagnosisService;

    public DiagnosisViewController(DiagnosisService diagnosisService) {
        this.diagnosisService = diagnosisService;
    }

    @GetMapping("/diagnoses")
    public String getAll(Model model) {
        model.addAttribute("diagnoses", diagnosisService.getAll());
        return "diagnosis/list";
    }

    @GetMapping("/diagnoses/create")
    public String createForm(Model model) {
        model.addAttribute("diagnosisCreateDto", new DiagnosisCreateDto());
        return "diagnosis/create";
    }

    @GetMapping("/diagnoses/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        var diagnosis = diagnosisService.getById(id);

        DiagnosisEditDto dto = new DiagnosisEditDto();
        dto.setId(diagnosis.getId());
        dto.setCode(diagnosis.getCode());
        dto.setName(diagnosis.getName());
        dto.setDescription(diagnosis.getDescription());

        model.addAttribute("diagnosisEditDto", dto);
        return "diagnosis/edit";
    }
}