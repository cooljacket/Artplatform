package com.gexin.artplatform.mine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.R;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.dlog.DLog;
import com.gexin.artplatform.utils.FileUtil;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.ImageUtil;
import com.gexin.artplatform.utils.NetUtil;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.ActionSheet;
import com.gexin.artplatform.view.ActionSheet.MenuItemClickListener;
import com.gexin.artplatform.view.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressLint("HandlerLeak")
public class UserInfoActivity extends Activity implements OnClickListener {

	protected static final int ALBUM_REQUEST_CODE = 0;
	protected static final int CAMERA_REQUEST_CODE = 1;
	protected static final int MODIFY_REQUEST_CODE = 2;
	public static final String ACTION_HEADER_MODIFY = "ACTION_HEADER_MODIFY";
	private static final int POST_IMAGE_SUCCESS = 3;
	private static final String POST_IMAGE_API = Constant.SERVER_URL
			+ "/api/image";
	private static final String TAG = "UserInoActivity";
	private static final String IMAGEDIR = Constant.APP_PATH + "image/";
	private String imagePath = "";
	private List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
	//private SimpleAdapter adapter;
	private String[] titles = { "用户ID", "名字", "性别", "省份", "身份", "学校", "修改密码", "手机号" };
	private String[] gradeArray = { "高三", "高二", "高一", "初中", "大学", "业余" };
	private List<String> values = new ArrayList<String>();
	private int mIndex = 0;
	private DisplayImageOptions headerOptions;

	//private ListView mListView;
	private Button btnExit;
	private ImageView ivHeader;
	private RelativeLayout rlHeader;
	private RelativeLayout rl_ID;
	private RelativeLayout rl_name;
	private RelativeLayout rl_sex;
	private RelativeLayout rl_province;
	private RelativeLayout rl_identity;
	private RelativeLayout rl_school;
	private RelativeLayout rl_change_pw;
	private RelativeLayout rl_pNumber;
	private TextView tv_ID;
	private TextView tv_name;
	private TextView tv_sex;
	private TextView tv_province;
	private TextView tv_identity;
	private TextView tv_school;
	private LinearLayout llBack;
	private TitleBar titleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		setTheme(R.style.ActionSheetStyleIOS7);
		initView();
		initData();
	}

	private void initData() {
        String userId = (String) SPUtil.get(this, "userId", "");
		String name = (String) SPUtil.get(this, "name", "未设置");
		int gender = (Integer) SPUtil.get(this, "gender", 0);
		String sex = "";
		if (gender == 0) {
			sex = "女";
		} else if (gender == 1) {
			sex = "男";
		} else {
			sex = "未设置";
		}
		String province = (String) SPUtil.get(this, "province", "未设置");
		String school = (String) SPUtil.get(this, "school", "未设置");
		int grade = (Integer) SPUtil.get(this, "grade", 0);
		String status = gradeArray[grade];
		if (name.isEmpty()) {
			name = "未设置";
		}
		if (province.isEmpty()) {
			province = "未设置";
		}
		if (status.isEmpty()) {
			status = "未设置";
		}
		if (school.isEmpty()) {
			school = "未设置";
		}
		values.add(userId);
		values.add(name);
		values.add(sex);
		values.add(province);
		values.add(status);
		values.add(school);
		for (int i = 0; i < titles.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", titles[i]);
			if (i < values.size()) {
				map.put("content", values.get(i));
			} else {
				map.put("content", "");
			}
			mList.add(map);
		}
		tv_ID.setText(userId);
		tv_name.setText(name);
		tv_sex.setText(sex);
		tv_province.setText(province);
		tv_identity.setText(status);
		tv_school.setText(school);
		
		
		//adapter = new SimpleAdapter(this, mList, R.layout.user_info_item,
				//new String[] { "title", "content" }, new int[] {
						//R.id.tv_title_user_info_item,
						//R.id.tv_content_user_info_item });
		//mListView.setAdapter(adapter);

		headerOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_contact_picture)
				.showImageForEmptyUri(R.drawable.ic_contact_picture)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		String avatarUrl = (String) SPUtil.get(this, "avatarUrl", "");
		ImageLoader.getInstance().displayImage(avatarUrl, ivHeader,
				headerOptions);
	}

	private void initView() {
		//mListView = (ListView) findViewById(R.id.lv_activity_userinfo);
		btnExit = (Button) findViewById(R.id.btn_exit_activity_userinfo);
		ivHeader = (ImageView) findViewById(R.id.iv_header_activity_userinfo);
		rlHeader = (RelativeLayout) findViewById(R.id.rl_userinfo_activity_userinfo);
		rl_ID = (RelativeLayout) findViewById(R.id.id1_content_user_info);

		rl_name = (RelativeLayout) findViewById(R.id.name_content_user_info);
		rl_sex = (RelativeLayout) findViewById(R.id.sex_content_user_info);
		rl_province = (RelativeLayout) findViewById(R.id.province_content_user_info);
		rl_identity = (RelativeLayout) findViewById(R.id.identity_content_user_info);
		rl_school = (RelativeLayout) findViewById(R.id.school_content_user_info);
		rl_change_pw = (RelativeLayout) findViewById(R.id.changePassword_content_user_info);
		rl_pNumber = (RelativeLayout) findViewById(R.id.phoneNumber_content_user_info);
		
		tv_ID = (TextView)findViewById(R.id.tv_id1_content_user_info_item);
		tv_name = (TextView)findViewById(R.id.tv_name_content_user_info_item);
		tv_sex = (TextView)findViewById(R.id.tv_sex_content_user_info_item);
		tv_province = (TextView)findViewById(R.id.tv_province_content_user_info_item);
		tv_identity = (TextView)findViewById(R.id.tv_identity_content_user_info_item);
		tv_school = (TextView)findViewById(R.id.tv_school_content_user_info_item);
		
		
		rl_ID.setTag(0);
		rl_name.setTag(1);
		rl_sex.setTag(2);
		rl_province.setTag(3);
		rl_identity.setTag(4);
		rl_school.setTag(5);
		rl_change_pw.setTag(6);
		rl_pNumber.setTag(7);
		
		titleBar = (TitleBar) findViewById(R.id.tb_userinfo_activity);
		//ivHeader = (ImageView) findViewById(R.id.iv_header_activity_userinfo);

		initTitleBar();
		rlHeader.setOnClickListener(this);
		ivHeader.setOnClickListener(this);
		btnExit.setOnClickListener(this);
		rl_ID.setOnClickListener(this);
		rl_name.setOnClickListener(this);
		rl_sex.setOnClickListener(this);
		rl_province.setOnClickListener(this);
		rl_identity.setOnClickListener(this);
		rl_school.setOnClickListener(this);
		rl_change_pw.setOnClickListener(this);
		rl_pNumber.setOnClickListener(this);
		
		/**mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mIndex = arg2;
				Intent intent = new Intent(UserInfoActivity.this,
						ModifyUserInfoActivity.class);
				intent.putExtra("index", arg2);
				intent.putExtra("title", titles[arg2]);
				intent.putExtra("value", (String) mList.get(arg2)
						.get("content"));
				startActivityForResult(intent, MODIFY_REQUEST_CODE);
			}
		});**/
	}

	private void showHeaderSelectDialog() {
		ActionSheet sheet = new ActionSheet(this);
		sheet.setCancelButtonTitle("取消");
		sheet.addItems("拍照", "从相册中选取");
		sheet.setItemClickListener(new MenuItemClickListener() {

			@Override
			public void onItemClick(int itemPosition) {
				if (itemPosition == 1) {
					Intent intent = new Intent(Intent.ACTION_PICK, null);
					// 设置数据来源和类型
					intent.setDataAndType(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							"image/*");
					startActivityForResult(intent, ALBUM_REQUEST_CODE);
				}
				if (itemPosition == 0) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					// 打开图片所在目录，如果该目录不存在，则创建该目录
					File dirFile = new File(IMAGEDIR);
					if (!dirFile.exists()) {
						dirFile.mkdirs();
					}
					int picCount = (Integer) SPUtil.get(UserInfoActivity.this,
							"picCount", 0);
					picCount++;
					// 将图片保存到该目录下
					intent.putExtra(
							MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(new File(IMAGEDIR, "pic" + picCount
									+ ".jpg")));
					SPUtil.put(UserInfoActivity.this, "picCount", picCount);
					startActivityForResult(intent, CAMERA_REQUEST_CODE);
				}
			}

		});
		sheet.showMenu();
	}

	private void initTitleBar() {
		llBack = new LinearLayout(this);
		ImageView ivBack = new ImageView(this);
		ivBack.setImageResource(R.drawable.back_icon);
		LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bitmap bitmap;
			switch (requestCode) {
			case ALBUM_REQUEST_CODE:
				imagePath = FileUtil.getRealFilePath(this, data.getData());
				bitmap = ImageUtil.getCompressedImage(imagePath, 50);
				if (bitmap == null) {
					ivHeader.setImageResource(R.drawable.add_icon);
				} else {
					ivHeader.setImageBitmap(bitmap);
				}
				uploadImage(imagePath);
				break;
			case CAMERA_REQUEST_CODE:
				int picCount = (Integer) SPUtil.get(this, "picCount", 0);
				String picName = "pic" + picCount + ".jpg";
				imagePath = IMAGEDIR + picName;
				bitmap = ImageUtil.getCompressedImage(imagePath, 50);
				if (bitmap == null) {
					ivHeader.setImageResource(R.drawable.add_icon);
				} else {
					ivHeader.setImageBitmap(bitmap);
				}
				uploadImage(imagePath);
				break;
			case MODIFY_REQUEST_CODE:
				if (resultCode == RESULT_OK) {
					Log.v("ok", "okokokokokokok");
					Toast.makeText(UserInfoActivity.this, "OK",
							Toast.LENGTH_SHORT).show();
				String value = data.getStringExtra("value");
					if (value.isEmpty()) {
						value = "未设置";
					}
					mList.get(mIndex).put("content", value);
					Log.v("ok", "okokokokokokok44");
					if (mIndex == 6 || mIndex == 7) {
						mList.get(mIndex).put("content", "");
					}
					Log.v("ok", "okokokokokokok55");
					Log.v("okok", value);
					switch(mIndex) {
					case 1:tv_name.setText(value);;break;
					case 2:tv_sex.setText(value);break;
					case 3:tv_province.setText(value);break;
					case 4:tv_identity.setText(value);break;
					case 5:tv_school.setText(value);break;
					default:break;
					}
					//adapter.notifyDataSetChanged();*/
				}
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@SuppressLint("HandlerLeak")
	private void uploadImage(String imagePath2) {
		final String userId = (String) SPUtil.get(this, "userId", "");
		new Thread(new Runnable() {
			private String imageUrl = "";
			private String modifyApi = Constant.SERVER_URL + Constant.USER_API
					+ "/" + userId;

			Handler handler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case HttpConnectionUtils.DID_SUCCEED:
						String response = (String) msg.obj;
						Log.v(TAG, "response:" + response);
						try {
							JSONObject jObject = new JSONObject(response);
							int state = jObject.getInt("stat");
							if (state == 1) {
								Toast.makeText(getApplicationContext(),
										"头像更新成功", Toast.LENGTH_SHORT).show();
								SPUtil.put(UserInfoActivity.this, "avatarUrl",
										imageUrl);
								Intent intent = new Intent();
								intent.putExtra("avatarUrl", imageUrl);
								intent.setAction(ACTION_HEADER_MODIFY);
								UserInfoActivity.this.sendBroadcast(intent);
							} else {
								Toast.makeText(getApplicationContext(),
										"头像更新失败", Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(), "头像更新失败",
									Toast.LENGTH_SHORT).show();
						}

						break;

					case POST_IMAGE_SUCCESS:
						uploadHeader();
					default:
						break;
					}
					super.handleMessage(msg);
				}

			};

			@Override
			public void run() {
				String imageResult = NetUtil.uploadFile(new File(imagePath),
						POST_IMAGE_API);
				if (imageResult != null && !imageResult.isEmpty()) {
					try {
						JSONObject jObject = new JSONObject(imageResult);
						int state = jObject.getInt("stat");
						if (state == 1) {
							imageUrl = jObject.getString("url");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (!imageUrl.isEmpty()) {
						handler.sendEmptyMessage(POST_IMAGE_SUCCESS);
					}
				}

			}

			private void uploadHeader() {
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				if (imageUrl != null && !imageUrl.isEmpty()) {
					list.add(new BasicNameValuePair("avatarUrl", imageUrl));
				}
				new HttpConnectionUtils(handler).put(modifyApi, list);
			}
		}).start();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_userinfo_activity_userinfo:
			showHeaderSelectDialog();
			break;
		case R.id.iv_header_activity_userinfo:
			showHeaderSelectDialog();
			break;
		case R.id.btn_exit_activity_userinfo:
			//String userId = (String) SPUtil.get(this, "userId", "");
			Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					switch (msg.what) {
					case HttpConnectionUtils.DID_SUCCEED:
						String response = (String) msg.obj;
						DLog.i("user logout: ", response);
						try {
							JSONObject jsonObject = new JSONObject(response);
							int stat = jsonObject.getInt("stat");
							if (stat == 1) {
								SPUtil.clear(UserInfoActivity.this);
								setResult(RESULT_OK);
								finish();
							} else {
								Toast.makeText(UserInfoActivity.this,
										"退出失败，请稍后重试", Toast.LENGTH_SHORT)
										.show();
							}
						} catch (JSONException e) {
							// TODO: handle exception
							e.printStackTrace();
							Toast.makeText(UserInfoActivity.this, "退出失败，请稍后重试",
									Toast.LENGTH_SHORT).show();
						}

						break;
					case HttpConnectionUtils.DID_ERROR:
						Toast.makeText(UserInfoActivity.this, "退出失败，请稍后重试",
								Toast.LENGTH_SHORT).show();
						break;
					
					default:
						break;
					}
					super.handleMessage(msg);
				}
			};
			//DLog.i("logout api", Constant.SERVER_URL + Constant.USER_LOGOUT_API);
			new HttpConnectionUtils(handler).get(Constant.SERVER_URL
					+ Constant.USER_LOGOUT_API);
			break;
			
		case R.id.id1_content_user_info:
			mIndex = (Integer)((RelativeLayout)v).getTag();
			Intent intent = new Intent(UserInfoActivity.this,
					ModifyUserInfoActivity.class);
			intent.putExtra("index", mIndex);
			intent.putExtra("title", titles[mIndex]);
			intent.putExtra("value", (String) mList.get(mIndex)
					.get("content"));
			startActivityForResult(intent, MODIFY_REQUEST_CODE);
			break;
		
		default:
			
			mIndex = (Integer) ((RelativeLayout)v).getTag();
			Intent intent2 = new Intent(UserInfoActivity.this,
					ModifyUserInfoActivity.class);
			intent2.putExtra("index", mIndex);
			intent2.putExtra("title", titles[mIndex]);
			intent2.putExtra("value", (String) mList.get(mIndex)
					.get("content"));
			startActivityForResult(intent2, MODIFY_REQUEST_CODE);
			break;
		}
	}
	
}
