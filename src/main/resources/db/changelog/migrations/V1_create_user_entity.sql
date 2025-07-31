-- liquibase formatted sql

-- changeset mark:changeSetId:V1_create_user_entity
CREATE TABLE user_entity (
    id bigint AUTO_INCREMENT PRIMARY KEY NOT NULL,
    telegram_id BIGINT NOT NULL,
    language VARCHAR(50) NOT NULL,
    UNIQUE (telegram_id)
);

-- rollback DROP TABLE user_entity;