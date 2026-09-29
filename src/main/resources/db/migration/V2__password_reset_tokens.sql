CREATE TABLE password_reset_token (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  token_hash CHAR(64) NOT NULL,
  expires_at TIMESTAMP NOT NULL,
  consumed_at TIMESTAMP NULL,
  CONSTRAINT uq_password_reset_token_hash UNIQUE (token_hash),
  CONSTRAINT fk_password_reset_token_user FOREIGN KEY (user_id) REFERENCES app_user(id)
);
CREATE INDEX ix_password_reset_token_user ON password_reset_token(user_id);
