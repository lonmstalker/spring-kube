--liquibase formatted sql logicalFilePath:db/changelog/1.0.sql/init.sql

-- changeset lonmstalker:create-uuid
CREATE EXTENSION "uuid-ossp";
-- rollback drop extension "uuid-ossp";

-- changeset lonmstalker:create-user_info-table
CREATE TABLE user_info
(
    id                  UUID                     DEFAULT uuid_generate_v1() PRIMARY KEY,
    username            VARCHAR(255) UNIQUE                    NOT NULL,
    email               VARCHAR(255) UNIQUE                    NOT NULL,
    first_name          VARCHAR(255)                           NOT NULL,
    last_name           VARCHAR(255)                           NULL,
    status              VARCHAR(50)                            NOT NULL,
    role                VARCHAR(100)                           NOT NULL,
    current_password_id UUID                                   NULL,
    login_attempts      SMALLINT                 DEFAULT 0     NOT NULL,
    invited_by          UUID REFERENCES user_info (id)         NULL,
    user_group_id       UUID                                   NULL,
    created_date        TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    last_login          TIMESTAMP WITH TIME ZONE DEFAULT now()
);
-- rollback drop table user_info;

-- changeset lonmstalker:create-user_group-table
CREATE TABLE user_group
(
    id           UUID                     DEFAULT uuid_generate_v1() PRIMARY KEY,
    title        VARCHAR(255)                           NOT NULL,
    invite_link  VARCHAR(255)                           NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    created_by   UUID REFERENCES user_info (id)         NOT NULL
);
-- rollback drop table user_group;

-- changeset lonmstalker:create-user_password-table
CREATE TABLE user_password
(
    id           UUID                     DEFAULT uuid_generate_v1() PRIMARY KEY,
    user_id      UUID REFERENCES user_info (id)         NOT NULL,
    value        VARCHAR(500)                           NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    type         VARCHAR(50)                            NOT NULL
);
-- rollback drop table user_password;

-- changeset lonmstalker:create-access_token-table
CREATE TABLE user_token
(
    id           UUID                     DEFAULT uuid_generate_v1() PRIMARY KEY,
    user_id      UUID REFERENCES user_info (id)         NOT NULL,
    value        VARCHAR(1000)                          NOT NULL,
    client       VARCHAR(50)                            NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    type         VARCHAR(50)                            NOT NULL
);
-- rollback drop table access_token;