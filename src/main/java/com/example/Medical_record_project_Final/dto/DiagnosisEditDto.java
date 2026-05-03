package com.example.Medical_record_project_Final.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DiagnosisEditDto {

    @NotNull(message = "Id is required.")
    private Integer id;

    @NotBlank(message = "Code is required.")
    @Size(max = 20, message = "Code must be up to 20 characters.")
    private String code;

    @NotBlank(message = "Name is required.")
    @Size(max = 100, message = "Name must be up to 100 characters.")
    private String name;

    private String description;

    public DiagnosisEditDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}