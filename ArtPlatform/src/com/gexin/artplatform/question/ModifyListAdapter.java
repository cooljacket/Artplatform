package com.gexin.artplatform.question;

import java.util.List;
import java.util.Map;

import com.gexin.artplatform.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ModifyListAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> mList;

	public ModifyListAdapter(Context mContext, List<Map<String, Object>> mList) {
		this.mContext = mContext;
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
		Map<String, Object> map = mList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.user_modify_item, null);
			holder.tvValue = (TextView) convertView
					.findViewById(R.id.tv_value_user_modify_item);
			holder.ivSelect = (ImageView) convertView
					.findViewById(R.id.iv_select_user_modify_item);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvValue.setText((String)map.get("value"));
		boolean flag = (Boolean) map.get("select");
		if(flag){
			holder.ivSelect.setImageResource(R.drawable.check2);
		}else {
			holder.ivSelect.setImageResource(R.drawable.check1);
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView tvValue;
		ImageView ivSelect;
	}

}
