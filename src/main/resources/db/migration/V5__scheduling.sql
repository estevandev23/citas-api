CREATE TABLE specialty (
  code VARCHAR(50) PRIMARY KEY,
  display_name VARCHAR(150) NOT NULL UNIQUE,
  duration_minutes SMALLINT NOT NULL,
  is_general BOOLEAN NOT NULL DEFAULT FALSE,
  requires_admin_approval BOOLEAN NOT NULL DEFAULT TRUE,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT ck_specialty_duration CHECK (duration_minutes IN (30,60))
);
INSERT INTO specialty(code,display_name,duration_minutes,is_general,requires_admin_approval) VALUES
 ('MEDICINA_GENERAL','Medicina General',30,TRUE,FALSE),('CARDIOLOGIA','Cardiología',30,FALSE,TRUE),('MEDICINA_INTERNA','Medicina Interna',30,FALSE,TRUE),('PEDIATRIA','Pediatría',30,FALSE,TRUE),('NEUROLOGIA','Neurología',60,FALSE,TRUE)
ON DUPLICATE KEY UPDATE display_name=VALUES(display_name),duration_minutes=VALUES(duration_minutes);

CREATE TABLE professional (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL UNIQUE,
  professional_code VARCHAR(40) NOT NULL UNIQUE,
  license_number VARCHAR(80) NOT NULL UNIQUE,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT fk_prof_user FOREIGN KEY(user_id) REFERENCES app_user(id)
);
CREATE TABLE professional_specialty (
  professional_id BIGINT NOT NULL, specialty_code VARCHAR(50) NOT NULL, is_primary BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY(professional_id,specialty_code), FOREIGN KEY(professional_id) REFERENCES professional(id), FOREIGN KEY(specialty_code) REFERENCES specialty(code)
);
CREATE TABLE professional_facility (
  professional_id BIGINT NOT NULL, facility_code VARCHAR(30) NOT NULL,
  PRIMARY KEY(professional_id,facility_code), FOREIGN KEY(professional_id) REFERENCES professional(id), FOREIGN KEY(facility_code) REFERENCES facility(code)
);
CREATE TABLE availability_block (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, professional_id BIGINT NOT NULL, facility_code VARCHAR(30) NOT NULL,
  available_date DATE NOT NULL, start_time TIME NOT NULL, end_time TIME NOT NULL, active BOOLEAN NOT NULL DEFAULT TRUE,
  FOREIGN KEY(professional_id) REFERENCES professional(id), FOREIGN KEY(facility_code) REFERENCES facility(code),
  INDEX ix_availability(professional_id,available_date,start_time)
);
CREATE TABLE appointment (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, patient_user_id BIGINT NOT NULL, professional_id BIGINT NOT NULL,
  facility_code VARCHAR(30) NOT NULL, specialty_code VARCHAR(50) NOT NULL, start_at DATETIME NOT NULL, end_at DATETIME NOT NULL,
  status_code VARCHAR(30) NOT NULL, rejection_reason VARCHAR(500), created_by BIGINT NOT NULL, approved_by BIGINT, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY(patient_user_id) REFERENCES app_user(id), FOREIGN KEY(professional_id) REFERENCES professional(id), FOREIGN KEY(facility_code) REFERENCES facility(code), FOREIGN KEY(specialty_code) REFERENCES specialty(code), FOREIGN KEY(created_by) REFERENCES app_user(id), FOREIGN KEY(approved_by) REFERENCES app_user(id),
  INDEX ix_appointment_patient(patient_user_id,start_at), INDEX ix_appointment_professional(professional_id,start_at), INDEX ix_appointment_status(status_code)
);
CREATE TABLE appointment_slot (
  professional_id BIGINT NOT NULL, slot_start DATETIME NOT NULL, appointment_id BIGINT NOT NULL,
  PRIMARY KEY(professional_id,slot_start), FOREIGN KEY(professional_id) REFERENCES professional(id), FOREIGN KEY(appointment_id) REFERENCES appointment(id) ON DELETE CASCADE
);
CREATE TABLE appointment_status_history (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, appointment_id BIGINT NOT NULL, status_code VARCHAR(30) NOT NULL, actor_user_id BIGINT, source_code VARCHAR(20) NOT NULL, reason VARCHAR(500), changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY(appointment_id) REFERENCES appointment(id) ON DELETE CASCADE, FOREIGN KEY(actor_user_id) REFERENCES app_user(id)
);
CREATE TABLE reschedule_request (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, appointment_id BIGINT NOT NULL, requested_by BIGINT NOT NULL, facility_code VARCHAR(30) NOT NULL,
  requested_start_at DATETIME NOT NULL, requested_end_at DATETIME NOT NULL, status_code VARCHAR(30) NOT NULL DEFAULT 'PENDING', decision_reason VARCHAR(500), decided_by BIGINT, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY(appointment_id) REFERENCES appointment(id) ON DELETE CASCADE, FOREIGN KEY(requested_by) REFERENCES app_user(id), FOREIGN KEY(facility_code) REFERENCES facility(code), FOREIGN KEY(decided_by) REFERENCES app_user(id), INDEX ix_reschedule_status(status_code)
);
CREATE TABLE reschedule_slot (
  professional_id BIGINT NOT NULL, slot_start DATETIME NOT NULL, request_id BIGINT NOT NULL,
  PRIMARY KEY(professional_id,slot_start), FOREIGN KEY(professional_id) REFERENCES professional(id), FOREIGN KEY(request_id) REFERENCES reschedule_request(id) ON DELETE CASCADE
);
