package com.example.Medical_record_project_Final.dto.report;

public class DoctorCountReportDto {

    private Integer doctorId;
    private String doctorName;
    private Long count;

    public DoctorCountReportDto(Integer doctorId, String doctorName, Long count) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.count = count;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public Long getCount() {
        return count;
    }
}