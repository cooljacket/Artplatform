package com.gexin.artplatform.discover;

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
import com.gexin.artplatform.bean.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FindFriendAdapter extends BaseAdapter {

	private Context mContext;
	private List<User> mList;
	private DisplayImageOptions avatarOptions;

	public FindFriendAdapter(Context mContext, List<User> mList) {
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
		User user = mList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.find_friend_item, null);
			holder = new ViewHolder();
			holder.ivHeader = (ImageView) convertView
					.findViewById(R.id.iv_header_find_friend_item);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tv_name_find_friend_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvName.setText(user.getName());
		ImageLoader.getInstance().displayImage(user.getAvatarUrl(),
				holder.ivHeader, avatarOptions);
		return convertView;
	}

	private static class ViewHolder {
		ImageView ivHeader;
		TextView tvName;
	}

}
