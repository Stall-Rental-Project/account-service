set search_path to mhmarket;

insert into permission(code, name, description, permission_category_id, created_at)
values ('VIEW_ANALYTICS', 'View Analytics', 'View Analytics', (select permission_category_id from permission_category where name = 'System Management'), now());

insert into role_permission(role_id, permission_id )
values((select role_id from role where code = 'CITY_MARKET_ADMIN'), (select permission_id from permission where code = 'VIEW_ANALYTICS'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'SYSTEM_ADMIN'), (select permission_id from permission where code = 'VIEW_ANALYTICS'));

insert into permission(code, name, description, permission_category_id, created_at)
values ('MARKET_LEASE_VIEW', 'View list lease', 'View list lease', (select permission_category_id from permission_category where name = 'Lease Management'), now());

insert into role_permission(role_id, permission_id )
values((select role_id from role where code = 'CITY_MARKET_ADMIN'), (select permission_id from permission where code = 'MARKET_LEASE_VIEW'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'SYSTEM_ADMIN'), (select permission_id from permission where code = 'MARKET_LEASE_VIEW'));
insert into role_permission(role_id, permission_id )
values((select role_id from role where code = 'PUBLIC'), (select permission_id from permission where code = 'MARKET_LEASE_VIEW'));
