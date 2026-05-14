package com.example.Medical_record_project_Final.data.service.dto;

import com.example.Medical_record_project_Final.data.entity.enums.PaidBy;
import com.example.Medical_record_project_Final.dto.ExaminationCreateDto;
import com.example.Medical_record_project_Final.dto.PatientCreateDto;
import com.example.Medical_record_project_Final.dto.SickLeaveCreateDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void patientCreateDtoShouldRejectInvalidEgn() {
        PatientCreateDto dto = new PatientCreateDto();
        dto.setUserId(1);
        dto.setPersonalDoctorId(1);
        dto.setEgn("12345");
        dto.setFirstName("Ivan");
        dto.setLastName("Petrov");
        dto.setDateOfBirth(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<PatientCreateDto>> violations = validator.validate(dto);

        assertTrue(hasViolationForField(violations, "egn"));
    }

    @Test
    void patientCreateDtoShouldRejectFutureDateOfBirth() {
        PatientCreateDto dto = new PatientCreateDto();
        dto.setUserId(1);
        dto.setPersonalDoctorId(1);
        dto.setEgn("0123456789");
        dto.setFirstName("Ivan");
        dto.setLastName("Petrov");
        dto.setDateOfBirth(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<PatientCreateDto>> violations = validator.validate(dto);

        assertTrue(hasViolationForField(violations, "dateOfBirth"));
    }

    @Test
    void examinationCreateDtoShouldRejectNegativePrice() {
        ExaminationCreateDto dto = new ExaminationCreateDto();
        dto.setExamDate(LocalDateTime.now());
        dto.setDoctorId(1);
        dto.setPatientId(1);
        dto.setDiagnosisId(1);
        dto.setTreatmentText("Treatment");
        dto.setPrice(BigDecimal.valueOf(-10));
        dto.setPaidBy(PaidBy.PATIENT);

        Set<ConstraintViolation<ExaminationCreateDto>> violations = validator.validate(dto);

        assertTrue(hasViolationForField(violations, "price"));
    }

    @Test
    void examinationCreateDtoShouldRejectFutureExamDate() {
        ExaminationCreateDto dto = new ExaminationCreateDto();
        dto.setExamDate(LocalDateTime.now().plusDays(1));
        dto.setDoctorId(1);
        dto.setPatientId(1);
        dto.setDiagnosisId(1);
        dto.setTreatmentText("Treatment");
        dto.setPrice(BigDecimal.valueOf(50));
        dto.setPaidBy(PaidBy.PATIENT);

        Set<ConstraintViolation<ExaminationCreateDto>> violations = validator.validate(dto);

        assertTrue(hasViolationForField(violations, "examDate"));
    }

    @Test
    void sickLeaveCreateDtoShouldRejectZeroDaysCount() {
        SickLeaveCreateDto dto = new SickLeaveCreateDto();
        dto.setExaminationId(1);
        dto.setStartDate(LocalDate.now());
        dto.setDaysCount(0);

        Set<ConstraintViolation<SickLeaveCreateDto>> violations = validator.validate(dto);

        assertTrue(hasViolationForField(violations, "daysCount"));
    }

    @Test
    void sickLeaveCreateDtoShouldRejectFutureStartDate() {
        SickLeaveCreateDto dto = new SickLeaveCreateDto();
        dto.setExaminationId(1);
        dto.setStartDate(LocalDate.now().plusDays(1));
        dto.setDaysCount(5);

        Set<ConstraintViolation<SickLeaveCreateDto>> violations = validator.validate(dto);

        assertTrue(hasViolationForField(violations, "startDate"));
    }

    @Test
    void validPatientCreateDtoShouldPassValidation() {
        PatientCreateDto dto = new PatientCreateDto();
        dto.setUserId(1);
        dto.setPersonalDoctorId(1);
        dto.setEgn("0123456789");
        dto.setFirstName("Ivan");
        dto.setLastName("Petrov");
        dto.setDateOfBirth(LocalDate.of(2000, 1, 1));
        dto.setGender("Male");

        Set<ConstraintViolation<PatientCreateDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    private boolean hasViolationForField(Set<? extends ConstraintViolation<?>> violations, String fieldName) {
        return violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
    }
}