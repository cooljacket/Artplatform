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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Follow;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.friends.ViewOtherUserActivity;
import com.gexin.artplatform.studio.RoomDetailActivity;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.TitleBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MyFocusActivity extends Activity {
	private static final String TAG = "MyFocusActivity";
	private String MyFans_API = Constant.SERVER_URL + Constant.USER_API;
	private Gson gson = new Gson();
	private ListView list;
	private MyFollowListAdapter adapter;
	private List<Follow> followList = new ArrayList<Follow>();
	private LinearLayout llBack;
	private TitleBar titleBar;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_focus);

		initTitleBar();

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
							followList.clear();
							followList.addAll(success(jObject));
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
		new HttpConnectionUtils(handler).get(MyFans_API + "/"
				+ (String) SPUtil.get(this, "userId", "") + "/follow");
	}
	
	private void initTitleBar() {
		titleBar = (TitleBar) findViewById(R.id.tb_myfocus);
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
	}
	
	private void initList() {
		list = (ListView) findViewById(R.id.lv_my_follow);
		adapter = new MyFollowListAdapter(this,followList);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(followList.get(position).getuType()==2){
					Intent intent = new Intent(MyFocusActivity.this,
							RoomDetailActivity.class);
					intent.putExtra("studioId",followList.get(position).get_id());
					startActivity(intent);
				}else {
					Intent intent = new Intent(MyFocusActivity.this, ViewOtherUserActivity.class);
					intent.putExtra("userId", followList.get(position).get_id());
					startActivity(intent);
				}
			}
		});
	}
	
	private List<Follow> success(JSONObject jObject) {
		int state = -1;
		List<Follow> temp = null;
		Log.i(TAG, "jObject:" + jObject.toString());
		try {
			state = jObject.getInt("stat");
			if (state == 1) {
				temp = gson.fromJson(jObject.getJSONArray("follow")
						.toString(), new TypeToken<List<Follow>>() {
				}.getType());
				if (temp != null) {
					
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		 Log.i(TAG, "success:" + temp);
		 return temp;

	}
}
