package com.game_machine.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import org.javalite.activejdbc.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

public class DbConnectionPool {
	private static final Logger log = LoggerFactory
			.getLogger(DbConnectionPool.class);

	public ConcurrentHashMap<String, BoneCP> pools = new ConcurrentHashMap<String, BoneCP>();
	public ConcurrentHashMap<String, BoneCPDataSource> datasources = new ConcurrentHashMap<String, BoneCPDataSource>();

	private DbConnectionPool() {
	}

	private static class LazyHolder {
		private static final DbConnectionPool INSTANCE = new DbConnectionPool();
	}

	public static DbConnectionPool getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	public Boolean connect(String id, String url, String driver,
			String username, String password) {
		if (pools.containsKey(id)) {
			return true;
		}

		log.warn("JDBC id:" + id + " url:" + url + " driver:" + driver
				+ " username:" + username + " password:" + password);
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			log.warn(e1.getMessage());
			return false;
		}

		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(url);
		config.setUsername(username);
		config.setPassword(password);
		config.setMinConnectionsPerPartition(5);
		config.setMaxConnectionsPerPartition(10);
		config.setPartitionCount(1);
		BoneCP pool;

		try {
			BoneCPDataSource ds = new BoneCPDataSource(config);
			datasources.put(id, ds);
			pool = new BoneCP(config);
			pools.put(id, pool);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
			return false;
		}
	}

	public BoneCPDataSource getDataSource(String id) {
		if (datasources.containsKey(id)) {
			return datasources.get(id);
		} else {
			return null;
		}
	}

	public Connection getConnection(String id) {
		try {
			if (pools.containsKey(id)) {
				return pools.get(id).getConnection();
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
