package com.gexin.artplatform;

import java.util.List;

import com.gexin.artplatform.utils.SPUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

public class ViewPagerAdapter extends PagerAdapter {
	
	private List<View> views;
	private Context context;
	
	public ViewPagerAdapter(List<View> views, Context context) {
		// TODO Auto-generated constructor stub
		this.views = views;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (views != null) {
			return views.size();
		}
		return 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return (arg0 == arg1);
	}
	
	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		((ViewPager)container).removeView(views.get(position));
	}
	
	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated method stub
		((ViewPager)container).addView(views.get(position), 0);
		if (position == views.size()-1) {
			container.findViewById(R.id.btn_guide).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					context.startActivity(new Intent(context, MainActivity.class));
					SPUtil.put(context, "isFirst", false);
					((Activity)context).finish();
				}
			});
		}
		return views.get(position);
	}

}
