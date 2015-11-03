package com.gexin.artplatform.friends;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.User;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.mine.FocusOrFansActivity;
import com.gexin.artplatform.mine.MyCollectActivity;
import com.gexin.artplatform.mine.MyCommentActivity;
import com.gexin.artplatform.mine.MyQuestionActivity;
import com.gexin.artplatform.mine.MySubscribeActivity;
import com.gexin.artplatform.mine.MyWorkActivity;
import com.gexin.artplatform.utils.BroadcastUtil;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.TitleBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ViewOtherUserActivity extends Activity {

	private String TAG = "ViewOtherUserActivity";
	private String userId;
	private User user;
	private Gson gson = new Gson();
	private boolean isFocus = false;

	private LinearLayout llFocus, llFans, llCollect;
	private TextView tvFocus, tvFans, tvCollect, tvWork, tvPump, tvComment,
			tvSubscribe, tvName;
	private TextView tvRightFocus;
	private ImageView ivHeader;
	private Button btnQuery;
	private TitleBar titleBar;
	private LinearLayout llBack, llRightFocus;
	private RelativeLayout rlWork, rlPump, rlComment, rlSubscribe, rlHeader;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_mine);

		Bundle bundle = this.getIntent().getExtras();
		userId = bundle.getString("userId");
		String api = Constant.SERVER_URL + Constant.USER_API + "/" + userId;
		initView();

		Handler handler = new HttpHandler(this) {
			@Override
			protected void succeed(JSONObject jObject) {
				if (jObject != null) {
					Log.v(TAG, jObject.toString());
					user = success(jObject);
					setDataToView();
				}
			}

		};

		new HttpConnectionUtils(handler).get(api);

	}

	private User success(JSONObject jObject) {
		int state = -1;
		User temp = null;
		try {
			state = jObject.getInt("stat");
			if (state == 1) {
				temp = gson.fromJson(jObject.getJSONObject("user").toString(),
						new TypeToken<User>() {
						}.getType());
				if (temp != null) {

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return temp;
	}

	private void initView() {
		llFocus = (LinearLayout) findViewById(R.id.ll_focus_fragment_mine);
		llFans = (LinearLayout) findViewById(R.id.ll_fans_fragment_mine);
		llCollect = (LinearLayout) findViewById(R.id.ll_collect_fragment_mine);
		rlWork = (RelativeLayout) findViewById(R.id.rl_work_fragment_mine);
		rlPump = (RelativeLayout) findViewById(R.id.rl_pump_fragment_mine);
		rlComment = (RelativeLayout) findViewById(R.id.rl_comment_fragment_mine);
		rlSubscribe = (RelativeLayout) findViewById(R.id.rl_subscribe_fragment_mine);
		rlHeader = (RelativeLayout) findViewById(R.id.rl_userinfo_fragment_mine);
		tvFocus = (TextView) findViewById(R.id.tv_focusnum_fragment_mine);
		tvFans = (TextView) findViewById(R.id.tv_fansnum_fragment_mine);
		tvCollect = (TextView) findViewById(R.id.tv_collectnum_fragment_mine);
		tvWork = (TextView) findViewById(R.id.tv_work_fragment_mine);
		tvPump = (TextView) findViewById(R.id.tv_pump_fragment_mine);
		tvComment = (TextView) findViewById(R.id.tv_comment_fragment_mine);
		tvSubscribe = (TextView) findViewById(R.id.tv_subscribe_fragment_mine);
		tvName = (TextView) findViewById(R.id.tv_name_fragment_mine);
		ivHeader = (ImageView) findViewById(R.id.iv_header_fragment_mine);
		btnQuery = (Button) findViewById(R.id.btn_inquire_fragment_mine);
		btnQuery.setVisibility(View.GONE);
		titleBar = (TitleBar) findViewById(R.id.tb_fragment_mine);
		initTitleBar();

		llFocus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ViewOtherUserActivity.this,
						FocusOrFansActivity.class);
				intent.putExtra("type", 1);
				intent.putExtra("id", user.getUserId());
				startActivity(intent);
			}
		});

		llFans.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ViewOtherUserActivity.this,
						FocusOrFansActivity.class);
				intent.putExtra("type", 2);
				intent.putExtra("id", user.getUserId());
				startActivity(intent);
			}
		});

		llCollect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ViewOtherUserActivity.this,
						MyCollectActivity.class);
				intent.putExtra("type", 2);
				intent.putExtra("id", user.getUserId());
				startActivity(intent);
			}
		});
		rlWork.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ViewOtherUserActivity.this,
						MyWorkActivity.class);
				intent.putExtra("type", 1);
				intent.putExtra("id", user.getUserId());
				startActivity(intent);
			}
		});
		rlComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ViewOtherUserActivity.this,
						MyCommentActivity.class);
				intent.putExtra("type", 1);
				intent.putExtra("id", user.getUserId());
				startActivity(intent);
			}
		});
		rlPump.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ViewOtherUserActivity.this,
						MyQuestionActivity.class);
				intent.putExtra("type", 1);
				intent.putExtra("id", user.getUserId());
				Log.v(TAG, "userId:" + user.getUserId());
				startActivity(intent);
			}
		});
		rlSubscribe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ViewOtherUserActivity.this,
						MySubscribeActivity.class);
				intent.putExtra("type", 1);
				intent.putExtra("id", user.getUserId());
				startActivity(intent);
			}
		});
	}

	private void initTitleBar() {
		titleBar.setTitle("他人详情");
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
		llRightFocus = new LinearLayout(this);
		tvRightFocus = new TextView(this);
		tvRightFocus.setText("关注");
		tvRightFocus.setTextSize(20);
		tvRightFocus.setTextColor(Color.WHITE);
		params = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 0, 10, 0);
		llRightFocus.setGravity(Gravity.CENTER_VERTICAL);
		llRightFocus.addView(tvRightFocus, params);
		llRightFocus.setBackgroundResource(R.drawable.selector_titlebar_btn);
		titleBar.setRightView(llRightFocus);

		llRightFocus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				postFocus();
			}
		});
	}

	private void setDataToView() {
		String name = user.getName();
		String avatarUrl = user.getAvatarUrl();
		int followNum = user.getFollowNum();
		int fanNum = user.getFanNum();
		int collectionNum = user.getCollectionNum();
		int workNum = user.getWorkNum();
		int askNum = user.getAskNum();
		int commentNum = user.getCommentNum();
		int subscriptionNum = user.getSubscriptionNum();
		if (name.isEmpty()) {
			name = "未设置";
		}
		tvName.setText(name);
		tvFocus.setText("" + followNum);
		tvFans.setText("" + fanNum);
		tvCollect.setText("" + collectionNum);
		if (workNum != 0) {
			tvWork.setText("作品(" + workNum + ")");
		} else {
			tvWork.setText("作品");
		}
		if (askNum != 0) {
			tvPump.setText("提问(" + askNum + ")");
		} else {
			tvPump.setText("提问");
		}
		if (commentNum != 0) {
			tvComment.setText("评论(" + commentNum + ")");
		} else {
			tvComment.setText("评论");
		}
		if (subscriptionNum != 0) {
			tvSubscribe.setText("订阅(" + subscriptionNum + ")");
		} else {
			tvSubscribe.setText("订阅");
		}
		setFocusStatus();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_contact_picture)
				.showImageForEmptyUri(R.drawable.ic_contact_picture)
				.showImageOnFail(R.drawable.ic_contact_picture)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		ImageLoader.getInstance().displayImage(avatarUrl, ivHeader, options);

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
								Toast.makeText(ViewOtherUserActivity.this,
										"取消关注成功", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(ViewOtherUserActivity.this,
										"关注成功", Toast.LENGTH_SHORT).show();
							}
							setFocusStatus();
							BroadcastUtil
									.updateUserInfo(ViewOtherUserActivity.this);
						} else {
							if (isFocus) {
								Toast.makeText(ViewOtherUserActivity.this,
										"取消关注失败", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(ViewOtherUserActivity.this,
										"关注失败", Toast.LENGTH_SHORT).show();
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
						if (isFocus) {
							Toast.makeText(ViewOtherUserActivity.this,
									"取消关注失败", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(ViewOtherUserActivity.this, "关注失败",
									Toast.LENGTH_SHORT).show();
						}
					}
					// setFocusStatus();
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("userId", this.userId));
		if (isFocus) {
			list.add(new BasicNameValuePair("follow", "-1"));
		} else {
			list.add(new BasicNameValuePair("follow", "1"));
		}
		new HttpConnectionUtils(handler).post(followApi, list);
	}

	@SuppressLint("HandlerLeak")
	private void setFocusStatus() {
		String userId = (String) SPUtil.get(this, "userId", "");
		if (userId.isEmpty()) {
			return;
		}
		String url = Constant.SERVER_URL + "/api/user/" + userId + "/relation/"
				+ user.getUserId();
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
								tvRightFocus.setText("取消关注");
								// Toast.makeText(ViewOtherUserActivity.this,
								// "关注成功", Toast.LENGTH_SHORT).show();
							} else {
								isFocus = false;
								tvRightFocus.setText("关注");
								// Toast.makeText(ViewOtherUserActivity.this,
								// "取消关注成功", Toast.LENGTH_SHORT).show();
							}
							// BroadcastUtil
							// .updateUserInfo(ViewOtherUserActivity.this);
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
