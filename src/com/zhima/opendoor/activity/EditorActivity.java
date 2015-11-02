package com.zhima.opendoor.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.zhima.opendoor.R;
import com.zhima.opendoor.application.ZmApplication;
import com.zhima.opendoor.entity.User;
import com.zhima.opendoor.entity.info;
import com.zhima.opendoor.utils.ImageLoaderUtils;
import com.zhima.opendoor.utils.StringPostRequest;
import com.zhima.opendoor.utils.urlUtils;
import com.zhima.opendoor.view.SwipeBackActivity;
/**
 * 
 * @author Administrator 用户个人信息设置界面
 */

public class EditorActivity extends SwipeBackActivity implements OnClickListener {
	private TextView myInfo;
	private ImageView header;
	private EditText userName;
	private EditText aboutMe;
	private TextView sex;
	private TextView save;
	private info info;
	private ImageView sexImg;
	private String photoPath;
	private Bitmap b;
	private User user;
	private static final int TAKE_FROM_CAPTURE = 1; // 拍照
	private static final int TAKE_FROM_ALBUM = 2;// 从本地相册读取
	private static final int RESULT_PHOTO = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor_activity_layout);
		user=ZmApplication.getInstance().getUser();
		myInfo = (TextView) findViewById(R.id.myInfo);
		header = (ImageView) findViewById(R.id.headView);
		userName = (EditText) findViewById(R.id.edit_username);
		aboutMe = (EditText) findViewById(R.id.aboutMe);
		sex = (TextView) findViewById(R.id.sex);
		save = (TextView) findViewById(R.id.save);
		sexImg = (ImageView) findViewById(R.id.sexImg);
		myInfo.setOnClickListener(this);
		sex.setOnClickListener(this);
		save.setOnClickListener(this);
		header.setOnClickListener(this);
		initStartData();
	}
	/**
	 * 初始加载数据
	 */
	private void initStartData(){
		userName.setText(user.getUser_name());
		if(user.getHead_img()!=null){
			ImageLoaderUtils.display(user.getHead_img(), header);
		}
		aboutMe.setText(user.getUser_desc());
		sex.setText(user.getSex());
		if(user.getSex().equals("女")){
			sexImg.setImageResource(R.drawable.icon_pop_girl);
		}else{
			sexImg.setImageResource(R.drawable.icon_pop_boy);
		}
	}

	/**
	 * 加载数据，并且传递
	 */
	private void initData() {
		info = new info();
		info.setName(userName.getText().toString());
		info.setSex(sex.getText().toString());
		info.setAboutMe(aboutMe.getText().toString());
		// 上传信息
		StringPostRequest sp = new StringPostRequest(urlUtils.localhost
				+ urlUtils.userInfoServlet, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				if(arg0.equals("success")){
					// 把信息写入user中
				user.setSex(sex.getText().toString());
				user.setUser_desc(aboutMe.getText().toString());
				user.setUser_name(userName.getText().toString());
					Intent intent = new Intent();
					intent.putExtra("info", info);
					setResult(RESULT_OK, intent);
					Toast.makeText(EditorActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
					finish();
				}
				
			

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				Toast.makeText(getApplicationContext(), "网络连接失败，请稍后再试", Toast.LENGTH_SHORT).show();

			}
		});
		sp.putParams("name", user.getUser_id());
		sp.putParams("sex", info.getSex());
		sp.putParams("desc", info.getAboutMe());
		sp.putParams("userName", info.getName());
		ZmApplication.getInstance().getRequestQueue().add(sp);
		
		
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.myInfo:
			checkFinish();
			break;
		case R.id.save:
			initData();
			break;
		case R.id.sex:
			checkSex();
			break;
		case R.id.headView:
			updateHeader();

		default:
			break;
		}

	}

	private void updateHeader() {
		AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(this);
		builder.setTitle("提示").setMessage("请选择上传方式 ");
		builder.setPositiveButton("相册",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Intent intent = new Intent(Intent.ACTION_PICK);
						intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(intent, TAKE_FROM_ALBUM);

					}
				});
		builder.setNegativeButton("拍照",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Intent intent = new Intent(
								android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						File parent = com.zhima.opendoor.utils.FileUitlity
								.getInstance(EditorActivity.this).makeDir(
										"opendoor_head");
						photoPath = parent.getPath() + File.separator
								+ System.currentTimeMillis() + ".png";
						intent.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(new File(photoPath)));
						startActivityForResult(intent, TAKE_FROM_CAPTURE);
						arg0.dismiss();

					}
				});
		AlertDialog dialog = builder.create();
		dialog.setCancelable(true);
		dialog.show();

	}

	private void checkSex() {
		AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(this);
		builder.setTitle("提示").setMessage("请选择性别 ");
		builder.setPositiveButton("女",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						sex.setText("女");
						sexImg.setImageResource(R.drawable.icon_pop_girl);
						arg0.dismiss();

					}
				});
		builder.setNegativeButton("男",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						sex.setText("男");
						sexImg.setImageResource(R.drawable.icon_pop_boy);
						arg0.dismiss();

					}
				});
		AlertDialog dialog = builder.create();
		dialog.setCancelable(true);
		dialog.show();

	}

	/**
	 * 点击我的资料时候的逻辑
	 */
	private void checkFinish() {
		// 服务器好了之后执行另外一段逻辑，和服务器中的数据对比
		info = new info();
		info.setName(userName.getText().toString());
		// info.setSex(sex.getText().toString());
		info.setAboutMe(aboutMe.getText().toString());
		if (info == null) {
			finish();
		} else {
			AlertDialog.Builder builder;
			builder = new AlertDialog.Builder(this);
			builder.setTitle("提示").setMessage("是否保存更改内容？ ");
			builder.setPositiveButton("确认",
					new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// 上传信息
							// 把信息写入user中
							initData();
						}
					});
			builder.setNegativeButton("取消",
					new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							finish();

						}
					});
			AlertDialog dialog = builder.create();
			dialog.setCancelable(false);
			dialog.show();
		}

	}

	/**
	 * 设置图片剪裁
	 * 
	 * @param uri
	 */

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESULT_PHOTO);
	}

	/**
	 * 实现图片的上传
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
		case TAKE_FROM_CAPTURE:
			startPhotoZoom(Uri.fromFile(new File(photoPath)));
			break;
		case TAKE_FROM_ALBUM:
			Cursor cursor = this.getContentResolver().query(data.getData(),
					new String[] { MediaStore.Images.Media.DATA }, null, null,
					null);
			cursor.moveToFirst();
			photoPath = cursor.getString(cursor
					.getColumnIndex(MediaStore.Images.Media.DATA));
			cursor.close();
			startPhotoZoom(Uri.fromFile(new File(photoPath)));
			// imageView.setImageURI(Uri.fromFile(new File(photoPath)));
			break;
		case RESULT_PHOTO:
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				b = bundle.getParcelable("data");
				uodateImage(convertBitmap(b));
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 将bitmap转化为字符串，实现上传
	 * 
	 * @param b
	 * @return
	 */
	public String convertBitmap(Bitmap b) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		// 压缩图片，转换到流中;
		b.compress(Bitmap.CompressFormat.PNG, 100, os);
		try {
			os.flush();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] bf = os.toByteArray();
		byte[] stringByte = Base64.encode(bf, Base64.DEFAULT);
		return new String(stringByte);
	}

	private void uodateImage(String bitmapurl) {
		final User u = ZmApplication.getInstance().getUser();
		final ProgressDialog dialog = ProgressDialog
				.show(this, "", "图片上传中....");

		StringPostRequest sp = new StringPostRequest(urlUtils.loginServlet,
				new Listener<String>() {
					@Override
					public void onResponse(String arg0) {
						header.setImageBitmap(b);
						u.setHead_img(arg0);
						ZmApplication.getInstance().setUser(u);
						dialog.dismiss();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						dialog.dismiss();
						Toast.makeText(EditorActivity.this, "网络连接出错！",
								Toast.LENGTH_SHORT).show();
					}
				});
		sp.putParams("uhead", bitmapurl);
		sp.putParams("username", user.getUser_id());
		sp.putParams("password", user.getPassword());
		sp.putParams("type", "3");
		ZmApplication.getInstance().getRequestQueue().add(sp);
	}

}
