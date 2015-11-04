package com.gexin.artplatform.studio;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Problem;
import com.gexin.artplatform.bean.User;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.question.PostProblemActivity;
import com.gexin.artplatform.question.QuestionAdapter;
import com.gexin.artplatform.question.QuestionInfoActivity;
import com.gexin.artplatform.utils.BroadcastUtil;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.TitleBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TeacherDetailActivity extends Activity {

	private static final String TAG = "TeacherDetailActiviity";
	private String teacherId;
	private User mTeacher;
	private List<Problem> problems = new ArrayList<Problem>();
	private QuestionAdapter adapter;
	private Gson gson = new Gson();
	private boolean isFocus;

	private TitleBar titleBar;
	private LinearLayout llBack, llAsk;
	private ImageView ivHeader;
	private ImageView ivFocus;
	private TextView tvName;
	private TextView tvProvince;
	private TextView tvArticleNum;
	private TextView tvCommentNum;
	private TextView tvAlbumNum;
	private TextView tvFanNum;
	private TextView tvFollowNum;
	private TextView tvStudioName;
	private TextView tvComNum;
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher_detail);

		teacherId = getIntent().getStringExtra("teacherId");

		initView();
		initData();
	}

	private void initData() {
		getTeacherInfo();
		getProblemList();
	}

	@SuppressLint("HandlerLeak")
	private void getProblemList() {
		String url = Constant.SERVER_URL + "/api/user/" + teacherId + "/answer";
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Log.v(TAG, "problems" + msg.obj);
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					try {
						JSONObject jsonObject = new JSONObject((String) msg.obj);
						int state = jsonObject.getInt("stat");
						if (state == 1) {
							List<Problem> tmpList = gson.fromJson(jsonObject
									.getJSONArray("problems").toString(),
									new TypeToken<List<Problem>>() {
									}.getType());
							problems.clear();
							problems.addAll(tmpList);
							Log.v(TAG, "problems:" + problems.toString());
							adapter.notifyDataSetChanged();
							tvComNum.setText("回答（" + problems.size() + "）");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		new HttpConnectionUtils(handler).get(url);
	}

	private void getTeacherInfo() {
		String url = Constant.SERVER_URL + "/api/user/" + teacherId;
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
				mTeacher = gson.fromJson(jObject.getJSONObject("user")
						.toString(), User.class);
			}
			setDataToView();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void setDataToView() {
		tvName.setText(mTeacher.getName());
		tvAlbumNum.setText(mTeacher.getWorkNum() + "");
		tvArticleNum.setText(mTeacher.getAnswerNum() + "");
		tvCommentNum.setText(mTeacher.getCommentNum() + "");
		tvFanNum.setText(mTeacher.getFanNum() + "");
		tvFollowNum.setText(mTeacher.getFollowNum() + "");
		tvProvince.setText(mTeacher.getPlace().getProvince());
		tvStudioName.setText(mTeacher.getStudioName());
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_contact_picture)
				.showImageForEmptyUri(R.drawable.ic_contact_picture)
				.showImageOnFail(R.drawable.ic_contact_picture)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		ImageLoader.getInstance().displayImage(mTeacher.getAvatarUrl(),
				ivHeader, options);
		setFocusStatus();
	}

	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_activity_teacher_detail);
		ivHeader = (ImageView) findViewById(R.id.iv_header_teacher_detail);
		ivFocus = (ImageView) findViewById(R.id.iv_focus_teacher_detail);
		tvName = (TextView) findViewById(R.id.tv_name_teacher_detail);
		tvProvince = (TextView) findViewById(R.id.tv_addr_teacher_detail);
		tvArticleNum = (TextView) findViewById(R.id.tv_tiezinum_teacher_detial);
		tvAlbumNum = (TextView) findViewById(R.id.tv_albumnum_teacher_detial);
		tvCommentNum = (TextView) findViewById(R.id.tv_commentnum_teacher_detial);
		tvFanNum = (TextView) findViewById(R.id.tv_fannum_teacher_detial);
		tvFollowNum = (TextView) findViewById(R.id.tv_focusnum_teacher_detial);
		tvStudioName = (TextView) findViewById(R.id.tv_artroom_teacher_detail);
		tvComNum = (TextView) findViewById(R.id.tv_comnum_teacher_detail);
		mListView = (ListView) findViewById(R.id.lv_activity_teacher_detail);

		initTitleBar();
		ivFocus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				postFocus();
			}
		});
		adapter = new QuestionAdapter(this, problems);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(TeacherDetailActivity.this,
						QuestionInfoActivity.class);
				intent.putExtra("problemId", problems.get(arg2).get_id());
				startActivity(intent);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private void postFocus() {
		String userId = (String) SPUtil.get(this, "userId", "");
		if (userId.isEmpty()) {
			Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
		}
		String followApi = Constant.SERVER_URL + "/api/user/" + userId
				+ "/follow";
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					try {
						JSONObject jsonObject = new JSONObject((String) msg.obj);
						if (jsonObject.getInt("stat") == 1) {
							if (isFocus) {
								Toast.makeText(TeacherDetailActivity.this,
										"取消关注成功", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(TeacherDetailActivity.this,
										"关注成功", Toast.LENGTH_SHORT).show();
							}
						} else {
							if (isFocus) {
								Toast.makeText(TeacherDetailActivity.this,
										"取消关注失败", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(TeacherDetailActivity.this,
										"关注失败", Toast.LENGTH_SHORT).show();
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
						if (isFocus) {
							Toast.makeText(TeacherDetailActivity.this,
									"取消关注失败", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(TeacherDetailActivity.this, "关注失败",
									Toast.LENGTH_SHORT).show();
						}
					}
					setFocusStatus();
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("userId", teacherId));
		if (isFocus) {
			list.add(new BasicNameValuePair("follow", "-1"));
		} else {

			list.add(new BasicNameValuePair("follow", "1"));
		}
		new HttpConnectionUtils(handler).post(followApi, list);
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

		llAsk = new LinearLayout(this);
		TextView tvAsk = new TextView(this);
		tvAsk.setText("向他提问");
		tvAsk.setTextSize(20);
		tvAsk.setTextColor(Color.WHITE);
		params = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 0, 10, 0);
		llAsk.setGravity(Gravity.CENTER_VERTICAL);
		llAsk.addView(tvAsk, params);
		llAsk.setBackgroundResource(R.drawable.selector_titlebar_btn);
		titleBar.setRightView(llAsk);

		llBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		llAsk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String userId = (String) SPUtil.get(TeacherDetailActivity.this,
						"userId", "");
				if (userId.isEmpty()) {
					Toast.makeText(TeacherDetailActivity.this, "请先登录再提问",
							Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = new Intent(TeacherDetailActivity.this,
						PostProblemActivity.class);
				intent.putExtra("teacherId", mTeacher.getUserId());
				startActivity(intent);
			}
		});
	}

	private void setFocusStatus() {
		String userId = (String) SPUtil.get(this, "userId", "");
		if (userId.isEmpty()) {
			return;
		}
		String url = Constant.SERVER_URL + "/api/user/" + userId + "/relation/"
				+ mTeacher.getUserId();
		Handler handler = new Handler() {
			@SuppressLint("HandlerLeak")
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					try {
						JSONObject jsonObject = new JSONObject((String) msg.obj);
						int state = jsonObject.getInt("stat");
						if (state == 1) {
							int relation = jsonObject.getInt("relation");
							if (relation == 1 || relation == 3) {
								isFocus = true;
								ivFocus.setImageResource(R.drawable.focus_cancle_icon);
							} else {
								isFocus = false;
								ivFocus.setImageResource(R.drawable.interest_icon_2);
							}
							BroadcastUtil.updateUserInfo(TeacherDetailActivity.this);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		new HttpConnectionUtils(handler).get(url);
	}
}
