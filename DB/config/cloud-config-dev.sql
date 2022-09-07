set search_path to emarketconfig;

DELETE
FROM ms_properties
WHERE application = 'account-service'
  AND profile = 'dev';

-- Port
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'server.port', '8080');
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'grpc.server.port', '6565');

-- Datasource
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'spring.datasource.url',
        'jdbc:postgresql://192.168.200.16:5432/emarketaccount');
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'spring.datasource.username', 'postgres');
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'spring.datasource.password', '2110');
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'spring.datasource.hikari.schema', 'emarket');

INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'spring.jpa.properties.hibernate.default_schema', 'emarket');
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'spring.jpa.properties.hibernate.metadata_builder_contributor',
        'com.banvien.emarket.account.config.QueryDslContributor');

INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'spring.redis.host', '192.168.200.16');
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'spring.redis.port', '6379');
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'spring.redis.password', '2110');

INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'spring.kafka.bootstrap-servers', '192.168.200.45:32107');
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'spring.kafka.consumer.group-id', 'emarket-account-service-group');

-- AWS S3
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'aws.s3.bucket', 'bv-emarket');
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'aws.s3.content-category', 'dev');
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'aws.ses.default-sender', 'emarket@oplacrm.com');
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'aws.ses.api-base-url', 'https://api-dev-emarket.banvien.com.vn');
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'aws.ses.web-base-url', 'https://dev-emarket.banvien.com.vn');
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'aws.ses.default-cc-emails',
        'man.dang-thi@banvien.com.vn');

-- Executor
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'executor.core-threads', '4');
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'executor.max-threads', '20');
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'executor.keep-alive-secs', '10');

-- Task Scheduler
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'task-scheduler.pool-size', '2');

-- Application customized config
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'signatory.signature-max-size', '2097152');

INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'qceservice.app-id',
        '5e14eda74c26431e64de63352401df69.unifysyscontrol.com');
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'qceservice.app-secret',
        '75c76ed99ff442f1797931604647b004b091b0f83bec0073d12cc1c589f18791');
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'qceservice.app-endpoint', 'https://qaqclb.unifysyscontrol.com/web-ums');
INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'qceservice.api-max-attempts', '3');

INSERT INTO ms_properties (application, profile, label, key, value)
VALUES ('account-service', 'dev', 'master', 'emarket.api-base-url', 'https://api-dev-emarket.banvien.com.vn');