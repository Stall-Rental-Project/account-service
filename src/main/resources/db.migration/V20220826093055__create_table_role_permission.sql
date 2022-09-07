set search_path to mhmarket;

create table role_permission(
                                role_id         uuid,
                                permission_id   uuid,
                                primary key (role_id, permission_id)
);
-- TODO: foreign key to users table
alter table role_permission add constraint role_permission_role_id foreign key (role_id) references role (role_id);
alter table role_permission add constraint role_permission_permission_id foreign key (permission_id) references permission (permission_id);