package com.example.Medical_record_project_Final.data.service;

import com.example.Medical_record_project_Final.data.entity.Diagnosis;

import java.util.List;

public interface DiagnosisService {

    Diagnosis create(Diagnosis diagnosis);

    Diagnosis update(Integer id, Diagnosis diagnosis);

    void deleteById(Integer id);

    Diagnosis getById(Integer id);

    List<Diagnosis> getAll();

    Diagnosis getByCode(String code);

    Diagnosis getByName(String name);
}