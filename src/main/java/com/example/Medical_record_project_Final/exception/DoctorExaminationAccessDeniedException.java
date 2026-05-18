package com.example.Medical_record_project_Final.exception;

public class DoctorExaminationAccessDeniedException extends RuntimeException {

  public DoctorExaminationAccessDeniedException() {
    super("Doctors can edit only their own examinations.");
  }

    public DoctorExaminationAccessDeniedException(String message) {
        super(message);
    }
}
