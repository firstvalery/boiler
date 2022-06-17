--liquibase formatted sql
--changeset firstvalery:2022-03-18-dml.add_analolog_parameter_table
--comment: define access values
-- object: public.analog_parameter | type: TABLE --
-- DROP TABLE IF EXISTS public.analog_parameter CASCADE;
CREATE TABLE public.analog_parameter (
                                         id smallserial NOT NULL,
                                         name text NOT NULL,
                                         description text NOT NULL,
                                         alarm_min double precision NOT NULL,
                                         lim_min double precision NOT NULL,
                                         lim_max double precision NOT NULL,
                                         alarm_max double precision NOT NULL,
                                         hysteresis double precision NOT NULL,
                                         blur double precision NOT NULL,
                                         data_offset smallint NOT NULL,
                                         CONSTRAINT analog_parameter_pk PRIMARY KEY (id)
);
-- ddl-end --
COMMENT ON COLUMN public.analog_parameter.id IS E'PK';
-- ddl-end --
COMMENT ON COLUMN public.analog_parameter.name IS E'name';
-- ddl-end --
COMMENT ON COLUMN public.analog_parameter.description IS E'description';
-- ddl-end --
COMMENT ON COLUMN public.analog_parameter.alarm_min IS E'alarm minimal setting';
-- ddl-end --
COMMENT ON COLUMN public.analog_parameter.lim_min IS E'limit minimal setting';
-- ddl-end --
COMMENT ON COLUMN public.analog_parameter.lim_max IS E'limit maximal setting';
-- ddl-end --
COMMENT ON COLUMN public.analog_parameter.alarm_max IS E'alarm maximal setting';
-- ddl-end --
COMMENT ON COLUMN public.analog_parameter.hysteresis IS E'hysteresis';
-- ddl-end --
COMMENT ON COLUMN public.analog_parameter.blur IS E'scale blur percentage';
-- ddl-end --
COMMENT ON COLUMN public.analog_parameter.data_offset IS E'offset address in the modbus holding register area';
-- ddl-end --
ALTER TABLE public.analog_parameter OWNER TO postgres;
-- ddl-end --

INSERT INTO public.analog_parameter (id, name, description, alarm_min, lim_min, lim_max, alarm_max, hysteresis, blur, data_offset) VALUES (E'1', E'TT1', E'Т вх ТЭН', E'10', E'15', E'50', E'60', E'1', E'1', E'0');
-- ddl-end --
INSERT INTO public.analog_parameter (id, name, description, alarm_min, lim_min, lim_max, alarm_max, hysteresis, blur, data_offset) VALUES (E'2', E'TT2', E'Т вых ТЭН', E'10', E'15', E'55', E'65', E'1', E'1', E'2');
-- ddl-end --
INSERT INTO public.analog_parameter (id, name, description, alarm_min, lim_min, lim_max, alarm_max, hysteresis, blur, data_offset) VALUES (E'3', E'TT3', E'Т возд', E'10', E'12', E'25', E'30', E'1', E'1', E'4');
-- ddl-end --


