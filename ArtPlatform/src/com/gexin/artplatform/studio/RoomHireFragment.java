package com.gexin.artplatform.studio;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Recruitment;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RoomHireFragment extends Fragment {

	private List<Recruitment> mList = new ArrayList<Recruitment>();
	private RoomHireAdapter adapter;
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
	}

	private void initData() {
		adapter = new RoomHireAdapter(getActivity(), mList);
		mListView.setAdapter(adapter);
	}

	@SuppressLint("HandlerLeak")
	public void setStudioId(String studioId) {
		String url = Constant.SERVER_URL + "/api/studio/" + studioId
				+ "/recruitment";
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
				List<Recruitment> tmpList = gson.fromJson(jsonObject
						.getJSONArray("recruitments").toString(),
						new TypeToken<List<Recruitment>>() {
						}.getType());
				try {
					mList.clear();
					mList.addAll(tmpList);
					adapter.notifyDataSetChanged();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
