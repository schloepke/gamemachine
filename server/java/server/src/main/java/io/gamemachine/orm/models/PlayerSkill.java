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
CREATE TABLE player_skills (
  id bigserial NOT NULL,

  player_skill_id character varying(128) NOT NULL,

  player_skill_name character varying(128) NOT NULL,

  player_skill_category character varying(128) NOT NULL,

  player_skill_damage_type character varying(128) DEFAULT NULL,

  player_skill_icon character varying(128) NOT NULL,

  player_skill_description character varying(128) DEFAULT NULL,

  player_skill_resource character varying(128) DEFAULT NULL,

  player_skill_resource_cost integer DEFAULT NULL,

  player_skill_character_id character varying(128) NOT NULL,

  player_skill_weapon_type character varying(128) DEFAULT NULL,

  player_skill_range integer DEFAULT NULL,

  player_skill_status_effect_id character varying(128) DEFAULT NULL,

  CONSTRAINT player_skill_pkey PRIMARY KEY (id)
);
alter table player_skills owner to gamemachine;

Mysql

Database/user creation
CREATE DATABASE IF NOT EXISTS gamemachine CHARACTER SET utf8 COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON gamemachine.* TO 'gamemachine'@'%' IDENTIFIED BY 'gamemachine' WITH GRANT OPTION;
FLUSH PRIVILEGES;

Table
CREATE TABLE `player_skills` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `player_skill_id` varchar(128) NOT NULL,

  `player_skill_name` varchar(128) NOT NULL,

  `player_skill_category` varchar(128) NOT NULL,

  `player_skill_damage_type` varchar(128) DEFAULT NULL,

  `player_skill_icon` varchar(128) NOT NULL,

  `player_skill_description` varchar(128) DEFAULT NULL,

  `player_skill_resource` varchar(128) DEFAULT NULL,

  `player_skill_resource_cost` int(11) DEFAULT NULL,

  `player_skill_character_id` varchar(128) NOT NULL,

  `player_skill_weapon_type` varchar(128) DEFAULT NULL,

  `player_skill_range` int(11) DEFAULT NULL,

  `player_skill_status_effect_id` varchar(128) DEFAULT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
*/

public class PlayerSkill extends Model {
	
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