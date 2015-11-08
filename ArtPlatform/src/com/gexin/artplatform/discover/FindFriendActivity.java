package com.gexin.artplatform.discover;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.User;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.dlog.DLog;
import com.gexin.artplatform.friends.ViewOtherUserActivity;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.view.TitleBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FindFriendActivity extends Activity {

	private static final String TAG = "FindFriendActivity";
	private List<User> mList = new ArrayList<User>();
	private FindFriendAdapter adapter;
	private Gson gson = new Gson();

	private LinearLayout llBack;
	private TitleBar titleBar;
	private ListView mListView;
	private SearchView mSearchView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_friend);

		initView();
		initData();
	}

	private void initData() {
		adapter = new FindFriendAdapter(this, mList);
		mListView.setAdapter(adapter);
		mSearchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String arg0) {
				getUsers(arg0);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String arg0) {
				return false;
			}
		});
		mSearchView.setOnSearchClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getUsers(mSearchView.getQuery().toString());
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(FindFriendActivity.this,
						ViewOtherUserActivity.class);
				intent.putExtra("userId", mList.get(arg2).getUserId());
				startActivity(intent);
			}
		});
		// 一开始加载所有用户数据
		getUsers("");
	}

	protected void getUsers(String name) {
		String url = Constant.SERVER_URL + "/api/user/name?name=" + name;
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
				List<User> tmpList = gson.fromJson(jObject
						.getJSONArray("users").toString(),
						new TypeToken<List<User>>() {
						}.getType());
				DLog.v(TAG, tmpList.toString());
				mList.clear();
				mList.addAll(tmpList);
				adapter.notifyDataSetChanged();
				if (mList.isEmpty()) {
					Toast.makeText(this, "没有匹配的好友", Toast.LENGTH_SHORT).show();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_activity_find_friend);
		mListView = (ListView) findViewById(R.id.lv_activity_find_friend);
		mSearchView = (SearchView) findViewById(R.id.sv_activity_find_friend);

		initTitleBar();
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
}
