package com.gexin.artplatform.utils;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gexin.artplatform.R;


public class ShareAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<Item> list;
	private LayoutInflater inflater;
	
	public ShareAdapter(Context context,ArrayList<Item> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.popwindowlayout_item,null);
			holder = new ViewHolder();
			holder.image = (ImageView)convertView.findViewById(R.id.umeng_socialize_shareboard_image);
			holder.name = (TextView)convertView.findViewById(R.id.umeng_socialize_shareboard_pltform_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.image.setImageResource(list.get(position).getImgId());
		holder.name.setText(list.get(position).getName());
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView image;
		TextView name;
	}
	
}

 class Item {
	private int imgId;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getImgId() {
		return imgId;
	}
	public void setImgId(int imgId) {
		this.imgId = imgId;
	}
}
