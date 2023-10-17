-- liquibase formatted sql

-- changeset golenko:1

create table users_data_table (
id bigserial primary key,
chat_id int8 not null,
first_name text not null,
last_name text not null,
user_name text not null,
registered_at bytea not null
)