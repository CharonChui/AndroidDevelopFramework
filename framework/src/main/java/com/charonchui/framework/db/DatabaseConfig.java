package com.charonchui.framework.db;

/**
 * Configuration class for the tables info in the database
 */
public interface DatabaseConfig {
	String TABLE_TEST = "test";

	String CREATE_TABLE_TEST = "CREATE TABLE " + TABLE_TEST
			+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "json TEXT);";
}
