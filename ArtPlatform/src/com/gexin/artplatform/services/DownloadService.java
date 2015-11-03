package com.gexin.artplatform.services;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpStatus;

import com.gexin.artplatform.bean.FileInfo;
import com.gexin.artplatform.constant.Constant;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class DownloadService extends Service {

	public static final String ACTION_START = "ACTION_START";
	public static final String ACTION_STOP = "ACTION_STOP";
	public static final String ACTION_UPDATE = "ACTION_UPDATE";
	public static final String DOWNLOAD_PATH = Constant.APP_PATH + "videos/";
	private static final String TAG = "DownloadService";
	public static final int MSG_INIT = 0;
	private DownloadTask mTask = null;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (ACTION_START.equals(intent.getAction())) {
			FileInfo fileInfo = (FileInfo) intent
					.getSerializableExtra("fileInfo");
			Log.v(TAG, "start:" + fileInfo);
			new InitThread(fileInfo).start();
		} else if (ACTION_STOP.equals(intent.getAction())) {
			FileInfo fileInfo = (FileInfo) intent
					.getSerializableExtra("fileInfo");
			Log.v(TAG, "pause:" + fileInfo);
			if(mTask!=null){
				mTask.isPause = true;
			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_INIT:
				FileInfo fileInfo = (FileInfo) msg.obj;
				Log.v(TAG, "Init:" + fileInfo);
				//启动下载任务
				mTask = new DownloadTask(DownloadService.this, fileInfo);
				mTask.download();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	class InitThread extends Thread {
		private FileInfo mFileInfo;

		public InitThread(FileInfo mFileInfo) {
			super();
			this.mFileInfo = mFileInfo;
		}

		@Override
		public void run() {
			HttpURLConnection conn = null;
			RandomAccessFile raf = null;
			try {
				// 连接网络文件
				URL url = new URL(mFileInfo.getUrl());
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(3000);
				conn.setRequestMethod("GET");
				int length = -1;
				if (conn.getResponseCode() == HttpStatus.SC_OK) {
					length = conn.getContentLength();
				}
				if (length <= 0) {
					return;
				}
				File dir = new File(DOWNLOAD_PATH);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// 创建本地文件
				File file = new File(dir, mFileInfo.getFileName());
				raf = new RandomAccessFile(file, "rwd");
				// 设置文件长度
				raf.setLength(length);
				mFileInfo.setLength(length);
				mHandler.obtainMessage(MSG_INIT, mFileInfo).sendToTarget();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.disconnect();
					}
					if (raf != null) {
						raf.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
