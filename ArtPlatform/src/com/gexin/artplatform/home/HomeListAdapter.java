package com.gexin.artplatform.home;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Article;
import com.gexin.artplatform.dlog.DLog;
import com.gexin.artplatform.largeImg.LargeImageActivity;
import com.gexin.artplatform.studio.RoomDetailActivity;
import com.gexin.artplatform.utils.DensityUtil;
import com.gexin.artplatform.utils.TimeUtil;
import com.gexin.artplatform.view.FlowLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeListAdapter extends BaseAdapter {

	private List<Article> mList;
	private Context mContext;
	private DisplayImageOptions avatarOptions;
	private DisplayImageOptions picOptions;
	
	private final int FIRST_HEAD = 0;
	private final int CONTENT_ITEM = 1;

	public HomeListAdapter(List<Article> mList, Context mContext) {
		super();
		this.mList = mList;
		this.mContext = mContext;
		avatarOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_menu_home)
				.showImageForEmptyUri(R.drawable.ic_menu_home)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
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
	
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if (position == 0) {
			return FIRST_HEAD;
		} else {
			return CONTENT_ITEM;
		}
	}
	
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (getItemViewType(position) == FIRST_HEAD) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.home_list_item_head, null);
			return convertView;
		}
		ViewHolder holder = null;
		final Article article = mList.get(position-1);
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
			holder.llContent = (LinearLayout) convertView.findViewById(R.id.ll_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvClickNum.setText("点击" + article.getViewNum());
		holder.tvContent.setText((article.getContent()));
		holder.tvName.setText(article.getStudioName());
		holder.tvTime.setText(TimeUtil.getDateString(article.getCreateTime()));
		holder.tvTitle.setText(article.getTitle());
		holder.ivHeader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext, RoomDetailActivity.class);
				intent.putExtra("studioId", article.getStudioId());
				mContext.startActivity(intent);
			}
		});
		holder.tvName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext, RoomDetailActivity.class);
				intent.putExtra("studioId", article.getStudioId());
				mContext.startActivity(intent);
			}
		});
		holder.llContent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,
						HomeItemInfoActivity.class);
				intent.putExtra("id", article.getArticleId());
				mContext.startActivity(intent);
			}
		});
		ImageLoader.getInstance().displayImage(article.getStudioAvatarUrl(),
				holder.ivHeader, avatarOptions);
		holder.flPics.removeAllViews();
		int cnt = 0;
		
		// 一行显示三张图片
		int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
		int magin = 5;
		int imageWidth = (screenWidth - magin * 6 - DensityUtil.dip2px(mContext, 50)) / 3;
		int imageHeight = imageWidth;
		for (String url : article.getImages()) {
//			DLog.i("before home url", "=================>" + url);
			url = url.replaceFirst("oss", "img");
//			url += "@80w_80h_50Q_1x.jpg";
			url += "@80h_50Q_1x.jpg";
//			DLog.i("after home url", "=================>" + url);
			ImageView imageView = new ImageView(mContext);
//			imageView.setMaxHeight(imageHeight);
//			imageView.setMaxWidth(imageWidth);
			imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			
			MarginLayoutParams lp = new MarginLayoutParams(
					imageWidth, imageHeight);
			lp.setMargins(magin, magin, magin, magin);
			
			
			holder.flPics.addView(imageView, lp);
			ImageLoader.getInstance().displayImage(url, imageView, picOptions);
			final int index = cnt;
			cnt++;
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
		LinearLayout llContent;
	}

}
