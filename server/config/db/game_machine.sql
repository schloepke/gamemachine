
// Mysql
DROP TABLE IF EXISTS `entities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entities` (
  `id` varchar(128) NOT NULL,
  `value` varbinary(2048) DEFAULT NULL,
  `datatype` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


// Postgresql
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
  OWNER TO gamemachine;

CREATE OR REPLACE RULE entities_merge AS
    ON INSERT TO entities
   WHERE (EXISTS ( SELECT 1
           FROM entities entities_1
          WHERE entities_1.id::text = new.id::text)) DO INSTEAD  UPDATE entities SET value = new.value, datatype = new.datatype
  WHERE entities.id::text = new.id::text;

// Gamecloud
CREATE OR REPLACE RULE entities_merge AS
    ON INSERT TO entities
   WHERE (EXISTS ( SELECT 1
           FROM entities entities_1
          WHERE entities_1.id::text = new.id::text AND entities_1.user_id::text = new.user_id::text)) DO INSTEAD  UPDATE entities SET value = new.value, datatype = new.datatype
  WHERE entities.id::text = new.id::text AND entities.user_id::text = new.user_id::text;




  // Postgresql non multi tenant
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