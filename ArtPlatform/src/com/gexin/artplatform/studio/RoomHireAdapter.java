package com.gexin.artplatform.studio;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Recruitment;

public class RoomHireAdapter extends BaseAdapter {

	private Context context;
	private List<Recruitment> mList;

	public RoomHireAdapter(Context context, List<Recruitment> mList) {
		this.context = context;
		this.mList = mList;
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
		Recruitment recruitment = mList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.room_hire_item, null);
			holder = new ViewHolder();
			holder.tvDescription = (TextView) convertView
					.findViewById(R.id.tv_require_room_hire);
			holder.tvSalary = (TextView) convertView
					.findViewById(R.id.tv_salary_room_hire);
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.tv_job_room_hire);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvTitle.setText(recruitment.getTitle());
		holder.tvSalary.setText(recruitment.getSalary());
		holder.tvDescription.setText(recruitment.getDescription());

		return convertView;
	}

	private static class ViewHolder {
		TextView tvTitle;
		TextView tvSalary;
		TextView tvDescription;
	}

}
