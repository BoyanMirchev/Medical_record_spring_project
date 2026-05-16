package com.example.Medical_record_project_Final.data.repo;

import com.example.Medical_record_project_Final.data.entity.*;
import com.example.Medical_record_project_Final.data.entity.enums.PaidBy;
import com.example.Medical_record_project_Final.dto.report.DoctorCountReportDto;
import com.example.Medical_record_project_Final.dto.report.MonthSickLeaveReportDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SickLeaveRepositoryTest {

    @Autowired
    private SickLeaveRepository sickLeaveRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Examination examOne;
    private Examination examTwo;
    private SickLeave leaveJanuary;
    private SickLeave leaveMarch;

    @BeforeEach
    void setUp() {
        Role doctorRole = persistRole("DOCTOR");
        Role patientRole = persistRole("PATIENT");
        Specialty specialty = persistSpecialty("General practice");

        Doctor doctorOne = persistDoctor(doctorRole, specialty, "DOC-001", "Anna", "Ivanova", true);
        Doctor doctorTwo = persistDoctor(doctorRole, specialty, "DOC-002", "Petar", "Georgiev", true);
        Patient patient = persistPatient(patientRole, doctorOne, "1234567890", "Maria", "Dimitrova");
        Diagnosis diagnosis = persistDiagnosis("J10", "Influenza", "Flu");

        examOne = persistExamination(
                LocalDateTime.of(2026, 1, 10, 9, 0),
                doctorOne,
                patient,
                diagnosis,
                new BigDecimal("30.00")
        );
        examTwo = persistExamination(
                LocalDateTime.of(2026, 3, 5, 10, 0),
                doctorTwo,
                patient,
                diagnosis,
                new BigDecimal("40.00")
        );

        leaveJanuary = persistSickLeave(examOne, LocalDate.of(2026, 1, 11), 5);
        leaveMarch = persistSickLeave(examTwo, LocalDate.of(2026, 3, 6), 3);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findByExaminationShouldReturnLinkedSickLeave() {
        Optional<SickLeave> found = sickLeaveRepository.findByExamination(examOne);

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(leaveJanuary.getId());
        assertThat(found.get().getDaysCount()).isEqualTo(5);
    }

    @Test
    void findAllByStartDateBetweenShouldReturnLeavesInRange() {
        List<SickLeave> inRange = sickLeaveRepository.findAllByStartDateBetween(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 2, 1)
        );

        assertThat(inRange).extracting(SickLeave::getId).containsExactly(leaveJanuary.getId());
    }

    @Test
    void findSickLeaveCountByMonthShouldGroupByMonth() {
        List<MonthSickLeaveReportDto> stats = sickLeaveRepository.findSickLeaveCountByMonth();

        assertThat(stats).hasSize(2);
        assertThat(stats.get(0).getCount()).isGreaterThanOrEqualTo(1L);
        assertThat(stats).extracting(MonthSickLeaveReportDto::getMonth).contains(1, 3);
    }

    @Test
    void findSickLeaveCountByDoctorShouldGroupByDoctor() {
        List<DoctorCountReportDto> stats = sickLeaveRepository.findSickLeaveCountByDoctor();

        assertThat(stats).hasSize(2);
        assertThat(stats.get(0).getCount()).isEqualTo(1L);
        assertThat(stats).extracting(DoctorCountReportDto::getDoctorName)
                .containsExactlyInAnyOrder("Anna Ivanova", "Petar Georgiev");
    }

    private Role persistRole(String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        return entityManager.persist(role);
    }

    private Specialty persistSpecialty(String name) {
        Specialty specialty = new Specialty();
        specialty.setName(name);
        return entityManager.persist(specialty);
    }

    private User persistUser(Role role, String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("encoded-password");
        user.setEmail(email);
        user.setEnabled(true);
        user.setRole(role);
        return entityManager.persist(user);
    }

    private Doctor persistDoctor(Role role, Specialty specialty, String identifier,
                                 String firstName, String lastName, boolean canBePersonalDoctor) {
        User user = persistUser(role, identifier.toLowerCase(), identifier.toLowerCase() + "@test.local");

        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setDoctorIdentifier(identifier);
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setSpecialty(specialty);
        doctor.setCanBePersonalDoctor(canBePersonalDoctor);
        return entityManager.persist(doctor);
    }

    private Patient persistPatient(Role role, Doctor personalDoctor, String egn,
                                   String firstName, String lastName) {
        User user = persistUser(role, "patient-" + egn, egn + "@test.local");

        Patient patient = new Patient();
        patient.setUser(user);
        patient.setEgn(egn);
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setPersonalDoctor(personalDoctor);
        return entityManager.persist(patient);
    }

    private Diagnosis persistDiagnosis(String code, String name, String description) {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setCode(code);
        diagnosis.setName(name);
        diagnosis.setDescription(description);
        return entityManager.persist(diagnosis);
    }

    private Examination persistExamination(LocalDateTime examDate, Doctor doctor, Patient patient,
                                           Diagnosis diagnosis, BigDecimal price) {
        Examination examination = new Examination();
        examination.setExamDate(examDate);
        examination.setDoctor(doctor);
        examination.setPatient(patient);
        examination.setDiagnosis(diagnosis);
        examination.setTreatmentText("Treatment");
        examination.setPrice(price);
        examination.setPaidBy(PaidBy.PATIENT);
        return entityManager.persist(examination);
    }

    private SickLeave persistSickLeave(Examination examination, LocalDate startDate, int daysCount) {
        SickLeave sickLeave = new SickLeave();
        sickLeave.setExamination(examination);
        sickLeave.setStartDate(startDate);
        sickLeave.setDaysCount(daysCount);
        return entityManager.persist(sickLeave);
    }
}
