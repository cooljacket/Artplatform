package com.gexin.artplatform.question;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Problem;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.database.DatabaseManager;
import com.gexin.artplatform.dlog.DLog;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.NetUtil;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.TitleBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 问答Fragment
 * 
 * @author xiaoxin 2015-4-29
 */
public class QuestionFragment extends Fragment {

	private static final String TAG = "QuestionFragment";
	private static final int POST_REQUEST_CODE = 1;
	private static final String PROBLEMS_API = Constant.SERVER_URL
			+ Constant.PROBLEM_API + "s";
	private Gson gson = new Gson();
	private DatabaseManager mManager;

	private PullToRefreshListView mListView;
	private TextView tvAsk;
	private QuestionAdapter adapter;
	private List<Problem> problems;
	private LinearLayout llAsk;
	private TitleBar titleBar;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_question, container,
				false);
		initView(view);
		return view;
	}

	/**
	 * 初始化控件
	 * 
	 * @param view
	 */
	private void initView(View view) {
		titleBar = (TitleBar) view.findViewById(R.id.tb_fragment_question);
		mListView = (PullToRefreshListView) view.findViewById(R.id.lv_question);
		// 设置为上下两侧都可以拉动
		mListView.setMode(Mode.BOTH);
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

		setRightView();

	}

	private void setRightView() {
		llAsk = new LinearLayout(getActivity());
		tvAsk = new TextView(getActivity());
		tvAsk.setText("提问");
		tvAsk.setTextSize(20);
		tvAsk.setTextColor(Color.WHITE);
		LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 0, 10, 0);
		llAsk.setGravity(Gravity.CENTER_VERTICAL);
		llAsk.addView(tvAsk, params);
		llAsk.setBackgroundResource(R.drawable.selector_titlebar_btn);
		titleBar.setRightView(llAsk);

		llAsk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String userId = (String) SPUtil
						.get(getActivity(), "userId", "");
				if (userId.isEmpty()) {
					Toast.makeText(getActivity(), "请先登录才能提问",
							Toast.LENGTH_SHORT).show();
					return;
				}
				startActivityForResult(new Intent(getActivity(),
						PostProblemActivity.class), POST_REQUEST_CODE);
			}
		});
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		IntentFilter filter = new IntentFilter();
		filter.addAction(QuestionInfoActivity.ACTION_PROBLEM_CHANGE);
		getActivity().registerReceiver(mReceiver, filter);
		initData();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.v(TAG, "positon:" + arg2);
				Problem problem = (Problem) adapter.getItem(arg2 - 1);
				Intent intent = new Intent(getActivity(),
						QuestionInfoActivity.class);
				intent.putExtra("problemId", problem.get_id());
				startActivity(intent);
			}
		});
	}

	/**
	 * 初始化数据
	 */
	@SuppressLint("HandlerLeak")
	private void initData() {
		mManager = new DatabaseManager(getActivity());
		String userId = (String) SPUtil.get(getActivity(), "userId", "");
		String api = PROBLEMS_API;
		if (!userId.isEmpty()) {
			api += "?userId=" + userId;
		}
		problems = new ArrayList<Problem>();
		adapter = new QuestionAdapter(getActivity(), problems);
		mListView.setAdapter(adapter);
		Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					String response = (String) msg.obj;
					DLog.i(TAG, response);
					try {
						JSONObject jObject = new JSONObject(
								response == null ? "" : response.trim());
						if (jObject != null) {
							List<Problem> tempList = success(jObject);
							if (tempList != null) {
								problems.clear();
								problems.addAll(tempList);
								adapter.notifyDataSetChanged();
								mManager.addProblems(problems);
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case HttpConnectionUtils.DID_ERROR:
					List<Problem> tmpList = mManager.getAllProblems();
					problems.clear();
					problems.addAll(tmpList);
					adapter.notifyDataSetChanged();
					break;

				default:
					break;
				}
			}

		};

		DLog.i(TAG, "api: " + api);
		new HttpConnectionUtils(handler).get(api);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v(TAG, "requestCode:" + requestCode);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case POST_REQUEST_CODE:

				break;

			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private class GetLatestDataTask extends
			AsyncTask<Void, Void, List<Problem>> {

		@Override
		protected List<Problem> doInBackground(Void... params) {
			// Simulates a background job.
			String userId = (String) SPUtil.get(getActivity(), "userId", "");
			String api = PROBLEMS_API;
			if (!userId.isEmpty()) {
				api += "?userId=" + userId;
			}
			String result = "";
			//Log.v(TAG, api);
			DLog.i("GetLatestDataTask", "api: " + api);
			result = NetUtil.connect(NetUtil.GET, api, null);
			// Log.v(TAG, "result:" + result);
			DLog.i("GetLatestDataTask", "result: " + result);
			try {
				JSONObject jObject = new JSONObject(result == null ? ""
						: result.trim());
				List<Problem> tempList = success(jObject);
				if (tempList != null) {
					Log.v(TAG, "Problems:" + tempList.toString());
					problems.clear();
					problems.addAll(tempList);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return problems;
		}

		@Override
		protected void onPostExecute(List<Problem> result) {
			adapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			mListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	private class GetNextDataTask extends AsyncTask<Void, Void, List<Problem>> {

		@Override
		protected List<Problem> doInBackground(Void... params) {
			// Simulates a background job.
			String result = "";
			// String prm = "?skip=" + problems.size();
			if (problems == null || problems.size() == 0) {
				return new ArrayList<Problem>();
			}
			String userId = (String) SPUtil.get(getActivity(), "userId", "");
			String api = PROBLEMS_API;
			api += "?timestamp="
					+ problems.get(problems.size() - 1).getTimestamp();
			if (!userId.isEmpty()) {
				api += "&userId=" + userId;
			}
			result = NetUtil.connect(NetUtil.GET, api, null);
			DLog.v(TAG, "url:" + api);
			try {
				JSONObject jObject = new JSONObject(result == null ? ""
						: result.trim());
				List<Problem> tempList = success(jObject);
				if (tempList != null) {
					problems.addAll(tempList);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return problems;
		}

		@Override
		protected void onPostExecute(List<Problem> result) {
			DLog.v(TAG, "problemNum:" + problems.size());
			adapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			mListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	/**
	 * 获取后台数据成功执行的操作
	 * 
	 * @param jObject
	 * @return
	 */
	private List<Problem> success(JSONObject jObject) {
		int state = -1;
		List<Problem> tempList = null;
		Log.v(TAG, "jObject:" + jObject.toString());
		try {
			state = jObject.getInt("stat");
			if (state == 1) {
				try {
					tempList = gson.fromJson(jObject.getJSONArray("problems")
							.toString(), new TypeToken<List<Problem>>() {
					}.getType());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// Log.v(TAG, "success:" + tempList);
		return tempList;

	}

	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(mReceiver);
		super.onDestroy();
	};

	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			Log.v(TAG, arg1.getAction());
			if (QuestionInfoActivity.ACTION_PROBLEM_CHANGE.equals(arg1
					.getAction())) {
				int zanNum = arg1.getIntExtra("zanNum", -1);
				int commentNum = arg1.getIntExtra("commentNum", -1);
				int isZan = arg1.getIntExtra("isZan", -1);
				String problemId = arg1.getStringExtra("problemId");
				DLog.v(TAG, "problemId"+problemId);
				for (int i=0;i<problems.size();i++) {
					if (problems.get(i).get_id().equals(problemId)) {
						if (zanNum != -1) {
							problems.get(i).setZan(zanNum);
						}
						if (commentNum != -1) {
							problems.get(i).setCommentNum(commentNum);
						}
						if (isZan != -1) {
							problems.get(i).setIsZan(isZan);
						}
						adapter.notifyDataSetChanged();
						break;
					}
				}
			}
		}
	};

}
