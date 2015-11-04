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
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.TitleBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MyWorkActivity extends Activity {

	private static final String TAG = "MyWorkActivity";
	protected static final int LARGE_IMAGE_REQUEST = 0;
	private List<String> mList = new ArrayList<String>();
	private Gson gson = new Gson();
	private int type = 0;
	private String userId = null;
	private GallaryGridAdapter adapter;
	private GridView mGridView;
	private LinearLayout llBack;
	private TitleBar titleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_gallery);
		type = getIntent().getIntExtra("type", 0);
		userId = getIntent().getStringExtra("id");
		initTitleBar();
		mGridView = (GridView) findViewById(R.id.gv_Gallary);
		adapter = new GallaryGridAdapter(MyWorkActivity.this, mList);
		mGridView.setAdapter(adapter);
		initData();
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(MyWorkActivity.this,
						LargeImageActivity.class);
				intent.putStringArrayListExtra("images",
						(ArrayList<String>) mList);
				intent.putExtra("index", arg2);
				intent.putExtra("type", 2);
				startActivityForResult(intent, LARGE_IMAGE_REQUEST);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private void initData() {
		if (userId == null) {
			userId = (String) SPUtil.get(MyWorkActivity.this, "userId", "");
		}
		String url = Constant.SERVER_URL + "/api/user/" + userId + "/work";

		Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					String response = (String) msg.obj;
					try {
						JSONObject jObject = new JSONObject(response);
						int state = jObject.getInt("stat");
						if (state == 1) {
							List<String> tempList = gson.fromJson(jObject
									.getJSONArray("work").toString(),
									new TypeToken<List<String>>() {
									}.getType());
							mList.addAll(tempList);
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

		new HttpConnectionUtils(handler).get(url);
	}

	private void initTitleBar() {
		titleBar = (TitleBar) findViewById(R.id.tb_mywork);
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
		if (type == 1) {
			titleBar.setTitle("作品");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case LARGE_IMAGE_REQUEST:
			if (resultCode == RESULT_OK) {
				// List<String> delUrls =
				// data.getStringArrayListExtra("delUrls");
				// for (String str : delUrls) {
				// UrlList.remove(str);
				// }
				String delUrl = data.getStringExtra("delUrl");
				mList.remove(delUrl);
				adapter.notifyDataSetChanged();
			}

			break;

		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

}
