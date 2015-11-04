package com.gexin.artplatform.studio;

import java.util.List;

import com.gexin.artplatform.R;
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

public class RoomGalleryAdapter extends BaseAdapter {

	private Context context;
	private List<String> mList;
	private DisplayImageOptions options;

	public RoomGalleryAdapter(Context context, List<String> mList) {
		this.context = context;
		this.mList = mList;
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_empty)
				.showImageForEmptyUri(R.drawable.ic_empty)
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
		String url = mList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.room_gallery_item, null);
		}
		
		ImageView ivPic = (ImageView) convertView
				.findViewById(R.id.iv_room_gallery_item);
		android.widget.AbsListView.LayoutParams params = new android.widget.AbsListView.LayoutParams(
				210, 210);
		ivPic.setLayoutParams(params);
		ImageLoader.getInstance().displayImage(url, ivPic, options);
		return convertView;
	}

}
