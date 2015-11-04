package com.gexin.artplatform.discover;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.gexin.artplatform.R;
import com.gexin.artplatform.SubClassActivity;
import com.gexin.artplatform.bean.Classification;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DiscoverFragment extends Fragment {

	private static final String TAG = "DiscoverFragment";
	private static final String Discover_API = Constant.SERVER_URL
			+ Constant.Discover_API + "/index";
	private List<Classification> discoverList = new ArrayList<Classification>();
	private Gson gson = new Gson();
	private DiscoverGridAdapter adapter;

	private GridView mGridView;
	private LinearLayout llFindFriend;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_discover, container,
				false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		mGridView = (GridView) view.findViewById(R.id.gv_discover_fragment);
		llFindFriend = (LinearLayout) view
				.findViewById(R.id.ll_find_discover_fragment);
		llFindFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getActivity(),
						FindFriendActivity.class));
			}
		});
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (discoverList.get(arg2).getType() == 0) {
					Intent intent = new Intent(getActivity(),
							FindStudioActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(getActivity(),
							SubClassActivity.class);
					intent.putExtra("name", discoverList.get(arg2).getName());
					intent.putExtra("classId", discoverList.get(arg2).get_id());
					startActivity(intent);
				}
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private void initData() {
		adapter = new DiscoverGridAdapter(getActivity(), discoverList);
		mGridView.setAdapter(adapter);
		Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					String response = (String) msg.obj;
					try {
						JSONObject jObject = new JSONObject(
								response == null ? "" : response.trim());
						if (jObject != null) {
							success(jObject);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			}

		};
		new HttpConnectionUtils(handler).get(Discover_API);
	}

	private void success(JSONObject jObject) {
		int state = -1;
		List<Classification> tempList = null;
		Log.i(TAG, "jObject:" + jObject.toString());
		try {
			state = jObject.getInt("stat");
			if (state == 1) {
				tempList = gson.fromJson(jObject.getJSONArray("contentData")
						.toString(), new TypeToken<List<Classification>>() {
				}.getType());
				if (tempList != null) {
					discoverList.addAll(tempList);
					adapter.notifyDataSetChanged();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// Log.i(TAG, "success:" + tempList);

	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initData();
	}

}
