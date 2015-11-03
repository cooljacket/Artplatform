package com.gexin.artplatform.studio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.FileInfo;
import com.gexin.artplatform.bean.VideoItem;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.services.DownloadService;
import com.gexin.artplatform.utils.BroadcastUtil;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.TitleBar;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

public class VideoDetailActivity extends Activity {

	private static final String TAG = "VideoDetailActivity";
	private String videoId = "";
	private Gson gson = new Gson();
	private VideoItem videoItem = null;
	private FileInfo fileInfo = null;
	private boolean isFocus = false;

	private TitleBar titleBar;
	private LinearLayout llBack;
	private TextView tvTitle;
	private TextView tvDescription;
	private TextView tvName;
	private ImageView ivFocus;
	private ImageView ivVideo;
	private ImageButton ibtnPlay;
	private ProgressBar pbProgress;
	private RelativeLayout rlTeacherInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_detail);
		videoId = getIntent().getStringExtra("id");
		initView();
		initData();
		IntentFilter filter = new IntentFilter();
		filter.addAction(DownloadService.ACTION_UPDATE);
		registerReceiver(mReceiver, filter);
	}

	private void initData() {
		String url = Constant.SERVER_URL + "/api/video/" + videoId;
		Handler handler = new HttpHandler(this) {
			@Override
			protected void succeed(JSONObject jObject) {
				dealResponse(jObject);
			}
		};
		new HttpConnectionUtils(handler).get(url);
	}

	private void dealResponse(JSONObject jObject) {
		try {
			int state = jObject.getInt("stat");
			if (state == 1) {
				videoItem = gson.fromJson(jObject.getJSONObject("video")
						.toString(), VideoItem.class);
				ImageLoader.getInstance().displayImage(videoItem.getImageUrl(),
						ivVideo);
				setFocusStatus();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "无法取得视频信息", Toast.LENGTH_SHORT).show();
		}

		fileInfo = new FileInfo(0, videoItem.getVideoUrl(),
				getFileNameFromUrl(videoItem.getVideoUrl()), 0, 0);
		tvTitle.setText(videoItem.getTitle());
		tvDescription.setText(videoItem.getDescription());
		titleBar.setTitle(videoItem.getTitle());
		String name = videoItem.getName();
		if (name != null && !name.isEmpty()) {
			tvName.setText(videoItem.getName());
			rlTeacherInfo.setVisibility(View.VISIBLE);
		} else {
			rlTeacherInfo.setVisibility(View.GONE);
		}
	}

	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_activity_video_detail);
		tvTitle = (TextView) findViewById(R.id.tv_title_video_detail);
		tvDescription = (TextView) findViewById(R.id.tv_describe_video_detail);
		ibtnPlay = (ImageButton) findViewById(R.id.ibtn_play_video_detail);
		ivVideo = (ImageView) findViewById(R.id.iv_shot_video_detail);
		pbProgress = (ProgressBar) findViewById(R.id.pb_activity_video_detail);
		tvName = (TextView) findViewById(R.id.tv_name_video_detail);
		ivFocus = (ImageView) findViewById(R.id.iv_focus_video_detail);
		rlTeacherInfo = (RelativeLayout) findViewById(R.id.rl_teacher_video_detail);
		initTitleBar();
		pbProgress.setMax(100);
		ibtnPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (existFile(fileInfo.getFileName())) {
					Log.v(TAG, "exist:" + true);
					String path = DownloadService.DOWNLOAD_PATH
							+ fileInfo.getFileName();
					Log.v(TAG, "path:" + path);
					Uri uri = Uri.parse("file://" + path);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(uri, "video/*");
					startActivity(intent);
				} else {
					Intent intent = new Intent(VideoDetailActivity.this,
							DownloadService.class);
					intent.setAction(DownloadService.ACTION_START);
					intent.putExtra("fileInfo", fileInfo);
					startService(intent);
				}
			}
		});
		ivFocus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				postFocus();
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private void postFocus() {
		String userId = (String) SPUtil.get(this, "userId", "");
		String followApi = Constant.SERVER_URL + "/api/user/" + userId
				+ "/follow";
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					try {
						JSONObject jsonObject = new JSONObject((String) msg.obj);
						if (jsonObject.getInt("stat") == 1) {
							if (isFocus) {
								Toast.makeText(VideoDetailActivity.this,
										"取消关注成功", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(VideoDetailActivity.this,
										"关注成功", Toast.LENGTH_SHORT).show();
							}
							setFocusStatus();
						} else {
							if (isFocus) {
								Toast.makeText(VideoDetailActivity.this,
										"取消关注失败", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(VideoDetailActivity.this,
										"关注失败", Toast.LENGTH_SHORT).show();
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
						if (isFocus) {
							Toast.makeText(VideoDetailActivity.this, "取消关注失败",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(VideoDetailActivity.this, "关注失败",
									Toast.LENGTH_SHORT).show();
						}
					}
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("userId", videoItem.getUserId()));
		if (isFocus) {
			list.add(new BasicNameValuePair("follow", "-1"));
		} else {
			list.add(new BasicNameValuePair("follow", "1"));
		}
		new HttpConnectionUtils(handler).post(followApi, list);
	}

	private void initTitleBar() {
		llBack = new LinearLayout(this);
		ImageView ivBack = new ImageView(this);
		ivBack.setImageResource(R.drawable.back_icon);
		LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		llBack.addView(ivBack, params);
		llBack.setGravity(Gravity.CENTER_VERTICAL);
		llBack.setBackgroundResource(R.drawable.selector_titlebar_btn);
		llBack.setPadding(20, 0, 20, 0);
		titleBar.setLeftView(llBack);
		llBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	// 更新UI的广播
	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			if (DownloadService.ACTION_UPDATE.equals(arg1.getAction())) {
				int finished = arg1.getIntExtra("finished", 0);
				pbProgress.setProgress(finished);
				if (finished == 100) {
					pbProgress.setVisibility(View.GONE);
					Toast.makeText(VideoDetailActivity.this, "下载完成",
							Toast.LENGTH_SHORT).show();
					ibtnPlay.setVisibility(View.VISIBLE);
				} else {
					pbProgress.setVisibility(View.VISIBLE);
					ibtnPlay.setVisibility(View.GONE);
				}
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	};

	private String getFileNameFromUrl(String url) {
		String fileName = url.substring(url.lastIndexOf("/") + 1);
		return fileName;
	}

	private boolean existFile(String fileName) {
		File file = new File(DownloadService.DOWNLOAD_PATH, fileName);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	private void setFocusStatus() {
		String userId = (String) SPUtil.get(this, "userId", "");
		if (userId.isEmpty() || videoItem == null
				|| videoItem.getUserId() == null
				|| videoItem.getUserId().isEmpty()) {
			return;
		}
		String url = Constant.SERVER_URL + "/api/user/" + userId + "/relation/"
				+ videoItem.getUserId();
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					try {
						JSONObject jsonObject = new JSONObject((String) msg.obj);
						int state = jsonObject.getInt("stat");
						if (state == 1) {
							int relation = jsonObject.getInt("relation");
							if (relation == 1 || relation == 3) {
								isFocus = true;
								ivFocus.setImageResource(R.drawable.focus_cancle_icon);
							} else {
								isFocus = false;
								ivFocus.setImageResource(R.drawable.interest_icon_2);
							}
							BroadcastUtil.updateUserInfo(VideoDetailActivity.this);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		new HttpConnectionUtils(handler).get(url);
	}
}
