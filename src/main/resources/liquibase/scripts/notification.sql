-- liquibase formatted sql

-- changeset cod:1

create table  IF NOT EXISTS users_data_table (
id bigserial primary key,
chat_id int8,
first_name varchar(20),
last_name varchar(20),
user_name varchar(20),
registered_at bytea
);

create table if not exists animal(
id bigserial primary key,
animal_type varchar(20),
name varchar(20),
age int4,
breed varchar(20),
path_to_photo varchar(150),
shelter_id int8
);

create table if not exists animal_owner(
id bigserial primary key,
name varchar(20),
phone_number varchar(20),
e_mail varchar(100),
trial_period boolean
);

create table if not exists pet_report(
id bigserial primary key,
diet varchar(150),
feelings varchar(150),
behaviour varchar(150),
check_inf boolean,
date timestamp,
animalowner_id int8
);

create table if not exists shelter(
id bigserial primary key,
shelter_type varchar(150),
shelter_name varchar(150),
address varchar(150),
information varchar(350)
);

create table if not exists volunteer(
id bigserial primary key,
name varchar(100),
phone_number varchar(100)
);
