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
CREATE TABLE guild_actions (
  id bigserial NOT NULL,

  guild_action_action character varying(128) NOT NULL,

  guild_action_to character varying(128) DEFAULT NULL,

  guild_action_from character varying(128) DEFAULT NULL,

  guild_action_response character varying(128) DEFAULT NULL,

  guild_action_guild_id character varying(128) DEFAULT NULL,

  guild_action_invite_id character varying(128) DEFAULT NULL,

  guild_action_guild_name character varying(128) DEFAULT NULL,

  CONSTRAINT guild_action_pkey PRIMARY KEY (id)
);
alter table guild_actions owner to gamemachine;

Mysql

Database/user creation
CREATE DATABASE IF NOT EXISTS gamemachine CHARACTER SET utf8 COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON gamemachine.* TO 'gamemachine'@'%' IDENTIFIED BY 'gamemachine' WITH GRANT OPTION;
FLUSH PRIVILEGES;

Table
CREATE TABLE `guild_actions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `guild_action_action` varchar(128) NOT NULL,

  `guild_action_to` varchar(128) DEFAULT NULL,

  `guild_action_from` varchar(128) DEFAULT NULL,

  `guild_action_response` varchar(128) DEFAULT NULL,

  `guild_action_guild_id` varchar(128) DEFAULT NULL,

  `guild_action_invite_id` varchar(128) DEFAULT NULL,

  `guild_action_guild_name` varchar(128) DEFAULT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
*/

public class GuildAction extends Model {
	
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