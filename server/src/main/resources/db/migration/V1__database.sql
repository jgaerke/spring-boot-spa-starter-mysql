CREATE TABLE spring_session (
  session_id            CHAR(36),
  creation_time         BIGINT NOT NULL,
  last_access_time      BIGINT NOT NULL,
  max_inactive_interval INT    NOT NULL,
  principal_name        VARCHAR(100),
  CONSTRAINT spring_session_pk PRIMARY KEY (session_id)
)
  ENGINE = InnoDB;

CREATE INDEX spring_session_ix1 ON spring_session (last_access_time);

CREATE TABLE spring_session_attributes (
  session_id      CHAR(36),
  attribute_name  VARCHAR(200),
  attribute_bytes BLOB,
  CONSTRAINT spring_session_attributes_pk PRIMARY KEY (session_id, attribute_name),
  CONSTRAINT spring_session_attributes_fk FOREIGN KEY (session_id) REFERENCES spring_session (session_id)
    ON DELETE CASCADE
)
  ENGINE = InnoDB;

CREATE INDEX spring_session_attributes_ix1 ON spring_session_attributes (session_id);


CREATE TABLE account (
  id                    BINARY(255)  NOT NULL,
  credentials_expired   BIT          NOT NULL,
  disabled              BIT          NOT NULL,
  email                 VARCHAR(255),
  expired               BIT          NOT NULL,
  first                 VARCHAR(255),
  last                  VARCHAR(255),
  locked                BIT          NOT NULL,
  password              VARCHAR(254) NOT NULL,
  password_reset_token  VARCHAR(255),
  payment_info          VARCHAR(255),
  plan                  VARCHAR(255),
  trial_expiration_date DATETIME,
  PRIMARY KEY (id)
);

CREATE TABLE role (
  name    VARCHAR(255) NOT NULL,
  account BINARY(255)  NOT NULL,
  PRIMARY KEY (name, account)
);

ALTER TABLE role ADD CONSTRAINT role_account FOREIGN KEY (account) REFERENCES account (id)
