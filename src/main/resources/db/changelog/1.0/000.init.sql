--liquibase formatted sql
--changeset firstvalery:2022-03-17-initial-script
--comment: create init tables


CREATE SEQUENCE public.access_level_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 32767
    START WITH 1
    CACHE 1
	NO CYCLE
	OWNED BY NONE;


ALTER SEQUENCE public.access_level_id_seq OWNER TO postgres;

CREATE TABLE public.access_level (id smallserial NOT NULL ,
                                     code text NOT NULL,
                                     description text NOT NULL,
                                     CONSTRAINT access_level_pkey PRIMARY KEY (id)
);

ALTER TABLE public.access_level OWNER TO postgres;



CREATE DOMAIN public.crtd_at AS bigint
    DEFAULT ((date_part('epoch'::text, now()) * (1000)::double precision))::bigint;

ALTER DOMAIN public.crtd_at OWNER TO postgres;


CREATE TABLE public.telegram_user (
                                 id uuid NOT NULL,
                                 chat_id text NOT NULL,
                                 username text NOT NULL,
                                 access_level_id smallint NOT NULL,
                                 retry smallint NOT NULL,
                                 created_at public.crtd_at NOT NULL,
                                 CONSTRAINT telegram_user_pkey PRIMARY KEY (id),
                                 CONSTRAINT uq_telegram_user_tbl_chat_id UNIQUE (chat_id)
);

ALTER TABLE public.telegram_user OWNER TO postgres;



ALTER TABLE public.telegram_user ADD CONSTRAINT fk_telegram_user_tbl_access_level_id FOREIGN KEY (access_level_id)
    REFERENCES public.access_level (id) MATCH SIMPLE
    ON DELETE NO ACTION ON UPDATE NO ACTION;

GRANT CREATE,USAGE
   ON SCHEMA public
   TO postgres;

GRANT CREATE,USAGE
   ON SCHEMA public
   TO PUBLIC;




