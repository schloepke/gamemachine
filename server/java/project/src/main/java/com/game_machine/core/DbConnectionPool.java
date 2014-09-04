package com.game_machine.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import com.mysql.jdbc.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

public class DbConnectionPool {
	private static final Logger log = LoggerFactory.getLogger(DbConnectionPool.class);

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

	private BoneCPConfig getConfig(String url, String username, String password) {
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(url);
		config.setUsername(username);
		config.setPassword(password);
		config.setMinConnectionsPerPartition(10);
		config.setMaxConnectionsPerPartition(20);
		config.setPartitionCount(1);
		return config;
	}

	public Boolean connect(String id, String url, String driver, String username, String password) throws SQLException {
		if (pools.containsKey(id)) {
			return true;
		}

		log.warn("JDBC id:" + id + " url:" + url + " driver:" + driver + " username:" + username + " password:"
				+ password);
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			log.warn(e1.getMessage());
			return false;
		}

		BoneCPConfig config = getConfig(url, username, password);
		BoneCPConfig config2 = getConfig(url, username, password);
		BoneCP pool;

		BoneCPDataSource ds = new BoneCPDataSource(config2);
		datasources.put(id, ds);
		pool = new BoneCP(config);
		pools.put(id, pool);
		return true;

	}

	public BoneCPDataSource getDataSource(String id) {
		if (datasources.containsKey(id)) {
			return datasources.get(id);
		} else {
			return null;
		}
	}

	public Connection getConnection(String id) throws SQLException {
		if (pools.containsKey(id)) {
			return pools.get(id).getConnection();
		} else {
			return null;
		}
	}

}
