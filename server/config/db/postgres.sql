
 CREATE TABLE entities
(
  id character varying(128) NOT NULL,
  value bytea,
  datatype smallint,
  CONSTRAINT entities_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE entities
  OWNER TO pvpgame;

-- Rule: entities_merge ON entities

-- DROP RULE entities_merge ON entities;

CREATE OR REPLACE RULE entities_merge AS
    ON INSERT TO entities
   WHERE (EXISTS ( SELECT 1
           FROM entities entities_1
          WHERE entities_1.id::text = new.id::text)) DO INSTEAD  UPDATE entities SET value = new.value, datatype = new.datatype
  WHERE entities.id::text = new.id::text;
