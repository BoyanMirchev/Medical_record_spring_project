package com.example.Medical_record_project_Final.data.repo;

import com.example.Medical_record_project_Final.data.entity.*;
import com.example.Medical_record_project_Final.data.entity.enums.PaidBy;
import com.example.Medical_record_project_Final.dto.report.DiagnosisCountReportDto;
import com.example.Medical_record_project_Final.dto.report.DoctorCountReportDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ExaminationRepositoryTest {

    private static final LocalDateTime JAN_10 = LocalDateTime.of(2026, 1, 10, 9, 0);
    private static final LocalDateTime JAN_15 = LocalDateTime.of(2026, 1, 15, 10, 0);
    private static final LocalDateTime JAN_20 = LocalDateTime.of(2026, 1, 20, 11, 0);
    private static final LocalDateTime FEB_01 = LocalDateTime.of(2026, 2, 1, 12, 0);

    @Autowired
    private ExaminationRepository examinationRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Doctor doctorOne;
    private Doctor doctorTwo;
    private Patient patient;
    private Diagnosis flu;
    private Diagnosis cold;
    private Examination examJan10;
    private Examination examJan15;
    private Examination examFeb01;

    @BeforeEach
    void setUp() {
        Role doctorRole = persistRole("DOCTOR");
        Role patientRole = persistRole("PATIENT");
        Specialty specialty = persistSpecialty("General practice");

        doctorOne = persistDoctor(doctorRole, specialty, "DOC-001", "Anna", "Ivanova", true);
        doctorTwo = persistDoctor(doctorRole, specialty, "DOC-002", "Petar", "Georgiev", true);

        patient = persistPatient(patientRole, doctorOne, "1234567890", "Maria", "Dimitrova");

        flu = persistDiagnosis("J10", "Influenza", "Seasonal flu");
        cold = persistDiagnosis("J00", "Common cold", "Upper respiratory infection");

        examJan10 = persistExamination(JAN_10, doctorOne, patient, flu, "Rest", new BigDecimal("30.00"), PaidBy.PATIENT);
        examJan15 = persistExamination(JAN_15, doctorOne, patient, cold, "Syrup", new BigDecimal("20.00"), PaidBy.NHIF);
        examFeb01 = persistExamination(FEB_01, doctorTwo, patient, flu, "Follow-up", new BigDecimal("50.00"), PaidBy.PATIENT);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void saveAndFindByIdShouldPersistExamination() {
        Examination found = examinationRepository.findById(examJan10.getId()).orElseThrow();

        assertThat(found.getTreatmentText()).isEqualTo("Rest");
        assertThat(found.getPrice()).isEqualByComparingTo("30.00");
        assertThat(found.getDoctor().getDoctorIdentifier()).isEqualTo("DOC-001");
    }

    @Test
    void findAllByDoctorShouldReturnOnlyThatDoctorsExaminations() {
        List<Examination> doctorOneExams = examinationRepository.findAllByDoctor(doctorOne);
        List<Examination> doctorTwoExams = examinationRepository.findAllByDoctor(doctorTwo);

        assertThat(doctorOneExams).extracting(Examination::getId).containsExactlyInAnyOrder(examJan10.getId(), examJan15.getId());
        assertThat(doctorTwoExams).extracting(Examination::getId).containsExactly(examFeb01.getId());
    }

    @Test
    void findAllByPatientShouldReturnAllPatientExaminations() {
        List<Examination> exams = examinationRepository.findAllByPatient(patient);

        assertThat(exams).hasSize(3);
        assertThat(exams).extracting(Examination::getId)
                .containsExactlyInAnyOrder(examJan10.getId(), examJan15.getId(), examFeb01.getId());
    }

    @Test
    void findAllByDiagnosisShouldFilterByDiagnosis() {
        List<Examination> fluExams = examinationRepository.findAllByDiagnosis(flu);
        List<Examination> coldExams = examinationRepository.findAllByDiagnosis(cold);

        assertThat(fluExams).extracting(Examination::getId).containsExactlyInAnyOrder(examJan10.getId(), examFeb01.getId());
        assertThat(coldExams).extracting(Examination::getId).containsExactly(examJan15.getId());
    }

    @Test
    void findAllByDiagnosisIdShouldMatchDiagnosis() {
        List<Examination> fluExams = examinationRepository.findAllByDiagnosisId(flu.getId());

        assertThat(fluExams).hasSize(2);
    }

    @Test
    void findAllByExamDateBetweenShouldReturnExamsInRange() {
        List<Examination> inRange = examinationRepository.findAllByExamDateBetween(JAN_10, JAN_20);

        assertThat(inRange).extracting(Examination::getId).containsExactlyInAnyOrder(examJan10.getId(), examJan15.getId());
        assertThat(inRange).extracting(Examination::getId).doesNotContain(examFeb01.getId());
    }

    @Test
    void findAllByDoctorAndExamDateBetweenShouldFilterDoctorAndDates() {
        List<Examination> results = examinationRepository.findAllByDoctorAndExamDateBetween(doctorOne, JAN_10, JAN_20);

        assertThat(results).extracting(Examination::getId).containsExactlyInAnyOrder(examJan10.getId(), examJan15.getId());
    }

    @Test
    void findAllByPatientAndExamDateBetweenShouldFilterPatientAndDates() {
        List<Examination> results = examinationRepository.findAllByPatientAndExamDateBetween(patient, JAN_10, JAN_20);

        assertThat(results).hasSize(2);
    }

    @Test
    void findAllByDoctorIdAndExamDateBetweenShouldFilterByDoctorId() {
        List<Examination> results = examinationRepository.findAllByDoctorIdAndExamDateBetween(
                doctorTwo.getId(), JAN_15, FEB_01);

        assertThat(results).extracting(Examination::getId).containsExactly(examFeb01.getId());
    }

    @Test
    void findAllByPaidByShouldReturnMatchingExaminations() {
        List<Examination> paidByPatient = examinationRepository.findAllByPaidBy(PaidBy.PATIENT);
        List<Examination> paidByNhif = examinationRepository.findAllByPaidBy(PaidBy.NHIF);

        assertThat(paidByPatient).extracting(Examination::getId).containsExactlyInAnyOrder(examJan10.getId(), examFeb01.getId());
        assertThat(paidByNhif).extracting(Examination::getId).containsExactly(examJan15.getId());
    }

    @Test
    void countByDoctorAndCountByPatientShouldReturnCorrectTotals() {
        assertThat(examinationRepository.countByDoctor(doctorOne)).isEqualTo(2);
        assertThat(examinationRepository.countByDoctor(doctorTwo)).isEqualTo(1);
        assertThat(examinationRepository.countByPatient(patient)).isEqualTo(3);
    }

    @Test
    void getTotalPaidByPatientsShouldSumOnlyPatientPaidExams() {
        BigDecimal total = examinationRepository.getTotalPaidByPatients();

        assertThat(total).isEqualByComparingTo("80.00");
    }

    @Test
    void getTotalPaidByPatientsByDoctorShouldSumForGivenDoctor() {
        BigDecimal doctorOneTotal = examinationRepository.getTotalPaidByPatientsByDoctor(doctorOne.getId());
        BigDecimal doctorTwoTotal = examinationRepository.getTotalPaidByPatientsByDoctor(doctorTwo.getId());

        assertThat(doctorOneTotal).isEqualByComparingTo("30.00");
        assertThat(doctorTwoTotal).isEqualByComparingTo("50.00");
    }

    @Test
    void findDiagnosisStatisticsShouldGroupAndOrderByCountDescending() {
        List<DiagnosisCountReportDto> stats = examinationRepository.findDiagnosisStatistics();

        assertThat(stats).hasSize(2);
        assertThat(stats.get(0).getDiagnosisName()).isEqualTo("Influenza");
        assertThat(stats.get(0).getCount()).isEqualTo(2L);
        assertThat(stats.get(1).getDiagnosisName()).isEqualTo("Common cold");
        assertThat(stats.get(1).getCount()).isEqualTo(1L);
    }

    @Test
    void findDoctorVisitStatisticsShouldGroupByDoctorAndOrderByCountDescending() {
        List<DoctorCountReportDto> stats = examinationRepository.findDoctorVisitStatistics();

        assertThat(stats).hasSize(2);
        assertThat(stats.get(0).getDoctorName()).isEqualTo("Anna Ivanova");
        assertThat(stats.get(0).getCount()).isEqualTo(2L);
        assertThat(stats.get(1).getDoctorName()).isEqualTo("Petar Georgiev");
        assertThat(stats.get(1).getCount()).isEqualTo(1L);
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

        Patient patientEntity = new Patient();
        patientEntity.setUser(user);
        patientEntity.setEgn(egn);
        patientEntity.setFirstName(firstName);
        patientEntity.setLastName(lastName);
        patientEntity.setPersonalDoctor(personalDoctor);
        return entityManager.persist(patientEntity);
    }

    private Diagnosis persistDiagnosis(String code, String name, String description) {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setCode(code);
        diagnosis.setName(name);
        diagnosis.setDescription(description);
        return entityManager.persist(diagnosis);
    }

    private Examination persistExamination(LocalDateTime examDate, Doctor doctor, Patient patientEntity,
                                           Diagnosis diagnosis, String treatment, BigDecimal price, PaidBy paidBy) {
        Examination examination = new Examination();
        examination.setExamDate(examDate);
        examination.setDoctor(doctor);
        examination.setPatient(patientEntity);
        examination.setDiagnosis(diagnosis);
        examination.setTreatmentText(treatment);
        examination.setPrice(price);
        examination.setPaidBy(paidBy);
        return entityManager.persist(examination);
    }
}
