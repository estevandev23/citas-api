CREATE TABLE health_insurer (
  code VARCHAR(30) NOT NULL PRIMARY KEY,
  display_name VARCHAR(120) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE TABLE insurance_plan (
  insurer_code VARCHAR(30) NOT NULL,
  code VARCHAR(30) NOT NULL,
  display_name VARCHAR(120) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (insurer_code, code),
  CONSTRAINT fk_plan_insurer FOREIGN KEY (insurer_code) REFERENCES health_insurer(code)
);
CREATE TABLE user_affiliation (
  user_id BIGINT NOT NULL PRIMARY KEY,
  insurer_code VARCHAR(30) NOT NULL,
  plan_code VARCHAR(30) NOT NULL,
  regime_code VARCHAR(30) NOT NULL,
  CONSTRAINT fk_affiliation_user FOREIGN KEY (user_id) REFERENCES app_user(id),
  CONSTRAINT fk_affiliation_plan FOREIGN KEY (insurer_code, plan_code) REFERENCES insurance_plan(insurer_code, code),
  CONSTRAINT fk_affiliation_regime FOREIGN KEY (regime_code) REFERENCES regime(code)
);
INSERT INTO health_insurer (code, display_name) VALUES ('EPS_LAB', 'EPS de laboratorio');
INSERT INTO insurance_plan (insurer_code, code, display_name) VALUES
  ('EPS_LAB', 'BASIC', 'Plan básico'), ('EPS_LAB', 'PLUS', 'Plan plus');
