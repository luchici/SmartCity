DROP TABLE IF EXISTS account,role,account_role;

CREATE TABLE account
(
    account_id                SERIAL PRIMARY KEY,
    username                  VARCHAR   NOT NULL UNIQUE,
    password                  VARCHAR   NOT NULL,
    first_name                 VARCHAR   NOT NULL,
    last_name                  VARCHAR   NOT NULL,
    homecity                  VARCHAR   NOT NULL,
    dob                       TIMESTAMP NOT NULL,
    email                     VARCHAR   NOT NULL UNIQUE,
    is_account_non_expired     BOOLEAN   NOT NULL,
    is_account_non_locked      BOOLEAN   NOT NULL,
    is_credentials_non_expired BOOLEAN   NOT NULL,
    is_enabled                BOOLEAN   NOT NULL
);

CREATE TABLE user_role
(
    role_id INTEGER PRIMARY KEY,
    name    VARCHAR,
    UNIQUE (name)
);

CREATE TABLE account_role
(
    account INTEGER,
    role_id    INTEGER,
    PRIMARY KEY (account, role_id),
    FOREIGN KEY (account) REFERENCES account (account_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES user_role (role_id) ON DELETE CASCADE
);
