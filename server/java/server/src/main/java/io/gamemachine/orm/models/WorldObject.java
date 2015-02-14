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
CREATE TABLE world_objects (
  id bigserial NOT NULL,

  world_object_player_item_id character varying(128) DEFAULT NULL,

  world_object_action integer DEFAULT NULL,

  world_object_id character varying(128) DEFAULT NULL,

  world_object_owner_id character varying(128) DEFAULT NULL,

  world_object_x integer DEFAULT NULL,

  world_object_y integer DEFAULT NULL,

  world_object_z integer DEFAULT NULL,

  world_object_rx integer DEFAULT NULL,

  world_object_ry integer DEFAULT NULL,

  world_object_rz integer DEFAULT NULL,

  world_object_rw integer DEFAULT NULL,

  world_object_max_health integer DEFAULT NULL,

  world_object_health integer DEFAULT NULL,

  world_object_parent_id character varying(128) DEFAULT NULL,

  world_object_destructable boolean DEFAULT NULL,

  CONSTRAINT world_object_pkey PRIMARY KEY (id)
);
alter table world_objects owner to gamemachine;

Mysql

Database/user creation
CREATE DATABASE IF NOT EXISTS gamemachine CHARACTER SET utf8 COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON gamemachine.* TO 'gamemachine'@'%' IDENTIFIED BY 'gamemachine' WITH GRANT OPTION;
FLUSH PRIVILEGES;

Table
CREATE TABLE `world_objects` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `world_object_player_item_id` varchar(128) DEFAULT NULL,

  `world_object_action` int(11) DEFAULT NULL,

  `world_object_id` varchar(128) DEFAULT NULL,

  `world_object_owner_id` varchar(128) DEFAULT NULL,

  `world_object_x` int(11) DEFAULT NULL,

  `world_object_y` int(11) DEFAULT NULL,

  `world_object_z` int(11) DEFAULT NULL,

  `world_object_rx` int(11) DEFAULT NULL,

  `world_object_ry` int(11) DEFAULT NULL,

  `world_object_rz` int(11) DEFAULT NULL,

  `world_object_rw` int(11) DEFAULT NULL,

  `world_object_max_health` int(11) DEFAULT NULL,

  `world_object_health` int(11) DEFAULT NULL,

  `world_object_parent_id` varchar(128) DEFAULT NULL,

  `world_object_destructable` tinyint(4) DEFAULT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
*/

public class WorldObject extends Model {
	
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