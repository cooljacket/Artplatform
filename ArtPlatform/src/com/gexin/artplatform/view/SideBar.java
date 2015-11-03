package com.gexin.artplatform.view;

import com.gexin.artplatform.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


/**
 * 自定义侧边栏视图
 * 
 * @author xiaoxin
 * 
 */
public class SideBar extends View {

	//private static final String TAG = "SideBar";
	// 字母表
	private static final String[] sAlphabet = { "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z" };
	// 选中的位置
	private int mChoosen = -1;
	// 绘图
	private Paint mPaint = new Paint();
	// 显示字母TextView
	private TextView mLetterShow;

	private onTouchLetterChangeListener mTouchLetterChangeListener;

	/**
	 * 触摸侧边字母的接口
	 * 
	 * @author xiaoxin
	 */
	public interface onTouchLetterChangeListener {
		public void onTouchLetterChange(String letter);
	}

	public SideBar(Context context) {
		this(context, null);
	}

	public SideBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void setLetterShow(TextView mLetterShow) {
		this.mLetterShow = mLetterShow;
	}

	public void setTouchLetterChangeListener(
			onTouchLetterChangeListener mTouchLetterChangeListener) {
		this.mTouchLetterChangeListener = mTouchLetterChangeListener;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 画布高度
		int height = getHeight();
		// 画布宽度
		int width = getWidth();
		// 单个字母高度
		int singleLetterHeight = height / sAlphabet.length;

		// 画出每个字母
		for (int i = 0; i < sAlphabet.length; i++) {
			// 设置画笔颜色
			mPaint.setColor(Color.rgb(33, 65, 98));
			// 设置字样
			mPaint.setTypeface(Typeface.DEFAULT_BOLD);
			// 设置抗锯齿
			mPaint.setAntiAlias(true);
			mPaint.setTextSize(20);

			// 字母被选中高亮
			if (i == mChoosen) {
				mPaint.setColor(Color.parseColor("#3399ff"));
				// 设置粗体
				mPaint.setFakeBoldText(true);
			}

			float xPos = width / 2 - mPaint.measureText(sAlphabet[i]) / 2;
			float yPos = singleLetterHeight * (i + 1);

			// 画出文本
			canvas.drawText(sAlphabet[i], xPos, yPos, mPaint);

			// 重置画笔
			mPaint.reset();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		
		final int action = event.getAction();
		// 点击的位置
		final float yPos = event.getY();
		// 上个选中的位置
		final int lastChoosen = mChoosen;
		final onTouchLetterChangeListener listener = mTouchLetterChangeListener;
		// 这次点击的字母索引
		final int index = (int) (yPos / getHeight() * sAlphabet.length);

		switch (action) {
		case MotionEvent.ACTION_UP:
			init();
			break;

		default:
			setBackgroundResource(R.drawable.sidebar_bg);
			if (lastChoosen != index) {
				if (index >= 0 && index < sAlphabet.length) {
					if (listener != null) {
						listener.onTouchLetterChange(sAlphabet[index]);
					}
					if (mLetterShow != null) {
						mLetterShow.setText(sAlphabet[index]);
						mLetterShow.setVisibility(View.VISIBLE);
					}

					mChoosen = index;
					invalidate();
				}
			}
			break;
		}
		return true;
	}

	/**
	 * 初始化sidebar
	 */
	@SuppressWarnings("deprecation")
	public void init(){
		setBackgroundDrawable(new ColorDrawable(0x00000000));
		mChoosen = -1;
		invalidate();
		if (mLetterShow != null) {
			mLetterShow.setVisibility(View.INVISIBLE);
		}

	}
}
