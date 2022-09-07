set search_path to mhmarket;

create table permission
(
    permission_id          uuid primary key default gen_random_uuid(),
    code                   varchar(255) not null,
    name                   varchar(255) not null,
    description            text,
    permission_category_id uuid         not null,
    unique (code),
    display_order          int
);

alter table permission
    add constraint permission_permission_category_id foreign key (permission_category_id) references permission_category (permission_category_id);

-- TODO: foreign key to users table