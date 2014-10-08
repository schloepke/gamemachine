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
CREATE TABLE test_objects (
  id bigserial NOT NULL,
  
  test_object_optional_string character varying(128) DEFAULT NULL,
  
  test_object_required_string character varying(128) NOT NULL,
  
  test_object_numbers integer DEFAULT NULL,

  test_object_bvalue boolean DEFAULT NULL,
  
  test_object_dvalue double precision DEFAULT NULL,
  
  test_object_fvalue double precision DEFAULT NULL,
  
  test_object_numbers64 integer DEFAULT NULL,

  test_object_id character varying(128) NOT NULL,

  player_id character varying(128) NOT NULL,
  CONSTRAINT test_object_pkey PRIMARY KEY (id),
  CONSTRAINT test_object_id_player_id_idx UNIQUE (test_object_id,player_id)
);
alter table test_objects owner to gamemachine;

Mysql

Database/user creation
CREATE DATABASE IF NOT EXISTS gamemachine CHARACTER SET utf8 COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON gamemachine.* TO 'gamemachine'@'%' IDENTIFIED BY 'gamemachine' WITH GRANT OPTION;
FLUSH PRIVILEGES;

Table
CREATE TABLE `test_objects` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  
  `test_object_optional_string` varchar(128) DEFAULT NULL,
  
  `test_object_required_string` varchar(128) NOT NULL,
  
  `test_object_numbers` int(11) DEFAULT NULL,

  `test_object_bvalue` tinyint(4) DEFAULT NULL,
  
  `test_object_dvalue` double DEFAULT NULL,
  
  `test_object_fvalue` float DEFAULT NULL,
  
  `test_object_numbers64` int(11) DEFAULT NULL,

  `test_object_id` varchar(128) NOT NULL,

  `player_id` varchar(128) NOT NULL,
  PRIMARY KEY(`id`),
  UNIQUE KEY `test_object_id_player_id_idx` (`test_object_id`,`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
*/

public class TestObject extends Model {
	
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