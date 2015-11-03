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
import android.widget.GridView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.largeImg.LargeImageActivity;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class StudioEnvironmentFragment extends Fragment {

	private List<String> mList = new ArrayList<String>();
	private RoomGalleryAdapter adapter;
	private String studioId = "";
	private Gson gson = new Gson();

	private GridView mGridView;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_room_gallery, container,
				false);
		initView(view);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	private void initData() {
		adapter = new RoomGalleryAdapter(getActivity(), mList);
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(),
						LargeImageActivity.class);
				intent.putStringArrayListExtra("images",
						(ArrayList<String>) mList);
				intent.putExtra("index", arg2);
				startActivity(intent);
			}
		});
	}

	private void initView(View view) {
		mGridView = (GridView) view.findViewById(R.id.gv_fragment_room_gallery);
	}

	public void setStudioId(String id) {
		studioId = id;
		getPictures();
		
	}

	@SuppressLint("HandlerLeak")
	private void getPictures() {
		String url = Constant.SERVER_URL + "/api/studio/" + studioId
				+ "/environment";
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
							List<String> tmpList = gson.fromJson(jObject
									.getJSONArray("environment").toString(),
									new TypeToken<List<String>>() {
									}.getType());
							mList.clear();
							mList.addAll(tmpList);
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
	
	
}
