package com.gexin.artplatform.question;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Answer;
import com.gexin.artplatform.bean.AnswerContent;
import com.gexin.artplatform.bean.Comment;
import com.gexin.artplatform.bean.Problem;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.friends.ViewOtherUserActivity;
import com.gexin.artplatform.largeImg.LargeImageActivity;
import com.gexin.artplatform.studio.RoomDetailActivity;
import com.gexin.artplatform.utils.BroadcastUtil;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.utils.TimeUtil;
import com.gexin.artplatform.view.TitleBar;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class QuestionInfoActivity extends Activity {

	public static final String ACTION_PROBLEM_CHANGE = "ACTION_PROBLEM_CHANGE";
	private static final String TAG = "QuestionInfoActivity";
	private String userId = "";
	private String replyTo = "";
	private String askAfterTo = "";
	private String answerId = "";
	private boolean isFocus = false;

	private ImageView ivHeader;
	private ImageView ivPic;
	private ImageView ivZan;
	private TextView tvName;
	private TextView tvTime;
	// private TextView tvCommentor;
	private TextView tvContent;
	private TextView tvType;
	private TextView tvAnsNum;
	private TextView tvZan;
	private TitleBar titleBar;
	private LinearLayout llBack;
	private EditText etComment;
	private LinearLayout llComment;
	private LinearLayout llAnswer;
	private LinearLayout llZan;
	private Button btnSubmit;
	private ImageButton ibtnFocus;
	private TextView tvAnswerNum;
	private TextView tvCommentNum;
	private RelativeLayout rlMain;
	private View answerLine, commentLine;

	private Problem problem;
	private Gson gson = new Gson();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_info);

		initView();

		userId = (String) SPUtil.get(this, "userId", "");
		getProblemInfo();
	}

	private void getProblemInfo() {
		Handler handler = new HttpHandler(this) {
			@Override
			protected void succeed(JSONObject jObject) {
				Log.v(TAG, "jObject:" + jObject.toString());
				setDataToView(jObject);
			}
		};
		String problemId = getIntent().getStringExtra("problemId");
		String api = Constant.SERVER_URL + Constant.PROBLEM_API + "/"
				+ problemId;
		if (!userId.isEmpty()) {
			api += "?userId=" + userId;
		}
		new HttpConnectionUtils(handler).get(api);
	}

	private void initView() {
		ivHeader = (ImageView) findViewById(R.id.iv_header_question_info);
		ivPic = (ImageView) findViewById(R.id.iv_pic_question_info);
		ivZan = (ImageView) findViewById(R.id.iv_zan_question_info);
		tvContent = (TextView) findViewById(R.id.tv_content_question_info);
		tvAnsNum = (TextView) findViewById(R.id.tv_ans_question_info);
		tvName = (TextView) findViewById(R.id.tv_name_question_info);
		tvTime = (TextView) findViewById(R.id.tv_time_question_info);
		tvType = (TextView) findViewById(R.id.tv_type_question_info);
		tvZan = (TextView) findViewById(R.id.tv_zan_question_info);
		titleBar = (TitleBar) findViewById(R.id.tb_question_info);
		etComment = (EditText) findViewById(R.id.et_comment_question_info);
		btnSubmit = (Button) findViewById(R.id.btn_comment_question_info);
		ibtnFocus = (ImageButton) findViewById(R.id.ibtn_interest_question_info);
		llComment = (LinearLayout) findViewById(R.id.ll_area_comment_question_info);
		llAnswer = (LinearLayout) findViewById(R.id.ll_area_answer_question_info);
		llZan = (LinearLayout) findViewById(R.id.ll_zan_question_info);
		tvAnswerNum = (TextView) findViewById(R.id.tv_answernum_question_info);
		tvCommentNum = (TextView) findViewById(R.id.tv_commentnum_question_info);
		answerLine = findViewById(R.id.v_answer_line_activity_question_info);
		commentLine = findViewById(R.id.v_comment_line_activity_question_info);
		rlMain = (RelativeLayout) findViewById(R.id.rl_main_activity_question_info);
		initTitleBar();
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String content = etComment.getText().toString();
				if (!content.isEmpty()) {
					if (askAfterTo.isEmpty()) {
						postComment(content);
					} else {
						postAskAfter(content, 0);
					}
				}
			}
		});

		ibtnFocus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				postFocus(problem.getUserId());
			}
		});
		rlMain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				etComment.setHint("回复楼主");
				replyTo = "";
				askAfterTo = "";
			}
		});
	}

	// 追问
	@SuppressLint("HandlerLeak")
	private void postAskAfter(String content, int aType) {
		String url = Constant.SERVER_URL + "/api/answer/" + answerId;
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					Log.v(TAG, "askAfterRes:" + msg.obj);
					try {
						JSONObject jsonObject = new JSONObject((String) msg.obj);
						int state = jsonObject.getInt("stat");
						if (state == 1) {
							Toast.makeText(QuestionInfoActivity.this, "发送成功",
									Toast.LENGTH_SHORT).show();
							etComment.setText("");
							getProblemInfo();
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
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("content", content));
		data.add(new BasicNameValuePair("aType", "" + aType));
		new HttpConnectionUtils(handler).put(url, data);
	}

	protected void postComment(String content) {
		String status = (String) SPUtil.get(this, "LOGIN", "NONE");
		if (userId.isEmpty()) {
			Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
			return;
		}
		final String commentAPI = Constant.SERVER_URL + "/api/user/" + userId
				+ "/comment";
		final String answerAPI = Constant.SERVER_URL + "/api/user/" + userId
				+ "/answer";
		Handler handler = new HttpHandler(this) {

			@Override
			protected void succeed(JSONObject jObject) {
				Log.v(TAG, "succeed:" + jObject.toString());
				commentSucceed(jObject);
			}

		};
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		if (replyTo != null && !replyTo.isEmpty()) {
			list.add(new BasicNameValuePair("replyTo", replyTo));
		}
		list.add(new BasicNameValuePair("content", content));
		list.add(new BasicNameValuePair("problemId", problem.get_id()));
		if (status.equals("STUDENT")) {
			new HttpConnectionUtils(handler).post(commentAPI, list);
		} else {
			if (answerId.isEmpty()) {
				new HttpConnectionUtils(handler).post(answerAPI, list);
			} else {
				postAskAfter(content, 1);
			}
		}
	}

	private void commentSucceed(JSONObject jObject) {
		int state = -1;
		try {
			state = jObject.getInt("stat");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (state == 1) {
			Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show();
			etComment.setText("");
			getProblemInfo();
		} else {
			Toast.makeText(this, "发布失败", Toast.LENGTH_SHORT).show();
		}
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

	private void setDataToView(JSONObject jObject) {
		int state;
		try {
			state = jObject.getInt("stat");
			if (state == 1) {
				JSONObject jsonObject = jObject.getJSONObject("problem");
				problem = gson.fromJson(jsonObject.toString(), Problem.class);
				Log.v(TAG, "problem:" + problem.toString());
				int ansNum = problem.getAnswerNum();
				int commentNum = problem.getCommentNum();
				int zan = problem.getZan();
				String name = problem.getName();
				String avatarUrl = problem.getAvatarUrl();
				String time = TimeUtil.getTimeDetailString(problem.getTimestamp());
				String askToName = problem.getAskToName();
				String tag = "";
				// userId = problem.getUserId();
				List<Comment> commentList = problem.getCommentList();
				List<Answer> answerList = problem.getAnswerList();
				if (problem.getTag() != null && problem.getTag().size() != 0) {
					String tmpStr = problem.getTag().toString();
					try {
						tag = tmpStr.substring(1, tmpStr.length() - 1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					tag = "未设置";
				}
				String content = problem.getContent();
				final String imageUrl = problem.getImage();
				if (askToName == null || askToName.isEmpty()) {
					tvContent.setText(content);
				} else {
					tvContent.setText("@" + askToName + " " + content);
				}
				tvTime.setText(time);
				tvName.setText(name);
				tvType.setText(tag);
				tvAnsNum.setText(commentNum + "");
				tvZan.setText(zan + "");
				if (problem.getIsZan() == 1) {
					ivZan.setImageResource(R.drawable.zan_icon_2);
				} else {
					ivZan.setImageResource(R.drawable.zan_icon_1);
				}
				
				setFocusStatus();
				broadcastProblemInfo();
				
				DisplayImageOptions headerOptions = new DisplayImageOptions.Builder()
						.showImageOnLoading(R.drawable.ic_contact_picture)
						.showImageForEmptyUri(R.drawable.ic_contact_picture)
						.showImageOnFail(R.drawable.ic_error)
						.cacheInMemory(true).cacheOnDisk(true)
						.considerExifParams(true)
						.bitmapConfig(Bitmap.Config.RGB_565).build();
				DisplayImageOptions picOptions = new DisplayImageOptions.Builder()
						.showImageOnLoading(R.drawable.ic_empty)
						.showImageOnFail(R.drawable.ic_error)
						.cacheInMemory(true).cacheOnDisk(true)
						.considerExifParams(true)
						.bitmapConfig(Bitmap.Config.RGB_565).build();
				ImageLoader.getInstance().displayImage(avatarUrl, ivHeader,
						headerOptions);
				if (imageUrl != null && !imageUrl.isEmpty()) {
					ivPic.setVisibility(View.VISIBLE);
					ImageLoader.getInstance().displayImage(imageUrl, ivPic,
							picOptions);
				} else {
					ivPic.setVisibility(View.GONE);
				}
				llZan.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						postZan();
					}
				});

				ivPic.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (imageUrl != null && !imageUrl.isEmpty()) {
							Intent intent = new Intent(
									QuestionInfoActivity.this,
									LargeImageActivity.class);
							List<String> images = new ArrayList<String>();
							images.add(imageUrl);
							intent.putStringArrayListExtra("images",
									(ArrayList<String>) images);
							startActivity(intent);
						}
					}
				});

				ivHeader.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						showUserInfo(problem.getUserId(), 0);
					}
				});

				tvName.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						showUserInfo(problem.getUserId(), 0);
					}
				});

				if (commentList != null && !commentList.isEmpty()) {
					setComment(commentList);
					tvCommentNum.setVisibility(View.VISIBLE);
					tvCommentNum.setText("评论区(" + commentList.size() + ")");
				} else {
					tvCommentNum.setVisibility(View.GONE);
					commentLine.setVisibility(View.GONE);
				}
				if (answerList != null && !answerList.isEmpty()) {
					setAnswer(answerList);
					tvAnswerNum.setVisibility(View.VISIBLE);
					tvAnswerNum.setText("回答(" + answerList.size() + ")");
				} else {
					tvAnswerNum.setVisibility(View.GONE);
					answerLine.setVisibility(View.GONE);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("HandlerLeak")
	private void postZan() {
		String zanAPI = Constant.SERVER_URL + "/api/problem/"
				+ problem.get_id() + "/zan";
		if (userId.isEmpty()) {
			Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
			return;
		}
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					try {
						JSONObject jsonObject = new JSONObject((String) msg.obj);
						int state = jsonObject.getInt("stat");
						if (state == 1) {
							if (problem.getIsZan() == 1) {
								problem.setIsZan(0);
								ivZan.setImageResource(R.drawable.zan_icon_1);
								problem.setZan(problem.getZan() - 1);
								tvZan.setText(problem.getZan() + "");
								Toast.makeText(QuestionInfoActivity.this,
										"取消赞成功", Toast.LENGTH_SHORT).show();
							} else {
								problem.setIsZan(1);
								ivZan.setImageResource(R.drawable.zan_icon_2);
								problem.setZan(problem.getZan() + 1);
								tvZan.setText(problem.getZan() + "");
								Toast.makeText(QuestionInfoActivity.this,
										"赞成功", Toast.LENGTH_SHORT).show();
							}
							broadcastProblemInfo();
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
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("userId", userId));
		if (problem.getIsZan() == 1) {
			list.add(new BasicNameValuePair("zan", "-1"));
		} else {
			list.add(new BasicNameValuePair("zan", "1"));
		}
		new HttpConnectionUtils(handler).put(zanAPI, list);
	}

	@SuppressLint("InflateParams")
	private void setAnswer(List<Answer> answerList) {
		DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_contact_picture)
				.showImageForEmptyUri(R.drawable.ic_contact_picture)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		llAnswer.removeAllViews();
		TextView tmpTvNum = new TextView(this);
		LayoutParams tmpLp = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		if (!answerList.isEmpty()) {
			tmpTvNum.setText("回答(" + answerList.size() + ")");
		}
		tmpTvNum.setTextColor(Color.RED);
		tmpTvNum.setPadding(10, 5, 0, 5);
		tmpTvNum.setBackgroundColor(Color.WHITE);
		llAnswer.addView(tmpTvNum, tmpLp);
		for (final Answer answer : answerList) {
			if (answer.getUserId().equals(userId)) {
				answerId = answer.get_id();
			}
			View view = LayoutInflater.from(this).inflate(
					R.layout.problem_answer_item, null);
			ImageView tmpIvHeader = (ImageView) view
					.findViewById(R.id.iv_header_answer_item);
			TextView tmpTvName = (TextView) view
					.findViewById(R.id.tv_name_answer_item);
			TextView tmpTvTime = (TextView) view
					.findViewById(R.id.tv_time_answer_item);
			TextView tmpTvStatus = (TextView) view
					.findViewById(R.id.tv_status_answer_item);
			LinearLayout tmpLlAsk = (LinearLayout) view
					.findViewById(R.id.ll_ask_answer_item);
			LinearLayout tmpLlContent = (LinearLayout) view
					.findViewById(R.id.ll_content_answer_item);
			ImageView tmpIvFocus = (ImageView) view
					.findViewById(R.id.iv_interest_answer_item);
			if (answer.getuType() == 1) {
				tmpTvStatus.setText("老师");
			} else if (answer.getuType() == 2) {
				tmpTvStatus.setText("画室");
			}
			tmpTvTime.setText(TimeUtil.getStandardDate(answer.getUpdateTime()));
			tmpTvName.setSingleLine(true);
			tmpTvName.setTextColor(Color.parseColor("#445bc8"));
			for (AnswerContent content : answer.getContent()) {
				LinearLayout linearLayout = new LinearLayout(this);
				linearLayout.setOrientation(LinearLayout.HORIZONTAL);
				TextView textView = new TextView(this);
				textView.setTextColor(Color.parseColor("#2e2e2e"));
				textView.setTextSize(15);
				textView.setGravity(Gravity.CENTER_VERTICAL);
				textView.setBackgroundResource(R.drawable.btn_group_pressed_holo_light);
				LayoutParams lp = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				if (content.getaType() == 1 || content.getaType() == 2) {
					textView.setText(content.getContent());
					lp.setMargins(0, 5, 0, 5);
					linearLayout.setGravity(Gravity.LEFT);
					linearLayout.addView(textView, lp);
					tmpLlContent.addView(linearLayout);
				} else {
					textView.setText(content.getContent());
					lp.setMargins(0, 5, 10, 5);
					linearLayout.setGravity(Gravity.RIGHT);
					TextView tvZhuiwen = new TextView(this);
					tvZhuiwen.setText("追问");
					tvZhuiwen.setTextSize(16);
					tvZhuiwen.setTextColor(Color.parseColor("#2e2e2e"));
					linearLayout.addView(textView, lp);
					linearLayout.addView(tvZhuiwen);
					tmpLlContent.addView(linearLayout);

				}
			}
			tmpTvName.setText(answer.getUserName());
			ImageLoader.getInstance().displayImage(answer.getAvatarUrl(),
					tmpIvHeader, imageOptions);
			llAnswer.addView(view);
			tmpLlAsk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(QuestionInfoActivity.this,
							PostProblemActivity.class);
					intent.putExtra("teacherId", answer.getUserId());
					startActivity(intent);
				}
			});
			tmpIvFocus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					postFocus(answer.getUserId());
				}
			});
			tmpIvHeader.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showUserInfo(answer.getUserId(), answer.getuType());
				}
			});
			tmpTvName.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showUserInfo(answer.getUserId(), answer.getuType());
				}
			});
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (!problem.getUserId().equals(userId)) {
						return;
					}
					String toTeacherName = answer.getUserName();
					if (toTeacherName.length() > 10) {
						toTeacherName = toTeacherName.substring(0, 10) + "...";
					}
					etComment.setHint("追问 " + toTeacherName + ":");
					askAfterTo = answer.getUserId();
					answerId = answer.get_id();
				}
			});
		}
	}

	@SuppressLint("HandlerLeak")
	protected void postFocus(String id) {
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
								Toast.makeText(QuestionInfoActivity.this,
										"取消关注成功", Toast.LENGTH_SHORT).show();
								problem.setIsZan(0);
							} else {
								Toast.makeText(QuestionInfoActivity.this,
										"关注成功", Toast.LENGTH_SHORT).show();
								problem.setIsZan(1);
							}
							setFocusStatus();
						} else {
							if (isFocus) {
								Toast.makeText(QuestionInfoActivity.this,
										"取消关注失败", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(QuestionInfoActivity.this,
										"关注失败", Toast.LENGTH_SHORT).show();
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
						if (isFocus) {
							Toast.makeText(QuestionInfoActivity.this, "取消关注失败",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(QuestionInfoActivity.this, "关注失败",
									Toast.LENGTH_SHORT).show();
						}
					}
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("userId", id));
		if (isFocus) {
			list.add(new BasicNameValuePair("follow", "-1"));
		} else {
			list.add(new BasicNameValuePair("follow", "1"));
		}
		new HttpConnectionUtils(handler).post(followApi, list);
	}

	@SuppressLint("InflateParams")
	private void setComment(List<Comment> list) {
		DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_contact_picture)
				.showImageForEmptyUri(R.drawable.ic_contact_picture)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		llComment.removeAllViews();
		TextView tmpTvNum = new TextView(this);
		LayoutParams tmpLp = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		if (!list.isEmpty()) {
			tmpTvNum.setText("评论(" + list.size() + ")");
		}
		tmpTvNum.setTextColor(Color.RED);
		tmpTvNum.setPadding(10, 5, 0, 5);
		tmpTvNum.setBackgroundColor(Color.WHITE);
		llComment.addView(tmpTvNum, tmpLp);
		for (final Comment comment : list) {
			View view = LayoutInflater.from(this).inflate(
					R.layout.comment_item, null);
			ImageView tmpIvHeader = (ImageView) view
					.findViewById(R.id.iv_header_comment_item);
			TextView tmpTvName = (TextView) view
					.findViewById(R.id.tv_name_comment_item);
			TextView tmpTvTime = (TextView) view
					.findViewById(R.id.tv_time_comment_item);
			TextView tmpTvContent = (TextView) view
					.findViewById(R.id.tv_content_comment_item);
			if (comment.getToUserName() == null
					|| comment.getToUserName().isEmpty()) {
				tmpTvContent.setText(comment.getContent());
			} else {
				String toUserName = comment.getToUserName();
				if (toUserName.length() > 10) {
					toUserName = toUserName.substring(0, 10) + "...";
				}
				tmpTvContent.setText("回复" + comment.getToUserName() + ":"
						+ comment.getContent());
			}
			tmpTvName.setSingleLine(true);
			tmpTvName.setTextColor(Color.parseColor("#445bc8"));
			tmpTvName.setText(comment.getFromUserName());
			tmpTvTime.setText(TimeUtil.getStandardDate(comment.getTimestamp()));
			ImageLoader.getInstance().displayImage(
					comment.getFromUserAvatarUrl(), tmpIvHeader, imageOptions);
			tmpIvHeader.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showUserInfo(comment.getFromUser(), 0);
				}
			});
			tmpTvName.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showUserInfo(comment.getFromUser(), 0);
				}
			});
			llComment.addView(view);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String status = (String) SPUtil.get(
							QuestionInfoActivity.this, "LOGIN", "");
					if (status.equals("TEACHER")) {
						return;
					}
					String hint = comment.getFromUserName();
					if (hint.length() > 10) {
						hint = hint.substring(0, 10) + "...";
					}
					etComment.setHint("回复 " + hint);
					replyTo = comment.getFromUser();
					Log.v(TAG, "replyTo:" + replyTo);
					askAfterTo = "";
				}
			});
		}
	}

	private void showUserInfo(String userId, int type) {
		if (type == 0 || type == 1) {
			Intent intent = new Intent(QuestionInfoActivity.this,
					ViewOtherUserActivity.class);
			intent.putExtra("userId", userId);
			startActivity(intent);
		} else if (type == 2) {
			Intent intent = new Intent(QuestionInfoActivity.this,
					RoomDetailActivity.class);
			intent.putExtra("studioId", userId);
			startActivity(intent);
		}

	}

	@SuppressLint("HandlerLeak")
	private void setFocusStatus() {
		String userId = (String) SPUtil.get(this, "userId", "");
		if (userId.isEmpty()) {
			return;
		}
		String url = Constant.SERVER_URL + "/api/user/" + userId + "/relation/"
				+ problem.getUserId();
		Handler handler = new Handler() {
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
								ibtnFocus
										.setImageResource(R.drawable.focus_cancle_icon);
							} else {
								isFocus = false;
								ibtnFocus
										.setImageResource(R.drawable.interest_icon_2);
							}
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
	
	private void broadcastProblemInfo(){
		Intent intent = new Intent();
		intent.putExtra("zanNum", problem.getZan());
		intent.putExtra("commentNum", problem.getCommentNum());
		intent.putExtra("isZan", problem.getIsZan());
		intent.putExtra("problemId", problem.get_id());
		intent.setAction(ACTION_PROBLEM_CHANGE);
		this.sendBroadcast(intent);
		BroadcastUtil.updateUserInfo(QuestionInfoActivity.this);
	}
}
