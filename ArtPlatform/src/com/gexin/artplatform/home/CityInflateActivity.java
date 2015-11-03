package com.gexin.artplatform.home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.gexin.artplatform.bean.CityItem;
import com.gexin.artplatform.utils.HanziToPinyin;
import com.gexin.artplatform.view.SideBar;
import com.gexin.artplatform.view.SideBar.onTouchLetterChangeListener;
import com.gexin.artplatform.view.TitleBar;

public class CityInflateActivity extends Activity {

	private static final String TAG = "CityInflateActivity";
	private List<CityItem> cityList = new ArrayList<CityItem>();
	private CitySortAdapter adapter;

	private ListView mListView;
	private SideBar mSideBar;
	private SearchView mSearchView;
	private TextView tvLetter;
	private TitleBar titleBar;
	private LinearLayout llBack;
	private TextView tvAllCity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_inflate);

		initView();
		initData();
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.lv_city_inflate);
		mSideBar = (SideBar) findViewById(R.id.sb_city_inflate);
		mSearchView = (SearchView) findViewById(R.id.sv_city_inflate);
		tvLetter = (TextView) findViewById(R.id.tv_letter_city_inflate);
		titleBar = (TitleBar) findViewById(R.id.tb_city_inflate);
		tvAllCity = (TextView) findViewById(R.id.tv_allcity_city_inflate);
		initTitleBar();
		tvAllCity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("city", "全国");
				setResult(RESULT_OK, intent);
				finish();
			}
		});
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

	private void initData() {
		cityList = CityItem.getCityList();
		Log.v(TAG, "size:" + cityList.size());
		try {
			Collections.sort(cityList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!HanziToPinyin.getInstance().getHasChinaCollator()) {
			mSideBar.setVisibility(View.GONE);
		}
		adapter = new CitySortAdapter(cityList, this);
		mListView.setAdapter(adapter);
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
		mSearchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String arg0) {
				onQueryChange(arg0);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String arg0) {
				onQueryChange(arg0);
				return false;
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.putExtra("city", cityList.get(arg2).getName());
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	@SuppressLint("DefaultLocale")
	private void onQueryChange(String arg0) {
		if (arg0.isEmpty()) {
			adapter.updateData(cityList);
		} else {
			List<CityItem> tmpList = new ArrayList<CityItem>();
			arg0 = arg0.toLowerCase();
			for (CityItem item : cityList) {
				if (item.getName().contains(arg0)
						|| item.getFullPinyin().contains(arg0)
						|| item.getSimplePinyin().contains(arg0)) {
					tmpList.add(item);
				}
			}
			adapter.updateData(tmpList);
		}
	}
}
