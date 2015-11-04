package com.gexin.artplatform.utils;

import java.io.File;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;

import com.gexin.artplatform.constant.Constant;

/**
 * 文件工具类
 * 
 * @author xiaoxin 2015-4-28
 */
public class FileUtil {

	private static final String TAG = "FileUtil";

	/**
	 * 将Uri地址转化为路径
	 * 
	 * @param context
	 *            所在上下文
	 * @param uri
	 *            Uri地址
	 * @return 文件路径
	 */
	public static String getRealFilePath(final Context context, final Uri uri) {
		if (null == uri)
			return null;
		final String scheme = uri.getScheme();
		String data = null;
		if (scheme == null)
			data = uri.getPath();
		else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			Cursor cursor = context.getContentResolver().query(uri,
					new String[] { MediaColumns.DATA }, null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(MediaColumns.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}

	public static File getCacheFile(String imageUri) {
		File cacheFile = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String fileName = getFileName(imageUri);
			File dir = new File(Constant.APP_PATH + Constant.CACHE_DIR);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			cacheFile = new File(dir, fileName);
			Log.i(TAG, "exists:" + cacheFile.exists() + ",dir:" + dir
					+ ",file:" + fileName);
		}

		return cacheFile;
	}

	public static String getFileName(String path) {
		int index = path.lastIndexOf("/");
		return path.substring(index + 1);
	}
}
