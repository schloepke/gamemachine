package com.game_machine.orm.models;

import org.javalite.activejdbc.DB;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;

import com.game_machine.core.DbConnectionPool;
import com.zaxxer.hikari.HikariDataSource;

/*
Postgresql

Database/user creation
CREATE ROLE gamemachine WITH ENCRYPTED PASSWORD 'gamemachine' LOGIN CREATEDB CREATEROLE;
CREATE DATABASE gamemachine WITH OWNER=gamemachine ENCODING='UTF8';

Table
CREATE TABLE players (
  id bigserial NOT NULL,

  player_id character varying(128) NOT NULL,

  player_authenticated boolean DEFAULT NULL,

  player_authtoken character varying(128) DEFAULT NULL,

  player_password_hash character varying(128) DEFAULT NULL,

  player_game_id character varying(128) DEFAULT NULL,

  player_role character varying(128) DEFAULT NULL,

  player_locked boolean DEFAULT NULL,

  CONSTRAINT player_pkey PRIMARY KEY (id)
);
alter table players owner to gamemachine;

Mysql

Database/user creation
CREATE DATABASE IF NOT EXISTS gamemachine CHARACTER SET utf8 COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON gamemachine.* TO 'gamemachine'@'%' IDENTIFIED BY 'gamemachine' WITH GRANT OPTION;
FLUSH PRIVILEGES;

Table
CREATE TABLE `players` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `player_id` varchar(128) NOT NULL,

  `player_authenticated` tinyint(4) DEFAULT NULL,

  `player_authtoken` varchar(128) DEFAULT NULL,

  `player_password_hash` varchar(128) DEFAULT NULL,

  `player_game_id` varchar(128) DEFAULT NULL,

  `player_role` varchar(128) DEFAULT NULL,

  `player_locked` tinyint(4) DEFAULT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
*/

public class Player extends Model {
	
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