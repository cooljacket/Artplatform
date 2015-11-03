package com.gexin.artplatform.mine;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
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
import com.gexin.artplatform.bean.Problem;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.question.QuestionAdapter;
import com.gexin.artplatform.question.QuestionInfoActivity;
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

public class MyQuestionActivity extends Activity {
	private static final String TAG = "MyQuestionActivity";

	private String MyQuestion_API = Constant.SERVER_URL + Constant.USER_API;
	private Gson gson = new Gson();
	private static final int POST_REQUEST_CODE = 1;
	private String userId = null;
	private int type = 0;
	private PullToRefreshListView mListView;
	private QuestionAdapter adapter;
	private List<Problem> problems;
	private LinearLayout llBack;
	private TitleBar titleBar;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_question);
		userId = getIntent().getStringExtra("id");
		type = getIntent().getIntExtra("type", 0);
		initView();
		initTitleBar();
		initData();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.v(TAG, "positon:" + arg2);
				Problem problem = (Problem) adapter.getItem(arg2 - 1);
				Intent intent = new Intent(MyQuestionActivity.this,
						QuestionInfoActivity.class);
				intent.putExtra("problemId", problem.get_id());
				startActivity(intent);
			}
		});

	}

	private void initView() {
		mListView = (PullToRefreshListView) findViewById(R.id.lv_my_question);
		mListView.setMode(Mode.BOTH);
		mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(
						MyQuestionActivity.this, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
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

	}

	/**
	 * 初始化数据
	 */
	@SuppressLint("HandlerLeak")
	private void initData() {
		if (userId == null) {
			userId = (String) SPUtil.get(MyQuestionActivity.this, "userId", "");
		}
		String api = MyQuestion_API + "/"
				+ userId + "/problems";
		Log.v(TAG, "url:"+api);
		problems = new ArrayList<Problem>();
		adapter = new QuestionAdapter(MyQuestionActivity.this, problems);
		mListView.setAdapter(adapter);
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
							List<Problem> tempList = success(jObject);
							if (tempList != null) {
								problems.clear();
								problems.addAll(tempList);
								adapter.notifyDataSetChanged();
							}
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
			String api = MyQuestion_API + "/" + userId + "/problems";
			if (!userId.isEmpty()) {
				api += "?userId=" + userId;
			}
			String result = "";
			Log.v(TAG, api);
			result = NetUtil.connect(NetUtil.GET, api, null);
			// Log.v(TAG, "result:" + result);
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
			String api = MyQuestion_API + "/" + userId + "/problems";
			String prm = "?timestamp="
					+ problems.get(problems.size() - 1).getTimestamp();
			if (!userId.isEmpty()) {
				api += "&userId=" + userId;
			}
			result = NetUtil.connect(NetUtil.GET, api + prm, null);
			// Log.v(TAG, "result:" + result);
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
			Log.v(TAG, "problemNum:" + problems.size());
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
//		String avatarUrl = (String) SPUtil.get(MyQuestionActivity.this,
//				"avatarUrl", "");
//		for (int i = 0; i < tempList.size(); i++) {
//			tempList.get(i).setAvatarUrl(avatarUrl);
//		}
		// Log.v(TAG, "success:" + tempList);
		return tempList;

	}

	private void initTitleBar() {
		titleBar = (TitleBar) findViewById(R.id.tb_myquestion);
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
		if(type==1){
			titleBar.setTitle("提问");
		}
	}

}
