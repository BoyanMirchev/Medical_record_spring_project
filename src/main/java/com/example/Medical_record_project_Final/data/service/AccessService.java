package com.example.Medical_record_project_Final.data.service;

public interface AccessService {

    boolean canAccessPatient(Integer patientId);

    boolean canAccessExamination(Integer examinationId);

    boolean canEditExamination(Integer examinationId);

    boolean isAdmin();

    boolean isDoctor();

    boolean isPatient();

    Integer getLoggedUserId();

    void checkCanAccessPatient(Integer patientId);

    void checkCanAccessExamination(Integer examinationId);

    void checkCanEditExamination(Integer examinationId);
}