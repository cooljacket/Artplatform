package com.gexin.artplatform.utils;

import android.content.Context;
import android.content.Intent;

import com.gexin.artplatform.mine.MineFragment;


public class BroadcastUtil {

	public static void updateUserInfo(Context context){
		Intent intent = new Intent();
		intent.setAction(MineFragment.ACTION_CHANGE_USERDATA);
		context.sendBroadcast(intent);
	}
}
