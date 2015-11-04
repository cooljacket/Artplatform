package com.gexin.artplatform.utils;

import android.annotation.SuppressLint;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeUtil {

	/**
	 * 将时间戳转为代表"距现在多久之前"的字符串
	 * 
	 * @param timeStr
	 *            时间戳
	 * @return
	 */
	public static String getStandardDate(long t) {

		StringBuffer sb = new StringBuffer();

		long time = System.currentTimeMillis() - t;
		long mill = (long) Math.ceil(time / 1000);// 秒前

		long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前

		long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时

		long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前

		if (day - 1 > 0) {
			sb.append(day + "天");
		} else if (hour - 1 > 0) {
			if (hour >= 24) {
				sb.append("1天");
			} else {
				sb.append(hour + "小时");
			}
		} else if (minute - 1 > 0) {
			if (minute == 60) {
				sb.append("1小时");
			} else {
				sb.append(minute + "分钟");
			}
		} else if (mill - 1 > 0) {
			if (mill == 60) {
				sb.append("1分钟");
			} else {
				sb.append(mill + "秒");
			}
		} else {
			sb.append("刚刚");
		}
		if (!sb.toString().equals("刚刚")) {
			sb.append("前");
		}
		return sb.toString();
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getDateString(long timestamp){
		Timestamp stamp= new Timestamp(timestamp);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		return sdf.format(stamp);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getTimeDetailString(long timestamp){
		Timestamp stamp= new Timestamp(timestamp);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		return sdf.format(stamp);
	}
}
