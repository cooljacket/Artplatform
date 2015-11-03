package com.gexin.artplatform.discover;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Classification;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DiscoverGridAdapter extends BaseAdapter {

	private List<Classification> mList;
	private Context mContext;

	public DiscoverGridAdapter(Context context, List<Classification> list) {
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
		Classification classification = mList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.discover_item, null);
			holder = new ViewHolder();
			holder.ivIcon = (ImageView) convertView
					.findViewById(R.id.iv_discover_item);
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.tv_discover_item);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		String title = classification.getName();
		String url = classification.getIcon();
		holder.tvTitle.setText(title);
		ImageLoader.getInstance().displayImage(url, holder.ivIcon);
		return convertView;
	}

	private static class ViewHolder {
		ImageView ivIcon;
		TextView tvTitle;
	}
}
