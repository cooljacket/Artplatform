package com.gexin.artplatform.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.gexin.artplatform.application.UILApplication;
import com.gexin.artplatform.bean.User;
import com.gexin.artplatform.constant.Conf;
import com.gexin.artplatform.dlog.DLog;
import com.google.gson.Gson;

public class JSONUtil {

	public static void analyseLoginJSON(Context context, String jsonObject) {
		Gson gson = new Gson();
		User user = gson.fromJson(jsonObject, User.class);
		DLog.v("AfterGson:", user.toString());
		user.putToSP(context);
		SPUtil.put(context, "isLogin", true);
		Conf.isLogin = true;
		((UILApplication)((Activity)context).getApplication()).setUser(user);
	}

}
