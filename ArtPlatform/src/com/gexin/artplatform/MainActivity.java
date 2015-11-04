package com.gexin.artplatform;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.discover.DiscoverFragment;
import com.gexin.artplatform.home.HomeFragment;
import com.gexin.artplatform.mine.MineFragment;
import com.gexin.artplatform.question.QuestionFragment;

public class MainActivity extends FragmentActivity implements
		OnPageChangeListener, OnClickListener {

	private static final String TAG = "MainActivity";
	
	private ViewPager mViewPager;
	private List<Fragment> mTabs = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;
	private HomeFragment homeFragment;
	private QuestionFragment questionFragment;
	private DiscoverFragment discoverFragment;
	private MineFragment mineFragment;

	private List<TextView> tvIndicators = new ArrayList<TextView>();
	private List<ImageView> ivIndicators = new ArrayList<ImageView>();
	private List<LinearLayout> llIndicators = new ArrayList<LinearLayout>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		getActionBar().setDisplayShowHomeEnabled(false);

		initView();
		initDatas();
		mViewPager.setAdapter(mAdapter);
		initEvent();

	}

	/**
	 * 初始化所有事件
	 */
	private void initEvent() {

		mViewPager.setOnPageChangeListener(this);

	}

	private void initDatas() {
		homeFragment = new HomeFragment();
		mTabs.add(homeFragment);
		questionFragment = new QuestionFragment();
		mTabs.add(questionFragment);
		discoverFragment = new DiscoverFragment();
		mTabs.add(discoverFragment);
		mineFragment = new MineFragment();
		mTabs.add(mineFragment);

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()){

			@Override
			public Fragment getItem(int arg0) {
				return mTabs.get(arg0);
			}

			@Override
			public int getCount() {
				return mTabs.size();
			}
		};
		
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		mViewPager.setOffscreenPageLimit(3);

		LinearLayout llOne = (LinearLayout) findViewById(R.id.ll_indicator_one_main);
		LinearLayout llTwo = (LinearLayout) findViewById(R.id.ll_indicator_two_main);
		LinearLayout llThree = (LinearLayout) findViewById(R.id.ll_indicator_three_main);
		LinearLayout llFour = (LinearLayout) findViewById(R.id.ll_indicator_four_main);
		TextView tvOne = (TextView) findViewById(R.id.tv_indicator_icon_one_main);
		TextView tvTwo = (TextView) findViewById(R.id.tv_indicator_icon_two_main);
		TextView tvThree = (TextView) findViewById(R.id.tv_indicator_icon_three_main);
		TextView tvFour = (TextView) findViewById(R.id.tv_indicator_icon_four_main);
		ImageView ivOne = (ImageView) findViewById(R.id.iv_indicator_icon_one_main);
		ImageView ivTwo = (ImageView) findViewById(R.id.iv_indicator_icon_two_main);
		ImageView ivThree = (ImageView) findViewById(R.id.iv_indicator_icon_three_main);
		ImageView ivFour = (ImageView) findViewById(R.id.iv_indicator_icon_four_main);
		llIndicators.add(llOne);
		llIndicators.add(llTwo);
		llIndicators.add(llThree);
		llIndicators.add(llFour);
		tvIndicators.add(tvOne);
		tvIndicators.add(tvTwo);
		tvIndicators.add(tvThree);
		tvIndicators.add(tvFour);
		ivIndicators.add(ivOne);
		ivIndicators.add(ivTwo);
		ivIndicators.add(ivThree);
		ivIndicators.add(ivFour);

		llOne.setOnClickListener(this);
		llTwo.setOnClickListener(this);
		llThree.setOnClickListener(this);
		llFour.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_indicator_one_main:
			mViewPager.setCurrentItem(0, false);
			setIndicators(0);
			break;
		case R.id.ll_indicator_two_main:
			mViewPager.setCurrentItem(1, false);
			setIndicators(1);
			break;
		case R.id.ll_indicator_three_main:
			mViewPager.setCurrentItem(2, false);
			setIndicators(2);
			break;
		case R.id.ll_indicator_four_main:
			mViewPager.setCurrentItem(3, false);
			setIndicators(3);
			break;
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onPageSelected(int position) {
		setIndicators(position);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub

	}
	
	private void setIndicators(int pos){
		switch (pos) {
		case 0:
			ivIndicators.get(0).setImageResource(R.drawable.home_icon_2);
			tvIndicators.get(0).setTextColor(Color.parseColor("#cdfe6060"));
			ivIndicators.get(1).setImageResource(R.drawable.question_icon_1);
			tvIndicators.get(1).setTextColor(Color.parseColor("#cd504f4f"));
			ivIndicators.get(2).setImageResource(R.drawable.discover_icon_1);
			tvIndicators.get(2).setTextColor(Color.parseColor("#cd504f4f"));
			ivIndicators.get(3).setImageResource(R.drawable.me_icon_1);
			tvIndicators.get(3).setTextColor(Color.parseColor("#cd504f4f"));
			break;
		case 1:
			ivIndicators.get(0).setImageResource(R.drawable.home_icon_1);
			tvIndicators.get(0).setTextColor(Color.parseColor("#cd504f4f"));
			ivIndicators.get(1).setImageResource(R.drawable.question_icon_2);
			tvIndicators.get(1).setTextColor(Color.parseColor("#cdfe6060"));
			ivIndicators.get(2).setImageResource(R.drawable.discover_icon_1);
			tvIndicators.get(2).setTextColor(Color.parseColor("#cd504f4f"));
			ivIndicators.get(3).setImageResource(R.drawable.me_icon_1);
			tvIndicators.get(3).setTextColor(Color.parseColor("#cd504f4f"));
			break;
		case 2:
			ivIndicators.get(0).setImageResource(R.drawable.home_icon_1);
			tvIndicators.get(0).setTextColor(Color.parseColor("#cd504f4f"));
			ivIndicators.get(1).setImageResource(R.drawable.question_icon_1);
			tvIndicators.get(1).setTextColor(Color.parseColor("#cd504f4f"));
			ivIndicators.get(2).setImageResource(R.drawable.discover_icon_2);
			tvIndicators.get(2).setTextColor(Color.parseColor("#cdfe6060"));
			ivIndicators.get(3).setImageResource(R.drawable.me_icon_1);
			tvIndicators.get(3).setTextColor(Color.parseColor("#cd504f4f"));
			break;
		case 3:
			ivIndicators.get(0).setImageResource(R.drawable.home_icon_1);
			tvIndicators.get(0).setTextColor(Color.parseColor("#cd504f4f"));
			ivIndicators.get(1).setImageResource(R.drawable.question_icon_1);
			tvIndicators.get(1).setTextColor(Color.parseColor("#cd504f4f"));
			ivIndicators.get(2).setImageResource(R.drawable.discover_icon_1);
			tvIndicators.get(2).setTextColor(Color.parseColor("#cd504f4f"));
			ivIndicators.get(3).setImageResource(R.drawable.me_icon_2);
			tvIndicators.get(3).setTextColor(Color.parseColor("#cdfe6060"));
			break;

		default:
			break;
		}
	}
	
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 实现按两下返回键退出功能
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				moveTaskToBack(false);
				finish();

			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
