-- create exporter user
CREATE ROLE pg_monitor WITH PASSWORD 'pg_monitor';
GRANT pg_monitor to postgres_exporter;