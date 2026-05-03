package com.example.Medical_record_project_Final.mapper;

import com.example.Medical_record_project_Final.data.entity.Diagnosis;
import com.example.Medical_record_project_Final.dto.DiagnosisCreateDto;
import com.example.Medical_record_project_Final.dto.DiagnosisEditDto;

public final class DiagnosisMapper {

    private DiagnosisMapper() {
    }

    public static Diagnosis toEntity(DiagnosisCreateDto dto) {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setCode(dto.getCode());
        diagnosis.setName(dto.getName());
        diagnosis.setDescription(dto.getDescription());
        return diagnosis;
    }

    public static void updateEntity(Diagnosis diagnosis, DiagnosisEditDto dto) {
        diagnosis.setCode(dto.getCode());
        diagnosis.setName(dto.getName());
        diagnosis.setDescription(dto.getDescription());
    }
}