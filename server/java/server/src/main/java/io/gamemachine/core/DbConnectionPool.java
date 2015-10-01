package io.gamemachine.core;

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

	private HikariConfig getConfig(String hostname, int port, String dbname, String ds, String username,
			String password) {
		
		HikariConfig config = new HikariConfig();
		config.setDataSourceClassName(ds);
		config.addDataSourceProperty("serverName", hostname);
		config.addDataSourceProperty("portNumber", port);
		config.addDataSourceProperty("databaseName", dbname);
		config.addDataSourceProperty("user", username);
		config.addDataSourceProperty("password", password);
		config.setMaximumPoolSize(5);
		config.setIdleTimeout(30000);
		config.setLeakDetectionThreshold(10000);
		return config;
	}
	
	private HikariConfig getSqliteConfig(String url) {
		
		HikariConfig config = new HikariConfig();
		config.setPoolName("SqlitePool");
		config.setDriverClassName("org.sqlite.JDBC");
		config.setJdbcUrl(url);
		config.setConnectionTestQuery("SELECT 1");
		config.setMaxLifetime(60000); // 60 Sec
		config.setIdleTimeout(45000); // 45 Sec
		config.setLeakDetectionThreshold(10000);
		return config;
	}
	
	public Boolean connect(String id, String hostname, int port, String dbname, String ds, String username,
			String password) throws SQLException {
		if (datasources.containsKey(id)) {
			return true;
		}

		try {
			Class.forName(ds);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		HikariConfig config = null;
		
		if (ds.equals("org.sqlite.JDBC")) {
			config = getSqliteConfig(dbname);
		} else {
			config = getConfig(hostname, port, dbname, ds, username, password);
		}
		
		// config.setAutoCommit(false);

		HikariDataSource datasource = new HikariDataSource(config);
		
		if (datasource == null) {
			return false;
		} else {
			datasources.put(id, datasource);
			return true;
		}
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
			HikariDataSource datasource = datasources.get(id);
			Connection connection = datasource.getConnection();
			return connection;
		} else {
			return null;
		}
	}

}
