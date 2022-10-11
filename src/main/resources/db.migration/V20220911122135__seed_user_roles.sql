set search_path to mhmarket;

delete
from users;

insert into users (email, first_name, last_name, middle_name, status,created_at)
values ('PublicUserCityOwned1@gmail.com', 'Arnel', 'PublicUserCityOwned1','Marnel', 1,now()),
       ('PublicUserCityOwned2@gmail.com', 'Eduard', 'PublicUserCityOwned2','remorin', 1,now()),
       ('Collector@gmail.com', 'Ariel', 'Collector1','Fajaron', 1,now()),
       ('CityMarketAddmin1@gmail.com', 'Jonald', 'CityMarketAdmin1', '',1,now()),
       ('CityMarketAddmin2@gmail.com', 'Michael', 'CityMarketAdmin2', 'Atienza', 1,now()),
       ('SystemAddmin1@gmail.com', 'Ngo', 'Hoang', '',1,now());

drop procedure seed_user_role_for_test_users();
drop procedure add_permission_for_system_admin();
drop procedure add_permission_for_cma();

create or replace procedure seed_user_role_for_test_users()
    language plpgsql
as
$$
declare
    root_id uuid;
    uid     uuid;
    rid     uuid;
    pid     uuid;
begin
    select role_id into rid from role where code = 'SYSTEM_ADMIN';

    for uid in (select user_id from users where email in ('SystemAddmin1@gmail.com'))
        loop
            insert into user_role (user_id, role_id) values (uid, rid);
        end loop;

    select role_id into rid from role where code = 'CITY_MARKET_ADMIN';

    for uid in (select user_id from users where email like ('CityMarketAddmin%'))
        loop
            insert into user_role (user_id, role_id) values (uid, rid);
        end loop;

    select role_id into rid from role where code = 'COLLECTOR';

    for uid in (select user_id from users where email like ('Collector%'))
        loop
            insert into user_role (user_id, role_id) values (uid, rid);
        end loop;

    select role_id into rid from role where code = 'PUBLIC';

    for uid in (select user_id from users where email like ('PublicUserCity%'))
        loop
            insert into user_role (user_id, role_id) values (uid, rid);
        end loop;
end;
$$;

call seed_user_role_for_test_users();

