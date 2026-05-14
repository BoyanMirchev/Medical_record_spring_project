package com.example.Medical_record_project_Final.dto.report;

public class MonthSickLeaveReportDto {

    private Integer month;
    private Long count;

    public MonthSickLeaveReportDto(Integer month, Long count) {
        this.month = month;
        this.count = count;
    }

    public Integer getMonth() {
        return month;
    }

    public Long getCount() {
        return count;
    }
}