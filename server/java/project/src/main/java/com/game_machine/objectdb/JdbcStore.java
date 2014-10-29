package com.game_machine.objectdb;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.game_machine.core.AppConfig;
import com.game_machine.core.DbConnectionPool;

public class JdbcStore implements Storable {

	private static final int MYSQL = 0;
	private static final int PG = 1;
	private DbConnectionPool pool;
	private int dbtype;
	private String connectionName = "game_machine";

	public JdbcStore() {
		if (AppConfig.Jdbc.getDriver().equals("org.postgresql.Driver")) {
			dbtype = PG;
		} else if (AppConfig.Jdbc.getDriver().equals("com.mysql.jdbc.Driver")) {
			dbtype = MYSQL;
		}

		pool = DbConnectionPool.getInstance();

	}

	@Override
	public void connect() {
		try {
			pool.connect(connectionName, AppConfig.Jdbc.getHostname(), AppConfig.Jdbc.getPort(),
					AppConfig.Jdbc.getDatabase(), AppConfig.Jdbc.getDs(), AppConfig.Jdbc.getUsername(),
					AppConfig.Jdbc.getPassword());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to establish database connection " + e.getMessage());
		}
	}

	private void cleanup(Connection c, Statement s) {
		try {
			if (s != null) {
				s.close();
			}
			if (c != null) {
				c.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Connection getConnection() {
		Connection connection;
		try {
			connection = pool.getConnection(connectionName);
			if (connection == null) {
				throw new RuntimeException("Unable to obtain connection (pool full?)");
			}
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error obtaining connection "+e.getMessage());
		}
	}
	
	@Override
	public boolean delete(String id) {
		Connection connection = getConnection();
		PreparedStatement s = null;
		try {
			s = connection.prepareStatement("DELETE from entities where id = ?");
			s.setString(1, id);
			s.executeUpdate();
			cleanup(connection, s);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			cleanup(connection, s);
			return false;
		} finally {

		}
	}

	private boolean set(String id, byte[] value, int type) {
		Connection connection = getConnection();
		PreparedStatement s = null;
		try {
			if (dbtype == MYSQL) {
				s = connection
						.prepareStatement("INSERT INTO entities (id,value,datatype) VALUES (?,?,?) ON DUPLICATE KEY UPDATE value=VALUES(value)");
			} else if (dbtype == PG) {
				s = connection.prepareStatement("INSERT INTO entities (id,value,datatype) VALUES (?,?,?)");
			} else {
				throw new RuntimeException("Invalid dbtype");
			}
			s.setString(1, id);
			s.setBytes(2, value);
			s.setInt(3, type);
			s.executeUpdate();
			cleanup(connection, s);
			return true;
		} catch (SQLException e) {
			cleanup(connection, s);
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean setString(String id, String message) {
		byte[] b = message.getBytes(Charset.forName("UTF-8"));
		return set(id, b, 0);
	}

	@Override
	public boolean setBytes(String id, byte[] message) {
		return set(id, message, 1);
	}

	private byte[] get(String id) {
		Connection connection = getConnection();
		Statement s = null;
		
		try {
			s = connection.createStatement();
			ResultSet res = s.executeQuery("SELECT value,datatype from entities where id = '" + id + "' LIMIT 1");
			byte[] value = null;
			//int type;
			if (res.next()) {
				value = res.getBytes("value");
				//type = res.getInt("datatype");
			}
			cleanup(connection, s);
			return value;
		} catch (SQLException e) {
			cleanup(connection, s);
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public String getString(String id) {
		byte[] bytes = get(id);
		if (bytes != null) {
			try {
				return new String(bytes, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public byte[] getBytes(String id) {
		byte[] bytes = get(id);
		if (bytes == null) {
			return null;
		} else {
			return bytes;
		}
	}

	@Override
	public void shutdown() {
	}

}
