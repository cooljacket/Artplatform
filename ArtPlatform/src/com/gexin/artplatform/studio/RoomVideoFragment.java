package com.gexin.artplatform.studio;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Video;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RoomVideoFragment extends Fragment {

	private List<Video> mList = new ArrayList<Video>();
	private RoomVideoAdapter adapter;
	private Gson gson = new Gson();
	private ListView mListView;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_room_commentlist,
				container, false);
		mListView = (ListView) view.findViewById(R.id.lv_fragment_room_common);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String videoId = mList.get(arg2).getVideoId();
				if(videoId==null||videoId.isEmpty()){
					Toast.makeText(getActivity(), "视频不存在", Toast.LENGTH_SHORT).show();
					return ;
				}
				Intent intent = new Intent(getActivity(),
						VideoDetailActivity.class);
//				intent.putExtra("url", mList.get(arg2).getVideoUrl());
//				intent.putExtra("image", mList.get(arg2).getImageUrl());
//				intent.putExtra("title", mList.get(arg2).getTitle());
//				intent.putExtra("description", mList.get(arg2).getDescription());
				intent.putExtra("id", videoId);
				startActivity(intent);
			}
		});
	}

	private void initData() {
		adapter = new RoomVideoAdapter(getActivity(), mList);
		mListView.setAdapter(adapter);
	}

	@SuppressLint("HandlerLeak")
	public void setStudioId(String studioId) {
		String url = Constant.SERVER_URL + "/api/studio/" + studioId
				+ "/video";
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					dealResponse((String) msg.obj);
					break;

				default:
					break;
				}
			};
		};
		new HttpConnectionUtils(handler).get(url);
	}

	private void dealResponse(String res) {
		try {
			JSONObject jsonObject = new JSONObject(res);
			int state = jsonObject.getInt("stat");
			if (state == 1) {
				List<Video> tmpList = gson.fromJson(
						jsonObject.getJSONArray("videos").toString(),
						new TypeToken<List<Video>>() {
						}.getType());
				mList.clear();
				mList.addAll(tmpList);
				adapter.notifyDataSetChanged();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
