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
CREATE TABLE craftable_items (
  id bigserial NOT NULL,

  craftable_item_id character varying(128) NOT NULL,

  craftable_item_item1 character varying(128) DEFAULT NULL,

  craftable_item_item1_quantity integer DEFAULT NULL,

  craftable_item_item2 character varying(128) DEFAULT NULL,

  craftable_item_item2_quantity integer DEFAULT NULL,

  craftable_item_item3 character varying(128) DEFAULT NULL,

  craftable_item_item3_quantity integer DEFAULT NULL,

  craftable_item_item4 character varying(128) DEFAULT NULL,

  craftable_item_item4_quantity integer DEFAULT NULL,

  CONSTRAINT craftable_item_pkey PRIMARY KEY (id)
);
alter table craftable_items owner to gamemachine;

Mysql

Database/user creation
CREATE DATABASE IF NOT EXISTS gamemachine CHARACTER SET utf8 COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON gamemachine.* TO 'gamemachine'@'%' IDENTIFIED BY 'gamemachine' WITH GRANT OPTION;
FLUSH PRIVILEGES;

Table
CREATE TABLE `craftable_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `craftable_item_id` varchar(128) NOT NULL,

  `craftable_item_item1` varchar(128) DEFAULT NULL,

  `craftable_item_item1_quantity` int(11) DEFAULT NULL,

  `craftable_item_item2` varchar(128) DEFAULT NULL,

  `craftable_item_item2_quantity` int(11) DEFAULT NULL,

  `craftable_item_item3` varchar(128) DEFAULT NULL,

  `craftable_item_item3_quantity` int(11) DEFAULT NULL,

  `craftable_item_item4` varchar(128) DEFAULT NULL,

  `craftable_item_item4_quantity` int(11) DEFAULT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
*/

public class CraftableItem extends Model {
	
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