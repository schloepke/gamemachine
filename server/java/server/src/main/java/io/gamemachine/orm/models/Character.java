package io.gamemachine.orm.models;

import org.javalite.activejdbc.DB;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;

import io.gamemachine.core.DbConnectionPool;
import com.zaxxer.hikari.HikariDataSource;

/*
Postgresql

Database/user creation
CREATE ROLE gamemachine WITH ENCRYPTED PASSWORD 'gamemachine' LOGIN CREATEDB CREATEROLE;
CREATE DATABASE gamemachine WITH OWNER=gamemachine ENCODING='UTF8';

Table
CREATE TABLE characters (
  id bigserial NOT NULL,

  character_id character varying(128) NOT NULL,

  character_uma_data character varying(128) DEFAULT NULL,

  character_health integer DEFAULT NULL,

  character_player_id character varying(128) NOT NULL,

  character_part integer DEFAULT NULL,

  character_parts integer DEFAULT NULL,

  character_worldx integer DEFAULT NULL,

  character_worldy integer DEFAULT NULL,

  character_worldz integer DEFAULT NULL,

  character_zone character varying(128) DEFAULT NULL,

  CONSTRAINT character_pkey PRIMARY KEY (id)
);
alter table characters owner to gamemachine;

Mysql

Database/user creation
CREATE DATABASE IF NOT EXISTS gamemachine CHARACTER SET utf8 COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON gamemachine.* TO 'gamemachine'@'%' IDENTIFIED BY 'gamemachine' WITH GRANT OPTION;
FLUSH PRIVILEGES;

Table
CREATE TABLE `characters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `character_id` varchar(128) NOT NULL,

  `character_uma_data` varchar(128) DEFAULT NULL,

  `character_health` int(11) DEFAULT NULL,

  `character_player_id` varchar(128) NOT NULL,

  `character_part` int(11) DEFAULT NULL,

  `character_parts` int(11) DEFAULT NULL,

  `character_worldx` int(11) DEFAULT NULL,

  `character_worldy` int(11) DEFAULT NULL,

  `character_worldz` int(11) DEFAULT NULL,

  `character_zone` varchar(128) DEFAULT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
*/

public class Character extends Model {
	
	public static void openTransaction() {
		open();
		DB db = new DB("default");
		db.openTransaction();
	}
	
	public static void commitTransaction() {
		DB db = new DB("default");
		db.commitTransaction();
		close();
	}
	
	public static void rollbackTransaction() {
		DB db = new DB("default");
		db.rollbackTransaction();
		close();
	}
	
	public static void close() {
		DB db = new DB("default");
		if(db.hasConnection()) {
			db.close();
		}
	}
	
	public static void open() {
		DB db = new DB("default");
		if(!db.hasConnection()) {
			HikariDataSource ds = DbConnectionPool.getInstance().getDataSource("game_machine_orm");
			db.open(ds);
		}
	}
}