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
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.SimpleStudio;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FindStudioAdapter extends BaseAdapter implements SectionIndexer {

	private Context mContext;
	private List<SimpleStudio> mList;
	private DisplayImageOptions avatarOptions;

	public FindStudioAdapter(Context mContext, List<SimpleStudio> mList) {
		super();
		this.mContext = mContext;
		this.mList = mList;
		avatarOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.home_icon_1)
				.showImageForEmptyUri(R.drawable.home_icon_1)
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
		SimpleStudio stuido = mList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_find_studio, null);
			holder = new ViewHolder();
			holder.ivHeader = (ImageView) convertView
					.findViewById(R.id.iv_header_item_find_studio);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tv_name_item_find_studio);
			holder.tvLetter = (TextView) convertView
					.findViewById(R.id.tv_catalog_item_find_studio);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int section = getSectionForPosition(position);

		if (position == getPositionForSection(section)) {
			holder.tvLetter.setVisibility(View.GONE);
			holder.tvLetter.setText(stuido.getSortLetter());
		} else {
			holder.tvLetter.setVisibility(View.GONE);
		}

		holder.tvName.setText(stuido.getName());
		ImageLoader.getInstance().displayImage(stuido.getAvatarUrl(),
				holder.ivHeader, avatarOptions);
		return convertView;
	}

	private static class ViewHolder {
		ImageView ivHeader;
		TextView tvName;
		TextView tvLetter;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public int getPositionForSection(int section) {
		try {
			for (int i = 0; i < getCount(); i++) {
				String sortStr = mList.get(i).getSortLetter();
				char firstChar = sortStr.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		int section = 0;
		try{
			section = mList.get(position).getSortLetter().charAt(0);
		}catch(Exception e){
			e.printStackTrace();
		}
		return section;
	}

	@Override
	public Object[] getSections() {
		return null;
	}
	
	public void updateData(List<SimpleStudio> list){
		mList = list;
		notifyDataSetChanged();
	}

}
