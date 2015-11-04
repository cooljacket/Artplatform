package com.gexin.artplatform.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自定义流式布局
 * 
 * @author xiaoxin 2015-4-9
 */
public class FlowLayout extends ViewGroup {

	private static final String TAG = "FlowLayout";

	public FlowLayout(Context context) {
		this(context, null);
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// mode EXACTLY
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

		// wrap_content mode AT_MOST
		int height = 0;
		int width = 0;
		// 记录每一行的宽度和高度
		int lineWidth = 0;
		int lineHeight = 0;
		// 得到内部元素的个数
		int cCount = getChildCount();

		for (int i = 0; i < cCount; i++) {
			View child = getChildAt(i);
			// 测量子View的宽度和高度
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			// 得到LayoutParams
			MarginLayoutParams lp = (MarginLayoutParams) child
					.getLayoutParams();
			// 子view占据的宽度
			int childWidth = child.getMeasuredWidth() + lp.leftMargin
					+ lp.rightMargin;
			// 子view占据的高度
			int childHeight = child.getMeasuredHeight() + lp.topMargin
					+ lp.bottomMargin;
			// 换行
			if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
				// 对比得到最大的宽度
				width = Math.max(width, lineWidth);
				// 重置lineWidth
				lineWidth = childWidth;
				// 记录行高
				height += lineHeight;
				lineHeight = childHeight;
			} else { // 未换行
				// 叠加行宽
				lineWidth += childWidth;
				// 取得最大行高
				lineHeight = Math.max(lineHeight, childHeight);
			}

			// 最后一个控件
			if (i == cCount - 1) {
				width = Math.max(lineWidth, width);
				height += lineHeight;
			}
		}

		Log.v(TAG, "sizeWidth:" + sizeWidth + ",width:" + width);
		Log.v(TAG, "sizeHeight:" + sizeHeight + ",height:" + height);
		setMeasuredDimension(
				//
				modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingLeft() + getPaddingRight(),
				modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop()+ getPaddingBottom()//
		);

//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	// 存储所有的View，按行存储
	private List<List<View>> mAllViews = new ArrayList<List<View>>();
	private List<Integer> mLineHeight = new ArrayList<Integer>();

	@SuppressLint("DrawAllocation") @Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		mAllViews.clear();
		mLineHeight.clear();
		// 当前ViewGroup的宽度
		int width = getWidth();
		int lineWidth = 0;
		int lineHeight = 0;

		List<View> lineViews = new ArrayList<View>();
		int cCount = getChildCount();

		for (int i = 0; i < cCount; i++) {
			View child = getChildAt(i);
			MarginLayoutParams lp = (MarginLayoutParams) child
					.getLayoutParams();

			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();

			// 如果需要换行
			if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width
					- getPaddingLeft() - getPaddingRight()) {
				// 记录LineHeight
				mLineHeight.add(lineHeight);
				// 记录当前行的Views
				mAllViews.add(lineViews);

				// 重置我们的行宽和行高
				lineWidth = 0;
				lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
				// 重置我们的View集合
				lineViews = new ArrayList<View>();
			}
			lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
			lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
					+ lp.bottomMargin);
			lineViews.add(child);
			
		}
		// 处理最后一行
		mLineHeight.add(lineHeight);
		mAllViews.add(lineViews);

		// 设置子View的位置

		int left = getPaddingLeft();
		int top = getPaddingTop();

		// 行数
		int lineNum = mAllViews.size();

		for (int i = 0; i < lineNum; i++)
		{
			// 当前行的所有的View
			lineViews = mAllViews.get(i);
			lineHeight = mLineHeight.get(i);

			for (int j = 0; j < lineViews.size(); j++)
			{
				View child = lineViews.get(j);
				// 判断child的状态
				if (child.getVisibility() == View.GONE)
				{
					continue;
				}

				MarginLayoutParams lp = (MarginLayoutParams) child
						.getLayoutParams();

				int lc = left + lp.leftMargin;
				int tc = top + lp.topMargin;
				int rc = lc + child.getMeasuredWidth();
				int bc = tc + child.getMeasuredHeight();

				// 为子View进行布局
				child.layout(lc, tc, rc, bc);

				left += child.getMeasuredWidth() + lp.leftMargin
						+ lp.rightMargin;
			}
			left = getPaddingLeft() ; 
			top += lineHeight ; 
		}
	}

	// 与当前ViewGroup对应的LayoutParams
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}
}
