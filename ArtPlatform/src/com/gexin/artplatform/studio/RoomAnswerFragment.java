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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.gexin.artplatform.R;
import com.gexin.artplatform.bean.Problem;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.question.QuestionAdapter;
import com.gexin.artplatform.question.QuestionInfoActivity;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RoomAnswerFragment extends Fragment {

	private static final String TAG = "RoomAnswerFragment";
	private QuestionAdapter adapter;
	private List<Problem> mList = new ArrayList<Problem>();
	private Gson gson = new Gson();
	
	private ListView mListView;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_room_answer, container,
				false);
		mListView = (ListView) view.findViewById(R.id.lv_room_answer);
		return view;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		adapter = new QuestionAdapter(getActivity(),mList);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(),
						QuestionInfoActivity.class);
				intent.putExtra("problemId", mList.get(arg2).get_id());
				startActivity(intent);
			}
		});
		super.onActivityCreated(savedInstanceState);
	}
	
	/**
	 * 初始化数据
	 */
	@SuppressLint("HandlerLeak")
	public void setStudioId(String studioId) {
		String api = Constant.SERVER_URL
				+ "/api/studio/"+studioId+"/answer";
		Log.v(TAG, "url:"+api);
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
							List<Problem> tempList = success(jObject);
							if (tempList != null) {
								mList.clear();
								mList.addAll(tempList);
								Log.v(TAG, "size:"+mList.size());
								adapter.notifyDataSetChanged();
//								adapter.updateData(tempList);
							}
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

		new HttpConnectionUtils(handler).get(api);
	}
	
	private List<Problem> success(JSONObject jObject) {
		int state = -1;
		List<Problem> tempList = null;
		try {
			state = jObject.getInt("stat");
			if (state == 1) {
				try {
					tempList = gson.fromJson(jObject.getJSONArray("problems")
							.toString(), new TypeToken<List<Problem>>() {
					}.getType());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tempList;

	}

}
