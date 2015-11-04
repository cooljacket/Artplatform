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
import com.gexin.artplatform.bean.Comment;
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

public class MyCommentActivity extends Activity {
	private static final String TAG = "MyCommentActivity";
	private String MyComment_API = Constant.SERVER_URL + Constant.USER_API;
	private static final int POST_REQUEST_CODE = 1;
	private List<Problem> problems = new ArrayList<Problem>();
	private List<Comment> commentList = new ArrayList<Comment>();
	private String userId = null;
	private int type = 0;

	private PullToRefreshListView mListView;
	private Gson gson = new Gson();
	private QuestionAdapter adapter;
	private LinearLayout llBack;
	private TitleBar titleBar;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_comment);

		userId = getIntent().getStringExtra("id");
		type = getIntent().getIntExtra("type", 0);
		if (userId == null) {
			userId = (String) SPUtil.get(MyCommentActivity.this, "userId", "");
		}
		initView();
		initTitleBar();
		adapter = new QuestionAdapter(MyCommentActivity.this, problems);
		mListView.setAdapter(adapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.v(TAG, "positon:" + arg2);
				// Problem problem = (Problem) adapter.getItem(arg2 - 1);
				Intent intent = new Intent(MyCommentActivity.this,
						QuestionInfoActivity.class);
				intent.putExtra("problemId", commentList.get(arg2 - 1)
						.getProblemId());
				startActivity(intent);
			}
		});

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
							commentList.addAll(success(jObject));
							for (int i = 0; i < commentList.size(); i++) {
								problems.add(commentList.get(i).get_problem());

							}
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

		new HttpConnectionUtils(handler).get(MyComment_API + "/" + userId
				+ "/comments");

	}

	private void initTitleBar() {
		titleBar = (TitleBar) findViewById(R.id.tb_mycomment);
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
			titleBar.setTitle("评论");
		}
	}

	private List<Comment> success(JSONObject jObject) {
		int state = -1;
		List<Comment> commentTemp = null;
		try {
			state = jObject.getInt("stat");
			if (state == 1) {
				commentTemp = gson.fromJson(jObject.getJSONArray("comments")
						.toString(), new TypeToken<List<Comment>>() {
				}.getType());

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return commentTemp;
	}

	// @SuppressLint("InflateParams")
	// private void setComment() {
	// DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
	// .showImageOnLoading(R.drawable.ic_contact_picture)
	// .showImageForEmptyUri(R.drawable.ic_contact_picture)
	// .showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
	// .cacheOnDisk(true).considerExifParams(true)
	// .bitmapConfig(Bitmap.Config.RGB_565).build();
	//
	// for (int i = comment.size() - 1; i >= 0 ; i--) {
	// View view = LayoutInflater.from(this).inflate(
	// R.layout.comment_item, null);
	// view.setClickable(true);
	// ImageView tmpIvHeader = (ImageView) view
	// .findViewById(R.id.iv_header_comment_item);
	// TextView tmpTvName = (TextView) view
	// .findViewById(R.id.tv_name_comment_item);
	// TextView tmpTvTime = (TextView) view
	// .findViewById(R.id.tv_time_comment_item);
	// TextView tmpTvContent = (TextView) view
	// .findViewById(R.id.tv_content_comment_item);
	// tmpTvContent.setText(comment.get(i).getContent());
	// tmpTvName.setText(problem.get(i).getName());
	// tmpTvTime.setText(TimeUtil.getStandardDate(comment.get(i).getTimestamp()));
	// ImageLoader.getInstance().displayImage(
	// problem.get(i).getImage(), tmpIvHeader, imageOptions);
	// llComment.addView(view);
	// final String _id = problem.get(i).get_id();
	// view.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// Intent intent = new Intent(MyCommentActivity.this,
	// QuestionInfoActivity.class);
	// intent.putExtra("problemId", _id);
	// startActivity(intent);
	// }
	// });
	// }
	// }

	private void initView() {
		mListView = (PullToRefreshListView) findViewById(R.id.lv_my_comment);
		mListView.setMode(Mode.BOTH);
		mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(MyCommentActivity.this,
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
		LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 0, 10, 0);
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
			String userId = (String) SPUtil.get(MyCommentActivity.this,
					"userId", "");
			String api = MyComment_API + "/" + userId + "/comments";
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
				List<Comment> tempList = success(jObject);
				List<Problem> probTemp = new ArrayList<Problem>();
				if (tempList != null) {
					for (int i = 0; i < tempList.size(); i++)
						probTemp.add(tempList.get(i).get_problem());
					problems.clear();
					problems.addAll(probTemp);
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
			String userId = (String) SPUtil.get(MyCommentActivity.this,
					"userId", "");
			String api = MyComment_API + "/" + userId + "/comments";
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
				List<Comment> tempList = success(jObject);
				List<Problem> probTemp = new ArrayList<Problem>();
				if (tempList != null) {
					for (int i = 0; i < tempList.size(); i++)
						probTemp.add(tempList.get(i).get_problem());
					problems.addAll(probTemp);
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

}
