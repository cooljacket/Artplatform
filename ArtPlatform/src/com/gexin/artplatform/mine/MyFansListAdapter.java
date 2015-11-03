package com.gexin.artplatform.mine;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Fans;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.SPUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyFansListAdapter extends BaseAdapter {

	private Context context;
	private List<Fans> mList;
	private DisplayImageOptions avatarOptions;
	private String[] userType = { "学生", "老师", "画室" };
	private String ownerId = null;

	public MyFansListAdapter(Context context, List<Fans> mList) {
		this.context = context;
		this.mList = mList;
		avatarOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_contact_picture)
				.showImageForEmptyUri(R.drawable.ic_contact_picture)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		ownerId = (String) SPUtil.get(context, "userId", "");
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final Fans fans = mList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.fans_list_item, null);
			holder = new ViewHolder();
			holder.ivHeader = (ImageView) convertView
					.findViewById(R.id.iv_fans_avatar);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tv_fans_name);
			holder.tvInfo = (TextView) convertView
					.findViewById(R.id.tv_fans_info);
			holder.tvFollowTA = (TextView) convertView
					.findViewById(R.id.tv_fans_operation);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvName.setText(fans.getName());
		holder.tvInfo.setText(userType[fans.getuType()]);
		ImageLoader.getInstance().displayImage(fans.getAvatarUrl(),
				holder.ivHeader, avatarOptions);
		holder.tvFollowTA.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				postFollow(fans,position);
			}
		});
		if (fans.getRelation() == 1 || fans.getRelation() == 3) {
			holder.tvFollowTA.setText("取消关注");
			holder.tvFollowTA.setVisibility(View.VISIBLE);
		} else {
			holder.tvFollowTA.setText("关注TA");
			holder.tvFollowTA.setVisibility(View.VISIBLE);
		}
		if(fans.get_id()!=null&&fans.get_id().equals(ownerId)){
			holder.tvFollowTA.setVisibility(View.GONE);
		}
		if(fans.getUserId()!=null&&fans.getUserId().equals(ownerId)){
			holder.tvFollowTA.setVisibility(View.GONE);
		}
		return convertView;
	}

	@SuppressLint("HandlerLeak")
	protected void postFollow(final Fans fans, final int position) {
		String userId = (String) SPUtil.get(context, "userId", "");
		String followApi = Constant.SERVER_URL + "/api/user/" + userId
				+ "/follow";
		String followStudioApi = Constant.SERVER_URL + "/api/user/" + userId
				+ "/studio";
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					try {
						JSONObject jsonObject = new JSONObject((String) msg.obj);
						int state = jsonObject.getInt("stat");
						if (state == 1) {
							if(fans.getRelation()==1||fans.getRelation()==3){
								Toast.makeText(context, "取消关注成功", Toast.LENGTH_SHORT)
								.show();
								mList.get(position).setRelation(0);
							}else {
								Toast.makeText(context, "关注成功", Toast.LENGTH_SHORT)
								.show();
								mList.get(position).setRelation(1);
							}
							notifyDataSetChanged();
							Intent intent = new Intent();
							intent.setAction(MineFragment.ACTION_CHANGE_USERDATA);
							context.sendBroadcast(intent);
						} else {
							Toast.makeText(context, "关注失败", Toast.LENGTH_SHORT)
									.show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
						Toast.makeText(context, "关注失败", Toast.LENGTH_SHORT)
								.show();
					}
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		if(fans.getRelation()==1||fans.getRelation()==3){
			list.add(new BasicNameValuePair("follow", "-1"));
		}else {
			list.add(new BasicNameValuePair("follow", "1"));
		}
		Log.v("postFollow", fans.getuType()+"");
		if(fans.getuType()==2){
			if(fans.getRelation()==1||fans.getRelation()==3){
				String url = Constant.SERVER_URL + "/api/user/" + userId
						+ "/studio?studioId="+fans.get_id();
				new HttpConnectionUtils(handler).delete(url);
			}else{
				list.add(new BasicNameValuePair("studioId", fans.get_id()));
				new HttpConnectionUtils(handler).put(followStudioApi, list);
			}
			
		}else {
			if(fans.get_id()==null){
				list.add(new BasicNameValuePair("userId", fans.getUserId()));
			}else {
				list.add(new BasicNameValuePair("userId", fans.get_id()));
			}
			new HttpConnectionUtils(handler).post(followApi, list);
		}
	}

	private static class ViewHolder {
		ImageView ivHeader;
		TextView tvName;
		TextView tvInfo;
		TextView tvFollowTA;
	}
}
