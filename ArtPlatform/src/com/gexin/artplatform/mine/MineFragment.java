package com.gexin.artplatform.mine;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.User;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.dlog.DLog;
import com.gexin.artplatform.mine.login.LoginActivity;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.SPUtil;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MineFragment extends Fragment {

	public static final String ACTION_CHANGE_USERDATA = "ACTION_CHANGE_USERDATA";
	private static final int LOGIN_REQUEST = 0;
	private static final int MODIFY_REQUEST = 1;
	private LinearLayout llFocus, llFans, llCollect;
	private RelativeLayout rlWork, rlPump, rlComment, rlSubscribe, rlHeader;
	private TextView tvFocus, tvFans, tvCollect, tvWork, tvPump, tvComment,
			tvSubscribe, tvName;
	private TextView tvStatus;
	private ImageView ivHeader;

	private int job = -1;// -1为未登录，0为学生，1为教师
	private Gson gson = new Gson();

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mine, container, false);
		initView(view);

		return view;
	}

	private void initView(View view) {
		llFocus = (LinearLayout) view.findViewById(R.id.ll_focus_fragment_mine);
		llFans = (LinearLayout) view.findViewById(R.id.ll_fans_fragment_mine);
		llCollect = (LinearLayout) view
				.findViewById(R.id.ll_collect_fragment_mine);
		rlWork = (RelativeLayout) view.findViewById(R.id.rl_work_fragment_mine);
		rlPump = (RelativeLayout) view.findViewById(R.id.rl_pump_fragment_mine);
		rlComment = (RelativeLayout) view
				.findViewById(R.id.rl_comment_fragment_mine);
		rlSubscribe = (RelativeLayout) view
				.findViewById(R.id.rl_subscribe_fragment_mine);
		rlHeader = (RelativeLayout) view
				.findViewById(R.id.rl_userinfo_fragment_mine);
		tvFocus = (TextView) view.findViewById(R.id.tv_focusnum_fragment_mine);
		tvFans = (TextView) view.findViewById(R.id.tv_fansnum_fragment_mine);
		tvCollect = (TextView) view
				.findViewById(R.id.tv_collectnum_fragment_mine);
		tvWork = (TextView) view.findViewById(R.id.tv_work_fragment_mine);
		tvPump = (TextView) view.findViewById(R.id.tv_pump_fragment_mine);
		tvComment = (TextView) view.findViewById(R.id.tv_comment_fragment_mine);
		tvSubscribe = (TextView) view
				.findViewById(R.id.tv_subscribe_fragment_mine);
		tvName = (TextView) view.findViewById(R.id.tv_name_fragment_mine);
		ivHeader = (ImageView) view.findViewById(R.id.iv_header_fragment_mine);
		tvStatus = (TextView) view.findViewById(R.id.tv_status_fragment_mine);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		IntentFilter filter = new IntentFilter();
		filter.addAction(UserInfoActivity.ACTION_HEADER_MODIFY);
		filter.addAction(MineFragment.ACTION_CHANGE_USERDATA);
		getActivity().registerReceiver(mReceiver, filter);
		initData();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case LOGIN_REQUEST:
			if (resultCode == Activity.RESULT_CANCELED) {
				job = -1;
			} else if (resultCode == Activity.RESULT_OK) {
				String state = (String) SPUtil.get(getActivity(), "LOGIN",
						"NONE");
				if (state.equals("STUDENT")) {
					job = 0;
				} else if (state.equals("TEACHER")) {
					job = 1;
				} else {
					job = -1;
				}
			}
			setDataToView();
			break;
		case MODIFY_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				job = -1;
			}
			setDataToView();
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void setDataToView() {
		String name = (String) SPUtil.get(getActivity(), "name", "");
		String avatarUrl = (String) SPUtil.get(getActivity(), "avatarUrl", "");
		int followNum = (Integer) SPUtil.get(getActivity(), "followNum", 0);
		int fanNum = (Integer) SPUtil.get(getActivity(), "fanNum", 0);
		int collectionNum = (Integer) SPUtil.get(getActivity(),
				"collectionNum", 0);
		int workNum = (Integer) SPUtil.get(getActivity(), "workNum", 0);
		int askNum = (Integer) SPUtil.get(getActivity(), "askNum", 0);
		int commentNum = (Integer) SPUtil.get(getActivity(), "commentNum", 0);
		int subscriptionNum = (Integer) SPUtil.get(getActivity(),
				"subscriptionNum", 0);
		if (name.isEmpty()) {
			name = "未设置";
		}
		if (job == -1) {
			name = "未登录";
		}
		tvName.setText(name);
		tvFocus.setText("" + followNum);
		tvFans.setText("" + fanNum);
		tvCollect.setText("" + collectionNum);
		if (workNum != 0 && job != -1) {
			tvWork.setText("我的作品(" + workNum + ")");
		} else {
			tvWork.setText("我的作品");
		}
		if (askNum != 0 && job != -1) {
			tvPump.setText("我的提问(" + askNum + ")");
		} else {
			tvPump.setText("我的提问");
		}
		if (commentNum != 0 && job != -1) {
			tvComment.setText("我的评论(" + commentNum + ")");
		} else {
			tvComment.setText("我的评论");
		}
		if (subscriptionNum != 0 && job != -1) {
			tvSubscribe.setText("我的订阅(" + subscriptionNum + ")");
		} else {
			tvSubscribe.setText("我的订阅");
		}
		if (job != -1) {
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.ic_contact_picture)
					.showImageForEmptyUri(R.drawable.ic_contact_picture)
					.showImageOnFail(R.drawable.ic_contact_picture)
					.cacheInMemory(true).cacheOnDisk(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
			ImageLoader.getInstance()
					.displayImage(avatarUrl, ivHeader, options);
		} else {
			ivHeader.setImageResource(R.drawable.ic_contact_picture);
		}
		if (job == 1) {
			tvStatus.setVisibility(View.VISIBLE);
		} else {
			tvStatus.setVisibility(View.GONE);
		}
	}

	private void initData() {
		String state = (String) SPUtil.get(getActivity(), "LOGIN", "NONE");
		if (state.equals("STUDENT")) {
			job = 0;
		} else if (state.equals("TEACHER")) {
			job = 1;
		}
		if (job == -1) {
			setDataToView();
		} else {
			getUserInfo();
		}

		rlPump.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (job != -1) {
					Intent intent = new Intent(getActivity(),
							MyQuestionActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		rlComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (job != -1) {
					Intent intent = new Intent(getActivity(),
							MyCommentActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		llFans.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (job != -1) {
					Intent intent = new Intent(getActivity(),
							MyFansActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		llCollect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (job != -1) {
					Intent intent = new Intent(getActivity(),
							MyCollectActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		llFocus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (job != -1) {
					Intent intent = new Intent(getActivity(),
							MyFocusActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		rlSubscribe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (job != -1) {
					Intent intent = new Intent(getActivity(),
							MySubscribeActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		rlWork.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (job != -1) {
					Intent intent = new Intent(getActivity(),
							MyWorkActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		rlHeader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (job == -1) {
					Intent intent = new Intent(getActivity(),
							LoginActivity.class);
					startActivityForResult(intent, LOGIN_REQUEST);
				} else {
					Intent intent = new Intent(getActivity(),
							UserInfoActivity.class);
					startActivityForResult(intent, MODIFY_REQUEST);
				}
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private void getUserInfo() {
		String userId = (String) SPUtil.get(getActivity(), "userId", "");
		String url = Constant.SERVER_URL + "/api/user/" + userId;
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					dealResponse((String) msg.obj);
					break;

				case HttpConnectionUtils.DID_ERROR:
					setDataToView();
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		new HttpConnectionUtils(handler).get(url);
	}

	protected void dealResponse(String obj) {
		DLog.v("MineFragment", "Response:" + obj);
		try {
			JSONObject jsonObject = new JSONObject(obj);
			int state = jsonObject.getInt("stat");
			if (state == 1) {
				User user = gson.fromJson(jsonObject.getJSONObject("user")
						.toString(), User.class);
				user.putToSP(getActivity());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setDataToView();
	}

	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			DLog.v("MineFragment", arg1.getAction());
			if (UserInfoActivity.ACTION_HEADER_MODIFY.equals(arg1.getAction())) {
				String avatarUrl = arg1.getStringExtra("avatarUrl");
				DisplayImageOptions options = new DisplayImageOptions.Builder()
						.showImageOnLoading(R.drawable.ic_contact_picture)
						.showImageForEmptyUri(R.drawable.ic_contact_picture)
						.showImageOnFail(R.drawable.ic_contact_picture)
						.cacheInMemory(true).cacheOnDisk(true)
						.considerExifParams(true)
						.bitmapConfig(Bitmap.Config.RGB_565).build();
				ImageLoader.getInstance().displayImage(avatarUrl, ivHeader,
						options);
			} else if (ACTION_CHANGE_USERDATA.equals(arg1.getAction())) {
				getUserInfo();
			}
		}
	};
}
