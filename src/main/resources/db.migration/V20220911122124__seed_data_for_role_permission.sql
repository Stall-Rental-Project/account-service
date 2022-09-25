set search_path to mhmarket;

-- Permission Category
insert into permission_category(name, description, created_at)
values ('Market Maintenance', 'Market Maintenance', now());
insert into permission_category(name, description, created_at)
values ('Application Maintenance', 'Application Maintenance', now());
insert into permission_category(name, description, created_at)
values ('Market Lease', 'Market Lease', now());
insert into permission_category(name, description, created_at)
values ('View Analytics and Reports', 'View Analytics and Reports', now());
insert into permission_category(name, description, created_at)
values ('System Management', 'System Management', now());


alter table permission add column created_at timestamptz not null default now();
alter table permission add column updated_at timestamptz;

-- Permission
insert into permission(code, name, description, permission_category_id, created_at)
values ('SEARCH_MARKET', 'Search Market', 'Search Market', (select permission_category_id from permission_category where name = 'Market Maintenance'), now());
insert into permission(code, name, description, permission_category_id, created_at)
values ('VIEW_MARKET', 'View Market', 'View Market', (select permission_category_id from permission_category where name = 'Market Maintenance'), now());
insert into permission(code, name, description, permission_category_id, created_at)
values ('MANAGE_MARKET','Manage Market', 'Manage Market', (select permission_category_id from permission_category where name = 'Market Maintenance'), now());
insert into permission(code, name, description, permission_category_id, created_at)
values ('APPROVE_PUBLISH_CHANGES', 'Approve/Publish changes', 'Approve/Publish changes', (select permission_category_id from permission_category where name = 'Market Maintenance'), now());

insert into permission(code, name, description, permission_category_id, created_at)
values ('SUBMIT_APPLICATION', 'Submit Application', 'Submit Application', (select permission_category_id from permission_category where name = 'Application Maintenance'), now());
insert into permission(code, name, description, permission_category_id, created_at)
values ('SEARCH_APPLICATION','Search Application', 'Search Application', (select permission_category_id from permission_category where name = 'Application Maintenance'), now());
insert into permission(code, name, description, permission_category_id, created_at)
values ('VIEW_EDIT_APPLICATION', 'View/Edit Application', 'View/Edit Application', (select permission_category_id from permission_category where name = 'Application Maintenance'), now());


insert into permission(code, name, description, permission_category_id, created_at)
values ('SEARCH_MARKET_LEASE','Search Market Lease', 'Search Market Lease', (select permission_category_id from permission_category where name = 'Market Lease'), now());
insert into permission(code, name, description, permission_category_id, created_at)
values ('VIEW_MARKET_LEASE', 'View Market Lease', 'View Market Lease', (select permission_category_id from permission_category where name = 'Market Lease'), now());
insert into permission(code, name, description, permission_category_id, created_at)
values ('TERMINATE_LEASE','Terminate Lease', 'Terminate Lease', (select permission_category_id from permission_category where name = 'Market Lease'), now());
insert into permission(code, name, description, permission_category_id, created_at)
values ('MAKE_PAYMENT', 'Make Payment', 'Make Payment', (select permission_category_id from permission_category where name = 'Market Lease'), now());
insert into permission(code, name, description, permission_category_id, created_at)
values ('CONFIRM_PAYMENT','Confirm Payment', 'Confirm Payment', (select permission_category_id from permission_category where name = 'Market Lease'), now());


insert into permission(code, name, description, permission_category_id, created_at)
values ('VIEW_ANALYTICS_AND_REPORTS','View Analytics and Reports', 'View Analytics and Reports', (select permission_category_id from permission_category where name = 'View Analytics and Reports'), now());


insert into permission(code, name, description, permission_category_id, created_at)
values ('ROLE_MANAGEMENT','Role Management', 'Role Management', (select permission_category_id from permission_category where name = 'System Management'), now());
insert into permission(code, name, description, permission_category_id, created_at)
values ('USER_MANAGEMENT', 'User Management', 'User Management', (select permission_category_id from permission_category where name = 'System Management'), now());
insert into permission(code, name, description, permission_category_id, created_at)
values ('RATE_MANAGEMENT', 'Rate Management', 'Rate Management', (select permission_category_id from permission_category where name = 'System Management'), now());

alter table role drop COLUMN initiator_id;
alter table role drop COLUMN modifier_id;

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
insert into role_permission(role_id, permission_id )
values((select role_id from role where code = 'PUBLIC'), (select permission_id from permission where code = 'SUBMIT_APPLICATION'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'PUBLIC'), (select permission_id from permission where code = 'SEARCH_APPLICATION'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'PUBLIC'), (select permission_id from permission where code = 'VIEW_EDIT_APPLICATION'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'PUBLIC'), (select permission_id from permission where code = 'SEARCH_MARKET_LEASE'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'PUBLIC'), (select permission_id from permission where code = 'VIEW_MARKET_LEASE'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'PUBLIC'), (select permission_id from permission where code = 'MAKE_PAYMENT'));
