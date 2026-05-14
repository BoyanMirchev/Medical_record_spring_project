package com.example.Medical_record_project_Final.dto.report;

public class DiagnosisCountReportDto {

    private Integer diagnosisId;
    private String diagnosisName;
    private Long count;

    public DiagnosisCountReportDto(Integer diagnosisId, String diagnosisName, Long count) {
        this.diagnosisId = diagnosisId;
        this.diagnosisName = diagnosisName;
        this.count = count;
    }

    public Integer getDiagnosisId() {
        return diagnosisId;
    }

    public String getDiagnosisName() {
        return diagnosisName;
    }

    public Long getCount() {
        return count;
    }
}