package com.example.Medical_record_project_Final.data.repo;

import com.example.Medical_record_project_Final.data.entity.*;
import com.example.Medical_record_project_Final.dto.report.DoctorCountReportDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Doctor doctorOne;
    private Doctor doctorTwo;
    private Patient patientOne;
    private Patient patientTwo;

    @BeforeEach
    void setUp() {
        Role doctorRole = persistRole("DOCTOR");
        Role patientRole = persistRole("PATIENT");
        Specialty specialty = persistSpecialty("Cardiology");

        doctorOne = persistDoctor(doctorRole, specialty, "DOC-001", "Anna", "Ivanova", true);
        doctorTwo = persistDoctor(doctorRole, specialty, "DOC-002", "Petar", "Georgiev", true);

        patientOne = persistPatient(patientRole, doctorOne, "1111111111", "Maria", "Dimitrova");
        patientTwo = persistPatient(patientRole, doctorTwo, "2222222222", "Ivan", "Petrov");

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findByEgnShouldReturnPatientWhenExists() {
        Optional<Patient> found = patientRepository.findByEgn("1111111111");

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(patientOne.getId());
    }

    @Test
    void findByUserIdShouldReturnPatientLinkedToUser() {
        Optional<Patient> found = patientRepository.findByUserId(patientOne.getUser().getId());

        assertThat(found).isPresent();
        assertThat(found.get().getEgn()).isEqualTo("1111111111");
    }

    @Test
    void existsByEgnShouldReflectDatabaseState() {
        assertThat(patientRepository.existsByEgn("1111111111")).isTrue();
        assertThat(patientRepository.existsByEgn("9999999999")).isFalse();
    }

    @Test
    void findAllByPersonalDoctorShouldFilterByDoctor() {
        List<Patient> doctorOnePatients = patientRepository.findAllByPersonalDoctor(doctorOne);
        List<Patient> doctorTwoPatients = patientRepository.findAllByPersonalDoctor(doctorTwo);

        assertThat(doctorOnePatients).extracting(Patient::getId).containsExactly(patientOne.getId());
        assertThat(doctorTwoPatients).extracting(Patient::getId).containsExactly(patientTwo.getId());
    }

    @Test
    void findAllByPersonalDoctorIdShouldFilterByDoctorId() {
        List<Patient> patients = patientRepository.findAllByPersonalDoctorId(doctorOne.getId());

        assertThat(patients).hasSize(1);
        assertThat(patients.get(0).getEgn()).isEqualTo("1111111111");
    }

    @Test
    void countByPersonalDoctorShouldReturnCorrectCount() {
        assertThat(patientRepository.countByPersonalDoctor(doctorOne)).isEqualTo(1);
        assertThat(patientRepository.countByPersonalDoctor(doctorTwo)).isEqualTo(1);
    }

    @Test
    void findPatientCountByPersonalDoctorShouldGroupAndOrderByCountDescending() {
        List<DoctorCountReportDto> stats = patientRepository.findPatientCountByPersonalDoctor();

        assertThat(stats).hasSize(2);
        assertThat(stats.get(0).getCount()).isEqualTo(1L);
        assertThat(stats.get(1).getCount()).isEqualTo(1L);
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
}
