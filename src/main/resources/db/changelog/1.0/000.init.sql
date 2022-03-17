--liquibase formatted sql
--changeset vik:2022-03-17-initial-script
--comment: create init tables


-- object: public.access_level_id_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.access_level_id_seq CASCADE;
CREATE SEQUENCE public.access_level_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 32767
    START WITH 1
    CACHE 1
	NO CYCLE
	OWNED BY NONE;

-- ddl-end --
ALTER SEQUENCE public.access_level_id_seq OWNER TO postgres;
-- ddl-end --

-- object: public.access_level | type: TABLE --
-- DROP TABLE IF EXISTS public.access_level CASCADE;
CREATE TABLE public.access_level (
                                     id smallint NOT NULL DEFAULT nextval('public.access_level_id_seq'::regclass),
                                     code text NOT NULL,
                                     description text NOT NULL,
                                     CONSTRAINT access_level_pkey PRIMARY KEY (id)
);
-- ddl-end --
ALTER TABLE public.access_level OWNER TO postgres;
-- ddl-end --

-- object: public.crtd_at | type: DOMAIN --
-- DROP DOMAIN IF EXISTS public.crtd_at CASCADE;
CREATE DOMAIN public.crtd_at AS bigint
    DEFAULT ((date_part('epoch'::text, now()) * (1000)::double precision))::bigint;
-- ddl-end --
ALTER DOMAIN public.crtd_at OWNER TO postgres;
-- ddl-end --

-- object: public.telegram | type: TABLE --
-- DROP TABLE IF EXISTS public.telegram CASCADE;
CREATE TABLE public.telegram (
                                 id uuid NOT NULL,
                                 chat_it text NOT NULL,
                                 username text NOT NULL,
                                 secret_key text,
                                 enable boolean,
                                 access_level_id smallint NOT NULL,
                                 created_at public.crtd_at NOT NULL,
                                 CONSTRAINT telegram_pkey PRIMARY KEY (id),
                                 CONSTRAINT uq_telegram_tbl_chat_id UNIQUE (chat_it)
);
-- ddl-end --
ALTER TABLE public.telegram OWNER TO postgres;
-- ddl-end --

-- object: fk_telegram_tbl_access_level_id | type: CONSTRAINT --
-- ALTER TABLE public.telegram DROP CONSTRAINT IF EXISTS fk_telegram_tbl_access_level_id CASCADE;
ALTER TABLE public.telegram ADD CONSTRAINT fk_telegram_tbl_access_level_id FOREIGN KEY (access_level_id)
    REFERENCES public.access_level (id) MATCH SIMPLE
    ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: "grant_CU_eb94f049ac" | type: PERMISSION --
GRANT CREATE,USAGE
   ON SCHEMA public
   TO postgres;
-- ddl-end --

-- object: "grant_CU_cd8e46e7b6" | type: PERMISSION --
GRANT CREATE,USAGE
   ON SCHEMA public
   TO PUBLIC;
-- ddl-end --



