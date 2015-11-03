package com.gexin.artplatform.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpStatus;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gexin.artplatform.bean.FileInfo;
import com.gexin.artplatform.bean.ThreadInfo;
import com.gexin.artplatform.db.ThreadDAO;
import com.gexin.artplatform.db.ThreadDAOImpl;

public class DownloadTask {

	private static final String TAG = "DownloadTask";
	private Context mContext = null;
	private FileInfo mFileInfo = null;
	private ThreadDAO mThreadDAO = null;
	private int mFinished = 0;
	public boolean isPause = false;

	public DownloadTask(Context mContext, FileInfo mFileInfo) {
		super();
		this.mContext = mContext;
		this.mFileInfo = mFileInfo;
		this.mThreadDAO = new ThreadDAOImpl(mContext);
	}

	public void download() {
		// 读取数据库的线程信息
		List<ThreadInfo> threadInfos = mThreadDAO
				.getThreads(mFileInfo.getUrl());
		ThreadInfo threadInfo = null;
		if (threadInfos.size() == 0) {
			threadInfo = new ThreadInfo(0, mFileInfo.getUrl(), 0,
					mFileInfo.getLength(), 0);
		} else {
			threadInfo = threadInfos.get(0);
		}
		// 创建子线程进行下载
		new DownloadThread(threadInfo).start();
	}

	/**
	 * 下载线程
	 * 
	 * @author xiaoxin
	 * 
	 */
	class DownloadThread extends Thread {
		private ThreadInfo mThreadInfo = null;

		public DownloadThread(ThreadInfo mThreadInfo) {
			super();
			this.mThreadInfo = mThreadInfo;
		}

		@Override
		public void run() {
			// 向数据库插入线程信息
			if (mThreadDAO.isExists(mThreadInfo.getUrl(), mThreadInfo.getId())) {
				mThreadDAO.insertThread(mThreadInfo);
			}
			HttpURLConnection conn = null;
			RandomAccessFile raf = null;
			InputStream inputStream = null;
			try {
				URL url = new URL(mThreadInfo.getUrl());
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(3000);
				conn.setRequestMethod("GET");
				// 设置下载位置
				int start = mThreadInfo.getStart() + mThreadInfo.getFinished();
				conn.setRequestProperty("Range", "bytes=" + start + "-"
						+ mThreadInfo.getEnd());
				// 设置文件写入位置
				File file = new File(DownloadService.DOWNLOAD_PATH,
						mFileInfo.getFileName());
				raf = new RandomAccessFile(file, "rwd");
				raf.seek(start);
				Intent intent = new Intent(DownloadService.ACTION_UPDATE);
				mFinished += mThreadInfo.getFinished();
				// 开始下载
				// 读取数据
				Log.v(TAG, "responseCode:" + conn.getResponseCode());
				if (conn.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT
						|| conn.getResponseCode() == HttpStatus.SC_OK) {
					inputStream = conn.getInputStream();
					byte[] buffer = new byte[1024 * 4];
					int len = -1;
					long time = System.currentTimeMillis();
					while ((len = inputStream.read(buffer)) != -1) {
						// 写入文件
						raf.write(buffer, 0, len);
						// 广播下载进度
						mFinished += len;
						if (System.currentTimeMillis() - time > 500) {
							time = System.currentTimeMillis();
							long progress = mFinished;
							// progress*=100;
							intent.putExtra("finished",
									(int) (progress * 100 / mFileInfo
											.getLength()));
							mContext.sendBroadcast(intent);
							Log.v(TAG,
									"percent:" + progress * 100
											/ mFileInfo.getLength());
						}
					}
					// 下载暂停时，保存下载进度
					if (isPause) {
						mThreadDAO.updateThread(mThreadInfo.getUrl(),
								mThreadInfo.getId(), mFinished);
						return;
					}
				}
				intent.putExtra("finished", 100);
				mContext.sendBroadcast(intent);
				// 删除线程信息
				mThreadDAO.deleteThread(mThreadInfo.getUrl(),
						mThreadInfo.getId());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
				try {
					if (inputStream != null) {
						inputStream.close();
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
