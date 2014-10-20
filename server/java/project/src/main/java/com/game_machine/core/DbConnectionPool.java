package com.game_machine.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DbConnectionPool {
	private static final Logger logger = LoggerFactory.getLogger(DbConnectionPool.class);

	public ConcurrentHashMap<String, HikariDataSource> datasources = new ConcurrentHashMap<String, HikariDataSource>();

	private DbConnectionPool() {
	}

	private static class LazyHolder {
		private static final DbConnectionPool INSTANCE = new DbConnectionPool();
	}

	public static DbConnectionPool getInstance() {
		return LazyHolder.INSTANCE;
	}

	public Boolean connect(String id, String hostname, int port, String dbname, String ds, String username,
			String password) throws SQLException {
		if (datasources.containsKey(id)) {
			return true;
		}

		try {
			Class.forName(ds);
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		HikariConfig config = new HikariConfig();
		config.setDataSourceClassName(ds);
		config.addDataSourceProperty("serverName", hostname);
		config.addDataSourceProperty("portNumber", port);
		config.addDataSourceProperty("databaseName", dbname);
		config.addDataSourceProperty("user", username);
		config.addDataSourceProperty("password", password);
		config.setMaximumPoolSize(10);
		config.setIdleTimeout(30000);
		config.setLeakDetectionThreshold(10000);
		// config.setAutoCommit(false);

		HikariDataSource datasource = new HikariDataSource(config);

		datasources.put(id, datasource);
		return true;

	}

	public HikariDataSource getDataSource(String id) {
		if (datasources.containsKey(id)) {
			return datasources.get(id);
		} else {
			return null;
		}
	}

	public Connection getConnection(String id) throws SQLException {
		if (datasources.containsKey(id)) {
			return datasources.get(id).getConnection();
		} else {
			return null;
		}
	}

}
