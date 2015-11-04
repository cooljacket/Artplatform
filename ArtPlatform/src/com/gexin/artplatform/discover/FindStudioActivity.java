package com.gexin.artplatform.discover;

import java.util.ArrayList;
import java.util.Collections;
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
import android.widget.TextView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.SimpleStudio;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.studio.RoomDetailActivity;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.view.SideBar;
import com.gexin.artplatform.view.SideBar.onTouchLetterChangeListener;
import com.gexin.artplatform.view.TitleBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FindStudioActivity extends Activity {

	private static final String TAG = "FindStudioActivity";
	private List<SimpleStudio> mList = new ArrayList<SimpleStudio>();
	private FindStudioAdapter adapter;
	private Gson gson = new Gson();
	private int type = 0;

	private SideBar mSideBar;
	private ListView mListView;
	private SearchView mSearchView;
	private TextView tvLetter;
	private TitleBar titleBar;
	private LinearLayout llBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_studio);
		type = getIntent().getIntExtra("type", 0);
		
		initView();
		initData();
		getStudioData();
	}

	private void initData() {
		adapter = new FindStudioAdapter(this, mList);
		mListView.setAdapter(adapter);
		
		if(type==0){
			titleBar.setTitle("优秀画室");
		}else {
			titleBar.setTitle("搜索画室");
		}

		mSideBar.setLetterShow(tvLetter);
		mSideBar.setTouchLetterChangeListener(new onTouchLetterChangeListener() {

			@Override
			public void onTouchLetterChange(String letter) {
				int position = adapter.getPositionForSection(letter.charAt(0));
				if (position != -1) {
					mListView.setSelection(position);
				}
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(FindStudioActivity.this,
						RoomDetailActivity.class);
				String studioId = ((SimpleStudio) adapter.getItem(arg2))
						.getStudioId();
				intent.putExtra("studioId", studioId);
				startActivity(intent);
			}
		});

		mSearchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String arg0) {
				inflateStudios(arg0);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String arg0) {
				inflateStudios(arg0);
				return false;
			}
		});
	}

	private void inflateStudios(String queryStr) {
		if (queryStr.isEmpty()) {
			adapter.updateData(mList);
			return;
		}
		List<SimpleStudio> tmpList = new ArrayList<SimpleStudio>();
		for (SimpleStudio studio : mList) {
			if (studio.getName().contains(queryStr) ) {
				tmpList.add(studio);
				continue;
			}
			try {
				if(studio.getFullPinyin().contains(queryStr)
						|| studio.getSimplePinyin().contains(queryStr)){
					tmpList.add(studio);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		adapter.updateData(tmpList);
	}

	private void getStudioData() {
		String url = Constant.SERVER_URL + "/api/studios";
		Handler handler = new HttpHandler(this) {
			@Override
			protected void succeed(JSONObject jObject) {
				try {
					int state = jObject.getInt("stat");
					if (state == 1) {
						List<SimpleStudio> tempList = gson.fromJson(jObject
								.getJSONArray("studios").toString(),
								new TypeToken<List<SimpleStudio>>() {
								}.getType());
						Log.v(TAG, "tempList:" + tempList.toString());
						mList.clear();
						mList.addAll(tempList);
						Collections.sort(mList);
						adapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		};
		new HttpConnectionUtils(handler).get(url);
	}

	private void initView() {
		mSearchView = (SearchView) findViewById(R.id.sv_activity_find_studio);
		mListView = (ListView) findViewById(R.id.lv_activity_find_studio);
		mSideBar = (SideBar) findViewById(R.id.sb_activity_find_studio);
		tvLetter = (TextView) findViewById(R.id.tv_letter_activity_find_studio);
		titleBar = (TitleBar) findViewById(R.id.tb_activity_find_studio);
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
				Log.v(TAG, "BackClick");
				finish();
			}
		});
	}
}
