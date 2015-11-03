package com.gexin.artplatform.mine;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.gexin.artplatform.R;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.discover.GallaryGridAdapter;
import com.gexin.artplatform.largeImg.LargeImageActivity;
import com.gexin.artplatform.utils.BroadcastUtil;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.TitleBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MyCollectActivity extends Activity {
	private static final String TAG = "MyCollectActivity";
	private static final String Collection_API = Constant.SERVER_URL
			+ Constant.USER_API;
	protected static final int LARGE_IMAGE_REQUEST = 0;
	private List<String> UrlList = new ArrayList<String>();
	private Gson gson = new Gson();
	private GallaryGridAdapter adapter;
	private int type = 0;

	private GridView mGridView;
	private LinearLayout llBack;
	private TitleBar titleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_favorite);
		type = getIntent().getIntExtra("type", 0);
		mGridView = (GridView) findViewById(R.id.gv_collection);
		adapter = new GallaryGridAdapter(MyCollectActivity.this, UrlList);
		mGridView.setAdapter(adapter);
		initTitleBar();
		initData();
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(MyCollectActivity.this,
						LargeImageActivity.class);
				intent.putStringArrayListExtra("images",
						(ArrayList<String>) UrlList);
				intent.putExtra("index", arg2);
				if(type==2){
					intent.putExtra("type", 0);
				}else {
					intent.putExtra("type", 1);
				}
//				intent.putExtra("type", type);
				startActivityForResult(intent, LARGE_IMAGE_REQUEST);
			}
		});
	}

	private void initTitleBar() {
		titleBar = (TitleBar) findViewById(R.id.tb_mycollect);
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
				Log.v(TAG, "BackClick");
				finish();
			}
		});
		if(type==2){
			titleBar.setTitle("收藏");
		}
	}

	@SuppressLint("HandlerLeak")
	private void initData() {
		String userId = (String) SPUtil.get(MyCollectActivity.this, "userId",
				"");
		String api = "";
		if (type == 0) {
			api = Collection_API + "/"
					+ (String) SPUtil.get(this, "userId", "") + "/collection";
		} else {
			String id = getIntent().getStringExtra("id");
			api = Collection_API + "/" + id + "/collection";
		}
		if (!userId.isEmpty()) {
			api += "?userId=" + userId;
		}

		Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					String response = (String) msg.obj;
					try {
						JSONObject jObject = new JSONObject(
								response == null ? "" : response.trim());
						if (jObject != null) {
							UrlList.clear();
							UrlList.addAll(success(jObject));
							adapter.notifyDataSetChanged();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			}

		};

		new HttpConnectionUtils(handler).get(api);
	}

	private List<String> success(JSONObject jObject) {
		int state = -1;
		List<String> tempList = null;
		Log.i(TAG, "jObject:" + jObject.toString());
		try {
			state = jObject.getInt("stat");
			if (state == 1) {
				tempList = gson.fromJson(jObject.getJSONArray("collection")
						.toString(), new TypeToken<List<String>>() {
				}.getType());
				BroadcastUtil.updateUserInfo(MyCollectActivity.this);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tempList;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case LARGE_IMAGE_REQUEST:
			if (resultCode == RESULT_OK) {
//				List<String> delUrls = data.getStringArrayListExtra("delUrls");
//				for (String str : delUrls) {
//					UrlList.remove(str);
//				}
				String delUrl = data.getStringExtra("delUrl");
				UrlList.remove(delUrl);
				adapter.notifyDataSetChanged();
			}

			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
