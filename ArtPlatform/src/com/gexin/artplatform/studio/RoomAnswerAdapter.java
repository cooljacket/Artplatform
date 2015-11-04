package com.gexin.artplatform.studio;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Article;
import com.gexin.artplatform.largeImg.LargeImageActivity;
import com.gexin.artplatform.utils.TimeUtil;
import com.gexin.artplatform.view.FlowLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RoomAnswerAdapter extends BaseAdapter {

	private List<Article> mList;
	private Context mContext;
	private DisplayImageOptions picOptions;

	public RoomAnswerAdapter(List<Article> mList, Context mContext) {
		super();
		this.mList = mList;
		this.mContext = mContext;
		picOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final Article article = mList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.home_list_item, null);
			holder.tvClickNum = (TextView) convertView
					.findViewById(R.id.tv_clicknum_home_item);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_content_home_item);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tv_name_home_item);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_time_home_item);
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.tv_title_home_item);
			holder.flPics = (FlowLayout) convertView
					.findViewById(R.id.fl_pics_home_item);
			holder.ivHeader = (ImageView) convertView
					.findViewById(R.id.iv_header_home_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvClickNum.setText("点击" + article.getViewNum());
		holder.tvContent.setText((article.getContent()));
		holder.tvName.setVisibility(View.GONE);
		holder.tvTime.setVisibility(View.GONE);
		holder.ivHeader.setVisibility(View.GONE);
		holder.tvTime.setText(TimeUtil.getDateString(article.getCreateTime()));
		holder.tvTitle.setText(article.getTitle());
		holder.tvName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext, RoomDetailActivity.class);
				intent.putExtra("studioId", article.getStudioId());
				mContext.startActivity(intent);
			}
		});
		holder.flPics.removeAllViews();
		int cnt = 0;
		for (String url : article.getImages()) {
			ImageView imageView = new ImageView(mContext);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			MarginLayoutParams lp = new MarginLayoutParams(
					130, 130);
			lp.setMargins(5, 5, 5, 5);
			ImageLoader.getInstance().displayImage(url, imageView, picOptions);
			holder.flPics.addView(imageView, lp);
			final int index = cnt;
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(mContext,
							LargeImageActivity.class);
					intent.putStringArrayListExtra("images",
							(ArrayList<String>) article.getImages());
					intent.putExtra("index", index);
					mContext.startActivity(intent);
				}
			});
		}

		return convertView;
	}

	private static class ViewHolder {
		TextView tvName;
		TextView tvContent;
		TextView tvClickNum;
		TextView tvTime;
		TextView tvTitle;
		FlowLayout flPics;
		ImageView ivHeader;
	}
}
