package com.gexin.artplatform.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

import com.gexin.artplatform.R;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.BaseShareContent;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class ShareUtil {

	private String SharedTag = "【美术帮】";

	private int[] imageId = { R.drawable.share_weixin_icon,
			R.drawable.share_friends_icon, R.drawable.share_qq_icon,
			R.drawable.share_qzone_icon, R.drawable.share_to_sinaweibo_icon };
	private String[] name = { "微信好友", "朋友圈", "QQ好友", "QQ空间", "新浪微博" };
	private ArrayList<Item> list;
	private ShareAdapter adapter;
	private GridView gridView;
	private PopupWindow window;

	private UMSocialService mShareService;
	private Activity activity;

	public ShareUtil(Activity activity) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.mShareService = UMServiceFactory.getUMSocialService(
				"com.umeng.share", RequestType.SOCIAL);
		addAllPlatform();
		com.umeng.socialize.utils.Log.LOG = true;
		mShareService.getConfig().openToast();
		mShareService.getConfig().setDefaultShareLocation(false);
		// 添加新浪微博平台
		// mShareService.getConfig().setSsoHandler(new SinaSsoHandler());
	}

	private void addAllPlatform() {
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(activity, "wxc6e1ddc34978819c",
				"ec2130d6f5f56d59abc4c7474f82de95");
		wxHandler.addToSocialSDK();
		// 添加微信朋友圈
		UMWXHandler wxCirclHandler = new UMWXHandler(activity,
				"wxc6e1ddc34978819c", "ec2130d6f5f56d59abc4c7474f82de95");
		wxCirclHandler.setToCircle(true);
		wxCirclHandler.addToSocialSDK();

		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity,
				"1104760933", "4J2dToj78ZZBkHLP");
		qqSsoHandler.addToSocialSDK();

		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity,
				"1104760933", "4J2dToj78ZZBkHLP");
		qZoneSsoHandler.addToSocialSDK();

	}

	private <T extends BaseShareContent> void addPlatformToShareMsg(String url,
			T platform, String title, String content, int type) {
		if (platform instanceof WeiXinShareContent) {
			if (type == 0) {
				setShareInfo(platform, null, title, SharedTag, new UMImage(
						activity, R.drawable.app_logo), url, null);
			} else {
				setShareInfo(platform, null, title, content, new UMImage(
						activity, R.drawable.app_logo), url, null);
			}
		} else if (platform instanceof CircleShareContent) {
			if (type == 0) {
				setShareInfo(platform, null, title, SharedTag, new UMImage(
						activity, R.drawable.app_logo), url, null);
			} else {
				setShareInfo(platform, null, title, content, new UMImage(
						activity, R.drawable.app_logo), url, null);
			}
		} else if (platform instanceof SinaShareContent) {
			title = "\n" + title;
			setShareInfo(platform, null, SharedTag, SharedTag + title + "。\n"
					+ url + content,
					new UMImage(activity, R.drawable.app_logo), url, null);
		} else if (platform instanceof QQShareContent) {
			if (type == 0) {
				setShareInfo(platform, null, title, SharedTag, new UMImage(
						activity, R.drawable.app_logo), url, null);
			} else {
				setShareInfo(platform, null, title, content, new UMImage(
						activity, R.drawable.app_logo), url, null);
			}
		}
		
	}

	private <T extends BaseShareContent> void setShareInfo(T platform,
			String webSite, String title, String content, UMImage image,
			String targetUrl, UMediaObject media) {
		if (webSite != null && !webSite.equals("")) {
			platform.setAppWebSite(webSite);
		}
		if (title != null && !title.equals("")) {
			platform.setTitle(title);
		}
		if (content != null && !content.equals("")) {
			platform.setShareContent(content);
		}
		if (targetUrl != null && !targetUrl.equals("")) {
			platform.setTargetUrl(targetUrl);
		}
		if (image != null) {
			platform.setShareImage(image);
		}
		if (media != null) {
			platform.setShareMedia(media);
		}
		mShareService.setShareMedia(platform);
	}

	private void startOneShare(SHARE_MEDIA platform) {
		mShareService.postShare(activity, platform, new SnsPostListener() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int stCode,
					SocializeEntity entry) {
				// TODO Auto-generated method stub
				if (stCode == 200) {
					Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
				} else if (stCode == 40000) {

				} else {
					Toast.makeText(activity, "分享失败: error code: " + stCode,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void initList() {
		list = new ArrayList<Item>();
		Item item;
		for (int i = 0; i < imageId.length; i++) {
			item = new Item();
			item.setImgId(imageId[i]);
			item.setName(name[i]);
			list.add(item);
		}
	}

	public void showPopwindow(Context context, final String url, View parent,
			final String title, final String content, final int type) {

		ViewGroup menuView = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.popwindowlayout, null);
		initList();
		adapter = new ShareAdapter(context, list);
		gridView = (GridView) menuView.findViewById(R.id.gridview);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					addPlatformToShareMsg(url, new WeiXinShareContent(), title,
							content, type);
					startOneShare(SHARE_MEDIA.WEIXIN);
					break;
				case 1:
					addPlatformToShareMsg(url, new CircleShareContent(), title,
							content, type);
					startOneShare(SHARE_MEDIA.WEIXIN_CIRCLE);
					break;
				case 2:
					addPlatformToShareMsg(url, new QQShareContent(), title,
							content, type);
					startOneShare(SHARE_MEDIA.QQ);
					break;
				case 3:
					addPlatformToShareMsg(url, new QZoneShareContent(), title,
							content, type);
					startOneShare(SHARE_MEDIA.QZONE);
					break;
				case 4:
					addPlatformToShareMsg(url, new SinaShareContent(), title,
							content, type);
					startOneShare(SHARE_MEDIA.SINA);
					break;

				default:
					break;
				}
			}
		});

		window = new PopupWindow(menuView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		window.setFocusable(true);

		window.setTouchable(true);
		window.setOutsideTouchable(true);
		// window.setBackgroundDrawable(new ColorDrawable(0x7DC0C0C0));
		window.setBackgroundDrawable(new BitmapDrawable(context.getResources(),
				(Bitmap) null));
		window.setAnimationStyle(R.style.anim_popupwindow);
		window.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				// window.dismiss();

			}
		});
		window.update();
	}
}
