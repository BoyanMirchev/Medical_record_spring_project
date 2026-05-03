package com.example.Medical_record_project_Final.mapper;

import com.example.Medical_record_project_Final.data.entity.InsuranceRecord;
import com.example.Medical_record_project_Final.data.entity.Patient;
import com.example.Medical_record_project_Final.dto.InsuranceRecordCreateDto;
import com.example.Medical_record_project_Final.dto.InsuranceRecordEditDto;

public final class InsuranceRecordMapper {

    private InsuranceRecordMapper() {
    }

    public static InsuranceRecord toEntity(InsuranceRecordCreateDto dto, Patient patient) {
        InsuranceRecord insuranceRecord = new InsuranceRecord();
        insuranceRecord.setPatient(patient);
        insuranceRecord.setRecordMonth(dto.getRecordMonth());
        insuranceRecord.setInsured(dto.isInsured());
        insuranceRecord.setNote(dto.getNote());
        return insuranceRecord;
    }

    public static void updateEntity(InsuranceRecord insuranceRecord, InsuranceRecordEditDto dto, Patient patient) {
        insuranceRecord.setPatient(patient);
        insuranceRecord.setRecordMonth(dto.getRecordMonth());
        insuranceRecord.setInsured(dto.isInsured());
        insuranceRecord.setNote(dto.getNote());
    }
}