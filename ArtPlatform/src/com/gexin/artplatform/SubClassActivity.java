package com.gexin.artplatform;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.gexin.artplatform.bean.Classification;
import com.gexin.artplatform.bean.ImageItem;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.discover.DiscoverGridAdapter;
import com.gexin.artplatform.discover.DiscoverImageGridAdapter;
import com.gexin.artplatform.discover.FindStudioActivity;
import com.gexin.artplatform.largeImg.LargeImageActivity;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.HttpHandler;
import com.gexin.artplatform.view.TitleBar;
import com.google.gson.Gson;

public class SubClassActivity extends Activity {

	private static final int FIND_STUDIO_REQUEST = 0;
	private List<Classification> mSubClassList = new ArrayList<Classification>();
	private List<ImageItem> mImageList = new ArrayList<ImageItem>();
	private List<String> mImageUrlList = new ArrayList<String>();
	private String name, classId;
	private DiscoverGridAdapter discoverGridAdapter;
	private DiscoverImageGridAdapter discoverImageGridAdapter;
	private Gson gson = new Gson();
	private String TAG = "SubClassActivity";
	private TitleBar titleBar;
	private GridView mGridView;
	private LinearLayout llBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub_class);

		name = getIntent().getStringExtra("name");
		classId = getIntent().getStringExtra("classId");

		initView();
		initData();

	}

	private void initData() {
		titleBar.setTitle(name);
		String url = Constant.SERVER_URL + "/api/classification/" + classId;
		Handler handler = new HttpHandler(this) {
			@Override
			protected void succeed(JSONObject jObject) {
				dealResponse(jObject);
			}
		};
		new HttpConnectionUtils(handler).get(url);
	}

	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_activity_subclass);
		mGridView = (GridView) findViewById(R.id.gv_activity_subclass);

		initTitleBar();

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case FIND_STUDIO_REQUEST:
			finish();
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	protected void dealResponse(JSONObject jObject) {
		try {
			int state = jObject.getInt("stat");
			if (state == 1) {
				Classification classification = gson.fromJson(jObject
						.getJSONObject("classification").toString(),
						Classification.class);
				if (classification.getType() !=1&&classification.getType()!=0) {
					mGridView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							Intent intent = new Intent(SubClassActivity.this,
									SubClassActivity.class);
							intent.putExtra("name", mSubClassList.get(arg2)
									.getName());
							intent.putExtra("classId", mSubClassList.get(arg2)
									.get_id());
							startActivity(intent);
						}
					});
					mSubClassList.clear();
					mSubClassList.addAll(classification.getSubclass());
					Log.i(TAG, "没有图片只有sub:" + mSubClassList.size() + "");
					discoverGridAdapter = new DiscoverGridAdapter(this,
							mSubClassList);
					mGridView.setAdapter(discoverGridAdapter);
					mGridView.setNumColumns(2);
				} else if (classification.getType() == 1) {
					mImageList.clear();
					mImageList.addAll(classification.getImage());
					Log.i(TAG, "没有图片只有sub:" + mImageList.size() + "");
					for (int i = 0; i < mImageList.size(); i++)
						mImageUrlList.add(mImageList.get(i).getUrl());
					discoverImageGridAdapter = new DiscoverImageGridAdapter(
							this, mImageUrlList);
					mGridView.setAdapter(discoverImageGridAdapter);
					mGridView.setNumColumns(3);
					mGridView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// ArrayList<String> Imgs = new ArrayList<String>();
							// Imgs.add(mImageList.get(arg2).getUrl());
							Intent intent = new Intent(SubClassActivity.this,
									LargeImageActivity.class);
							intent.putStringArrayListExtra("images",
									(ArrayList<String>) mImageUrlList);
							intent.putExtra("index", arg2);
							startActivity(intent);
						}
					});
				} else if (classification.getType() == 4) {
					Intent intent = new Intent(SubClassActivity.this,
							FindStudioActivity.class);
					startActivityForResult(intent, FIND_STUDIO_REQUEST);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
