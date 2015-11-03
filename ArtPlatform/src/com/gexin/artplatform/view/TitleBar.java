package com.gexin.artplatform.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gexin.artplatform.R;

public class TitleBar extends RelativeLayout {

	private static final String TAG = "TitleBar";

	private TextView tvTitle;
	private RelativeLayout leftView, rightView;
	private String titleText;
	private int infoTextColor;
	private float infoTextSize;

	// private TitleBarListener listener;
	//
	// public interface TitleBarListener {
	// void leftClick();
	//
	// void rightClick();
	// }

	public TitleBar(Context context) {
		this(context, null);
	}

	public TitleBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAttr(context, attrs);
		initView(context);
	}

	public String getTitle() {
		return titleText;
	}

	public void setTitle(String titleText) {
		this.titleText = titleText;
		tvTitle.setText(titleText);
	}

	public void setLeftView(View view) {
		leftView.removeAllViews();
		LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		leftView.addView(view, params);
	}

	public void setLeftView(View view, LayoutParams params) {
		leftView.removeAllViews();
		leftView.addView(view, params);
	}

	public void setRightView(View view) {
		rightView.removeAllViews();
		LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		rightView.addView(view, params);
	}

	public void setRightView(View view, LayoutParams params) {
		rightView.removeAllViews();
		rightView.addView(view, params);
	}

	// public void setTitleBarListener(TitleBarListener listener) {
	// this.listener = listener;
	// }

	private void initAttr(Context context, AttributeSet attrs) {
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.Titlebar);
		titleText = ta.getString(R.styleable.Titlebar_titleText);
		infoTextColor = ta.getColor(R.styleable.Titlebar_titleColor, 0);
		infoTextSize = ta.getDimension(R.styleable.Titlebar_titleSize, 0);
		infoTextSize = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, infoTextSize, getResources()
						.getDisplayMetrics());
		ta.recycle();
	}

	private void initView(Context context) {
		Log.v(TAG, "initView");
		setGravity(Gravity.CENTER_VERTICAL);
		tvTitle = new TextView(context);
		tvTitle.setText(titleText);
		tvTitle.setTextSize(23);
		tvTitle.setTextColor(infoTextColor);

		leftView = new RelativeLayout(context);
		rightView = new RelativeLayout(context);

		LayoutParams titleParams = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		titleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		addView(tvTitle, titleParams);

		LayoutParams leftParams = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		leftParams.addRule(RelativeLayout.CENTER_VERTICAL);
		addView(leftView, leftParams);

		LayoutParams rightParams = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rightParams.addRule(RelativeLayout.CENTER_VERTICAL);
		addView(rightView, rightParams);

		// leftView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// Log.v(TAG, "leftClick");
		// listener.leftClick();
		// }
		// });
		//
		// rightView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// Log.v(TAG, "rightClick");
		// listener.rightClick();
		// }
		// });
	}

}
