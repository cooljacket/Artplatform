package com.gexin.artplatform.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gexin.artplatform.bean.Article;
import com.gexin.artplatform.bean.Problem;
import com.gexin.artplatform.constant.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 数据库管理类
 * 
 * @author xiaoxin 2015-4-2
 */
public class DatabaseManager {

	private DatabaseHelper mHelper;
	private SQLiteDatabase mDatabase;
	private Gson gson = new Gson();

	public DatabaseManager(Context context) {
		mHelper = new DatabaseHelper(context);
		mDatabase = mHelper.getWritableDatabase();
	}

	public void addArticle(Article article) {
		ContentValues cv = new ContentValues();
		cv.put("articleId", article.getArticleId());
		cv.put("content", article.getContent());
		cv.put("createTime", article.getCreateTime());
		cv.put("studioAvatarUrl", article.getStudioAvatarUrl());
		cv.put("studioId", article.getStudioId());
		cv.put("studioName", article.getStudioName());
		cv.put("title", article.getTitle());
		cv.put("viewNum", article.getViewNum());
		mDatabase.insert(Constant.ARTICLE_TABLE_NAME, null, cv);
	}

	public void addArticles(List<Article> articles) {
		mDatabase.beginTransaction();
		try {
			for (Article article : articles) {
				String images = gson.toJson(article.getImages(),
						new TypeToken<List<String>>() {
						}.getType());
				mDatabase.execSQL(
						"INSERT INTO " + Constant.ARTICLE_TABLE_NAME
								+ " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ? ,? )",
						new Object[] { article.getArticleId(),
								article.getContent(), article.getCreateTime(),
								article.getStudioAvatarUrl(),
								article.getViewNum(), article.getStudioId(),
								article.getStudioName(), article.getTitle(),
								images });
			}
			mDatabase.setTransactionSuccessful();
		} finally {
			mDatabase.endTransaction();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Article> getAllArticles() {
		List<Article> list = new ArrayList<Article>();
		Cursor c = mDatabase.rawQuery("SELECT * FROM "
				+ Constant.ARTICLE_TABLE_NAME, null);
		while (c.moveToNext()) {
			Article article = new Article();
			article.setArticleId(c.getString(c.getColumnIndex("articleId")));
			article.setContent(c.getString(c.getColumnIndex("content")));
			article.setCreateTime(c.getLong(c.getColumnIndex("createTime")));
			article.setStudioAvatarUrl(c.getString(c
					.getColumnIndex("studioAvatarUrl")));
			article.setStudioId(c.getString(c.getColumnIndex("studioId")));
			article.setStudioName(c.getString(c.getColumnIndex("studioName")));
			article.setTitle(c.getString(c.getColumnIndex("title")));
			article.setViewNum(c.getInt(c.getColumnIndex("viewNum")));
			article.setImages((List<String>) gson.fromJson(
					c.getString(c.getColumnIndex("images")),
					new TypeToken<List<String>>() {
					}.getType()));
			list.add(article);
		}
		c.close();
		return list;
	}

	public void clearArticleTable() {
		mDatabase.execSQL("DELETE FROM " + Constant.ARTICLE_TABLE_NAME);
	}

	public void addProblems(List<Problem> problems) {
		mDatabase.beginTransaction();
		try {
			for (Problem problem : problems) {
				String tag = gson.toJson(problem.getTag(),
						new TypeToken<List<String>>() {
						}.getType());
				mDatabase.execSQL("INSERT INTO " + Constant.PROBLEM_TABLE_NAME
						+ " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ? ,? )",
						new Object[] { problem.get_id(), problem.getContent(),
								problem.getTimestamp(), problem.getAvatarUrl(),
								problem.getCommentNum(), problem.getImage(),
								problem.getZan(), problem.getIsZan(), tag });
			}
			mDatabase.setTransactionSuccessful();
		} finally {
			mDatabase.endTransaction();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Problem> getAllProblems(){
		List<Problem> list = new ArrayList<Problem>();
		Cursor c = mDatabase.rawQuery("SELECT * FROM "
				+ Constant.PROBLEM_TABLE_NAME, null);
		while (c.moveToNext()) {
			Problem problem = new Problem();
			problem.set_id(c.getString(c.getColumnIndex("_id")));
			problem.setContent(c.getString(c.getColumnIndex("content")));
			problem.setTimestamp(c.getLong(c.getColumnIndex("timestamp")));
			problem.setAvatarUrl(c.getString(c
					.getColumnIndex("avatarUrl")));
			problem.setImage(c.getString(c.getColumnIndex("image")));
			problem.setZan(c.getInt(c.getColumnIndex("zan")));
			problem.setIsZan(c.getInt(c.getColumnIndex("isZan")));
			problem.setCommentNum(c.getInt(c.getColumnIndex("commentNum")));
			problem.setTag((List<String>) gson.fromJson(
					c.getString(c.getColumnIndex("tag")),
					new TypeToken<List<String>>() {
					}.getType()));
			list.add(problem);
		}
		c.close();
		return list;
	}
	
	public void clearProblemTable(){
		mDatabase.execSQL("DELETE FROM " + Constant.PROBLEM_TABLE_NAME);
	}

}
