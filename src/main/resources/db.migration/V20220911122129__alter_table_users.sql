set search_path to mhmarket;

alter table users
    add column market_codes text;
create index users_market_code_idx on users using btree (market_codes);

ALTER TABLE users
    ADD CONSTRAINT email_unique UNIQUE (email);
