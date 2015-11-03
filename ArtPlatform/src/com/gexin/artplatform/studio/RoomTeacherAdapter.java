package com.gexin.artplatform.studio;

import java.util.List;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Teacher;
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

public class RoomTeacherAdapter extends BaseAdapter {

	private Context context;
	private List<Teacher> mList;
	private DisplayImageOptions avatarOptions;

	public RoomTeacherAdapter(Context context, List<Teacher> mList) {
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
		Teacher teacher = mList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.room_teacher_item, null);
			holder = new ViewHolder();
			holder.ivHeader = (ImageView) convertView
					.findViewById(R.id.iv_header_teacher_item);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tv_name_teacher_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvName.setText(teacher.getName());
		ImageLoader.getInstance().displayImage(teacher.getAvatarUrl(),
				holder.ivHeader, avatarOptions);
		return convertView;
	}

	private static class ViewHolder {
		ImageView ivHeader;
		TextView tvName;
	}

}
