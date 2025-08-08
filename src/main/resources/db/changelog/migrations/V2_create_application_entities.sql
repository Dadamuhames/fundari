-- liquibase formatted sql

-- changeset mark:changeSetId:V2.1-create-application-entity-table
CREATE TABLE application_entity (
    id bigint AUTO_INCREMENT PRIMARY KEY NOT NULL,
    user_id BIGINT NOT NULL,
    project_name VARCHAR(255) NOT NULL,
    project_type ENUM('BUSINESS', 'STARTUP', 'IDEA') NOT NULL,
    business_application_id BIGINT,
    startup_application_id BIGINT,
    idea_application_id BIGINT,
    created_at DATETIME NOT NULL,
    UNIQUE (business_application_id),
    UNIQUE (startup_application_id),
    UNIQUE (idea_application_id)
);
-- rollback DROP TABLE application_entity;

-- changeset mark:changeSetId:V2.2-create-business-application-entity-table
CREATE TABLE business_application_entity (
    id bigint AUTO_INCREMENT PRIMARY KEY NOT NULL,
    application_id BIGINT NOT NULL,
    industry VARCHAR(255) NOT NULL,
    business_age VARCHAR(255) NOT NULL,
    avg_monthly_profit DECIMAL(19,4) NOT NULL,
    net_profit DECIMAL(19,4) NOT NULL,
    estimate_value_of_assets DECIMAL(19,4) DEFAULT NULL,
    team_size INT NOT NULL,
    has_assets BOOLEAN NOT NULL,
    has_debts_or_loans BOOLEAN NOT NULL,
    region_of_activity VARCHAR(255) NOT NULL,
    UNIQUE (application_id)
);
-- rollback DROP TABLE business_application_entity;

-- changeset mark:changeSetId:V2.3-create-idea-application-entity-table
CREATE TABLE idea_application_entity (
    id bigint AUTO_INCREMENT PRIMARY KEY NOT NULL,
    application_id BIGINT NOT NULL,
    description VARCHAR(255) NOT NULL,
    target ENUM('INDIVIDUALS', 'BUSINESSES', 'GOVERNMENT', 'GLOBAL'),
    has_team BOOLEAN,
    is_concept_only BOOLEAN,
    is_there_similar_products BOOLEAN,
    has_investors BOOLEAN,
    difference TEXT(5000) DEFAULT NULL,
    UNIQUE (application_id)
);
-- rollback DROP TABLE idea_application_entity;

-- changeset mark:changeSetId:V2.4-create-startup-application-entity-table
CREATE TABLE startup_application_entity (
    id bigint AUTO_INCREMENT PRIMARY KEY NOT NULL,
    application_id BIGINT NOT NULL,
    stage ENUM('IDEA', 'FIRST_CLIENTS', 'MVP', 'REVENUE', 'SCALING'),
    description VARCHAR(255) NOT NULL,
    last_month_revenue DECIMAL(19,4),
    active_user_count INT NOT NULL,
    invested_money_amount DECIMAL(19,4),
    team_size INT NOT NULL,
    competitors VARCHAR(255),
    region_of_activity VARCHAR(255) NOT NULL,
    UNIQUE (application_id)
);
-- rollback DROP TABLE startup_application_entity;

-- changeset mark:changeSetId:V2.5-add-foreign-keys-to-application-entity
ALTER TABLE application_entity
    ADD CONSTRAINT fk_application_user FOREIGN KEY (user_id) REFERENCES user_entity(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_business_app FOREIGN KEY (business_application_id) REFERENCES business_application_entity(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_startup_app FOREIGN KEY (startup_application_id) REFERENCES startup_application_entity(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_idea_app FOREIGN KEY (idea_application_id) REFERENCES idea_application_entity(id) ON DELETE CASCADE;

-- changeset mark:changeSetId:V2.6-add-foreign-keys-to-app-type-business
ALTER TABLE business_application_entity
    ADD CONSTRAINT fk_business_application FOREIGN KEY (application_id) REFERENCES application_entity(id) ON DELETE CASCADE;

-- changeset mark:changeSetId:V2.7-add-foreign-keys-to-app-type-idea
ALTER TABLE idea_application_entity
    ADD CONSTRAINT fk_idea_application FOREIGN KEY (application_id) REFERENCES application_entity(id) ON DELETE CASCADE;

-- changeset mark:changeSetId:V2.8-add-foreign-keys-to-app-type-startup
ALTER TABLE startup_application_entity
    ADD CONSTRAINT fk_startup_application FOREIGN KEY (application_id) REFERENCES application_entity(id) ON DELETE CASCADE;
