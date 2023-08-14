set search_path to mhmarket;
create extension if not exists pgcrypto;

create table role (
                      role_id         uuid primary key default gen_random_uuid(),
                      code            varchar(255) not null,
                      name            varchar(255) not null,
                      description     text,
                      status          integer     not null, -- 0 -> inactive, 1 -> active
                      created_at      timestamptz not null,
                      updated_at      timestamptz,
                      initiator_id    uuid not null,
                      modifier_id     uuid,
                      unique (code)
);
-- TODO: foreign key to users table