package com.example.Medical_record_project_Final.web.view;

import com.example.Medical_record_project_Final.data.service.ExaminationService;
import com.example.Medical_record_project_Final.data.service.SickLeaveService;
import com.example.Medical_record_project_Final.dto.SickLeaveCreateDto;
import com.example.Medical_record_project_Final.dto.SickLeaveEditDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SickLeaveViewController {

    private final SickLeaveService sickLeaveService;
    private final ExaminationService examinationService;

    public SickLeaveViewController(SickLeaveService sickLeaveService,
                                   ExaminationService examinationService) {
        this.sickLeaveService = sickLeaveService;
        this.examinationService = examinationService;
    }

    @GetMapping("/sick-leaves")
    public String getAll(Model model) {
        model.addAttribute("sickLeaves", sickLeaveService.getAll());
        return "sick-leave/list";
    }

    @GetMapping("/sick-leaves/create")
    public String createForm(Model model) {
        model.addAttribute("sickLeaveCreateDto", new SickLeaveCreateDto());
        model.addAttribute("examinations", examinationService.getAll());
        return "sick-leave/create";
    }

    @GetMapping("/sick-leaves/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        var sickLeave = sickLeaveService.getById(id);

        SickLeaveEditDto dto = new SickLeaveEditDto();
        dto.setId(sickLeave.getId());
        dto.setExaminationId(sickLeave.getExamination().getId());
        dto.setStartDate(sickLeave.getStartDate());
        dto.setDaysCount(sickLeave.getDaysCount());

        model.addAttribute("sickLeaveEditDto", dto);
        model.addAttribute("examinations", examinationService.getAll());

        return "sick-leave/edit";
    }
}