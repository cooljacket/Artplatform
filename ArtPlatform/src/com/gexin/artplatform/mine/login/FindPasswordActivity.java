package com.gexin.artplatform.mine.login;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.gexin.artplatform.R;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.dlog.DLog;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.view.TitleBar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class FindPasswordActivity extends Activity implements OnClickListener {

	private static final String FIND_ANSWER_API = Constant.SERVER_URL
			+ Constant.USER_FIND_PASSWORD;

	private static final String TAG = "FindAnswerActivity";
	private String APPKEY = "7e05c212106c";
	private String APPSECRET = "88833159ff50194e297cade044655927";

	private EditText etPhone, etPw, etPwAgain, etIdentCode;
	private Button btnConfirm, btnSendCode;
	private TitleBar titleBar;
	private LinearLayout llBack;

	// 记录接受验证码的手机号
	private String receivePhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_password);
		initView();
		SMSSDK.initSDK(this, APPKEY, APPSECRET);
		SMSSDK.registerEventHandler(new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		});
	}

	private void initView() {
		etPhone = (EditText) findViewById(R.id.et_phone);
		etPw = (EditText) findViewById(R.id.et_password);
		etPwAgain = (EditText) findViewById(R.id.et_password_again);
		etIdentCode = (EditText) findViewById(R.id.reg_identify_edit);
		btnConfirm = (Button) findViewById(R.id.btn_confirm_reg);
		btnSendCode = (Button) findViewById(R.id.reg_identify_btn);
		titleBar = (TitleBar) findViewById(R.id.tb_reg_activity);
		initTitleBar();
		btnConfirm.setOnClickListener(this);
		btnSendCode.setOnClickListener(this);
	}

	private void success(JSONObject jObject) {
		try {
			int state = jObject.getInt("stat");
			if (state == 1) {
				Toast.makeText(this, "修改密码成功", Toast.LENGTH_SHORT).show();
				finish();
			} else {
				Toast.makeText(this, "修改失败，请稍后重试", Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	Handler httpHandler = new HttpHandler(FindPasswordActivity.this) {

		@Override
		protected void succeed(JSONObject jObject) {
			DLog.v(TAG, "succeed:" + jObject.toString());
			success(jObject);
		}

	};

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			DLog.e("event", "event=" + event);
			if (result == SMSSDK.RESULT_COMPLETE) {
				DLog.i(TAG, "return success");
				// 短信注册成功后，返回RegisterActivity,然后提示新好友
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
					DLog.i(TAG, "提交验证码成功");
					// String phone = etPhone.getText().toString().trim();
					String password = etPw.getText().toString().trim();
					String pwagain = etPwAgain.getText().toString().trim();
					if (password.equals("")) {
						Toast.makeText(FindPasswordActivity.this, "请填写完整信息",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if (!password.equals(pwagain)) {
						Toast.makeText(FindPasswordActivity.this, "两次输入的密码不相同",
								Toast.LENGTH_SHORT).show();
						return;
					}
					Toast.makeText(getApplicationContext(), "提交验证码成功",
							Toast.LENGTH_SHORT).show();
					HttpConnectionUtils httpConnectionUtils = new HttpConnectionUtils(
							httpHandler);
					List<NameValuePair> list = new ArrayList<NameValuePair>();
					list.add(new BasicNameValuePair("phone", receivePhone));
					list.add(new BasicNameValuePair("password", password));
					// list.add(new BasicNameValuePair("validation", "233"));
					DLog.i(TAG, "phone: " + receivePhone + " password: "
							+ password);
					DLog.i(TAG, "FIND_ANSWER_API: " + FIND_ANSWER_API);
					httpConnectionUtils.post(FIND_ANSWER_API, list);

				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					Toast.makeText(getApplicationContext(), "验证码已经发送",
							Toast.LENGTH_SHORT).show();
					DLog.i(TAG, "验证码已经发送");
				} else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {// 返回支持发送验证码的国家列表
					DLog.i(TAG, "返回支持发送验证码的国家列表");
				}
			} else {
				if (event != 2) {
					((Throwable) data).printStackTrace();
					Toast.makeText(FindPasswordActivity.this, "验证码错误",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(FindPasswordActivity.this, "服务商原因，验证码发送失败",
							Toast.LENGTH_SHORT).show();
				}
			}

		}
	};

	private boolean checkPhoneNum(String phoneNumString) {
		Pattern pattern = Pattern.compile("[0-9]*");
		if (phoneNumString == null || phoneNumString.equals("")) {
			Toast.makeText(FindPasswordActivity.this, "请输入手机号码",
					Toast.LENGTH_LONG).show();
			etPhone.requestFocus();
			return false;
		} else if (!pattern.matcher(phoneNumString).matches()) {
			Toast.makeText(FindPasswordActivity.this, "请输入正确的电话号码",
					Toast.LENGTH_LONG).show();
			etPhone.requestFocus();
			return false;
		} else if (phoneNumString.length() != 11 && phoneNumString.length() > 0) {
			Toast.makeText(FindPasswordActivity.this, "请输入正确的电话号码",
					Toast.LENGTH_LONG).show();
			etPhone.requestFocus();
			return false;
		} else if (phoneNumString.charAt(0) != '1') {
			Toast.makeText(FindPasswordActivity.this, "请输入正确的电话号码",
					Toast.LENGTH_SHORT).show();
			etPhone.requestFocus();
			return false;
		}
		return true;
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
				Log.v(TAG, "BackClick");
				finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String phone = etPhone.getText().toString().trim();
		String password = etPw.getText().toString().trim();
		String pwagain = etPwAgain.getText().toString().trim();
		if (checkPhoneNum(phone) == false) {
			return;
		}
		if (password.equals("")) {
			Toast.makeText(FindPasswordActivity.this, "请填写完整信息",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!password.equals(pwagain)) {
			Toast.makeText(FindPasswordActivity.this, "两次输入的密码不相同",
					Toast.LENGTH_SHORT).show();
			return;
		}
		switch (v.getId()) {
		case R.id.btn_confirm_reg:
			SMSSDK.submitVerificationCode("86", phone, etIdentCode.getText()
					.toString().trim());
			break;
		case R.id.reg_identify_btn:
			receivePhone = phone;
			SMSSDK.getVerificationCode("86", phone);
			btnSendCode.setClickable(false);
			handler.postDelayed(CountDown, 1000);
			break;

		default:
			break;
		}
	}

	private int time = 60;

	Runnable CountDown = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (time > 0) {
				btnSendCode.setText(time + "秒后\n重新获取");
				--time;
				httpHandler.postDelayed(CountDown, 1000);
			} else {
				time = 60;
				// btnSendCode.setFocusable(true);
				// btnSendCode.setFocusableInTouchMode(true);
				btnSendCode.setText("重新获取");
				btnSendCode.setClickable(true);
			}
		}
	};

}
