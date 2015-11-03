package com.gexin.artplatform.studio;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.StudioComment;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.SPUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class StudioCommentFragment extends Fragment {

	private static final String TAG = "StudioCommentFragment";
	private List<StudioComment> mList = new ArrayList<StudioComment>();
	private StudioCommentAdapter adapter;
	private String studioId = "";
	private Gson gson = new Gson();

	private PullToRefreshListView mListView;
	private Button btnComment;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_studio_comment,
				container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		mListView = (PullToRefreshListView) view
				.findViewById(R.id.lv_studio_comment);
		btnComment = (Button) view
				.findViewById(R.id.btn_comment_studio_comment);

		btnComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showCommentDialog();
			}
		});
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	private void initData() {
		adapter = new StudioCommentAdapter(getActivity(), mList);
		mListView.setAdapter(adapter);
	}

	@SuppressLint("HandlerLeak")
	public void setStudioId(String id) {
		studioId = id;
		String url = Constant.SERVER_URL + "/api/studio/" + studioId
				+ "/studio-comment";
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:

					JSONObject jObject;
					try {
						jObject = new JSONObject((String) msg.obj);
						int state = jObject.getInt("stat");
						if (state == 1) {
							List<StudioComment> tmpList = gson.fromJson(jObject
									.getJSONArray("studioComments").toString(),
									new TypeToken<List<StudioComment>>() {
									}.getType());
							mList.clear();
							mList.addAll(tmpList);
							Log.v(TAG, mList.toString());
							adapter.notifyDataSetChanged();
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
		new HttpConnectionUtils(handler).get(url);
	}

	private void showCommentDialog() {
		final EditText editText = new EditText(getActivity());
		new AlertDialog.Builder(getActivity())
				.setTitle("评论")
				.setView(editText)
				.setPositiveButton("发送", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String str = editText.getText().toString();
						if (str.isEmpty()) {
							Toast.makeText(getActivity(), "您的评论不能为空",
									Toast.LENGTH_SHORT).show();
							return;
						}
						Log.v(TAG, editText.getText().toString());
						sendComment(str);
						dialog.dismiss();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	@SuppressLint("HandlerLeak")
	private void sendComment(String str) {
		String userId = (String) SPUtil.get(getActivity(), "userId", "");
		if (userId.isEmpty()) {
			Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
		}
		String url = Constant.SERVER_URL + "/api/user/" + userId
				+ "/studio-comment";
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					Log.v(TAG, "response:" + msg.obj);
					try {
						JSONObject jsonObject = new JSONObject((String) msg.obj);
						int state = jsonObject.getInt("stat");
						if (state == 1) {
							Toast.makeText(getActivity(), "评论成功",
									Toast.LENGTH_SHORT).show();
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
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("content", str));
		data.add(new BasicNameValuePair("studioId", studioId));
		new HttpConnectionUtils(handler).post(url, data);
	}
}
