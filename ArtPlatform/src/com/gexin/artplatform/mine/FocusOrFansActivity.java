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
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Fans;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.friends.ViewOtherUserActivity;
import com.gexin.artplatform.studio.RoomDetailActivity;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.TitleBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FocusOrFansActivity extends Activity {

	private Gson gson = new Gson();
	private List<Fans> fansList = new ArrayList<Fans>();
	private MyFansListAdapter adapter;
	private LinearLayout llBack;
	private String id;
	private int type;

	private TitleBar titleBar;
	private ListView mListView;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_fans);
		initView();
		initTitleBar();
		id = getIntent().getStringExtra("id");
		String url = "";
		type = getIntent().getIntExtra("type", 0);
		if (type == 2) {
			url = Constant.SERVER_URL + "/api/user/" + id + "/fan";
			titleBar.setTitle("他的粉丝");
		} else if (type == 1) {
			url = Constant.SERVER_URL + "/api/user/" + id + "/follow";
			titleBar.setTitle("他的关注");
		} else if (type == 3) {
			url = Constant.SERVER_URL + "/api/studio/" + id + "/fan";
			titleBar.setTitle("他的粉丝");
		} else if (type == 4) {
			url = Constant.SERVER_URL + "/api/studio/" + id + "/follow";
			titleBar.setTitle("他的关注");
		}

		String userId = (String) SPUtil.get(this, "userId", "");
		if (!userId.isEmpty()) {
			url += "?userId=" + userId;
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
							fansList.clear();
							fansList.addAll(success(jObject));
							initList();

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

	private void initView() {
		mListView = (ListView) findViewById(R.id.lv_fans_list);
	}

	private void initTitleBar() {
		titleBar = (TitleBar) findViewById(R.id.tb_myfans);
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

	private void initList() {

		adapter = new MyFansListAdapter(this, fansList);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (fansList.get(position).getuType() == 2) {
					Intent intent = new Intent(FocusOrFansActivity.this,
							RoomDetailActivity.class);
					intent.putExtra("studioId", fansList.get(position).get_id());
					startActivity(intent);
				} else {
					Intent intent = new Intent(FocusOrFansActivity.this,
							ViewOtherUserActivity.class);
					intent.putExtra(
							"userId",
							fansList.get(position).get_id() == null ? fansList
									.get(position).getUserId() : fansList.get(
									position).get_id());
					startActivity(intent);
				}
			}
		});
	}

	private List<Fans> success(JSONObject jObject) {
		int state = -1;
		List<Fans> temp = null;
		try {
			state = jObject.getInt("stat");
			if (state == 1) {
				if (type == 1) {
					temp = gson.fromJson(jObject.getJSONArray("follow")
							.toString(), new TypeToken<List<Fans>>() {
					}.getType());
				} else if (type == 2) {
					temp = gson.fromJson(
							jObject.getJSONArray("fan").toString(),
							new TypeToken<List<Fans>>() {
							}.getType());
				} else if (type == 3) {
					temp = gson.fromJson(jObject.getJSONArray("fans")
							.toString(), new TypeToken<List<Fans>>() {
					}.getType());
				} else if (type == 4) {
					temp = gson.fromJson(jObject.getJSONArray("follows")
							.toString(), new TypeToken<List<Fans>>() {
					}.getType());
				}
				if (temp == null) {
					temp = new ArrayList<Fans>();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return temp;
	}
}
