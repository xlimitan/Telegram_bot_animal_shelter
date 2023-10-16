-- liquibase formatted sql

-- changeset golenko:1

create table notification_task (
id bigserial primary key,
chat_id int8 not null,
firstName text not null,
lastName text not null,
userName text not null,
registered_at timestamp not null
)