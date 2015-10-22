package com.charonchui.framework.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Subclass of SQLiteOpenHelper that manage database creation and version
 * management this is a singleton class
 */
public class BaseSQLiteOpenHelper extends SQLiteOpenHelper {
	private static final String databaseName = "test.db";
	private static final int version = 1;

	private static BaseSQLiteOpenHelper mInstance;

	private BaseSQLiteOpenHelper(Context context) {
		super(context, databaseName, null, version);
	}

	public static BaseSQLiteOpenHelper getInstance(Context context) {
		if (mInstance == null) {
			synchronized (BaseSQLiteOpenHelper.class) {
				if (mInstance == null) {
					mInstance = new BaseSQLiteOpenHelper(context);
				}
			}
		}
		return mInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DatabaseConfig.CREATE_TABLE_TEST);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseConfig.TABLE_TEST);
		this.onCreate(db);
	}

	/**
	 * Convenience method for updating rows in the database.
	 * 
	 * @param table
	 *            the table to update in
	 * @param values
	 *            a map from column names to new column values. null is a valid
	 *            value that will be translated to NULL.
	 * @param whereClause
	 *            the optional WHERE clause to apply when updating. Passing null
	 *            will update all rows.
	 * @param whereArgs
	 *            You may include ?s in the where clause, which will be replaced
	 *            by the values from whereArgs. The values will be bound as
	 *            Strings.
	 * @return the number of rows affected
	 */
	public int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		SQLiteDatabase writableDatabase = getWritableDatabase();
		int update = writableDatabase.update(table, values, whereClause,
				whereArgs);
		closeDatabase(writableDatabase, null);
		return update;
	}

	/**
	 * Convenience method for inserting a row into the database.
	 * 
	 * @param table
	 *            the table to insert the row into
	 * @param nullColumnHack
	 *            optional; may be <code>null</code>. SQL doesn't allow
	 *            inserting a completely empty row without naming at least one
	 *            column name. If your provided <code>values</code> is empty, no
	 *            column names are known and an empty row can't be inserted. If
	 *            not set to null, the <code>nullColumnHack</code> parameter
	 *            provides the name of nullable column name to explicitly insert
	 *            a NULL into in the case where your <code>values</code> is
	 *            empty.
	 * @param values
	 *            this map contains the initial column values for the row. The
	 *            keys should be the column names and the values the column
	 *            values
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long insert(String table, String nullColumnHack, ContentValues values) {
		SQLiteDatabase db = getWritableDatabase();
		long insert = db.insert(table, nullColumnHack, values);
		closeDatabase(db, null);
		return insert;
	}

	/**
	 * updating rows in the database if it exists else inserting a row into the
	 * database
	 * 
	 * @param table
	 *            The table name to compile the query against.
	 * @param values
	 *            a map from column names to new column values. null is a valid
	 *            value that will be translated to NULL.
	 * @param selection
	 *            A filter declaring which rows to return, formatted as an SQL
	 *            WHERE clause (excluding the WHERE itself). Passing null will
	 *            return all rows for the given table.
	 * @param selectionArgs
	 *            You may include ?s in selection, which will be replaced by the
	 *            values from selectionArgs, in order that they appear in the
	 *            selection. The values will be bound as Strings
	 * @return true is success else false.
	 */
	public boolean insertOrUpdate(String table, ContentValues values,
			String selection, String[] selectionArgs) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(table, null, selection, selectionArgs, null, null,
				null);
		int count = c.getCount();
		closeDatabase(db, c);
		if (count > 0) {
			return update(table, values, selection, selectionArgs) > 0;
		} else {
			return insert(table, null, values) > 0L;
		}
	}

	/**
	 * Convenience method for deleting rows in the database.
	 * 
	 * @param table
	 *            the table to delete from
	 * @param whereClause
	 *            the optional WHERE clause to apply when deleting. Passing null
	 *            will delete all rows.
	 * @param whereArgs
	 *            You may include ?s in the where clause, which will be replaced
	 *            by the values from whereArgs. The values will be bound as
	 *            Strings.
	 * @return the number of rows affected if a whereClause is passed in, 0
	 *         otherwise. To remove all rows and get a count pass "1" as the
	 *         whereClause.
	 */
	public int delete(String table, String whereClause, String[] whereArgs) {
		SQLiteDatabase db = getWritableDatabase();
		int delete = db.delete(table, whereClause, whereArgs);
		return delete;
	}

	/**
	 * Query the given table, returning a {@link Cursor} over the result set.
	 * 
	 * @param table
	 *            The table name to compile the query against.
	 * @param columns
	 *            A list of which columns to return. Passing null will return
	 *            all columns, which is discouraged to prevent reading data from
	 *            storage that isn't going to be used.
	 * @param selection
	 *            A filter declaring which rows to return, formatted as an SQL
	 *            WHERE clause (excluding the WHERE itself). Passing null will
	 *            return all rows for the given table.
	 * @param selectionArgs
	 *            You may include ?s in selection, which will be replaced by the
	 *            values from selectionArgs, in order that they appear in the
	 *            selection. The values will be bound as Strings.
	 * @param groupBy
	 *            A filter declaring how to group rows, formatted as an SQL
	 *            GROUP BY clause (excluding the GROUP BY itself). Passing null
	 *            will cause the rows to not be grouped.
	 * @param having
	 *            A filter declare which row groups to include in the cursor, if
	 *            row grouping is being used, formatted as an SQL HAVING clause
	 *            (excluding the HAVING itself). Passing null will cause all row
	 *            groups to be included, and is required when row grouping is
	 *            not being used.
	 * @param orderBy
	 *            How to order the rows, formatted as an SQL ORDER BY clause
	 *            (excluding the ORDER BY itself). Passing null will use the
	 *            default sort order, which may be unordered.
	 * @return A {@link Cursor} object, which is positioned before the first
	 *         entry. Note that {@link Cursor}s are not synchronized, see the
	 *         documentation for more details.
	 * @see Cursor
	 */
	public Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(table, columns, selection, selectionArgs, groupBy,
				having, orderBy);
		return c;
	}

	/**
	 * Runs the provided SQL and returns a {@link Cursor} over the result set.
	 * 
	 * @param sql
	 *            the SQL query. The SQL string must not be ; terminated
	 * @param selectionArgs
	 *            You may include ?s in where clause in the query, which will be
	 *            replaced by the values from selectionArgs. The values will be
	 *            bound as Strings.
	 * @return true if this exist, else false
	 */
	public boolean exist(String sql, String[] selectionArgs) {
		boolean result = false;
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.rawQuery(sql, selectionArgs);
		if (c.moveToNext()) {
			result = true;
		}
		closeDatabase(db, c);
		return result;
	}

	public void closeDatabase(SQLiteDatabase db, Cursor c) {
		if (c != null) {
			c.close();
		}
		if (db != null) {
			db.close();
		}
		// close(); 不关会提高效率
	}
}
