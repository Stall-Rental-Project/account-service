set search_path to mhmarket;

alter table users drop column divisions;
alter table users drop column external_id;
alter table users drop column market_codes;