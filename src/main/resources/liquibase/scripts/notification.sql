-- liquibase formatted sql

-- changeset cod:2

create table  IF NOT EXISTS users_data_table (
chat_id bigserial primary key,
first_name varchar(20),
last_name varchar(20),
user_name varchar(20),
phone_number varchar(20),
e_Mail varchar(100),
animal_id int8,
date DATE,
trial_period boolean,
period int4
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
trial_period boolean
);

create table if not exists pet_report(
id bigserial primary key,
report text,
date DATE,
correct boolean,
user_id int8,
photo_id int8
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
create table if not exists photo(
id bigserial primary key,
file_path varchar(1024),
file_size bigserial,
media_type varchar(1024),
data bytea,
pet_report_id int8,
animal_id int8
);
create table if not exists photo_report(
id bigserial primary key,
user_id int8,
date DATE,
path text
)

