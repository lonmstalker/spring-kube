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

-- changeset lonmstalker:create-bot_user_info-table
CREATE TABLE bot_user_info
(
    id               UUID                     DEFAULT uuid_generate_v1() PRIMARY KEY,
    bot_id           UUID REFERENCES bot (id)                 NOT NULL,
    telegram_id      VARCHAR(255)                             NOT NULL,
    username         VARCHAR(255)                             NOT NULL,
    current_position VARCHAR(100)             DEFAULT 'START' NOT NULL,
    email            VARCHAR(255)                             NULL,
    phone            VARCHAR(255)                             NULL,
    full_name        VARCHAR(255)                             NULL,
    current_locale   VARCHAR(10)              DEFAULT 'ru'    NOT NULL,
    created_date     TIMESTAMP WITH TIME ZONE DEFAULT now()   NOT NULL
);
-- rollback drop table bot_user_info;

-- changeset lonmstalker:create-bot_action-table
CREATE TABLE bot_action
(
    id            UUID                     DEFAULT uuid_generate_v1() PRIMARY KEY,
    title         VARCHAR(255)                           NOT NULL,
    type          VARCHAR(50)                            NOT NULL,
    data          JSONB                                  NOT NULL,
    user_group_id UUID                                   NOT NULL,
    created_by    UUID                                   NOT NULL,
    locale        VARCHAR(10)              DEFAULT 'ru'  NOT NULL,
    created_date  TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    version       INT                      DEFAULT 0     NOT NULL
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
    user_group_id UUID                                   NOT NULL,
    locale        VARCHAR(10)              DEFAULT 'ru'  NOT NULL,
    created_by    UUID                                   NOT NULL,
    created_date  TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    version       INT                      DEFAULT 0     NOT NULL,
    UNIQUE (from_position, bot_id, locale)
);
-- rollback drop table bot_position_info;

-- changeset lonmstalker:create-bot_action_audit-table
CREATE TABLE bot_action_audit
(
    id           UUID                     DEFAULT uuid_generate_v1() PRIMARY KEY,
    position_id  UUID REFERENCES bot_position_info (id) NOT NULL,
    user_id      UUID REFERENCES bot_user_info (id)     NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL
);
-- rollback drop table bot_action_audit;

-- changeset lonmstalker:create-idx_bot_position_info
CREATE INDEX idx_bot_position_info ON bot_position_info (bot_id, from_position);
-- rollback drop index idx_bot_position_info;

-- changeset lonmstalker:create-idx_bot_user_info_tg_id
CREATE INDEX idx_bot_user_info_tg_id ON bot_user_info (bot_id, telegram_id);
-- rollback drop index idx_bot_user_info_tg_id;