package com.gexin.artplatform.constant;

import android.os.Environment;

public class Constant {

	private Constant(){
		
	}
	public static final String DATABASE_NAME = "art.db";
	public static final int DATABASE_VERSION = 1;
	public static final String ARTICLE_TABLE_NAME = "article";
	public static final String PROBLEM_TABLE_NAME = "problem";
	
	public static final String CACHE_DIR = "imagecache/";

	public static String APP_PATH = Environment.getExternalStorageDirectory()
			+ "/gexin/";

	// 服务器地址
	//public static String SERVER_URL = "http://120.26.192.176/msq";
	public static String SERVER_URL = "http://120.26.192.176:5001";
	// 用户相关API
	public static String USER_API = "/api/user";
	// 用户退出登录
	public static String USER_LOGOUT_API = "/api/user/logout";
	// 找回密码
	public static String USER_FIND_PASSWORD = "/api/user/forget_pass";
	// 问答相关API
	public static String PROBLEM_API = "/api/problem";
	// 发现页相关API
	public static String Discover_API = "/api/classification";
	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}
	
	public static class Extra {
		public static final String FRAGMENT_INDEX = "com.nostra13.example.universalimageloader.FRAGMENT_INDEX";
		public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
	}
	
	public static String SINA_CONSUMER_KEY = "3394076065";

	public static String SINA_OAUTH = "https://api.weibo.com/oauth2/authorize?client_id="
			+ SINA_CONSUMER_KEY
			+ "&response_type=code&redirect_uri=http://120.26.192.176/msq/api/user/weibo/auth"
			+ "&display=mobile";
}
