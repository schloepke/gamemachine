package com.game_machine.orm.models;

import org.javalite.activejdbc.DB;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;

import com.game_machine.core.DbConnectionPool;
import com.jolbox.bonecp.BoneCPDataSource;

/*

Mysql
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
			BoneCPDataSource ds = DbConnectionPool.getInstance().getDataSource("game_machine_orm");
			db.open(ds);
		}
	}
}