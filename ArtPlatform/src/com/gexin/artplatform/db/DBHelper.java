package com.gexin.artplatform.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类
 * @author xiaoxin
 *
 */
public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "platform.db";
	private static final int VERSION = 1;
	private static final String SQL_CREATE = "create table thread_info(_id integer primary key autoincrement," +
			"thread_id integer,url text,start integer,end integer,finished integer)";
	private static final String SQL_DROP = "create table if exists thread_info";
	
	public DBHelper(Context context) {
		super(context, DB_NAME,null,VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		arg0.execSQL(SQL_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		arg0.execSQL(SQL_DROP);
		arg0.execSQL(SQL_CREATE);
	}

}
