set search_path to mhmarket;

DELETE
FROM properties
WHERE application = 'account-service'
  AND profile = 'dev' ;

-- Port
INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'server.port', '8080');
INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'grpc.server.port', '9565');

-- Datasource
INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'spring.datasource.url',
        'jdbc:postgresql://103.162.20.152:5432/mhmarketaccount');
INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'spring.datasource.username', 'postgres');
INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'spring.datasource.password', '123456');
INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'spring.datasource.hikari.schema', 'mhmarket');

INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'spring.jpa.properties.hibernate.default_schema', 'mhmarket');
--
INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'spring.kafka.bootstrap-servers', '103.162.20.152:9092');
INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'spring.kafka.consumer.group-id', 'account-service-group');
INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'spring.jpa.properties.hibernate.metadata_builder_contributor', 'com.srs.account.config.QueryDslContributor');


INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'spring.redis.host', '103.162.20.152');
INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'spring.redis.port', '6379');

INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'spring.mail.host', 'smtp.gmail.com');
INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'spring.mail.port', '587');
INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'spring.mail.username', 'hoanggg2110@gmail.com');


INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'spring.mail.default-cc-email', 'hoanggg2110@gmail.com,ngohoang211020@gmail.com');
INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'spring.mail.default-sender', 'hoanggg2110@gmail.com');

INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'spring.mail.password', 'ulypdgvuguptufdj');
INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'spring.mail.properties.mail.transport.protocol', 'stmt');
INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'spring.mail.properties.mail.smtp.auth', 'true');


INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'spring.mail.properties.mail.smtp.starttls.enable', 'true');
INSERT INTO properties (application, profile, label, "KEY", "VALUE")
VALUES ('account-service', 'dev', 'master', 'spring.mail.properties.mail.stmt.ssl.enable', 'true');