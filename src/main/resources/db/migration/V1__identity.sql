CREATE TABLE app_user (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  document_type VARCHAR(30) NOT NULL,
  document_number VARCHAR(40) NOT NULL,
  email VARCHAR(255) NOT NULL,
  phone VARCHAR(30) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT uq_app_user_email UNIQUE (email),
  CONSTRAINT uq_app_user_document UNIQUE (document_type, document_number)
);

CREATE TABLE role (
  code VARCHAR(30) NOT NULL PRIMARY KEY
);
INSERT INTO role (code) VALUES ('USER'), ('PROFESSIONAL'), ('ADMIN');

CREATE TABLE user_role (
  user_id BIGINT NOT NULL,
  role_code VARCHAR(30) NOT NULL,
  PRIMARY KEY (user_id, role_code),
  CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES app_user(id),
  CONSTRAINT fk_user_role_role FOREIGN KEY (role_code) REFERENCES role(code)
);

CREATE TABLE refresh_session (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  token_id_hash CHAR(64) NOT NULL,
  expires_at TIMESTAMP NOT NULL,
  consumed_at TIMESTAMP NULL,
  CONSTRAINT uq_refresh_token_hash UNIQUE (token_id_hash),
  CONSTRAINT fk_refresh_user FOREIGN KEY (user_id) REFERENCES app_user(id)
);
CREATE INDEX ix_refresh_user ON refresh_session(user_id);
