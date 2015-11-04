package com.gexin.artplatform.question;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Problem;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.friends.ViewOtherUserActivity;
import com.gexin.artplatform.largeImg.LargeImageActivity;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.utils.TimeUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class QuestionAdapter extends BaseAdapter {

	private static final String TAG = "QuestionAdapter";
	private Context mContext;
	private List<Problem> mList;
	private DisplayImageOptions avatarOptions;
	private DisplayImageOptions picOptions;

	public QuestionAdapter(Context mContext, List<Problem> mList) {
		super();
		this.mContext = mContext;
		this.mList = mList;
		avatarOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_contact_picture)
				.showImageForEmptyUri(R.drawable.ic_contact_picture)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		picOptions = new DisplayImageOptions.Builder()
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final Problem item = mList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.question_item, null);
			holder = new ViewHolder();
			holder.ivHeader = (ImageView) convertView
					.findViewById(R.id.iv_header_question_item);
			holder.ivPic = (ImageView) convertView
					.findViewById(R.id.iv_pic_question_item);
			holder.ivZan = (ImageView) convertView
					.findViewById(R.id.iv_zan_question_item);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_content_question_item);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tv_name_question_item);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_time_question_item);
			holder.tvType = (TextView) convertView
					.findViewById(R.id.tv_type_question_item);
			holder.tvZan = (TextView) convertView
					.findViewById(R.id.tv_zan_question_item);
			holder.tvAnsNum = (TextView) convertView
					.findViewById(R.id.tv_ansnum_question_item);
			holder.llZan = (LinearLayout) convertView
					.findViewById(R.id.ll_zan_question_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int ansNum = item.getAnswerNum();
		int commentNum = item.getCommentNum();
		int zan = item.getZan();
		final String id = item.get_id();
		String name = item.getName();
		String time = TimeUtil.getTimeDetailString(item.getTimestamp());
		String askToName = item.getAskToName();
		String type = "";
		String avatarUrl = item.getAvatarUrl();
		if (item.getTag() != null && item.getTag().size() != 0) {
			String tmpStr = item.getTag().toString();
			try {
				type = tmpStr.substring(1, tmpStr.length() - 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			type = "未设置";
		}
		String content = item.getContent();
		final String imageUrl = item.getImage();
		holder.tvName.setText(name);
		holder.tvTime.setText(time);
		holder.tvType.setText(type);
		if(askToName==null||askToName.isEmpty()){
			holder.tvContent.setText(content);
		}else {
			holder.tvContent.setText("@"+askToName+" "+content);
		}
		holder.tvZan.setText("" + zan);
		holder.tvAnsNum.setText("" + commentNum);

		if (item.getIsZan() == 1) {
			holder.ivZan.setImageResource(R.drawable.zan_icon_2);
		} else {
			holder.ivZan.setImageResource(R.drawable.zan_icon_1);
		}
		final ImageView tmpIvZan = holder.ivZan;
		final TextView tmpTvZan = holder.tvZan;
		holder.llZan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.v(TAG, "ZanClick");
				postZan(id, item.getIsZan(), position, tmpIvZan, tmpTvZan);
			}
		});
		ImageLoader.getInstance().displayImage(avatarUrl, holder.ivHeader,
				avatarOptions);
		if (imageUrl != null && !imageUrl.isEmpty()) {
			holder.ivPic.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(imageUrl, holder.ivPic,
					picOptions);
		} else {
			holder.ivPic.setVisibility(View.GONE);
		}
		holder.ivPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (imageUrl != null && !imageUrl.isEmpty()) {
					Intent intent = new Intent(mContext,
							LargeImageActivity.class);
					List<String> images = new ArrayList<String>();
					images.add(imageUrl);
					intent.putStringArrayListExtra("images",
							(ArrayList<String>) images);
					mContext.startActivity(intent);
				}
			}
		});
		holder.ivHeader.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showUserInfo(item.getUserId());
			}
		});
		holder.tvName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showUserInfo(item.getUserId());
			}
		});

		return convertView;
	}

	protected void showUserInfo(String userId) {
		Intent intent = new Intent(mContext,ViewOtherUserActivity.class);
		intent.putExtra("userId", userId);
		mContext.startActivity(intent);
	}

	@SuppressLint("HandlerLeak")
	private void postZan(String id, final int isZan, final int position,
			final ImageView tmpIvZan, final TextView tmpTvZan) {
		String zanAPI = Constant.SERVER_URL + "/api/problem/" + id + "/zan";
		String userId = (String) SPUtil.get(mContext, "userId", "");
		if (userId.isEmpty()) {
			Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
			return;
		}
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					Log.v(TAG, (String) msg.obj);
					try {
						JSONObject jsonObject = new JSONObject((String) msg.obj);
						int state = jsonObject.getInt("stat");
						if (state == 1) {
							int zanNum = Integer.parseInt(tmpTvZan.getText()
									.toString());
							if (isZan == 1) {
								zanNum--;
								mList.get(position).setIsZan(0);
								mList.get(position).setZan(zanNum);
								Toast.makeText(mContext, "取消赞成功",
										Toast.LENGTH_SHORT).show();
								tmpIvZan.setImageResource(R.drawable.zan_icon_1);
								tmpTvZan.setText(zanNum + "");
							} else {
								zanNum++;
								mList.get(position).setIsZan(1);
								mList.get(position).setZan(zanNum);
								Toast.makeText(mContext, "赞成功",
										Toast.LENGTH_SHORT).show();
								tmpIvZan.setImageResource(R.drawable.zan_icon_2);
								tmpTvZan.setText(zanNum + "");
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("userId", userId));
		if (isZan == 1) {
			list.add(new BasicNameValuePair("zan", "-1"));
		} else {
			list.add(new BasicNameValuePair("zan", "1"));
		}
		new HttpConnectionUtils(handler).put(zanAPI, list);
	}
	
	public void updateData(List<Problem> list){
		this.mList = list;
		notifyDataSetChanged();
	}

	private final class ViewHolder {
		ImageView ivHeader;
		ImageView ivPic;
		ImageView ivZan;
		TextView tvName;
		TextView tvTime;
		TextView tvContent;
		TextView tvType;
		TextView tvZan;
		TextView tvAnsNum;
		LinearLayout llZan;
	}

}
