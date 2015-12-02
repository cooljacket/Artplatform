package com.gexin.artplatform.database;

import com.gexin.artplatform.constant.Constant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * 辅助访问数据库
 * 
 * @author KM
 * 
 */

public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context) {
		super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + Constant.ARTICLE_TABLE_NAME
				+ "(rowid INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "articleId TEXT, content TEXT, createTime TIMESTAMP,"
				+ "studioAvatarUrl TEXT, " + "viewNum INTEGER,"
				+ "studioId TEXT, studioName TEXT, title TEXT,images TEXT);");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + Constant.PROBLEM_TABLE_NAME
				+ "(rowid INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "_id TEXT, content TEXT, timestamp TIMESTAMP,"
				+ "avatarUrl TEXT, " + "commentNum INTEGER,"
				+ "image TEXT, zan INTEGER, isZan INTEGER, tag TEXT);");
	}
	
	public void dropTable(SQLiteDatabase db,String tableName){
		db.execSQL("DROP TABLE "+tableName);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("ALTER TABLE article ADD COLUMN other STRING");
	}
}
