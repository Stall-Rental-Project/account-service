set search_path to mhmarket;

create table users
(
    user_id        uuid primary key      default gen_random_uuid(),
    email          varchar(255) not null,
    external_id    varchar(127) not null,
    password       varchar(255) not null default 'P1@zz@2022',
    first_name     varchar(127) not null,
    middle_name    varchar(127) not null default '',
    last_name      varchar(127) not null,
    status         integer      not null default 0,
    market_codes    varchar(255),
        divisions varchar(127),
    created_at       timestamptz  not null default now(),
    updated_at       timestamptz
);

create index users_market_code_idx on users using btree (market_codes);