package com.gexin.artplatform.mine.login;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.gexin.artplatform.R;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.utils.JSONUtil;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.TitleBar;

public class LoginActivity extends Activity {

	private static final String TAG = "LoginActivity";
	private static final String LOGIN_API = Constant.SERVER_URL
			+ Constant.USER_API + "/login";
	protected static final int WEIBO_LOGIN_REQUEST = 0;
	protected static final int REG_REQUEST = 1;

	private Button btnLogin, btnReg, btnWeiboLog, btnFindPw;
	private EditText etUsername, etPassword;
	private String username, password;
	private TitleBar titleBar;
	private LinearLayout llBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		initView();

	}

	private void initView() {
		btnLogin = (Button) findViewById(R.id.login_login_btn);
		btnReg = (Button) findViewById(R.id.login_reg_btn);
		btnWeiboLog = (Button) findViewById(R.id.btn_weibolog_activity_login);
		btnFindPw = (Button) findViewById(R.id.btn_find_password);
		etUsername = (EditText) findViewById(R.id.login_user_edit);
		etPassword = (EditText) findViewById(R.id.login_passwd_edit);
		titleBar = (TitleBar) findViewById(R.id.tb_login_activity);
		initTitleBar();

		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				username = etUsername.getText().toString();
				password = etPassword.getText().toString();
				if (username.isEmpty() || password.isEmpty()) {
					Toast.makeText(LoginActivity.this, "请完整填写账号和密码",
							Toast.LENGTH_SHORT).show();
					return;
				}
				Handler handler = new HttpHandler(LoginActivity.this) {

					@Override
					protected void succeed(JSONObject jObject) {
						Log.v(TAG, jObject.toString());
						success(jObject);
					}

				};
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("account", username));
				list.add(new BasicNameValuePair("password", password));
				Log.v(TAG, "param:" + list.toString());
				new HttpConnectionUtils(handler).post(LOGIN_API, list);
			}
		});

		btnReg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegisterActivity.class);
				startActivityForResult(intent, REG_REQUEST);
			}
		});

		btnWeiboLog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, WeiboLoginActivity.class);
				startActivityForResult(intent, WEIBO_LOGIN_REQUEST);
			}
		});
		
		btnFindPw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LoginActivity.this, FindPasswordActivity.class));
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case WEIBO_LOGIN_REQUEST:
			if (resultCode == RESULT_OK) {
				setResult(RESULT_OK);
				int isTeacher = (Integer) SPUtil.get(this, "isTeacher", 0);
				Log.v(TAG, "isTeacher:" + isTeacher);
				finish();
			}
			break;

		case REG_REQUEST:
			if (resultCode == RESULT_OK) {
				String phone = data.getStringExtra("phone");
				etUsername.setText(phone);
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void success(JSONObject jObject) {

		try {
			int state = jObject.getInt("stat");
			if (state == 1) {
				JSONUtil.analyseLoginJSON(this, jObject.getJSONObject("user")
						.toString());
				Log.v(TAG, "success");
				Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
				setResult(RESULT_OK);
				finish();
			} else {
				Toast.makeText(this, "请检查账号或密码", Toast.LENGTH_SHORT).show();
				return;
			}
		} catch (JSONException e) {
			e.printStackTrace();
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
}
