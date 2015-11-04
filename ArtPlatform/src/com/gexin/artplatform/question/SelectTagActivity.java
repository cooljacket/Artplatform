package com.gexin.artplatform.question;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.MainActivity;
import com.gexin.artplatform.R;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.BroadcastUtil;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.NetUtil;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.TitleBar;

public class SelectTagActivity extends Activity {

	private static final String TAG = "SelectTagActivity";
	private static final int POST_IMAGE_SUCCESS = 1;
	private static final String POST_IMAGE_API = Constant.SERVER_URL
			+ "/api/image";
	private String[] tagNames = { "素描", "色彩", "速写", "设计", "报考", "动漫", "油画",
			"书法", "吐槽" };
	private List<Boolean> checkFlags = new ArrayList<Boolean>();
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private List<String> choosenTags = new ArrayList<String>();

	private ListView mListView;
	private TitleBar titleBar;
	private LinearLayout llLeft, llRight;

	private SimpleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_tag);

		initView();
		initData();
	}

	private void initData() {
		for (String str : tagNames) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("tagName", str);
			map.put("checkicon", R.drawable.check1);
			list.add(map);
			checkFlags.add(false);
		}
		adapter = new SimpleAdapter(this, list, R.layout.tag_item,
				new String[] { "tagName", "checkicon" }, new int[] {
						R.id.tv_name_tag_item, R.id.iv_check_tag_item });
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// list.clear();
				// for (int i = 0; i < tagNames.length; i++) {
				// if (i == arg2) {
				// checkFlags.set(i, true);
				// } else {
				// checkFlags.set(i, false);
				// }
				// Map<String, Object> map = new HashMap<String, Object>();
				// map.put("tagName", tagNames[i]);
				// if (checkFlags.get(i)) {
				// map.put("checkicon", R.drawable.check2);
				// } else {
				// map.put("checkicon", R.drawable.check1);
				// }
				// list.add(map);
				// }
				if (checkFlags.get(arg2)) {
					checkFlags.set(arg2, false);
					list.get(arg2).put("checkicon", R.drawable.check1);
				} else {
					checkFlags.set(arg2, true);
					list.get(arg2).put("checkicon", R.drawable.check2);
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.lv_tags_select);
		titleBar = (TitleBar) findViewById(R.id.tb_select_tag);
		initTitleBar();
	}

	private void initTitleBar() {
		llLeft = new LinearLayout(this);
		llLeft.setGravity(Gravity.CENTER_VERTICAL);
		TextView tvLeft = new TextView(this);
		tvLeft.setText("上一步");
		tvLeft.setTextSize(20);
		tvLeft.setTextColor(Color.WHITE);
		LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 0, 10, 0);
		llLeft.setBackgroundResource(R.drawable.selector_titlebar_btn);
		llLeft.addView(tvLeft, params);

		llRight = new LinearLayout(this);
		llRight.setGravity(Gravity.CENTER_VERTICAL);
		TextView tvRight = new TextView(this);
		tvRight.setText("提交");
		tvRight.setTextSize(20);
		tvRight.setTextColor(Color.WHITE);
		llRight.setBackgroundResource(R.drawable.selector_titlebar_btn);
		llRight.addView(tvRight, params);
		titleBar.setLeftView(llLeft);
		titleBar.setRightView(llRight);

		llLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.v(TAG, "LeftClick");
				finish();
			}
		});

		llRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.v(TAG, "RightClick");
				if (postProblem()) {
					Toast.makeText(SelectTagActivity.this, "提交问题中...",
							Toast.LENGTH_SHORT).show();
					startActivity(new Intent(SelectTagActivity.this,
							MainActivity.class));
				}
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private boolean postProblem() {
		String userId = (String) SPUtil.get(this, "userId", "");
		choosenTags.clear();
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < checkFlags.size(); i++) {
			if (checkFlags.get(i)) {
				choosenTags.add(tagNames[i]);
				jsonArray.put(tagNames[i]);
			}
		}
		final String tag = jsonArray.toString();
		if (tag.isEmpty()) {
			Toast.makeText(this, "请为问题添加标签", Toast.LENGTH_SHORT).show();
			return false;
		}
		final String postProblemAPI = Constant.SERVER_URL + Constant.USER_API
				+ "/" + userId + "/problem";
		final String content = getIntent().getStringExtra("content");
		final String imagePath = getIntent().getStringExtra("image");
		final String teacherId = getIntent().getStringExtra("teacherId");
		new Thread(new Runnable() {
			private String imageUrl = "";

			Handler handler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case HttpConnectionUtils.DID_SUCCEED:
						String response = (String) msg.obj;
						Log.v(TAG, "response:" + response);
						try {
							JSONObject jObject = new JSONObject(response);
							int state = jObject.getInt("stat");
							if (state == 1) {
								Toast.makeText(getApplicationContext(), "提问成功",
										Toast.LENGTH_SHORT).show();
								BroadcastUtil.updateUserInfo(SelectTagActivity.this);
							} else {
								Toast.makeText(getApplicationContext(), "提问失败",
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(), "提问失败",
									Toast.LENGTH_SHORT).show();
						}

						break;

					case POST_IMAGE_SUCCESS:
						postAll();

					default:
						break;
					}
					super.handleMessage(msg);
				}

			};

			@Override
			public void run() {
				Log.v(TAG, "imagePath:" + imagePath);
				if (imagePath == null || imagePath.isEmpty()) {
					handler.sendEmptyMessage(POST_IMAGE_SUCCESS);
					return;
				}
				String imageResult = NetUtil.uploadFile(new File(imagePath),
						POST_IMAGE_API);
				if (imageResult != null && !imageResult.isEmpty()) {
					try {
						JSONObject jObject = new JSONObject(imageResult);
						int state = jObject.getInt("stat");
						if (state == 1) {
							imageUrl = jObject.getString("url");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (!imageUrl.isEmpty()) {
						handler.sendEmptyMessage(POST_IMAGE_SUCCESS);
					}
				}

			}

			private void postAll() {
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("content", content));
				list.add(new BasicNameValuePair("tag", tag));
				if (teacherId != null && !teacherId.isEmpty()) {
					list.add(new BasicNameValuePair("teacherId", teacherId));
				}
				if (imageUrl != null && !imageUrl.isEmpty()) {
					list.add(new BasicNameValuePair("image", imageUrl));
				}
				new HttpConnectionUtils(handler).post(postProblemAPI, list);
			}
		}).start();
		return true;
	}

}
