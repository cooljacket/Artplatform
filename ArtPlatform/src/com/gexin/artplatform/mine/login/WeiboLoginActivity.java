package com.gexin.artplatform.mine.login;


import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.User;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



public class WeiboLoginActivity extends Activity {
	public WebView webview;
	private static final String TAG = "WeiboLoginActivity";
	private Gson gson = new Gson();

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo_login);

		webview = (WebView) this.findViewById(R.id.wv_activity_weibolog);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setFocusable(true);
		webview.loadUrl(Constant.SINA_OAUTH);

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				Log.i("onPageFinished", url + "网页加载完毕");
				super.onPageFinished(view, url);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.i("shouldOverrideUrlLoading", url);
				webview.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}
			

			private void success(JSONObject jObject) {
				int state = -1;
				User tempUser = null;
				Log.i(TAG, "jObject:"+jObject.toString());
				 
				try {
					state = jObject.getInt("stat");
					if (state == 1) {
						tempUser = gson.fromJson(jObject.getJSONObject("user")
								.toString(), new TypeToken<User>() {
						}.getType());
						Log.i(TAG, "temp:"+tempUser.toString());
						tempUser.putToSP(WeiboLoginActivity.this);
						Toast.makeText(getApplicationContext(), "微博登陆成功", Toast.LENGTH_SHORT).show();
						setResult(RESULT_OK);
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}

			@SuppressLint("HandlerLeak")
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				Log.v("onPageStarted", url + "开始加载界面。。。。。");
				if (url.startsWith("http://120.26.192.176")) {
					// 取消授权后的界面

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
										success(jObject);
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
					new HttpConnectionUtils(handler).get(url);
					view.cancelLongPress();
					view.stopLoading();
//					setResult(RESULT_OK);
//					finish();
				}
				super.onPageStarted(view, url, favicon);
			}
		});
	}
}
