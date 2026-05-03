package com.example.Medical_record_project_Final.web.view;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class DoctorViewController {

    private final DoctorService doctorService;
    private final UserService userService;
    private final SpecialtyRepository specialtyRepository;

    public DoctorViewController(DoctorService doctorService,
                                UserService userService,
                                SpecialtyRepository specialtyRepository) {
        this.doctorService = doctorService;
        this.userService = userService;
        this.specialtyRepository = specialtyRepository;
    }

    @GetMapping("/doctors")
    public String getAll(Model model) {
        model.addAttribute("doctors", doctorService.getAll());
        return "doctor/list";
    }

    @GetMapping("/doctors/create")
    public String createForm(Model model) {
        model.addAttribute("doctorCreateDto", new DoctorCreateDto());
        model.addAttribute("users", userService.getAll());
        model.addAttribute("specialties", specialtyRepository.findAll());
        return "doctor/create";
    }

    @PostMapping("/doctors/create")
    public String create(@Valid @ModelAttribute("doctorCreateDto") DoctorCreateDto doctorCreateDto,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.getAll());
            model.addAttribute("specialties", specialtyRepository.findAll());
            return "doctor/create";
        }

        User user = userService.getById(doctorCreateDto.getUserId());

        Specialty specialty = specialtyRepository.findById(doctorCreateDto.getSpecialtyId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Specialty with id " + doctorCreateDto.getSpecialtyId() + " not found."
                ));

        Doctor doctor = DoctorMapper.toEntity(doctorCreateDto, user, specialty);
        doctorService.create(doctor);

        return "redirect:/doctors";
    }

    @GetMapping("/doctors/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        var doctor = doctorService.getById(id);

        DoctorEditDto dto = new DoctorEditDto();
        dto.setId(doctor.getId());
        dto.setUserId(doctor.getUser().getId());
        dto.setDoctorIdentifier(doctor.getDoctorIdentifier());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());
        dto.setSpecialtyId(doctor.getSpecialty().getId());
        dto.setCanBePersonalDoctor(doctor.isCanBePersonalDoctor());

        model.addAttribute("doctorEditDto", dto);
        model.addAttribute("users", userService.getAll());
        model.addAttribute("specialties", specialtyRepository.findAll());

        return "doctor/edit";
    }

    @PostMapping("/doctors/{id}/edit")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("doctorEditDto") DoctorEditDto doctorEditDto,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.getAll());
            model.addAttribute("specialties", specialtyRepository.findAll());
            return "doctor/edit";
        }

        Doctor existingDoctor = doctorService.getById(id);
        User user = userService.getById(doctorEditDto.getUserId());

        Specialty specialty = specialtyRepository.findById(doctorEditDto.getSpecialtyId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Specialty with id " + doctorEditDto.getSpecialtyId() + " not found."
                ));

        DoctorMapper.updateEntity(existingDoctor, doctorEditDto, user, specialty);
        doctorService.update(id, existingDoctor);

        return "redirect:/doctors";
    }
}