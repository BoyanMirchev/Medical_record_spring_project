package com.example.Medical_record_project_Final.data.service;

public interface AccessService {

    boolean canAccessPatient(Integer patientId);

    boolean canAccessExamination(Integer examinationId);

    boolean canEditExamination(Integer examinationId);

    boolean isAdmin();
}