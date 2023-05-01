-- create exporter user
CREATE ROLE pg_monitor WITH PASSWORD 'pg_monitor';
GRANT pg_monitor to postgres_exporter;

-- create service user в бд spring_kube
CREATE ROLE logic_user WITH PASSWORD 'logic_user';
GRANT CONNECT ON DATABASE spring_kube TO logic_user;
GRANT SELECT, INSERT, DELETE, UPDATE ON ALL TABLES IN SCHEMA public TO logic_user;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres
    GRANT SELECT, INSERT, DELETE, UPDATE ON TABLES TO logic_user;

-- create auth user в бд spring_kube_auth
CREATE ROLE auth_user WITH PASSWORD 'auth_user';
GRANT CONNECT ON DATABASE spring_kube_auth TO auth_user;
GRANT SELECT, INSERT, DELETE, UPDATE ON ALL TABLES IN SCHEMA public TO auth_user;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres
    GRANT SELECT, INSERT, DELETE, UPDATE ON TABLES TO auth_user;