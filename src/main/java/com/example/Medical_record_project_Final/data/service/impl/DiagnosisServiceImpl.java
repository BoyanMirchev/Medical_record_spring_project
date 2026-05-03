package com.example.Medical_record_project_Final.data.service.impl;

import com.example.Medical_record_project_Final.data.entity.Diagnosis;
import com.example.Medical_record_project_Final.data.repo.DiagnosisRepository;
import com.example.Medical_record_project_Final.data.service.DiagnosisService;
import com.example.Medical_record_project_Final.exception.DuplicateEntityException;
import com.example.Medical_record_project_Final.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiagnosisServiceImpl implements DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;

    public DiagnosisServiceImpl(DiagnosisRepository diagnosisRepository) {
        this.diagnosisRepository = diagnosisRepository;
    }

    @Override
    public Diagnosis create(Diagnosis diagnosis) {
        if (diagnosisRepository.existsByCode(diagnosis.getCode())) {
            throw new DuplicateEntityException("Diagnosis with code " + diagnosis.getCode() + " already exists.");
        }

        if (diagnosisRepository.existsByName(diagnosis.getName())) {
            throw new DuplicateEntityException("Diagnosis with name " + diagnosis.getName() + " already exists.");
        }

        return diagnosisRepository.save(diagnosis);
    }

    @Override
    public Diagnosis update(Integer id, Diagnosis diagnosis) {
        Diagnosis existing = getById(id);

        if (!existing.getCode().equals(diagnosis.getCode()) &&
                diagnosisRepository.existsByCode(diagnosis.getCode())) {
            throw new DuplicateEntityException("Diagnosis with code " + diagnosis.getCode() + " already exists.");
        }

        if (!existing.getName().equals(diagnosis.getName()) &&
                diagnosisRepository.existsByName(diagnosis.getName())) {
            throw new DuplicateEntityException("Diagnosis with name " + diagnosis.getName() + " already exists.");
        }

        existing.setCode(diagnosis.getCode());
        existing.setName(diagnosis.getName());
        existing.setDescription(diagnosis.getDescription());

        return diagnosisRepository.save(existing);
    }

    @Override
    public void deleteById(Integer id) {
        Diagnosis diagnosis = getById(id);
        diagnosisRepository.delete(diagnosis);
    }

    @Override
    public Diagnosis getById(Integer id) {
        return diagnosisRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Diagnosis with id " + id + " not found."));
    }

    @Override
    public List<Diagnosis> getAll() {
        return diagnosisRepository.findAll();
    }

    @Override
    public Diagnosis getByCode(String code) {
        return diagnosisRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Diagnosis with code " + code + " not found."));
    }

    @Override
    public Diagnosis getByName(String name) {
        return diagnosisRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Diagnosis with name " + name + " not found."));
    }
}