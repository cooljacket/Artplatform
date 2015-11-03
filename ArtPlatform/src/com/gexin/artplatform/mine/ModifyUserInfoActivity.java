package com.gexin.artplatform.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.R;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.question.ModifyListAdapter;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.ClearableEditText;
import com.gexin.artplatform.view.TitleBar;

public class ModifyUserInfoActivity extends Activity {

	private int mIndex = 0;
	private static final String TAG = "ModifyUserInfoActivity";
	private String mTitle = "";
	private String mValue = "";
	private List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
	private String[] genderArray = { "男", "女" };
	private String[] statusArray = { "高三", "高二", "高一", "初中", "大学", "业余" };
	private String[] provinceArray = { "北京", "天津", "河北", "山西", "内蒙", "辽宁",
			"吉林", "黑龙江", "上海", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北",
			"湖南", "广东", "广西", "海南", "重庆", "四川", "贵州", "云南", "西藏", "陕西", "甘肃",
			"青海", "宁夏", "新疆", "香港", "澳门", "台湾 " };
	private ModifyListAdapter adapter;
	private int selectPos = -1;

	private LinearLayout llBack;
	private LinearLayout llSave;
	private TitleBar titleBar;
	private ClearableEditText cetInput;
	private EditText etOldPswd;
	private EditText etNewPswd;
	private ListView mListView;
	private Button btnSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_user_info);

		mIndex = getIntent().getIntExtra("index", -1);
		mTitle = getIntent().getStringExtra("title");
		mValue = getIntent().getStringExtra("value");
		if (mIndex == 6) {
			mValue = "" + SPUtil.get(this, "phone", 0L);
		}
		if (mValue.equals("未设置")) {
			mValue = "";
		}
		Log.v(TAG, "index:" + mIndex + ",title:" + mTitle + ",value:" + mValue);

		initView();
		initData();
	}

	private void initData() {
		switch (mIndex) {
		case 0:
			cetInput.setText(mValue);
			break;
		case 1:
			for (int i = 0; i < genderArray.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("value", genderArray[i]);
				map.put("select", false);
				if (mValue.equals(genderArray[i])) {
					selectPos = i;
				}
				mList.add(map);
			}
			if (selectPos >= 0) {
				mList.get(selectPos).put("select", true);
			}
			break;
		case 2:
			for (int i = 0; i < provinceArray.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("value", provinceArray[i]);
				map.put("select", false);
				if (mValue.equals(provinceArray[i])) {
					selectPos = i;
				}
				mList.add(map);
			}
			if (selectPos >= 0) {
				mList.get(selectPos).put("select", true);
			}
			break;
		case 3:
			for (int i = 0; i < statusArray.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("value", statusArray[i]);
				map.put("select", false);
				if (mValue.equals(statusArray[i])) {
					selectPos = i;
				}
				mList.add(map);
			}
			if (selectPos >= 0) {
				mList.get(selectPos).put("select", true);
			}
			break;
		case 4:
			cetInput.setText(mValue);
			break;
		case 5:
			break;
		case 6:
			if (mValue == null || mValue.isEmpty() || mValue.equals("0")) {
				mValue = "";
			}
			cetInput.setText(mValue);
			cetInput.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		default:
			break;
		}

		if (mIndex == 1 || mIndex == 2 || mIndex == 3) {
			adapter = new ModifyListAdapter(this, mList);
			mListView.setAdapter(adapter);
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					for (int i = 0; i < mList.size(); i++) {
						if (i == arg2) {
							mList.get(i).put("select", true);
						} else {
							mList.get(i).put("select", false);
						}
					}
					selectPos = arg2;
					adapter.notifyDataSetChanged();
				}
			});
		}
	}

	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_activity_modify_userinfo);
		cetInput = (ClearableEditText) findViewById(R.id.cet_content_modify_user_info);
		etOldPswd = (EditText) findViewById(R.id.et_oldpswd_activity_modify_user_info);
		etNewPswd = (EditText) findViewById(R.id.et_newpswd_activity_modify_user_info);
		mListView = (ListView) findViewById(R.id.lv_activity_modify_user_info);
		btnSubmit = (Button) findViewById(R.id.btn_submit_activity_modify_user_info);
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				postToServer();
			}
		});
		initTitleBar();

		if (mIndex == 0 || mIndex == 4 || mIndex == 6) {
			cetInput.setVisibility(View.VISIBLE);
		}
		if (mIndex == 1 || mIndex == 2 || mIndex == 3) {
			mListView.setVisibility(View.VISIBLE);
		}
		if (mIndex == 5) {
			etOldPswd.setVisibility(View.VISIBLE);
			etNewPswd.setVisibility(View.VISIBLE);
			btnSubmit.setVisibility(View.VISIBLE);
		}
	}

	private void initTitleBar() {
		titleBar.setTitle(mTitle);
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

		if (mIndex != 5) {
			llSave = new LinearLayout(this);
			TextView tvAsk = new TextView(this);
			tvAsk.setText("保存");
			tvAsk.setTextSize(20);
			tvAsk.setTextColor(Color.WHITE);
			params = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.setMargins(10, 0, 10, 0);
			llSave.setGravity(Gravity.CENTER_VERTICAL);
			llSave.addView(tvAsk, params);
			llSave.setBackgroundResource(R.drawable.selector_titlebar_btn);
			titleBar.setRightView(llSave);
			llSave.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					postToServer();
				}
			});
		}
	}

	private void postToServer() {
		String userId = (String) SPUtil.get(this, "userId", "");
		String url = Constant.SERVER_URL + "/api/user/" + userId;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		switch (mIndex) {
		case 0:
			mValue = cetInput.getText().toString();
			list.add(new BasicNameValuePair("name", mValue));
			break;
		case 1:
			if (selectPos == 0) {
				list.add(new BasicNameValuePair("gender", "1"));
				mValue = "男";
			} else {
				list.add(new BasicNameValuePair("gender", "0"));
				mValue = "女";
			}
			break;
		case 2:
			mValue = provinceArray[selectPos];
			list.add(new BasicNameValuePair("province", mValue));
			break;
		case 3:
			list.add(new BasicNameValuePair("grade", "" + selectPos));
			mValue = statusArray[selectPos];
			break;
		case 4:
			mValue = cetInput.getText().toString();
			list.add(new BasicNameValuePair("school", mValue));
			break;
		case 5:
			String oldPswd = etOldPswd.getText().toString();
			String newPswd = etNewPswd.getText().toString();
			url += "/password";
			mValue = "";
			list.add(new BasicNameValuePair("oldPassword", oldPswd));
			list.add(new BasicNameValuePair("newPassword", newPswd));
			break;
		case 6:
			try {
				Long.parseLong(cetInput.getText().toString());
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(ModifyUserInfoActivity.this, "手机号必须全为数字",
						Toast.LENGTH_SHORT).show();
				return;
			}
			mValue = cetInput.getText().toString();
			list.add(new BasicNameValuePair("phone", mValue));
			break;
		default:
			break;
		}
		Handler handler = new HttpHandler(this) {
			@Override
			protected void succeed(JSONObject jObject) {
				dealResponse(jObject);
			}
		};
		new HttpConnectionUtils(handler).put(url, list);
	}

	protected void dealResponse(JSONObject jObject) {
		try {
			int state = jObject.getInt("stat");
			if (state == 1) {
				Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
				putToSP();
				Intent intent = new Intent();
				intent.putExtra("value", mValue);
				setResult(RESULT_OK, intent);
				finish();

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void putToSP() {
		switch (mIndex) {
		case 0:
			SPUtil.put(this, "name", mValue);
			break;
		case 1:
			SPUtil.put(this, "gender", 1 - selectPos);
			break;
		case 2:
			SPUtil.put(this, "province", mValue);
			break;
		case 3:
			SPUtil.put(this, "grade", selectPos);
			break;
		case 4:
			SPUtil.put(this, "school", mValue);
		case 5:
			break;
		case 6:
			SPUtil.put(this, "phone", Long.parseLong(mValue));
			break;
		default:
			break;
		}
	}
}
