package com.example.Medical_record_project_Final.mapper;

import com.example.Medical_record_project_Final.data.entity.Diagnosis;
import com.example.Medical_record_project_Final.data.entity.Doctor;
import com.example.Medical_record_project_Final.data.entity.Examination;
import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.dto.ExaminationCreateDto;
import com.example.Medical_record_project_Final.dto.ExaminationEditDto;

public final class ExaminationMapper {

    private ExaminationMapper() {
    }

    public static Examination toEntity(
            ExaminationCreateDto dto,
            Doctor doctor,
            Patient patient,
            Diagnosis diagnosis
    ) {
        Examination examination = new Examination();
        examination.setExamDate(dto.getExamDate());
        examination.setDoctor(doctor);
        examination.setPatient(patient);
        examination.setDiagnosis(diagnosis);
        examination.setTreatmentText(dto.getTreatmentText());
        examination.setPrice(dto.getPrice());
        examination.setPaidBy(dto.getPaidBy());
        examination.setNotes(dto.getNotes());
        return examination;
    }

    public static void updateEntity(
            Examination examination,
            ExaminationEditDto dto,
            Doctor doctor,
            Patient patient,
            Diagnosis diagnosis
    ) {
        examination.setExamDate(dto.getExamDate());
        examination.setDoctor(doctor);
        examination.setPatient(patient);
        examination.setDiagnosis(diagnosis);
        examination.setTreatmentText(dto.getTreatmentText());
        examination.setPrice(dto.getPrice());
        examination.setPaidBy(dto.getPaidBy());
        examination.setNotes(dto.getNotes());
    }
}