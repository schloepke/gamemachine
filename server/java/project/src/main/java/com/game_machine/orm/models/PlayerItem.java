package com.game_machine.orm.models;

import org.javalite.activejdbc.DB;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;

import com.game_machine.core.DbConnectionPool;
import com.jolbox.bonecp.BoneCPDataSource;

/*

Mysql
CREATE TABLE `player_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  
  `player_item_id` varchar(128) NOT NULL,
  
  `player_item_name` varchar(128) NOT NULL,
  
  `player_item_quantity` int(11) NOT NULL,
  
  `player_item_color` varchar(128) DEFAULT NULL,

  `weapon_attack` int(11) DEFAULT NULL,
  	
  `weapon_delay` int(11) DEFAULT NULL,

  `consumable_type` varchar(128) DEFAULT NULL,
  	
  `consumable_size` varchar(128) DEFAULT NULL,

  `cost_amount` float DEFAULT NULL,
  	
  `cost_currency` varchar(128) DEFAULT NULL,

  `player_id` varchar(128) NOT NULL,
  PRIMARY KEY(`id`),
  UNIQUE KEY `player_item_id_player_id_idx` (`player_item_id`,`player_id`)
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
			BoneCPDataSource ds = DbConnectionPool.getInstance().getDataSource("game_machine_orm");
			db.open(ds);
		}
	}
}