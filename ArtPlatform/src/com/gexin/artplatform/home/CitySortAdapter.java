package com.gexin.artplatform.home;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.CityItem;

/**
 * 将联系人进行排序的适配器
 * 
 * @author xiaoxin
 * 
 */
public class CitySortAdapter extends BaseAdapter implements SectionIndexer {

	// private static final String TAG = "SortAdapter";
	// 要显示的数据信息
	private List<CityItem> mCitys = null;
	// 控件所在上下文
	private Context mContext;

	public CitySortAdapter(List<CityItem> mCitys, Context mContext) {
		// super();
		this.mCitys = mCitys;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		if (mCitys == null)
			return 0;
		return mCitys.size();
	}

	@Override
	public Object getItem(int position) {
		return mCitys.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder holder = null;
		final CityItem item = mCitys.get(position);
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.city_list_item, null);
			holder.tvLetter = (TextView) view
					.findViewById(R.id.tv_catalog_city_item);
			holder.tvName = (TextView) view
					.findViewById(R.id.tv_name_city_item);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		int section = getSectionForPosition(position);

		if (position == getPositionForSection(section)) {
			holder.tvLetter.setVisibility(View.VISIBLE);
			holder.tvLetter.setText(item.getSortLetter());
		} else {
			holder.tvLetter.setVisibility(View.GONE);
		}

		holder.tvName.setText(mCitys.get(position).getName());
		return view;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public int getPositionForSection(int section) {
		try {
			for (int i = 0; i < getCount(); i++) {
				String sortStr = mCitys.get(i).getSortLetter();
				char firstChar = sortStr.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		int section = 0;
		try {
			section = mCitys.get(position).getSortLetter().charAt(0);
		} catch (Exception e) {
		}
		return section;
	}

	@Override
	public Object[] getSections() {

		return null;
	}

	private final static class ViewHolder {
		TextView tvLetter;
		TextView tvName;
	}

	public void updateData(List<CityItem> list) {
		this.mCitys = list;
		notifyDataSetChanged();
	}

}
