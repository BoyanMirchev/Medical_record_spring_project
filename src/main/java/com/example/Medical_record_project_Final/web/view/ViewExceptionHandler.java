package com.example.Medical_record_project_Final.web.view;

import com.example.Medical_record_project_Final.exception.DoctorExaminationAccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ViewExceptionHandler {

    @ExceptionHandler(DoctorExaminationAccessDeniedException.class)
    public String handleDoctorExaminationAccessDeniedException(
            DoctorExaminationAccessDeniedException ex,
            Model model
    ) {
        model.addAttribute("message", ex.getMessage());
        return "error/403";
    }
}