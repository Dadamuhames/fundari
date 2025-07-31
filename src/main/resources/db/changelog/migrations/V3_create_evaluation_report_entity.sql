-- liquibase formatted sql

-- changeset mark:changeSetId:V3_create_evaluation_report_entity
CREATE TABLE evaluation_report_entity (
    id bigint AUTO_INCREMENT PRIMARY KEY NOT NULL,
    report_json TEXT(20000) NOT NULL,
    created_at DATETIME NOT NULL,
    application_id BIGINT NOT NULL,
    UNIQUE (application_id)
);

-- rollback DROP TABLE evaluation_report_entity;

-- changeset mark:changeSetId:V3.1_add_application_report_connection
ALTER TABLE evaluation_report_entity
    ADD CONSTRAINT fk_evaluation_report FOREIGN KEY (application_id) REFERENCES application_entity(id) ON DELETE CASCADE;
