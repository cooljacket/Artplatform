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
import com.gexin.artplatform.bean.Fans;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.friends.ViewOtherUserActivity;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.TitleBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MyFansActivity extends Activity {
	private static final String TAG = "MyFansActivity";
	private String MyFans_API = Constant.SERVER_URL + Constant.USER_API;
	private Gson gson = new Gson();
	private List<Fans> fansList = new ArrayList<Fans>();
	private ListView list;
	private MyFansListAdapter adapter;
	private LinearLayout llBack;
	private TitleBar titleBar;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_fans);

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
							fansList.clear();
							fansList.addAll(success(jObject));
//							adapter.notifyDataSetChanged();
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
				+ (String) SPUtil.get(this, "userId", "") + "/fan");
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
				Log.v(TAG, "BackClick");
				finish();
			}
		});
	}

	private void initList() {
		list = (ListView) findViewById(R.id.lv_fans_list);
		adapter = new MyFansListAdapter(this,fansList);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putString("userId", fansList.get(position).get_id());
				Intent intent = new Intent(MyFansActivity.this, ViewOtherUserActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
	
//	private static Bitmap getBitMBitmap(String urlpath) {
//		Bitmap map = null;
//		try {
//			URL url = new URL(urlpath);
//			URLConnection conn = url.openConnection();
//			conn.connect();
//			InputStream in;
//			in = conn.getInputStream();
//			map = BitmapFactory.decodeStream(in);
//			in.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return map;
//	}
	
	private List<Fans> success(JSONObject jObject) {
		int state = -1;
		List<Fans> temp = null;
		try {
			state = jObject.getInt("stat");
			if (state == 1) {
				temp = gson.fromJson(jObject.getJSONArray("fan").toString(),
						new TypeToken<List<Fans>>() {
						}.getType());
				if (temp == null) {
					temp = new ArrayList<Fans>();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return temp;
	}
//
//	@SuppressLint("HandlerLeak")
//	private void setData() {
//		final Handler adaptHhandler = new Handler(){
//			@Override
//			public void handleMessage(Message msg) {
//				
//				switch (msg.what){
//				case 1:
//					list.setAdapter(adapter);
//					break;
//				default:
//					break;
//				}
//			}
//		};
//		new Thread(){
//			@Override
//			public void run() {
//				Log.i(TAG, "running");
//				for (int i = 0; i < fansList.size(); i++) {
//					Log.i(TAG, "running"+i);
//					Map<String, Object> map = new HashMap<String, Object>();
//					map.put("name", fansList.get(i).getName());
//					map.put("avatarUrl", getBitMBitmap(fansList.get(i).getAvatarUrl()));
//					mDataList.add(map);
//					adaptHhandler.sendEmptyMessage(1);
//				}
//			}
//		}.start();
//
//	}
}
