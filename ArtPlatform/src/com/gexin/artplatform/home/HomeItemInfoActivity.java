package com.gexin.artplatform.home;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Article;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.largeImg.LargeImageActivity;
import com.gexin.artplatform.studio.RoomDetailActivity;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.utils.TimeUtil;
import com.gexin.artplatform.view.FlowLayout;
import com.gexin.artplatform.view.TitleBar;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeItemInfoActivity extends Activity {

	private Gson gson = new Gson();
	private Article article = null;
	private DisplayImageOptions avatarOptions;
	private DisplayImageOptions picOptions;

	private TextView tvName;
	private TextView tvContent;
	private TextView tvTime;
	private TextView tvClickNum;
	private ImageView ivHeader;
	private FlowLayout flPics;
	private LinearLayout llBack;
	private TitleBar titleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_item_info);

		initView();
		initData();
	}

	private void initData() {
		avatarOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_contact_picture)
				.showImageForEmptyUri(R.drawable.ic_contact_picture)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		picOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		String articleId = getIntent().getStringExtra("id");
		final String api = Constant.SERVER_URL + "/api/article/" + articleId + "?b_web=2";
		Handler handler = new HttpHandler(this) {
			@Override
			protected void succeed(JSONObject jObject) {
				dealResponse(jObject);
			}
		};
		new HttpConnectionUtils(handler).get(api);
	}

	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_home_item_info);
		tvName = (TextView) findViewById(R.id.tv_name_home_item_info);
		tvContent = (TextView) findViewById(R.id.tv_content_home_item_info);
		tvTime = (TextView) findViewById(R.id.tv_time_home_item_info);
		tvClickNum = (TextView) findViewById(R.id.tv_clicknum_home_item_info);
		ivHeader = (ImageView) findViewById(R.id.iv_header_home_item_info);
		flPics = (FlowLayout) findViewById(R.id.fl_pics_home_item_info);
		initTitleBar();
		ivHeader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(HomeItemInfoActivity.this,
						RoomDetailActivity.class);
				intent.putExtra("studioId", article.getStudioId());
				startActivity(intent);
			}
		});
		tvName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(HomeItemInfoActivity.this,
						RoomDetailActivity.class);
				intent.putExtra("studioId", article.getStudioId());
				startActivity(intent);
			}
		});
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
				finish();
			}
		});
	}

	protected void dealResponse(JSONObject jObject) {
		try {
			int state = jObject.getInt("stat");
			if (state == 1) {
				article = gson.fromJson(jObject.getJSONObject("article")
						.toString(), Article.class);
				tvName.setText(article.getStudioName());
				tvContent.setText(article.getContent());
				tvTime.setText(TimeUtil.getDateString(article.getCreateTime()));
				tvClickNum.setText("点击" + article.getViewNum());
				ImageLoader.getInstance().displayImage(
						article.getStudioAvatarUrl(), ivHeader, avatarOptions);
				int cnt = 0;
				for (String url : article.getImages()) {
					ImageView imageView = new ImageView(this);
					imageView.setScaleType(ScaleType.CENTER_CROP);
					MarginLayoutParams lp = new MarginLayoutParams(130, 130);
					lp.setMargins(5, 5, 5, 5);
					ImageLoader.getInstance().displayImage(url, imageView,
							picOptions);
					flPics.addView(imageView, lp);
					final int index = cnt;
					cnt++;
					imageView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Intent intent = new Intent(
									HomeItemInfoActivity.this,
									LargeImageActivity.class);
							intent.putStringArrayListExtra("images",
									(ArrayList<String>) article.getImages());
							intent.putExtra("index", index);
							startActivity(intent);
						}
					});
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
