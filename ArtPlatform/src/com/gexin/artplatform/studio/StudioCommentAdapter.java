package com.gexin.artplatform.studio;

import java.util.List;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.StudioComment;
import com.gexin.artplatform.utils.TimeUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StudioCommentAdapter extends BaseAdapter {

	private Context mContext;
	private List<StudioComment> mList;
	private DisplayImageOptions avatarOptions;

	public StudioCommentAdapter(Context mContext, List<StudioComment> mList) {
		super();
		this.mContext = mContext;
		this.mList = mList;
		avatarOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_contact_picture)
				.showImageForEmptyUri(R.drawable.ic_contact_picture)
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
		StudioComment comment = mList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_studio_comment, null);
			holder = new ViewHolder();
			holder.ivHeader = (ImageView) convertView
					.findViewById(R.id.iv_header_item_studio_comment);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tv_name_item_studio_comment);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_time_item_studio_comment);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_content_item_studio_comment);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvName.setText(comment.getFromUserName());
		holder.tvTime.setText(TimeUtil.getDateString(comment.getTimestamp()));
		holder.tvContent.setText(comment.getContent());
		ImageLoader.getInstance().displayImage(comment.getFromUserAvatarUrl(),
				holder.ivHeader, avatarOptions);
		return convertView;
	}

	private static class ViewHolder {
		ImageView ivHeader;
		TextView tvName;
		TextView tvTime;
		TextView tvContent;
	}

}
