set search_path to mhmarket;

create table user_role
(
    user_role_id uuid primary key default gen_random_uuid(),
    user_id      uuid not null,
    role_id      uuid not null,
    created_at   timestamptz      default now()
);

alter table user_role
    add constraint user_role_user_fk foreign key (user_id) references users (user_id) on delete cascade;
alter table user_role
    add constraint user_role_role_fk foreign key (role_id) references role (role_id) on delete cascade;