--liquibase formatted sql
--changeset firstvalery:2022-03-17-dml.define_access_values
--comment: define access values

INSERT INTO public.access_level (id, code, description) VALUES (1, 'ACCESS_DENIED', 'access denied');
-- ddl-end --
INSERT INTO public.access_level (id, code, description) VALUES (2, 'MONITORING_ONLY', 'only monitoring is available');
-- ddl-end --
INSERT INTO public.access_level (id, code, description) VALUES (3, 'FULL_CONTROL', 'full control is available');
-- ddl-end --

