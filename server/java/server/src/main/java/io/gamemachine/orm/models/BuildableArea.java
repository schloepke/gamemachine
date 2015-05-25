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
CREATE TABLE buildable_areas (
  id bigserial NOT NULL,

  buildable_area_owner_id character varying(128) NOT NULL,

  buildable_area_px double precision NOT NULL,

  buildable_area_py double precision NOT NULL,

  buildable_area_pz double precision NOT NULL,

  buildable_area_sx integer NOT NULL,

  buildable_area_sy integer NOT NULL,

  buildable_area_sz integer NOT NULL,

  CONSTRAINT buildable_area_pkey PRIMARY KEY (id)
);
alter table buildable_areas owner to gamemachine;

Mysql

Database/user creation
CREATE DATABASE IF NOT EXISTS gamemachine CHARACTER SET utf8 COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON gamemachine.* TO 'gamemachine'@'%' IDENTIFIED BY 'gamemachine' WITH GRANT OPTION;
FLUSH PRIVILEGES;

Table
CREATE TABLE `buildable_areas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `buildable_area_owner_id` varchar(128) NOT NULL,

  `buildable_area_px` float NOT NULL,

  `buildable_area_py` float NOT NULL,

  `buildable_area_pz` float NOT NULL,

  `buildable_area_sx` int(11) NOT NULL,

  `buildable_area_sy` int(11) NOT NULL,

  `buildable_area_sz` int(11) NOT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
*/

public class BuildableArea extends Model {
	
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