--liquibase formatted sql logicalFilePath:db/changelog/1.0.sql/init.sql

-- changeset lonmstalker:create-uuid
CREATE EXTENSION "uuid-ossp";
-- rollback drop extension "uuid-ossp";

-- changeset lonmstalker:create-bot-table
CREATE TABLE bot
(
    id            UUID                     DEFAULT uuid_generate_v1() PRIMARY KEY,
    title         VARCHAR(255)                           NOT NULL,
    url           VARCHAR(255)                           NOT NULL,
    hash          VARCHAR(255)                           NOT NULL,
    status        VARCHAR(50)                            NOT NULL,
    user_group_id UUID                                   NOT NULL,
    created_by    UUID                                   NOT NULL,
    created_date  TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    version       INT                      DEFAULT 0     NOT NULL
);
-- rollback drop table bot;

-- changeset lonmstalker:create-bot_action-table
CREATE TABLE bot_action
(
    id           UUID                     DEFAULT uuid_generate_v1() PRIMARY KEY,
    title        VARCHAR(255)                           NOT NULL,
    type         VARCHAR(50)                            NOT NULL,
    data         JSONB                                  NOT NULL,
    created_by   UUID                                   NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    version      INT                      DEFAULT 0     NOT NULL
);
-- rollback drop table bot_action;

-- changeset lonmstalker:create-bot_position_info-table
CREATE TABLE bot_position_info
(
    id            UUID                     DEFAULT uuid_generate_v1() PRIMARY KEY,
    bot_id        UUID REFERENCES bot (id),
    action_id     UUID REFERENCES bot_action (id),
    from_position VARCHAR(100)                           NOT NULL,
    to_position   VARCHAR(100)                           NULL,
    created_by    UUID                                   NOT NULL,
    created_date  TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    version       INT                      DEFAULT 0     NOT NULL
);
-- rollback drop table bot_position_info;

-- changeset lonmstalker:create-idx_bot_position_info
CREATE INDEX idx_bot_position_info ON bot_position_info (bot_id, from_position);
-- rollback drop index idx_bot_position_info;