package com.gexin.artplatform.studio;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Studio;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.mine.FocusOrFansActivity;
import com.gexin.artplatform.question.PostProblemActivity;
import com.gexin.artplatform.utils.BroadcastUtil;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.ActionSheet;
import com.gexin.artplatform.view.ActionSheet.MenuItemClickListener;
import com.gexin.artplatform.view.TitleBar;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RoomDetailActivity extends FragmentActivity implements
		OnClickListener {

	private static final String TAG = "RoomDetailActivity";
	private int selectPos = 0; // 从做到右的五个选项卡
	private List<Fragment> mTabs = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;
	private RoomAnswerFragment roomAnswerFragment;
	private RoomGalleryFragment roomGalleryFragment;
	private RoomTeacherFragment roomTeacherFragment;
	private RoomHireFragment roomHireFragment;
	private RoomVideoFragment roomVideoFragment;
	private List<TextView> tvList;
	private Gson gson = new Gson();
	private Studio studio;
	private String studioId;
	private DisplayImageOptions logoOptions;

	private TextView tvAnswer;
	private TextView tvGallery;
	private TextView tvTeacher;
	private TextView tvHire;
	private TextView tvVideo;
	private TextView tvPhone;
	private TextView tvName;
	private Button btnDial;
	private TitleBar titleBar;
	private LinearLayout llBack;
	private LinearLayout llAsk;
	private ViewPager mViewPager;
	private TextView tvDescribe;
	private TextView tvAnswerNum;
	private TextView tvFocusNum;
	private TextView tvFanNum;
	private ImageView ivHeader;
	private ImageButton ibtnFocus;
	private Button btnEnvirDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room_detail);
		setTheme(R.style.ActionSheetStyleIOS7);

		studioId = getIntent().getStringExtra("studioId");

		initView();
		initData();
	}

	private void initData() {
		roomAnswerFragment = new RoomAnswerFragment();
		roomGalleryFragment = new RoomGalleryFragment();
		roomTeacherFragment = new RoomTeacherFragment();
		roomHireFragment = new RoomHireFragment();
		roomVideoFragment = new RoomVideoFragment();

		roomAnswerFragment.setStudioId(studioId);
		roomTeacherFragment.setStudioId(studioId);
		roomHireFragment.setStudioId(studioId);
		roomVideoFragment.setStudioId(studioId);
		roomGalleryFragment.setStudioId(studioId);

		mTabs.add(roomAnswerFragment);
		mTabs.add(roomGalleryFragment);
		mTabs.add(roomTeacherFragment);
		mTabs.add(roomHireFragment);
		mTabs.add(roomVideoFragment);
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mTabs.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return mTabs.get(arg0);
			}
		};
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				Log.v(TAG, "onPageSelected:" + arg0);
				for (int i = 0; i < tvList.size(); i++) {
					if (i == arg0) {
						tvList.get(i).setTextColor(
								Color.parseColor("#80fe6060"));
					} else {
						tvList.get(i).setTextColor(Color.parseColor("#959595"));
					}
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		ibtnFocus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				postFocusStudio();
			}
		});

		btnEnvirDetail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(RoomDetailActivity.this,
						StudioEnvironmentActivity.class);
				intent.putExtra("studioId", studioId);
				startActivity(intent);
			}
		});

		// tvAnswerNum.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		tvFocusNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(RoomDetailActivity.this,
						FocusOrFansActivity.class);
				intent.putExtra("type", 4);
				intent.putExtra("id", studio.getStudioId());
				startActivity(intent);
			}
		});
		tvFanNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(RoomDetailActivity.this,
						FocusOrFansActivity.class);
				intent.putExtra("type", 3);
				intent.putExtra("id", studio.getStudioId());
				startActivity(intent);
			}
		});

		getStudioInfo();
	}

	private void postFocusStudio() {
		String userId = (String) SPUtil.get(this, "userId", "");
		if (userId.isEmpty()) {
			Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
			return;
		}
		String url = Constant.SERVER_URL + "/api/user/" + userId + "/studio";
		Handler handler = new HttpHandler(this) {
			@Override
			protected void succeed(JSONObject jObject) {
				try {
					int state = jObject.getInt("stat");
					if (state == 1) {
						if (studio.getIsWatched() == 0) {
							Toast.makeText(RoomDetailActivity.this, "关注成功",
									Toast.LENGTH_SHORT).show();
							studio.setIsWatched(1);
							studio.setFanNum(studio.getFanNum() + 1);
							tvFanNum.setText("粉丝" + studio.getFanNum() + "");
							ibtnFocus
									.setImageResource(R.drawable.focus_cancle_icon);
						} else {
							Toast.makeText(RoomDetailActivity.this, "取消关注成功",
									Toast.LENGTH_SHORT).show();
							studio.setIsWatched(0);
							studio.setFanNum(studio.getFanNum() - 1);
							tvFanNum.setText("粉丝" + studio.getFanNum() + "");
							ibtnFocus
									.setImageResource(R.drawable.interest_icon_1);
						}
						BroadcastUtil.updateUserInfo(RoomDetailActivity.this);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("studioId", studioId));
		if (studio.getIsWatched() == 0) {
			new HttpConnectionUtils(handler).put(url, list);
		} else {
			url += "?studioId=" + studioId;
			new HttpConnectionUtils(handler).delete(url);
		}
	}

	private void getStudioInfo() {
		String url = Constant.SERVER_URL + "/api/studio/" + studioId;
		String userId = (String) SPUtil.get(this, "userId", "");
		if (!userId.isEmpty()) {
			url += "?userId=" + userId;
		}
		Handler handler = new HttpHandler(this) {
			@Override
			protected void succeed(JSONObject jObject) {
				try {
					int state = jObject.getInt("stat");
					if (state == 1) {
						studio = gson.fromJson(jObject.getJSONObject("studio")
								.toString(), Studio.class);
						setDataToView();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		new HttpConnectionUtils(handler).get(url);
	}

	protected void setDataToView() {
		tvName.setText(studio.getName());
		tvPhone.setText(studio.getPhone());
		tvDescribe.setText(studio.getDescription());
		tvAnswerNum.setText("回答 " + studio.getAnswerNum());
		tvFocusNum.setText("关注 " + studio.getFollowNum());
		tvFanNum.setText("粉丝 " + studio.getFanNum());

		logoOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_menu_home)
				.showImageOnFail(R.drawable.ic_menu_home).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		ImageLoader.getInstance().displayImage(studio.getAvatarUrl(), ivHeader,
				logoOptions);
		Log.v(TAG, "isWatched:" + studio.getIsWatched());
		if (studio.getIsWatched() == 0) {
			ibtnFocus.setImageResource(R.drawable.interest_icon_1);
		} else {
			ibtnFocus.setImageResource(R.drawable.focus_cancle_icon);
		}
	}

	private void initView() {
		tvAnswer = (TextView) findViewById(R.id.tv_answered_room_detail);
		tvGallery = (TextView) findViewById(R.id.tv_gallery_room_detail);
		tvTeacher = (TextView) findViewById(R.id.tv_teachers_room_detail);
		tvHire = (TextView) findViewById(R.id.tv_hire_room_detail);
		tvVideo = (TextView) findViewById(R.id.tv_video_room_detail);
		tvPhone = (TextView) findViewById(R.id.tv_phone_room_detail);
		tvName = (TextView) findViewById(R.id.tv_name_room_detail);
		titleBar = (TitleBar) findViewById(R.id.tb_activity_room_detail);
		btnDial = (Button) findViewById(R.id.btn_dial_room_detail);
		mViewPager = (ViewPager) findViewById(R.id.vp_activity_room_detail);
		tvDescribe = (TextView) findViewById(R.id.tv_describe_room_detail);
		tvAnswerNum = (TextView) findViewById(R.id.tv_answernum_room_detail);
		tvFocusNum = (TextView) findViewById(R.id.tv_focusnum_room_detail);
		tvFanNum = (TextView) findViewById(R.id.tv_fannum_room_detail);
		ivHeader = (ImageView) findViewById(R.id.iv_roomlogo_room_detail);
		ibtnFocus = (ImageButton) findViewById(R.id.ibtn_interest_room_detail);
		btnEnvirDetail = (Button) findViewById(R.id.btn_envir_info_room_detail);

		tvAnswer.setOnClickListener(this);
		tvGallery.setOnClickListener(this);
		tvTeacher.setOnClickListener(this);
		tvHire.setOnClickListener(this);
		tvVideo.setOnClickListener(this);
		btnDial.setOnClickListener(this);

		tvList = new ArrayList<TextView>();
		tvList.add(tvAnswer);
		tvList.add(tvGallery);
		tvList.add(tvTeacher);
		tvList.add(tvHire);
		tvList.add(tvVideo);

		initTitleBar();
		mViewPager.setOffscreenPageLimit(4);
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
		tvAsk.setText("提问");
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
				String userId = (String) SPUtil.get(RoomDetailActivity.this,
						"userId", "");
				if (userId.isEmpty()) {
					Toast.makeText(RoomDetailActivity.this, "请先登录再提问",
							Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = new Intent(RoomDetailActivity.this,
						PostProblemActivity.class);
				intent.putExtra("teacherId", studioId);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_answered_room_detail:
			tvAnswer.setTextColor(Color.parseColor("#80fe6060"));
			tvGallery.setTextColor(Color.parseColor("#959595"));
			tvTeacher.setTextColor(Color.parseColor("#959595"));
			tvHire.setTextColor(Color.parseColor("#959595"));
			tvVideo.setTextColor(Color.parseColor("#959595"));
			showDetailList(0);
			break;
		case R.id.tv_gallery_room_detail:
			tvAnswer.setTextColor(Color.parseColor("#959595"));
			tvGallery.setTextColor(Color.parseColor("#80fe6060"));
			tvTeacher.setTextColor(Color.parseColor("#959595"));
			tvHire.setTextColor(Color.parseColor("#959595"));
			tvVideo.setTextColor(Color.parseColor("#959595"));
			showDetailList(1);
			break;
		case R.id.tv_teachers_room_detail:
			tvAnswer.setTextColor(Color.parseColor("#959595"));
			tvGallery.setTextColor(Color.parseColor("#959595"));
			tvTeacher.setTextColor(Color.parseColor("#80fe6060"));
			tvHire.setTextColor(Color.parseColor("#959595"));
			tvVideo.setTextColor(Color.parseColor("#959595"));
			showDetailList(2);
			break;
		case R.id.tv_hire_room_detail:
			tvAnswer.setTextColor(Color.parseColor("#959595"));
			tvGallery.setTextColor(Color.parseColor("#959595"));
			tvTeacher.setTextColor(Color.parseColor("#959595"));
			tvHire.setTextColor(Color.parseColor("#80fe6060"));
			tvVideo.setTextColor(Color.parseColor("#959595"));
			showDetailList(3);
			break;
		case R.id.tv_video_room_detail:
			tvAnswer.setTextColor(Color.parseColor("#959595"));
			tvGallery.setTextColor(Color.parseColor("#959595"));
			tvTeacher.setTextColor(Color.parseColor("#959595"));
			tvHire.setTextColor(Color.parseColor("#959595"));
			tvVideo.setTextColor(Color.parseColor("#80fe6060"));
			showDetailList(4);
			break;
		case R.id.btn_dial_room_detail:
			showDialDialog();
			break;

		default:
			break;
		}
	}

	private void showDialDialog() {
		ActionSheet sheet = new ActionSheet(this);
		sheet.setCancelButtonTitle("取消");
		final String phone = tvPhone.getText().toString();
		sheet.setTitle(phone + "可能是一个电话号码，你可以");
		sheet.addItems("呼叫", "复制", "添加到通讯录");
		sheet.setItemClickListener(new MenuItemClickListener() {

			@SuppressLint("ShowToast")
			@SuppressWarnings("deprecation")
			@Override
			public void onItemClick(int itemPosition) {
				switch (itemPosition) {
				case 0:
					Intent intent = new Intent(Intent.ACTION_CALL, Uri
							.parse("tel:" + phone));
					startActivity(intent);
					break;
				case 1:
					ClipboardManager cbm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
					cbm.setText(phone);
					Toast.makeText(RoomDetailActivity.this, "已添加到粘贴板",
							Toast.LENGTH_SHORT);
					break;
				case 2:
					addToContact();
					break;
				default:
					break;
				}
			}
		});
		sheet.showMenu();
	}

	private void addToContact() {
		String name = tvName.getText().toString();
		String phone = tvPhone.getText().toString();
		ContentValues values = new ContentValues();
		Uri rawContactUri = getContentResolver().insert(
				RawContacts.CONTENT_URI, values);
		long rawContactId = ContentUris.parseId(rawContactUri);
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		values.put(StructuredName.GIVEN_NAME, name);
		getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Phone.NUMBER, phone);
		values.put(Phone.TYPE, Phone.TYPE_MOBILE);
		getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

		Toast.makeText(this, "成功添加到通讯录", Toast.LENGTH_SHORT).show();

	}

	private void showDetailList(int pos) {
		selectPos = pos;
		if (selectPos < mTabs.size()) {
			mViewPager.setCurrentItem(selectPos, false);
			Log.v(TAG, "pos:" + selectPos);
		}
	}
}
