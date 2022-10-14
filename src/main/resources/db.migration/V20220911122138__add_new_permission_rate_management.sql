set search_path to mhmarket;

insert into permission(code, name, description, permission_category_id, created_at)
values ('RATE_MANAGEMENT', 'Rate Management', 'Rate Management', (select permission_category_id from permission_category where name = 'System Management'), now());

insert into role_permission(role_id, permission_id )
values((select role_id from role where code = 'CITY_MARKET_ADMIN'), (select permission_id from permission where code = 'RATE_MANAGEMENT'));
insert into role_permission(role_id, permission_id)
values((select role_id from role where code = 'SYSTEM_ADMIN'), (select permission_id from permission where code = 'RATE_MANAGEMENT'));
