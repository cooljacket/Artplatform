package com.gexin.artplatform;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GuideActivity extends Activity implements OnPageChangeListener {

	private ImageView[] dots;
	private ViewPager viewPager;
	private ViewPagerAdapter adapter;
	private List<View> views;
	private View viewOne, viewTwo, viewThree, viewFour;
	int currentIndex = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		initView();
	}
	
	private void initView() {
		LayoutInflater inflater = LayoutInflater.from(this);
		views = new ArrayList<View>();
		viewOne = inflater.inflate(R.layout.activity_guide_one, null);
		viewTwo = inflater.inflate(R.layout.activity_guide_two, null);
		viewThree = inflater.inflate(R.layout.activity_guide_three, null);
		viewFour = inflater.inflate(R.layout.activity_guide_four, null);
		views.add(viewOne);
		views.add(viewTwo);
		views.add(viewThree);
		views.add(viewFour);
		adapter = new ViewPagerAdapter(views, this);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll_guide_dots);
		dots = new ImageView[views.size()];
		
		for (int i = 0; i < views.size(); ++i) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setImageResource(R.drawable.dot_normal);
		}
		dots[currentIndex].setImageResource(R.drawable.dot_focused);
		
	}
	
	private void setCurrentDot(int position) {
		if (position < 0 || position > views.size() - 1
				|| currentIndex == position) {
			return;
		}

		dots[position].setImageResource(R.drawable.dot_focused);
		dots[currentIndex].setImageResource(R.drawable.dot_normal);

		currentIndex = position;
	}


	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		setCurrentDot(arg0);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		viewOne = null;
		viewTwo = null;
		viewThree = null;
		dots = null;
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
