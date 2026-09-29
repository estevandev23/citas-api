ALTER TABLE role ADD COLUMN display_name VARCHAR(100) NULL;
ALTER TABLE role ADD COLUMN display_order SMALLINT NOT NULL DEFAULT 0;
UPDATE role SET display_name = 'Usuario', display_order = 10 WHERE code = 'USER';
UPDATE role SET display_name = 'Profesional', display_order = 20 WHERE code = 'PROFESSIONAL';
UPDATE role SET display_name = 'Administrador', display_order = 30 WHERE code = 'ADMIN';
ALTER TABLE role MODIFY COLUMN display_name VARCHAR(100) NOT NULL;

CREATE TABLE appointment_status (
  code VARCHAR(30) NOT NULL PRIMARY KEY,
  display_name VARCHAR(100) NOT NULL,
  display_order SMALLINT NOT NULL
);
INSERT INTO appointment_status (code, display_name, display_order) VALUES
  ('REQUESTED', 'Solicitada', 10),
  ('APPROVED', 'Aprobada', 20),
  ('REJECTED', 'Rechazada', 30),
  ('CANCELLED', 'Cancelada', 40),
  ('COMPLETED', 'Completada', 50),
  ('NO_SHOW', 'No asistió', 60);

CREATE TABLE rescheduling_status (
  code VARCHAR(30) NOT NULL PRIMARY KEY,
  display_name VARCHAR(100) NOT NULL,
  display_order SMALLINT NOT NULL
);
INSERT INTO rescheduling_status (code, display_name, display_order) VALUES
  ('PENDING', 'Pendiente', 10),
  ('APPROVED', 'Aprobada', 20),
  ('REJECTED', 'Rechazada', 30);

CREATE TABLE regime (
  code VARCHAR(30) NOT NULL PRIMARY KEY,
  display_name VARCHAR(100) NOT NULL,
  display_order SMALLINT NOT NULL
);
INSERT INTO regime (code, display_name, display_order) VALUES
  ('CONTRIBUTORY', 'Contributivo', 10),
  ('SUBSIDIZED', 'Subsidiado', 20);

CREATE TABLE facility (
  code VARCHAR(30) NOT NULL PRIMARY KEY,
  display_name VARCHAR(180) NOT NULL,
  display_order SMALLINT NOT NULL
);
INSERT INTO facility (code, display_name, display_order) VALUES
  ('HIC', 'Hospital Internacional de Colombia (HIC)', 10),
  ('ICV', 'Instituto Cardiovascular (ICV)', 20);
