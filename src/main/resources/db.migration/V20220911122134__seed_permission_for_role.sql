set search_path to mhmarket;

delete from role_permission cascade;
delete from permission cascade;
delete from permission_category cascade;
delete from role cascade;

-- Permission Category
insert into permission_category(name, description, created_at)
values ('System Management', 'System Management', now());
insert into permission_category(name, description, created_at)
values ('Market Management', 'Market Management', now());
insert into permission_category(name, description, created_at)
values ('Application Management', 'Application Management', now());
insert into permission_category(name, description, created_at)
values ('View Analytics and Reports', 'View Analytics and Reports', now());

-- Permission
insert into permission(code, name, description, permission_category_id, created_at)
values ('ROLE_MANAGEMENT', 'Role Management', 'Role Management', (select permission_category_id from permission_category where name = 'System Management'), now());
insert into permission(code, name, description, permission_category_id, created_at)
values ('USER_MANAGEMENT', 'User Management', 'User Management', (select permission_category_id from permission_category where name = 'System Management'), now());
insert into permission(code, name, description, permission_category_id, created_at)
values ('APPROVE_CHANGES_IN_SYSTEM_MANAGEMENT', 'Approve Changes in System Management', 'Approve Changes in System Management', (select permission_category_id from permission_category where name = 'System Management'), now());


insert into permission(code, name, description, permission_category_id, created_at)
values ('MARKET_VIEW', 'View', 'View', (select permission_category_id from permission_category where name = 'Market Management'), now());
insert into permission(code, name, description, permission_category_id, created_at)
values ('MARKET_ADD_UPDATE','Add/Update', 'Add/Update', (select permission_category_id from permission_category where name = 'Market Management'), now());
insert into permission(code, name, description, permission_category_id, created_at)
values ('MARKET_DELETE', 'Delete', 'Delete', (select permission_category_id from permission_category where name = 'Market Management'), now());
insert into permission(code, name, description, permission_category_id, created_at)
values ('MARKET_APPROVE_PUBLISH','Approve/Publish', 'Approve/Publish', (select permission_category_id from permission_category where name = 'Market Management'), now());

insert into permission(code, name, description, permission_category_id, created_at)
values ('APPLICATION_SUBMIT', 'Submit', 'Submit', (select permission_category_id from permission_category where name = 'Application Management'), now());
insert into permission(code, name, description, permission_category_id, created_at)
values ('APPLICATION_VIEW','View', 'View', (select permission_category_id from permission_category where name = 'Application Management'), now());
insert into permission(code, name, description, permission_category_id, created_at)
values ('APPLICATION_PROCESS_PAYMENT', 'Process Payment', 'Process Payment', (select permission_category_id from permission_category where name = 'Application Management'), now());

-- Role
-- Role
insert into role(code, name, description, status, created_at)
values('PUBLIC', 'Public Users', 'Public Users', 1, now());
insert into role(code, name, description, status, created_at)
values('COLLECTOR', 'Collector', 'Collector', '1', now());
insert into role(code, name, description, status, created_at)
values('CITY_MARKET_ADMIN', 'City Market Admin', 'City Market Admin', 1, now());
insert into role(code, name, description, status, created_at)
values('SYSTEM_ADMIN', 'System Admin', 'System Admin', 1, now());

-- Role Permission

-- Public User
insert into role_permission(role_id, permission_id )
values((select role_id from role where code = 'PUBLIC'), (select permission_id from permission where code = 'APPLICATION_SUBMIT'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'PUBLIC'), (select permission_id from permission where code = 'APPLICATION_VIEW'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'PUBLIC'), (select permission_id from permission where code = 'APPLICATION_PROCESS_PAYMENT'));

--City Market Admin
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'CITY_MARKET_ADMIN'), (select permission_id from permission where code = 'MARKET_VIEW'));

insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'CITY_MARKET_ADMIN'), (select permission_id from permission where code = 'MARKET_APPROVE_PUBLISH'));

insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'CITY_MARKET_ADMIN'), (select permission_id from permission where code = 'MARKET_DELETE'));

insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'CITY_MARKET_ADMIN'), (select permission_id from permission where code = 'MARKET_ADD_UPDATE'));

insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'CITY_MARKET_ADMIN'), (select permission_id from permission where code = 'APPLICATION_VIEW'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'CITY_MARKET_ADMIN'), (select permission_id from permission where code = 'APPLICATION_SUBMIT'));


--Collector--
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'COLLECTOR'), (select permission_id from permission where code = 'APPLICATION_VIEW'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'COLLECTOR'), (select permission_id from permission where code = 'APPLICATION_SUBMIT'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'COLLECTOR'), (select permission_id from permission where code = 'APPLICATION_PROCESS_PAYMENT'));

--System admin
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'SYSTEM_ADMIN'), (select permission_id from permission where code = 'ROLE_MANAGEMENT'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'SYSTEM_ADMIN'), (select permission_id from permission where code = 'USER_MANAGEMENT'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'SYSTEM_ADMIN'), (select permission_id from permission where code = 'MARKET_VIEW'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'SYSTEM_ADMIN'), (select permission_id from permission where code = 'MARKET_ADD_UPDATE'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'SYSTEM_ADMIN'), (select permission_id from permission where code = 'MARKET_DELETE'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'SYSTEM_ADMIN'), (select permission_id from permission where code = 'APPLICATION_VIEW'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'SYSTEM_ADMIN'), (select permission_id from permission where code = 'APPLICATION_SUBMIT'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'SYSTEM_ADMIN'), (select permission_id from permission where code = 'APPLICATION_PROCESS_PAYMENT'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'SYSTEM_ADMIN'), (select permission_id from permission where code = 'APPROVE_CHANGES_IN_SYSTEM_MANAGEMENT'));



