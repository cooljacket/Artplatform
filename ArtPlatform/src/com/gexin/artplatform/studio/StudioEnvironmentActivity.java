package com.gexin.artplatform.studio;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.view.TitleBar;

public class StudioEnvironmentActivity extends FragmentActivity {

	private String studioId;
	private List<Fragment> mTabs = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;
	private StudioEnvironmentFragment studioEnvironmentFragment;
	private StudioCommentFragment studioCommentFragment;
	
	private ViewPager mViewPager;
	private LinearLayout llPics;
	private LinearLayout llComments;
	private View vLeftLine;
	private View vRightLine;
	private TextView tvPics;
	private TextView tvComments;
	private TitleBar titleBar;
	private LinearLayout llBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_studio_environment);
		
		studioId = getIntent().getStringExtra("studioId");
		initView();
		initData();
	}

	private void initData() {
		studioEnvironmentFragment = new StudioEnvironmentFragment();
		studioCommentFragment = new StudioCommentFragment();
		
		studioEnvironmentFragment.setStudioId(studioId);
		studioCommentFragment.setStudioId(studioId);
		mTabs.add(studioEnvironmentFragment);
		mTabs.add(studioCommentFragment);
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
				if(arg0==0){
					tvPics.setTextColor(Color.parseColor("#cdfe6060"));
					vLeftLine.setVisibility(View.VISIBLE);
					tvComments.setTextColor(Color.parseColor("#cd504f4f"));
					vRightLine.setVisibility(View.GONE);
				}else {
					tvPics.setTextColor(Color.parseColor("#cd504f4f"));
					vLeftLine.setVisibility(View.GONE);
					tvComments.setTextColor(Color.parseColor("#cdfe6060"));
					vRightLine.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.vp_activity_studio_environment);
		titleBar = (TitleBar) findViewById(R.id.tb_activity_studio_environment);
		llPics = (LinearLayout) findViewById(R.id.ll_pics_studio_environment);
		llComments = (LinearLayout) findViewById(R.id.ll_comments_studio_environment);
		tvPics = (TextView) findViewById(R.id.tv_pics_studio_environment);
		tvComments = (TextView) findViewById(R.id.tv_comments_studio_environment);
		vLeftLine = findViewById(R.id.v_leftline_studio_environment);
		vRightLine = findViewById(R.id.v_rightline_studio_environment);
		
		initTitleBar();
		llPics.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				tvPics.setTextColor(Color.parseColor("#cdfe6060"));
				vLeftLine.setVisibility(View.VISIBLE);
				tvComments.setTextColor(Color.parseColor("#cd504f4f"));
				vRightLine.setVisibility(View.GONE);
				mViewPager.setCurrentItem(0);
			}
		});
		llComments.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				tvPics.setTextColor(Color.parseColor("#cd504f4f"));
				vLeftLine.setVisibility(View.GONE);
				tvComments.setTextColor(Color.parseColor("#cdfe6060"));
				vRightLine.setVisibility(View.VISIBLE);
				mViewPager.setCurrentItem(1);
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
}
