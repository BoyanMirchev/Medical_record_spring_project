package com.example.Medical_record_project_Final.web.view;

import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.data.repo.DoctorRepository;
import com.example.Medical_record_project_Final.data.repo.PatientRepository;
import com.example.Medical_record_project_Final.data.service.AccessService;
import com.example.Medical_record_project_Final.data.service.DoctorService;
import com.example.Medical_record_project_Final.data.service.PatientService;
import com.example.Medical_record_project_Final.data.service.UserService;
import com.example.Medical_record_project_Final.dto.PatientCreateDto;
import com.example.Medical_record_project_Final.dto.PatientEditDto;
import com.example.Medical_record_project_Final.exception.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PatientViewController {

    private final PatientService patientService;
    private final UserService userService;
    private final DoctorService doctorService;
    private final AccessService accessService;
    private final PatientRepository patientRepository;

    public PatientViewController(PatientService patientService,
                                 UserService userService,
                                 DoctorService doctorService, AccessService accessService, PatientRepository patientRepository) {
        this.patientService = patientService;
        this.userService = userService;
        this.doctorService = doctorService;
        this.accessService = accessService;
        this.patientRepository = patientRepository;
    }

    @GetMapping("/patients")
    public String getAll(Model model) {
        if (accessService.isPatient()) {
            Integer userId = accessService.getLoggedUserId();
            Patient patient = patientRepository.findByUserId(userId)
                    .orElseThrow(() -> new AccessDeniedException("Patient profile not found."));

            model.addAttribute("patients", List.of(patient));
        } else {
            model.addAttribute("patients", patientService.getAll());
        }

        return "patient/list";
    }

    @GetMapping("/patients/{id}")
    public String details(@PathVariable Integer id, Model model) {
        accessService.checkCanAccessPatient(id);

        model.addAttribute("patient", patientService.getById(id));
        model.addAttribute("history", patientService.getPatientHistory(id));
        return "patient/details";
    }

    @GetMapping("/patients/create")
    public String createForm(Model model) {
        model.addAttribute("patientCreateDto", new PatientCreateDto());
        model.addAttribute("users", userService.getAll());
        model.addAttribute("personalDoctors", doctorService.getAllPersonalDoctors());
        return "patient/create";
    }

    @GetMapping("/patients/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {

        accessService.checkCanAccessPatient(id);

        var patient = patientService.getById(id);

        PatientEditDto dto = new PatientEditDto();
        dto.setId(patient.getId());
        dto.setUserId(patient.getUser().getId());
        dto.setEgn(patient.getEgn());
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setDateOfBirth(patient.getDateOfBirth());
        dto.setGender(patient.getGender());
        dto.setPersonalDoctorId(patient.getPersonalDoctor().getId());

        model.addAttribute("patientEditDto", dto);
        model.addAttribute("users", userService.getAll());
        model.addAttribute("personalDoctors", doctorService.getAllPersonalDoctors());

        return "patient/edit";
    }
}