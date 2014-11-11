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
CREATE TABLE player_items (
  id bigserial NOT NULL,

  player_item_id character varying(128) NOT NULL,

  player_item_name character varying(128) NOT NULL,

  player_item_quantity integer NOT NULL,

  player_item_color character varying(128) DEFAULT NULL,

  player_item_player_id character varying(128) DEFAULT NULL,

  weapon_attack integer DEFAULT NULL,
  	
  weapon_delay integer DEFAULT NULL,

  consumable_type character varying(128) DEFAULT NULL,
  	
  consumable_size character varying(128) DEFAULT NULL,

  cost_amount double precision DEFAULT NULL,
  	
  cost_currency character varying(128) DEFAULT NULL,

  CONSTRAINT player_item_pkey PRIMARY KEY (id)
);
alter table player_items owner to gamemachine;

Mysql

Database/user creation
CREATE DATABASE IF NOT EXISTS gamemachine CHARACTER SET utf8 COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON gamemachine.* TO 'gamemachine'@'%' IDENTIFIED BY 'gamemachine' WITH GRANT OPTION;
FLUSH PRIVILEGES;

Table
CREATE TABLE `player_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `player_item_id` varchar(128) NOT NULL,

  `player_item_name` varchar(128) NOT NULL,

  `player_item_quantity` int(11) NOT NULL,

  `player_item_color` varchar(128) DEFAULT NULL,

  `player_item_player_id` varchar(128) DEFAULT NULL,

  	  `weapon_attack` int(11) DEFAULT NULL,
  	
  	  `weapon_delay` int(11) DEFAULT NULL,

  	  `consumable_type` varchar(128) DEFAULT NULL,
  	
  	  `consumable_size` varchar(128) DEFAULT NULL,

  	  `cost_amount` float DEFAULT NULL,
  	
  	  `cost_currency` varchar(128) DEFAULT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
*/

public class PlayerItem extends Model {
	
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