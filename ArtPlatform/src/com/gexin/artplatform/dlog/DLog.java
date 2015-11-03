package com.gexin.artplatform.dlog;

import android.util.Log;

public class DLog {

	private static boolean flag = false;
	private static boolean iflag = true;
	private static boolean wflag = true;
	private static boolean vflag = true;
	private static boolean eflag = true;

	public static int i(String tag, String msg) {
		if (flag == true && iflag == true)
			return Log.i(tag, msg);
		return 0;
	}
	
	public static int w(String tag, String msg) {
		if (flag == true && wflag == true) {
			return Log.w(tag, msg);
		}
		return 0;
	}
	
	public static int v(String tag, String msg) {
		if (flag == true && vflag == true) {
			return Log.v(tag, msg);
		}
		return 0;
	}
	
	public static int e(String tag, String msg) {
		if (flag == true && eflag == true) {
			return Log.e(tag, msg);
		}
		return 0;
	}
}
