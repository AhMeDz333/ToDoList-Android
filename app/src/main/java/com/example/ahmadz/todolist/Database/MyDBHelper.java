package com.example.ahmadz.todolist.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ahmadz.todolist.Models.TodoDate;
import com.example.ahmadz.todolist.Models.TodoItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmadz on 6/5/16.
 */

public class MyDBHelper extends SQLiteOpenHelper{

	private static MyDBHelper mInstance;
	private final String TAG = this.getClass().getSimpleName();
	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "TodoDB";
	//Table Names
	public static String TABLE_TODO = "table_todo";
	//To-do Table Columns
	public static String COLUMN_ITEM_TITLE = "todo_title";
	public static String COLUMN_ITEM_BODY = "todo_body";
	public static String ID_COLUMN = "id";
	public static String COLUMN_ITEM_YEAR = "todo_year";
	public static String COLUMN_ITEM_MONTH = "todo_month";
	public static String COLUMN_ITEM_DAY = "todo_day";
	public static String COLUMN_ITEM_HOUR = "todo_hour";
	public static String COLUMN_ITEM_MINUTE = "todo_minute";

	public static synchronized MyDBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new MyDBHelper(context.getApplicationContext());
		}
		return mInstance;
	}

	private MyDBHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
	}

	public List<TodoItemModel> getAllTodoItems() {

		List<TodoItemModel> todoItems = new ArrayList<>();

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.rawQuery(
				String.format("select * from %s", TABLE_TODO),
				null
		);

		if (cur == null)
			Log.e(TAG, "null");

		else if (!cur.moveToFirst())
			Log.e(TAG, "empty todoItems");

		else {

			int idCol = cur.getColumnIndex(ID_COLUMN);
			int titleCol = cur.getColumnIndex(COLUMN_ITEM_TITLE);
			int bodyCol = cur.getColumnIndex(COLUMN_ITEM_BODY);

			do {
				long id = cur.getLong(idCol);
				String title = cur.getString(titleCol);
				String body = cur.getString(bodyCol);

				TodoDate todoDate = new TodoDate(cur);
				TodoItemModel todoItem = new TodoItemModel(id, title, body, todoDate);
				todoItems.add(todoItem);

			} while (cur.moveToNext());
		}

		if (cur != null)
			cur.close();
		db.close();

		return todoItems;
	}

	public long addTodoItem(String title){
		ContentValues values = new ContentValues();
		values.put(COLUMN_ITEM_TITLE, title);

		SQLiteDatabase db = this.getWritableDatabase();

		long id = db.insert(TABLE_TODO, null, values);

		db.close();

		return id;
	}

	public void deleteTodoItem(long item_id){

		SQLiteDatabase db = this.getWritableDatabase();

		String table = TABLE_TODO;
		String whereClause = ID_COLUMN + "=?";
		String[] whereArgs = new String[] { String.valueOf(item_id) };

		db.delete(table, whereClause, whereArgs);
		db.close();
	}

	public void editTodoItem(long item_id, String title, String body){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ITEM_TITLE, title);
		cv.put(COLUMN_ITEM_BODY, body);

		String table = TABLE_TODO;
		String whereClause = ID_COLUMN + "=?";
		String[] whereArgs = new String[] { String.valueOf(item_id) };

		db.update(table, cv, whereClause, whereArgs);
		db.close();
	}

	public void editTodoDate(long item_id, int year, int month, int day){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ITEM_YEAR, year);
		cv.put(COLUMN_ITEM_MONTH, month);
		cv.put(COLUMN_ITEM_DAY, day);

		String table = TABLE_TODO;
		String whereClause = ID_COLUMN + "=?";
		String[] whereArgs = new String[] { String.valueOf(item_id) };

		db.update(table, cv, whereClause, whereArgs);
		db.close();
	}

	public void editTodoTime(long item_id, int hour, int minute){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ITEM_HOUR, hour);
		cv.put(COLUMN_ITEM_MINUTE, minute);

		String table = TABLE_TODO;
		String whereClause = ID_COLUMN + "=?";
		String[] whereArgs = new String[] { String.valueOf(item_id) };

		db.update(table, cv, whereClause, whereArgs);
		db.close();
	}

	public void editTodoItemTitle(long item_id, String title){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ITEM_TITLE, title);

		String table = TABLE_TODO;
		String whereClause = ID_COLUMN + "=?";
		String[] whereArgs = new String[] { String.valueOf(item_id) };

		db.update(table, cv, whereClause, whereArgs);
		db.close();
	}

	private void createTables(SQLiteDatabase db) {
		// Fragment
		String CREATE_TODO_TABLE = String.format("CREATE TABLE %s ( " +
						"%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"%s TEXT, " +
						"%s TEXT DEFAULT '', " +
						"%s INT, " +
						"%s INT, " +
						"%s INT, " +
						"%s INT, " +
						"%s INT " +
						")"
				,
				TABLE_TODO,
				ID_COLUMN,
				COLUMN_ITEM_TITLE,
				COLUMN_ITEM_BODY,
				COLUMN_ITEM_YEAR,
				COLUMN_ITEM_MONTH,
				COLUMN_ITEM_DAY,
				COLUMN_ITEM_HOUR,
				COLUMN_ITEM_MINUTE
		);
		db.execSQL(CREATE_TODO_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//nothing for now
	}
}
