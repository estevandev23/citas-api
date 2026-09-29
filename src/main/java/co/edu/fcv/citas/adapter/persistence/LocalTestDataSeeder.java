package co.edu.fcv.citas.adapter.persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import javax.sql.DataSource;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Synthetic users and appointments for the local Docker profile only. */
@Component
@Profile("local")
@ConditionalOnProperty(name = "app.test-data.enabled", havingValue = "true")
class LocalTestDataSeeder {
    private static final String PASSWORD = "CitasDemo123!";
    private final JdbcTemplate jdbc;
    private final DataSource dataSource;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    LocalTestDataSeeder(JdbcTemplate jdbc, DataSource dataSource) { this.jdbc = jdbc; this.dataSource = dataSource; }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void seed() {
        try (var connection = dataSource.getConnection()) {
            if (connection.getMetaData().getDatabaseProductName().toLowerCase().contains("h2")) return;
        } catch (java.sql.SQLException ex) {
            throw new IllegalStateException("No se pudo identificar la base de datos local", ex);
        }
        long patient = user("demo.patient@example.test", "Paciente", "Demo", "DEMO-PATIENT-001", "USER");
        long professionalUser = user("demo.professional@example.test", "Profesional", "Demo", "DEMO-PRO-001", "USER", "PROFESSIONAL");
        long admin = user("demo.admin@example.test", "Administrador", "Demo", "DEMO-ADMIN-001", "USER", "ADMIN");
        jdbc.update("INSERT IGNORE INTO user_affiliation(user_id,insurer_code,plan_code,regime_code) VALUES(?,?,?,?)", patient, "EPS_LAB", "BASIC", "CONTRIBUTORY");
        jdbc.update("INSERT IGNORE INTO user_affiliation(user_id,insurer_code,plan_code,regime_code) VALUES(?,?,?,?)", professionalUser, "EPS_LAB", "PLUS", "CONTRIBUTORY");
        long professional = professional(professionalUser);
        jdbc.update("INSERT IGNORE INTO professional_specialty(professional_id,specialty_code,is_primary) VALUES(?,?,?)", professional, "MEDICINA_GENERAL", true);
        jdbc.update("INSERT IGNORE INTO professional_specialty(professional_id,specialty_code,is_primary) VALUES(?,?,?)", professional, "CARDIOLOGIA", false);
        jdbc.update("INSERT IGNORE INTO professional_facility(professional_id,facility_code) VALUES(?,?)", professional, "HIC");
        jdbc.update("INSERT IGNORE INTO professional_facility(professional_id,facility_code) VALUES(?,?)", professional, "ICV");

        LocalDate day = LocalDate.now(ZoneId.of("America/Bogota")).plusDays(1);
        jdbc.update("INSERT INTO availability_block(professional_id,facility_code,available_date,start_time,end_time,active) SELECT ?,?,?,?,?,TRUE WHERE NOT EXISTS (SELECT 1 FROM availability_block WHERE professional_id=? AND available_date=? AND facility_code=?)", professional, "HIC", day, LocalTime.of(8, 0), LocalTime.of(12, 0), professional, day, "HIC");
        LocalDateTime start = day.atTime(9, 0);
        LocalDateTime end = start.plusMinutes(30);
        Integer appointments = jdbc.queryForObject("SELECT COUNT(*) FROM appointment WHERE patient_user_id=? AND professional_id=? AND start_at=?", Integer.class, patient, professional, start);
        if (appointments == null || appointments == 0) {
            int updated = jdbc.update("INSERT INTO appointment(patient_user_id,professional_id,facility_code,specialty_code,start_at,end_at,status_code,created_by,created_at) VALUES(?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)", patient, professional, "HIC", "MEDICINA_GENERAL", start, end, "APPROVED", patient);
            if (updated == 1) {
                Long appointment = jdbc.queryForObject("SELECT id FROM appointment WHERE patient_user_id=? AND professional_id=? AND start_at=?", Long.class, patient, professional, start);
                jdbc.update("INSERT INTO appointment_slot(professional_id,slot_start,appointment_id) VALUES(?,?,?)", professional, start, appointment);
                jdbc.update("INSERT INTO appointment_status_history(appointment_id,status_code,actor_user_id,source_code) VALUES(?,?,?,?)", appointment, "APPROVED", admin, "SEED");
            }
        }
    }

    private long user(String email, String firstName, String lastName, String document, String... roles) {
        var found = jdbc.query("SELECT id FROM app_user WHERE email=?", rs -> rs.next() ? rs.getLong(1) : null, email);
        long id;
        if (found == null) {
            jdbc.update("INSERT INTO app_user(first_name,last_name,document_type,document_number,email,phone,password_hash,active) VALUES(?,?,?,?,?,?,?,TRUE)", firstName, lastName, "CC", document, email, "3000000000", encoder.encode(PASSWORD));
            id = jdbc.queryForObject("SELECT id FROM app_user WHERE email=?", Long.class, email);
        } else id = found;
        for (String role : roles) jdbc.update("INSERT IGNORE INTO user_role(user_id,role_code) VALUES(?,?)", id, role);
        return id;
    }

    private long professional(long userId) {
        var found = jdbc.query("SELECT id FROM professional WHERE user_id=?", rs -> rs.next() ? rs.getLong(1) : null, userId);
        if (found != null) return found;
        jdbc.update("INSERT INTO professional(user_id,professional_code,license_number,active) VALUES(?,?,?,TRUE)", userId, "DEMO-PRO", "DEMO-LIC");
        return jdbc.queryForObject("SELECT id FROM professional WHERE user_id=?", Long.class, userId);
    }
}
