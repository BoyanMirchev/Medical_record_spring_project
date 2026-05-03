package com.example.Medical_record_project_Final.web.view;

import com.example.Medical_record_project_Final.data.service.DiagnosisService;
import com.example.Medical_record_project_Final.data.service.DoctorService;
import com.example.Medical_record_project_Final.data.service.ExaminationService;
import com.example.Medical_record_project_Final.data.service.PatientService;
import com.example.Medical_record_project_Final.dto.ExaminationCreateDto;
import com.example.Medical_record_project_Final.dto.ExaminationEditDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ExaminationViewController {

    private final ExaminationService examinationService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final DiagnosisService diagnosisService;

    public ExaminationViewController(ExaminationService examinationService,
                                     DoctorService doctorService,
                                     PatientService patientService,
                                     DiagnosisService diagnosisService) {
        this.examinationService = examinationService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.diagnosisService = diagnosisService;
    }

    @GetMapping("/examinations")
    public String getAll(Model model) {
        model.addAttribute("examinations", examinationService.getAll());
        return "examination/list";
    }

    @GetMapping("/examinations/{id}")
    public String details(@PathVariable Integer id, Model model) {
        model.addAttribute("examination", examinationService.getById(id));
        return "examination/details";
    }

    @GetMapping("/examinations/create")
    public String createForm(Model model) {
        model.addAttribute("examinationCreateDto", new ExaminationCreateDto());
        model.addAttribute("doctors", doctorService.getAll());
        model.addAttribute("patients", patientService.getAll());
        model.addAttribute("diagnoses", diagnosisService.getAll());
        return "examination/create";
    }

    @GetMapping("/examinations/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        var examination = examinationService.getById(id);

        ExaminationEditDto dto = new ExaminationEditDto();
        dto.setId(examination.getId());
        dto.setExamDate(examination.getExamDate());
        dto.setDoctorId(examination.getDoctor().getId());
        dto.setPatientId(examination.getPatient().getId());
        dto.setDiagnosisId(examination.getDiagnosis().getId());
        dto.setTreatmentText(examination.getTreatmentText());
        dto.setPrice(examination.getPrice());
        dto.setPaidBy(examination.getPaidBy());
        dto.setNotes(examination.getNotes());

        model.addAttribute("examinationEditDto", dto);
        model.addAttribute("doctors", doctorService.getAll());
        model.addAttribute("patients", patientService.getAll());
        model.addAttribute("diagnoses", diagnosisService.getAll());

        return "examination/edit";
    }
}