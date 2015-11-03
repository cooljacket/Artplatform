package com.gexin.artplatform.discover;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gexin.artplatform.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DiscoverImageGridAdapter extends BaseAdapter {
	private List<String> mList;
	private Context mContext;

	public DiscoverImageGridAdapter(Context context, List<String> list) {
		this.mContext = context;
		this.mList = list;
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

	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		String workUrl = mList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.discover_image_item, null);
			holder = new ViewHolder();
			holder.ivIcon = (ImageView) convertView
					.findViewById(R.id.iv_discover_image_item);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(workUrl, holder.ivIcon);
		return convertView;
	}

	private static class ViewHolder {
		ImageView ivIcon;
	}
}
