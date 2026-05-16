package com.example.Medical_record_project_Final.web.view;

import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.data.repo.PatientRepository;
import com.example.Medical_record_project_Final.data.service.*;
import com.example.Medical_record_project_Final.dto.ExaminationCreateDto;
import com.example.Medical_record_project_Final.dto.ExaminationEditDto;
import com.example.Medical_record_project_Final.exception.AccessDeniedException;
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
    private final AccessService accessService;
    private final PatientRepository patientRepository;

    public ExaminationViewController(ExaminationService examinationService,
                                     DoctorService doctorService,
                                     PatientService patientService,
                                     DiagnosisService diagnosisService, AccessService accessService, PatientRepository patientRepository) {
        this.examinationService = examinationService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.diagnosisService = diagnosisService;
        this.accessService = accessService;
        this.patientRepository = patientRepository;
    }

    @GetMapping("/examinations")
    public String getAll(Model model) {
        if (accessService.isPatient()) {
            Integer userId = accessService.getLoggedUserId();
            Patient patient = patientRepository.findByUserId(userId)
                    .orElseThrow(() -> new AccessDeniedException("Patient profile not found."));

            model.addAttribute("examinations", examinationService.getByPatient(patient.getId()));
        } else {
            model.addAttribute("examinations", examinationService.getAll());
        }

        return "examination/list";
    }

    @GetMapping("/examinations/{id}")
    public String details(@PathVariable Integer id, Model model) {
        accessService.checkCanAccessExamination(id);

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
        accessService.checkCanEditExamination(id);

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