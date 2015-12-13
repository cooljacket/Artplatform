package com.gexin.artplatform.largeImg;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.hacky.HackyViewPager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.R;
import com.gexin.artplatform.constant.Conf;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.dlog.DLog;
import com.gexin.artplatform.utils.BroadcastUtil;
import com.gexin.artplatform.utils.HttpConnectionUtils;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.utils.ShareUtil;
import com.gexin.artplatform.view.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class LargeImageActivity extends Activity {

	private static final String TAG = "LargeImageActivity";
	private List<String> imageList;
	private List<String> delList = new ArrayList<String>();
	private int mIndex = 0;
	private int type = 0;
	private LargeImageAdapter adapter;
	private boolean isShow = false;
	// private FrontiaSocialShare mSocialShare;
	// private FrontiaSocialShareContent mImageContent = new
	// FrontiaSocialShareContent();

	private ViewPager mViewPager;
	private TitleBar titleBar;
	private LinearLayout llBack;
	private LinearLayout llMore;
	private LinearLayout llCmdFrame;
	private TextView tvCollect;
	private TextView tvShare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_large_image);

		// Frontia.init(this.getApplicationContext(), Conf.APIKEY);
		initView();
		initData();
	}

	private void initData() {
		imageList = getIntent().getStringArrayListExtra("images");
		mIndex = getIntent().getIntExtra("index", 0);
		type = getIntent().getIntExtra("type", 0);
		DLog.v(TAG, "index:" + mIndex);
		if (type == 0) {
			adapter = new LargeImageAdapter(imageList, this);
		} else {
			adapter = new LargeImageAdapter(this, imageList, type);
		}
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(mIndex);
		// mSocialShare = Frontia.getSocialShare();
		// mSocialShare.setContext(this);
		if (type == 1 || type == 2) {
			tvCollect.setText("删除");
		}
	}

	private void initView() {
		mViewPager = (HackyViewPager) findViewById(R.id.vp_large_image);
		titleBar = (TitleBar) findViewById(R.id.tb_large_image);
		llCmdFrame = (LinearLayout) findViewById(R.id.ll_cmd_large_image);
		tvCollect = (TextView) findViewById(R.id.tv_collect_large_image);
		tvShare = (TextView) findViewById(R.id.tv_share_large_image);
		initTitleBar();

		tvCollect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				addCollection();
				isShow = false;
				llCmdFrame.setVisibility(View.GONE);

			}
		});
		tvShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				isShow = false;
				llCmdFrame.setVisibility(View.GONE);
				sharePic();
			}
		});

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				mIndex = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	protected void sharePic() {
		ShareUtil shareUtil = new ShareUtil(this);
		shareUtil.showPopwindow(this, imageList.get(mIndex),
				findViewById(R.id.image_layout), "美术帮", "我在美术帮上发现一幅不错的画", 1);
	}

	@SuppressLint("HandlerLeak")
	private void addCollection() {
		int index = mViewPager.getCurrentItem();
		final String imageUrl = imageList.get(index);
		String userId = (String) SPUtil.get(this, "userId", "");
		Log.v(TAG, "url:" + imageUrl);
		if (userId.isEmpty()) {
			Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
			return;
		}
		String url = Constant.SERVER_URL + "/api/user/" + userId
				+ "/collection";
		Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HttpConnectionUtils.DID_SUCCEED:
					try {
						JSONObject jsonObject = new JSONObject((String) msg.obj);
						int state = jsonObject.getInt("stat");
						if (state == 1) {
							if (type == 0) {
								Toast.makeText(LargeImageActivity.this, "收藏成功",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(LargeImageActivity.this, "删除成功",
										Toast.LENGTH_SHORT).show();
								imageList.remove(imageUrl);
								delList.add(imageUrl);
								Intent intent = new Intent();
								// intent.putStringArrayListExtra("delUrls",
								// (ArrayList) delList);
								intent.putExtra("delUrl", imageUrl);
								setResult(RESULT_OK, intent);
								adapter.notifyDataSetChanged();
								finish();
								// if (imageList.isEmpty()) {
								// finish();
								// } else {
								// mViewPager.setCurrentItem(0);
								// }
							}
							BroadcastUtil
									.updateUserInfo(LargeImageActivity.this);
						} else {
							if (type == 0) {
								Toast.makeText(LargeImageActivity.this, "收藏失败",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(LargeImageActivity.this, "删除失败",
										Toast.LENGTH_SHORT).show();
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
						if (type == 0) {
							Toast.makeText(LargeImageActivity.this, "收藏失败",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(LargeImageActivity.this, "删除失败",
									Toast.LENGTH_SHORT).show();
						}
					}
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}

		};
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("imageUrl", imageUrl));
		if (type == 0) {
			new HttpConnectionUtils(handler).put(url, data);
		} else if (type == 1) {
			url = Constant.SERVER_URL + "/api/user/" + userId
					+ "/collection?imageUrl=" + imageUrl;
			new HttpConnectionUtils(handler).delete(url);
		} else if (type == 2) {
			url = Constant.SERVER_URL + "/api/user/" + userId
					+ "/work?imageUrl=" + imageUrl;
			new HttpConnectionUtils(handler).delete(url);
		}
	}

	private void initTitleBar() {
		llBack = new LinearLayout(this);
		ImageView ivBack = new ImageView(this);
		ivBack.setImageResource(R.drawable.back_icon);
		LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		llBack.addView(ivBack, params);
		llBack.setGravity(Gravity.CENTER_VERTICAL);
		llBack.setBackgroundResource(R.drawable.selector_titlebar_btn);
		llBack.setPadding(20, 0, 20, 0);
		titleBar.setLeftView(llBack);

		llMore = new LinearLayout(this);
		ImageView ivMore = new ImageView(this);
		ivMore.setImageResource(R.drawable.more_icon);
		llMore.addView(ivMore, params);
		llMore.setGravity(Gravity.CENTER_VERTICAL);
		llMore.setBackgroundResource(R.drawable.selector_titlebar_btn);
		llMore.setPadding(20, 0, 20, 0);
		titleBar.setRightView(llMore);

		llBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		llMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isShow) {
					isShow = false;
					llCmdFrame.setVisibility(View.GONE);
				} else {
					isShow = true;
					llCmdFrame.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		// adapter.clearAttacher();
		super.onDestroy();
	}

	private static class LargeImageAdapter extends PagerAdapter {

		private List<String> mList;
		private LayoutInflater inflater;
		private DisplayImageOptions picOptions;
		@SuppressWarnings("unused")
		private int type = 0;

		public LargeImageAdapter(List<String> mList, Context context) {
			super();
			this.mList = mList;
			this.inflater = LayoutInflater.from(context);
			picOptions = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.ic_empty)
					.showImageOnFail(R.drawable.ic_error).cacheInMemory(false)
					// to avoid out of memory
					.cacheOnDisk(true).considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
		}

		public LargeImageAdapter(Context context, List<String> mList, int type) {
			super();
			this.inflater = LayoutInflater.from(context);
			this.mList = mList;
			this.type = type;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.item_large_page, view,
					false);
			assert imageLayout != null;
			final ImageView imageView = (PhotoView) imageLayout
					.findViewById(R.id.iv_large_image_item);
			final ProgressBar spinner = (ProgressBar) imageLayout
					.findViewById(R.id.pb_large_image_item);

			ImageLoader.getInstance().displayImage(mList.get(position),
					imageView, picOptions, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							spinner.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							String message = null;
							switch (failReason.getType()) {
							case IO_ERROR:
								message = "Input/Output error";
								break;
							case DECODING_ERROR:
								message = "Image can't be decoded";
								break;
							case NETWORK_DENIED:
								message = "Downloads are denied";
								break;
							case OUT_OF_MEMORY:
								message = "Out Of Memory error";
								break;
							case UNKNOWN:
								message = "Unknown error";
								break;
							}
							message = "网络连接异常";
							Toast.makeText(view.getContext(), message,
									Toast.LENGTH_SHORT).show();

							spinner.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							spinner.setVisibility(View.GONE);
							// mAttacher = new PhotoViewAttacher(imageView);
							// mAttacher.setScaleType(ScaleType.FIT_CENTER);
						}
					});

			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	// private class ShareListener implements FrontiaSocialShareListener {
	//
	// @Override
	// public void onSuccess() {
	// Log.d("Test", "share success");
	// }
	//
	// @Override
	// public void onFailure(int errCode, String errMsg) {
	// Log.d("Test", "share errCode " + errCode);
	// }
	//
	// @Override
	// public void onCancel() {
	// Log.d("Test", "cancel ");
	// }
	//
	// }

}
