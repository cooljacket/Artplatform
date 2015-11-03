package com.gexin.artplatform.question;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.gexin.artplatform.R;
import com.gexin.artplatform.constant.Constant;
import com.gexin.artplatform.utils.FileUtil;
import com.gexin.artplatform.utils.ImageUtil;
import com.gexin.artplatform.utils.SPUtil;
import com.gexin.artplatform.view.ActionSheet;
import com.gexin.artplatform.view.ActionSheet.MenuItemClickListener;
import com.gexin.artplatform.view.TitleBar;

public class PostProblemActivity extends Activity {

	private static final String TAG = "PostProblemActivity";
	protected static final int ALBUM_REQUEST_CODE = 0;
	protected static final int CAMERA_REQUEST_CODE = 1;
	private static final String IMAGEDIR = Constant.APP_PATH + "image/";
	private String imagePath = "";
	private String teacherId = "";

	private EditText etContent;
	private ImageButton ibtnImage;
	private TextView tvWordleft;
	private LinearLayout llLeft, llRight;
	private TitleBar titleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_problem);
		setTheme(R.style.ActionSheetStyleIOS7);
		teacherId = getIntent().getStringExtra("teacherId");
		initView();
	}

	private void initView() {
		etContent = (EditText) findViewById(R.id.et_content_postproblem);
		ibtnImage = (ImageButton) findViewById(R.id.ibtn_image_postproblem);
		tvWordleft = (TextView) findViewById(R.id.tv_wordleft_post_problem);
		titleBar = (TitleBar) findViewById(R.id.tb_post_problem);
		setTitleBtn();

		ibtnImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showPicDialog();
			}
		});
		etContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				String content = etContent.getText().toString();
				tvWordleft.setText("还可以输入" + (140 - content.length()) + "字");
			}
		});
	}

	private void setTitleBtn() {
		llLeft = new LinearLayout(this);
		llLeft.setGravity(Gravity.CENTER_VERTICAL);
		TextView tvLeft = new TextView(this);
		tvLeft.setText("取消");
		tvLeft.setTextSize(20);
		tvLeft.setTextColor(Color.WHITE);
		LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 0, 10, 0);
		llLeft.setBackgroundResource(R.drawable.selector_titlebar_btn);
		llLeft.addView(tvLeft, params);

		llRight = new LinearLayout(this);
		llRight.setGravity(Gravity.CENTER_VERTICAL);
		TextView tvRight = new TextView(this);
		tvRight.setText("下一步");
		tvRight.setTextSize(20);
		tvRight.setTextColor(Color.WHITE);
		llRight.setBackgroundResource(R.drawable.selector_titlebar_btn);
		llRight.addView(tvRight, params);
		titleBar.setLeftView(llLeft);
		titleBar.setRightView(llRight);

		llLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.v(TAG, "LeftClick");
				finish();
			}
		});

		llRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.v(TAG, "RightClick");
				String content = etContent.getText().toString();
				if (content.isEmpty()) {
					Toast.makeText(PostProblemActivity.this, "内容不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = new Intent(PostProblemActivity.this,
						SelectTagActivity.class);
				intent.putExtra("content", content);
				intent.putExtra("image", imagePath);
				intent.putExtra("teacherId", teacherId);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bitmap bitmap;
			switch (requestCode) {
			case ALBUM_REQUEST_CODE:
				imagePath = FileUtil.getRealFilePath(this, data.getData());
				Log.v(TAG, imagePath);
				bitmap = ImageUtil.getCompressedImage(imagePath, 50);
				if (bitmap == null) {
					ibtnImage.setImageResource(R.drawable.add_icon);
				} else {
					ibtnImage.setImageBitmap(bitmap);
				}
				break;
			case CAMERA_REQUEST_CODE:
				int picCount = (Integer) SPUtil.get(this, "picCount", 0);
				String picName = "pic" + picCount + ".jpg";
				imagePath = IMAGEDIR + picName;
				bitmap = ImageUtil.getCompressedImage(imagePath, 50);
				if (bitmap == null) {
					ibtnImage.setImageResource(R.drawable.add_icon);
				} else {
					ibtnImage.setImageBitmap(bitmap);
				}
				break;

			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	protected void showPicDialog() {
		ActionSheet menuView = new ActionSheet(this);
		menuView.setCancelButtonTitle("取消");
		menuView.addItems("拍照", "从相册中选择");

		menuView.setItemClickListener(new MenuItemClickListener() {

			@Override
			public void onItemClick(int itemPosition) {
				if (itemPosition == 1) {
					Intent intent = new Intent(Intent.ACTION_PICK, null);
					// 设置数据来源和类型
					intent.setDataAndType(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							"image/*");
					startActivityForResult(intent, ALBUM_REQUEST_CODE);
				}
				if (itemPosition == 0) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					// 打开图片所在目录，如果该目录不存在，则创建该目录
					File dirFile = new File(IMAGEDIR);
					if (!dirFile.exists()) {
						dirFile.mkdirs();
					}
					int picCount = (Integer) SPUtil.get(
							PostProblemActivity.this, "picCount", 0);
					picCount++;
					// 将图片保存到该目录下
					intent.putExtra(
							MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(new File(IMAGEDIR, "pic" + picCount
									+ ".jpg")));
					SPUtil.put(PostProblemActivity.this, "picCount", picCount);
					startActivityForResult(intent, CAMERA_REQUEST_CODE);
				}
			}

		});
		menuView.showMenu();
		// new AlertDialog.Builder(this)
		// .setTitle("选择图片")
		// .setNegativeButton("相册", new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// // 让对话框消失
		// dialog.dismiss();
		// // ACTION_PICK，从数据集合中选择一个返回，官方文档解释如下
		// // Activity Action:
		// // Pick an item from the data, returning what was
		// // selected.
		// Intent intent = new Intent(Intent.ACTION_PICK, null);
		// // 设置数据来源和类型
		// intent.setDataAndType(
		// MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
		// "image/*");
		// startActivityForResult(intent, ALBUM_REQUEST_CODE);
		// }
		// })
		// .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int arg1) {
		// dialog.dismiss();
		// /**
		// * 下面这句还是老样子，调用快速拍照功能，至于为什么叫快速拍照，大家可以参考如下官方
		// * 文档，you_sdk_path/docs/guide/topics/media/camera.html
		// */
		// Intent intent = new Intent(
		// MediaStore.ACTION_IMAGE_CAPTURE);
		// // 打开图片所在目录，如果该目录不存在，则创建该目录
		// File dirFile = new File(IMAGEDIR);
		// if (!dirFile.exists()) {
		// dirFile.mkdirs();
		// }
		// int picCount = (Integer) SPUtil.get(
		// PostProblemActivity.this, "picCount", 0);
		// picCount++;
		// // 将图片保存到该目录下
		// intent.putExtra(
		// MediaStore.EXTRA_OUTPUT,
		// Uri.fromFile(new File(IMAGEDIR, "pic"
		// + picCount + ".jpg")));
		// SPUtil.put(PostProblemActivity.this, "picCount",
		// picCount);
		// startActivityForResult(intent, CAMERA_REQUEST_CODE);
		// }
		// }).show();
	}

}
