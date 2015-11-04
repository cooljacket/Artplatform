package com.gexin.artplatform.question;

import java.util.ArrayList;
import java.util.List;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Problem;
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

public class SimpleProblemAdapter extends BaseAdapter {

	private Context mContext;
	private List<Problem> mList = new ArrayList<Problem>();
	private DisplayImageOptions picOptions;

	public SimpleProblemAdapter(Context mContext, List<Problem> mList) {
		super();
		this.mContext = mContext;
		this.mList = mList;
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
		Problem problem = mList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_simple_problem, null);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_content_item_simple_problem);
			holder.ivPic = (ImageView) convertView
					.findViewById(R.id.iv_pic_item_simple_problem);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvContent.setText(problem.getContent());
		if (problem.getImage() != null && !problem.getImage().isEmpty()) {
			holder.ivPic.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(problem.getImage(),
					holder.ivPic, picOptions);
		}else {
			holder.ivPic.setVisibility(View.GONE);
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView tvContent;
		ImageView ivPic;
	}

}
