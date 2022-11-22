set search_path to mhmarket;

insert into permission_category(name, description, created_at)
values ('Lease Management', 'Lease Management', now());

insert into permission(code, name, description, permission_category_id, created_at)
values ('TERMINATE_LEASE', 'Terminate Lease', 'Terminate Lease', (select permission_category_id from permission_category where name = 'Lease Management'), now());
insert into permission(code, name, description, permission_category_id, created_at)
values ('CANCEL_TERMINATION_REQUEST', 'Cancel terminate lease', 'Cancel terminate lease', (select permission_category_id from permission_category where name = 'Lease Management'), now());

insert into role_permission(role_id, permission_id )
values((select role_id from role where code = 'CITY_MARKET_ADMIN'), (select permission_id from permission where code = 'TERMINATE_LEASE'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'SYSTEM_ADMIN'), (select permission_id from permission where code = 'TERMINATE_LEASE'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'PUBLIC'), (select permission_id from permission where code = 'TERMINATE_LEASE'));

insert into role_permission(role_id, permission_id )
values((select role_id from role where code = 'CITY_MARKET_ADMIN'), (select permission_id from permission where code = 'CANCEL_TERMINATION_REQUEST'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'SYSTEM_ADMIN'), (select permission_id from permission where code = 'CANCEL_TERMINATION_REQUEST'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'PUBLIC'), (select permission_id from permission where code = 'CANCEL_TERMINATION_REQUEST'));
