package com.game_machine.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import com.mysql.jdbc.Driver;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

	public Boolean connect(String id, String hostname, int port, String dbname, String ds, String username, String password) throws SQLException {
		if (datasources.containsKey(id)) {
			return true;
		}
		
		logger.warn("JDBC "+ "id " + id + "hostname " +hostname + "port " +port + "dbname " +dbname + "ds " +ds + "username " +username + "password " +password);
		HikariConfig config = new HikariConfig();
		config.setDataSourceClassName(ds);
		config.addDataSourceProperty("serverName", hostname);
		config.addDataSourceProperty("portNumber", port);
		config.addDataSourceProperty("databaseName", dbname);
		config.addDataSourceProperty("user", username);
		config.addDataSourceProperty("password", password);
		config.setMaximumPoolSize(10);

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
