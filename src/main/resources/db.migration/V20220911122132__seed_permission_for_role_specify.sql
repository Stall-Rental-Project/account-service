set search_path to mhmarket;

create or replace procedure add_permission_for_system_admin()
    language plpgsql
as
$$
declare
    pid uuid;
    rid uuid;
begin
    select role_id into rid from role where code = 'SYSTEM_ADMIN';
    for pid in (select permission_id from permission)
        loop
            insert into role_permission (role_id, permission_id)
            values (rid, pid);
        end loop;
end;
$$;

call add_permission_for_system_admin();

create or replace procedure add_permission_for_cma()
    language plpgsql
as
$$
declare
    pid uuid;
    rid uuid;
begin
    select role_id into rid from role where code = 'CITY_MARKET_ADMIN';
    for pid in (select permission_id from permission)
        loop
            insert into role_permission (role_id, permission_id)
            values (rid, pid);
        end loop;
end;
$$;

call add_permission_for_cma();

