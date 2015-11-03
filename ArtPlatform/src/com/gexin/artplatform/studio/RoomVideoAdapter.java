package com.gexin.artplatform.studio;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Video;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RoomVideoAdapter extends BaseAdapter {

	private Context context;
	private List<Video> mList;
	private DisplayImageOptions avatarOptions;

	public RoomVideoAdapter(Context context, List<Video> mList) {
		this.context = context;
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
		Video video = mList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.room_teacher_item, null);
			holder = new ViewHolder();
			holder.ivPic = (ImageView) convertView
					.findViewById(R.id.iv_header_teacher_item);
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.tv_name_teacher_item);
			holder.tvDescription = (TextView) convertView
					.findViewById(R.id.tv_intro_teacher_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvTitle.setText(video.getTitle());
		holder.tvDescription.setText(video.getDescription());
		ImageLoader.getInstance().displayImage(video.getImageUrl(),
				holder.ivPic, avatarOptions);
		return convertView;
	}

	private static class ViewHolder {
		ImageView ivPic;
		TextView tvTitle;
		TextView tvDescription;
	}

}
