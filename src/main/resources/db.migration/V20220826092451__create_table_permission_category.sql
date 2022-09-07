set search_path to mhmarket;

create table permission_category (
                                     permission_category_id uuid primary key default gen_random_uuid(),
                                     name            varchar(255) not null,
                                     description     text,
                                     created_at      timestamptz not null,
                                     updated_at      timestamptz
);

-- TODO: foreign key to users table