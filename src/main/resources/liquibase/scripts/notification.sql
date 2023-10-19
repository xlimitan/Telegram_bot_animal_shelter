-- liquibase formatted sql

-- changeset cod:2

create table  IF NOT EXISTS users_data_table (
id bigserial primary key,
chat_id int8 not null,
first_name text not null,
last_name text,
user_name text not null,
registered_at bytea not null
)