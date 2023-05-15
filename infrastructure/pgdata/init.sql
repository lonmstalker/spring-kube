-- create exporter user
CREATE ROLE postgres_exporter WITH PASSWORD 'postgres_exporter';
GRANT CONNECT ON DATABASE spring_kube TO postgres_exporter;
GRANT pg_monitor to postgres_exporter; -- pg_monitor reserved
ALTER ROLE "postgres_exporter" WITH LOGIN;

-- create service user в бд spring_kube
CREATE ROLE logic_user WITH PASSWORD 'logic_user';
GRANT CONNECT ON DATABASE spring_kube TO logic_user;
GRANT SELECT, INSERT, DELETE, UPDATE ON ALL TABLES IN SCHEMA public TO logic_user;
ALTER ROLE "logic_user" WITH LOGIN;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres
    GRANT SELECT, INSERT, DELETE, UPDATE ON TABLES TO logic_user;

-- create auth user в бд spring_kube_auth
CREATE DATABASE spring_kube_auth;
CREATE ROLE auth_user WITH PASSWORD 'auth_user';
GRANT CONNECT ON DATABASE spring_kube_auth TO auth_user;
GRANT SELECT, INSERT, DELETE, UPDATE ON ALL TABLES IN SCHEMA public TO auth_user;
ALTER ROLE "auth_user" WITH LOGIN;
ALTER DEFAULT PRIVILEGES FOR ROLE postgres
    GRANT SELECT, INSERT, DELETE, UPDATE ON TABLES TO auth_user;