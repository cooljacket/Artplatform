package com.gexin.artplatform.mine;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.gexin.artplatform.R;
import com.gexin.artplatform.view.TitleBar;

public class MySubscribeActivity extends Activity {
	
	private LinearLayout llBack;
	private TitleBar titleBar;
	private int type = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_subscribe);
		type = getIntent().getIntExtra("type", 0);
		initTitleBar();
	}

	private void initTitleBar() {
		titleBar = (TitleBar) findViewById(R.id.tb_mysuscribe);
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
		if(type==1){
			titleBar.setTitle("订阅");
		}
	}
}
