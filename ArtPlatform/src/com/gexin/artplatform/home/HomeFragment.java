package com.gexin.artplatform.home;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Article;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.database.DatabaseManager;
import com.gexin.artplatform.discover.FindStudioActivity;
import com.gexin.artplatform.dlog.DLog;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.utils.NetUtil;
import com.gexin.artplatform.view.TitleBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class HomeFragment extends Fragment {

	private static final String TAG = "HomeFragment";
	private static final String ARTICLE_API = Constant.SERVER_URL
			+ "/api/articles";
	protected static final int CITY_INFLATE_REQUEST = 0;
	private List<Article> dataList = new ArrayList<Article>();
	private HomeListAdapter adapter;
	private Gson gson = new Gson();
	private int option = 0;
	private String city = "广州";
	private DatabaseManager mManager;

	private TitleBar titleBar;
	private LinearLayout llAddr;
	private LinearLayout llSearch;
	private LinearLayout llTimeFirst;
	private LinearLayout llHotFirst;
	private View vLeftLine;
	private View vRightLine;
	private TextView tvHotFirst;
	private TextView tvTimeFirst;
	private PullToRefreshListView mListView;
	private TextView tvAddr;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		initView(view);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	private void initData() {
		mManager = new DatabaseManager(getActivity());
		adapter = new HomeListAdapter(dataList, getActivity());
		mListView.setMode(Mode.BOTH);
		mListView.setAdapter(adapter);

		mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// Do work to refresh the list here.
				new GetLatestDataTask().execute();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new GetNextDataTask().execute();

			}
		});
		getData(0);
	}

	@SuppressLint("HandlerLeak")
	private void getData(int option) {
		Handler handler = new HttpHandler(getActivity()) {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					dealResponse((String) msg.obj);
					adapter.notifyDataSetChanged();
					break;
				case HttpConnectionUtils.DID_ERROR:
					List<Article> tmpList = mManager.getAllArticles();
					dataList.clear();
					dataList.addAll(tmpList);
					DLog.v(TAG, "Error:" + dataList.toString());
					adapter.notifyDataSetChanged();
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		String api = genApiUrl(option, 0);
		DLog.i(TAG, "url:" + api);
		new HttpConnectionUtils(handler).get(api);
	}

	private String genApiUrl(int i, long timestamp) {
		String api = ARTICLE_API + "?sort=" + i;
		if (timestamp != 0) {
			api += "&timestamp=" + timestamp;
		}
		if (!city.equals("全国")) {
			api += "&city=" + city;
		}
		api += "&b_web=2";
		return api;
	}

	private void initView(View view) {
		titleBar = (TitleBar) view.findViewById(R.id.tb_fragment_home);
		llTimeFirst = (LinearLayout) view
				.findViewById(R.id.ll_timefirst_fragment_home);
		llHotFirst = (LinearLayout) view
				.findViewById(R.id.ll_hotfirst_fragment_home);
		tvHotFirst = (TextView) view
				.findViewById(R.id.tv_hotfirst_fragment_home);
		tvTimeFirst = (TextView) view
				.findViewById(R.id.tv_timefirst_fragment_home);
		vLeftLine = view.findViewById(R.id.v_leftline_fragment_home);
		vRightLine = view.findViewById(R.id.v_rightline_fragment_home);
		mListView = (PullToRefreshListView) view
				.findViewById(R.id.lv_fragment_home);

		initTitleBar();
		llHotFirst.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				tvHotFirst.setTextColor(Color.parseColor("#cdfe6060"));
				vLeftLine.setVisibility(View.VISIBLE);
				tvTimeFirst.setTextColor(Color.parseColor("#cd504f4f"));
				vRightLine.setVisibility(View.GONE);
				option = 1;
				getData(1);
			}
		});
		llTimeFirst.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				tvHotFirst.setTextColor(Color.parseColor("#cd504f4f"));
				vLeftLine.setVisibility(View.GONE);
				tvTimeFirst.setTextColor(Color.parseColor("#cdfe6060"));
				vRightLine.setVisibility(View.VISIBLE);
				option = 0;
				getData(0);
			}
		});

	}

	private void initTitleBar() {
		llAddr = new LinearLayout(getActivity());
		ImageView ivAddr = new ImageView(getActivity());
		ivAddr.setImageResource(R.drawable.map_icon);
		LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 0, 10, 0);
		llAddr.addView(ivAddr, params);
		tvAddr = new TextView(getActivity());
		tvAddr.setText(city);
		tvAddr.setTextSize(20);
		tvAddr.setTextColor(Color.WHITE);
		llAddr.addView(tvAddr);
		llAddr.setGravity(Gravity.CENTER_VERTICAL);
		llAddr.setBackgroundResource(R.drawable.selector_titlebar_btn);
		titleBar.setLeftView(llAddr);

		llAddr.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),
						CityInflateActivity.class);
				startActivityForResult(intent, CITY_INFLATE_REQUEST);
			}
		});

		llSearch = new LinearLayout(getActivity());
		ImageView ivSearch = new ImageView(getActivity());
		ivSearch.setBackgroundResource(R.drawable.search_icon);
		params.setMargins(10, 10, 10, 10);
		llSearch.addView(ivSearch, params);
		llSearch.setGravity(Gravity.CENTER_VERTICAL);
		llSearch.setPadding(0, 0, 20, 0);
		titleBar.setRightView(llSearch);

		llSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),
						FindStudioActivity.class);
				intent.putExtra("type", 1);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CITY_INFLATE_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				// tvHotFirst.setTextColor(Color.parseColor("#cd504f4f"));
				// vLeftLine.setVisibility(View.GONE);
				// tvTimeFirst.setTextColor(Color.parseColor("#cdfe6060"));
				// vRightLine.setVisibility(View.VISIBLE);
				city = data.getStringExtra("city");
				tvAddr.setText(city);
				// option = 0;
				getData(option);
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private class GetLatestDataTask extends
			AsyncTask<Void, Void, List<Article>> {

		@Override
		protected List<Article> doInBackground(Void... params) {
			// Simulates a background job.
			String api = genApiUrl(option, 0);
			String result = "";
			DLog.i(TAG, api);
			result = NetUtil.connect(NetUtil.GET, api, null);
			dealResponse(result);
			return dataList;
		}

		@Override
		protected void onPostExecute(List<Article> result) {
			adapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			mListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	private class GetNextDataTask extends AsyncTask<Void, Void, List<Article>> {

		@Override
		protected List<Article> doInBackground(Void... params) {
			// Simulates a background job.
			String result = "";
			if (dataList == null || dataList.size() == 0) {
				return new ArrayList<Article>();
			}
			String api = genApiUrlBySkip(option, dataList.size());
			result = NetUtil.connect(NetUtil.GET, api, null);
			DLog.i(TAG, "url:" + api);
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(result);
				int state = jsonObject.getInt("stat");
				if (state == 1) {
					List<Article> tmpList = gson.fromJson(jsonObject
							.getJSONArray("articles").toString(),
							new TypeToken<List<Article>>() {
							}.getType());
					dataList.addAll(tmpList);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return dataList;
		}

		@Override
		protected void onPostExecute(List<Article> result) {
			DLog.i(TAG, "problemNum:" + dataList.size());
			adapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			mListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	
	/**
	 * @param option 0:timefirst 1:hotfirst
	 * @param skip timestamp
	 * */
	private String genApiUrlBySkip(int option, int skip) {
		String url = ARTICLE_API + "?sort=" + option + "&skip=" + skip;
		if (!city.equals("全国")) {
			url += "&city=" + city;
		}
		// 表示android端，为了解决web网页跟客户端样式不一致问题
		url += "&b_web=2";
		return url;
	}

	private void dealResponse(String obj) {
		int state = 0;
		try {
			JSONObject jsonObject = new JSONObject(obj);
			state = jsonObject.getInt("stat");
			if (state == 1) {
				List<Article> tmpList = gson.fromJson(
						jsonObject.getJSONArray("articles").toString(),
						new TypeToken<List<Article>>() {
						}.getType());
				dataList.clear();
				dataList.addAll(tmpList);
				DLog.v(TAG, "dataList:" + dataList.toString());
				// adapter.notifyDataSetChanged();
				mManager.clearArticleTable();
				mManager.addArticles(dataList);
				// Log.v(TAG, mManager.getAllArticles().toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
