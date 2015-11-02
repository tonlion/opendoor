package com.zhima.opendoor.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.zhima.opendoor.R;
import com.zhima.opendoor.application.ZmApplication;
import com.zhima.opendoor.entity.Module;
import com.zhima.opendoor.utils.ExpressionUtil;
import com.zhima.opendoor.utils.StringPostRequest;
import com.zhima.opendoor.view.MyListView;
import com.zhima.opendoor.view.SwipeBackActivity;

public class WriteArticleActivity extends SwipeBackActivity implements OnClickListener,
		OnFocusChangeListener {
	private TextView publishArticle;
	private Button publish;
	private EditText title;
	private EditText content;
	private Button addPic;
	private Button addExp;
	private List<String> spanner;
	private List<String> images;
	private String all;
	private Uri uri;
	private Bitmap pic;
	private Intent intent;
	private Module module;
	// 添加表情的相关控件
	private int[] eImages;
	private int[] eImages1;
	private int[] eImages2;
	private SimpleAdapter simpleAdapter;
	private List<Map<String, Object>> listItems;
	private ViewPager viewPager;
	private ArrayList<GridView> grids;
	private GridView gView1;
	private GridView gView2;
	private GridView gView3;
	private ImageView page0;
	private ImageView page1;
	private ImageView page2;
	private LinearLayout llBottonLin = null;
	private boolean isShow = false;
	private CharSequence string;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_article_layout);
		intent = getIntent();
		images = new ArrayList<String>();
		spanner = new ArrayList<String>();
		module = intent.getParcelableExtra("module");
		publishArticle = (TextView) findViewById(R.id.publishArticle);
		publishArticle.setOnClickListener(this);
		publish = (Button) findViewById(R.id.publish);
		publish.setOnClickListener(this);
		title = (EditText) findViewById(R.id.write_article_title);
		content = (EditText) findViewById(R.id.write_article_content);
		content.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				if (start + count < s.length() && count > 1) {
				/*	Toast.makeText(getApplicationContext(),
							s.subSequence(start, count + start),
							Toast.LENGTH_LONG).show();*/
					String sub = s.subSequence(start, count + start).toString();
					for (int i = 0; i < spanner.size(); i++) {
						if (spanner.get(i).equals(sub)) {
							spanner.remove(i);
							images.remove(i);
						}
					}
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				string = s;
			}
		});
		addPic = (Button) findViewById(R.id.writeAddPic);
		addPic.setOnClickListener(this);
		title.setOnFocusChangeListener(this);
		addExp = (Button) findViewById(R.id.writeAddExp);
		// addExp.setOnClickListener(this);
		content.setOnFocusChangeListener(this);
		// 表情相关
		llBottonLin = (LinearLayout) findViewById(R.id.ll_botton_lin2);
		viewPager = (ViewPager) findViewById(R.id.viewpager2);
		page0 = (ImageView) findViewById(R.id.page0_select2);
		page1 = (ImageView) findViewById(R.id.page1_select2);
		page2 = (ImageView) findViewById(R.id.page2_select2);
		showData();
		setListener();
		// checkFocus();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.publishArticle:
			finish();
			break;
		case R.id.publish:
			if(title.getText().toString()==null||title.getText().toString().length()==0){
				Toast.makeText(this, "标题不可为空哦~", Toast.LENGTH_SHORT).show();
				return;
			}
			all = string.toString();
			for (int i = 0; i < spanner.size(); i++) {
				all = all.replaceAll(spanner.get(i), images.get(i));
			}
			post();
			break;
		case R.id.writeAddPic:
			// 添加图片
			// 点击上传
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, 2);

			break;
		default:
			break;
		}

	}

	/**
	 * 通过路径获取系统图片
	 * 
	 * @param uri
	 * @return
	 */
	private Bitmap getBitmap(Uri uri) {
		Bitmap pic = null;
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inJustDecodeBounds = true;
		Display display = getWindowManager().getDefaultDisplay();
		int dw = display.getWidth();
		int dh = display.getHeight();
		try {
			pic = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri), null, op);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int wRatio = (int) Math.ceil(op.outWidth / (float) dw);
		int hRatio = (int) Math.ceil(op.outHeight / (float) dh);
		if (wRatio > 1 && hRatio > 1) {
			op.inSampleSize = wRatio + hRatio;
		}
		op.inJustDecodeBounds = false;
		try {
			pic = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri), null, op);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return pic;
	}

	/**
	 * 图片转成SpannableString加到EditText中
	 * 
	 * @param pic
	 * @param uri
	 * @return
	 */
	private SpannableString getBitmapMime(Bitmap pic, Uri uri) {
		String smile = uri.getPath();
		SpannableString ss = new SpannableString(smile);
		ImageSpan span = new ImageSpan(this, pic);
		ss.setSpan(span, 0, smile.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	/**
	 * 这里是重点
	 */
	private void insertIntoEditText(SpannableString ss) {
		Editable et = content.getText();// 先获取Edittext中的内容
		int start = content.getSelectionStart();
		et.insert(start, ss);// 设置ss要添加的位置
		content.setText(et);// 把et添加到Edittext中
		content.setSelection(start + ss.length());// 设置Edittext中光标在最后面显示
	}

	/**
	 * 上传照片到服务器
	 * 
	 * @param uriStr
	 */
	public void sendImage(String uriStr) {
		final ProgressDialog dialog = ProgressDialog.show(this, "", "加载中");
		StringPostRequest request = new StringPostRequest(
				"http://192.168.253.1/zhi/servlet/ImageServlet",
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						// StringRequest请求时
						String img = "<br/><zhima><img width='330' src=\"" + arg0
								+ "\"/></zhima><br/>";
						images.add(img);
						dialog.dismiss();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						dialog.dismiss();
						Toast.makeText(getApplicationContext(), "出错率",
								Toast.LENGTH_SHORT).show();
						images.add("");
					}
				});
		request.putParams("image", uriStr);
		ZmApplication.getInstance().getRequestQueue().add(request);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case 2:
			uri = data.getData();
			pic = getBitmap(data.getData());
			sendImage(convertBitMap(pic));
			insertIntoEditText(getBitmapMime(pic, uri));
			spanner.add(uri.getPath());
			break;
		}
	}

	/**
	 * 将bitmap转换成base64位编码的字符串
	 * 
	 * @param b
	 * @return
	 */
	public String convertBitMap(Bitmap b) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.JPEG, 100, out);
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] te = out.toByteArray();
		byte[] tes = Base64.encode(te, Base64.DEFAULT);
		return new String(tes);
	}

	/**
	 * 将内容上传
	 */
	private void post() {
		final ProgressDialog dialog = ProgressDialog.show(this, "", "加载中");
		StringPostRequest request = new StringPostRequest(
				"http://192.168.253.1/zhi/servlet/AddArticleServlet",
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						if (arg0.equals("error")) {
							dialog.dismiss();
							Toast.makeText(getApplicationContext(), "失败",
									Toast.LENGTH_SHORT).show();
						} else {
							dialog.dismiss();
							Toast.makeText(getApplicationContext(), "文章发表成功",
									Toast.LENGTH_SHORT).show();
							startActivity(new Intent(WriteArticleActivity.this,ModuleDetailsActivity.class));
							
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						dialog.dismiss();
						Toast.makeText(getApplicationContext(), "联网失败",
								Toast.LENGTH_SHORT).show();
					}
				});
		request.putParams("title", title.getText().toString());
		request.putParams("content", all);
		request.putParams("module", module.getModule_id() + "");
		request.putParams("auther", ZmApplication.getInstance().getUser()
				.getUser_id());
		ZmApplication.getInstance().getRequestQueue().add(request);
	}

	/**
	 * 表情相关！
	 */
	private void showData() {
		ExpressionUtil.getIntense();
		eImages = ExpressionUtil.expressionImgs;
		ExpressionUtil.getIntense();
		ExpressionUtil.getIntense();
		eImages1 = ExpressionUtil.expressionImgs1;
		ExpressionUtil.getIntense();
		ExpressionUtil.getIntense();
		eImages2 = ExpressionUtil.expressionImgs2;
		ExpressionUtil.getIntense();
		LayoutInflater inflater = LayoutInflater.from(this);
		grids = new ArrayList<GridView>();
		gView1 = (GridView) inflater.inflate(R.layout.grid1, null);
		listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 24; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", eImages[i]);
			listItems.add(listItem);
		}
		simpleAdapter = new SimpleAdapter(WriteArticleActivity.this, listItems,
				R.layout.single_expression, new String[] { "image" },
				new int[] { R.id.image });
		gView1.setAdapter(simpleAdapter);
		grids.add(gView1);
		gView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String imageName = view.getResources()
						.getResourceName(eImages[position]).split("/")[1];
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						eImages[position]);
				ImageSpan imageSpan = new ImageSpan(WriteArticleActivity.this,
						bitmap);
				SpannableString spannableString = new SpannableString(imageName);
				spannableString.setSpan(imageSpan, 0, imageName.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				content.append(spannableString);
			}
		});

		gView2 = (GridView) inflater.inflate(R.layout.grid2, null);
		grids.add(gView2);
		gView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String imageName = view.getResources()
						.getResourceName(eImages1[position]).split("/")[1];
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						eImages1[position]);
				ImageSpan imageSpan = new ImageSpan(WriteArticleActivity.this,
						bitmap);
				SpannableString spannableString = new SpannableString(imageName);
				spannableString.setSpan(imageSpan, 0, imageName.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				content.append(spannableString);
			}
		});

		gView3 = (GridView) inflater.inflate(R.layout.grid3, null);
		grids.add(gView3);
		gView3.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String imageName = view.getResources()
						.getResourceName(eImages2[position]).split("/")[1];
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						eImages2[position]);
				ImageSpan imageSpan = new ImageSpan(WriteArticleActivity.this,
						bitmap);
				SpannableString spannableString = new SpannableString(imageName);
				spannableString.setSpan(imageSpan, 0, imageName.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				content.append(spannableString);
			}
		});

		// 填充ViewPager的数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return grids.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(grids.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(grids.get(position));
				return grids.get(position);
			}
		};

		viewPager.setAdapter(mPagerAdapter);
		// viewPager.setAdapter();
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
	}

	private void setListener() {
		addExp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				if (isShow) {
					llBottonLin.setVisibility(View.GONE);
					viewPager.setVisibility(View.GONE);
					isShow = false;
				} else {
					llBottonLin.setVisibility(View.VISIBLE);
					viewPager.setVisibility(View.VISIBLE);
					isShow = true;
				}
			}
		});
	}

	// ** 指引页面改监听器 */
	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			System.out.println("页面滚动" + arg0);

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			System.out.println("换页了" + arg0);
		}

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				page0.setImageDrawable(getResources().getDrawable(
						R.drawable.page_focused));
				page1.setImageDrawable(getResources().getDrawable(
						R.drawable.page_unfocused));

				break;
			case 1:
				page1.setImageDrawable(getResources().getDrawable(
						R.drawable.page_focused));
				page0.setImageDrawable(getResources().getDrawable(
						R.drawable.page_unfocused));
				page2.setImageDrawable(getResources().getDrawable(
						R.drawable.page_unfocused));
				List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
				// 生成24个表情
				for (int i = 0; i < 24; i++) {
					Map<String, Object> listItem = new HashMap<String, Object>();
					listItem.put("image", eImages1[i]);
					listItems.add(listItem);
				}

				SimpleAdapter simpleAdapter = new SimpleAdapter(
						WriteArticleActivity.this, listItems,
						R.layout.single_expression, new String[] { "image" },
						new int[] { R.id.image });
				gView2.setAdapter(simpleAdapter);
				break;
			case 2:
				page2.setImageDrawable(getResources().getDrawable(
						R.drawable.page_focused));
				page1.setImageDrawable(getResources().getDrawable(
						R.drawable.page_unfocused));
				page0.setImageDrawable(getResources().getDrawable(
						R.drawable.page_unfocused));
				List<Map<String, Object>> listItems1 = new ArrayList<Map<String, Object>>();
				// 生成24个表情
				for (int i = 0; i < 24; i++) {
					Map<String, Object> listItem = new HashMap<String, Object>();
					listItem.put("image", eImages2[i]);
					listItems1.add(listItem);
				}

				SimpleAdapter simpleAdapter1 = new SimpleAdapter(
						WriteArticleActivity.this, listItems1,
						R.layout.single_expression, new String[] { "image" },
						new int[] { R.id.image });
				gView3.setAdapter(simpleAdapter1);
				break;

			}
		}
	}

	// 隐藏软键盘
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {

			// 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
			View v = getCurrentFocus();

			if (isShouldHideInput(v, ev)) {
				hideSoftInput(v.getWindowToken());
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
	 * 
	 * @param v
	 * @param event
	 * @return
	 */
	private boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText || v instanceof MyListView)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击EditText的事件，忽略它。
				llBottonLin.setVisibility(View.GONE);
				viewPager.setVisibility(View.GONE);
				isShow = false;
				return false;
			} else {
				return true;
			}
		}
		// 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
		return false;
	}

	/**
	 * 多种隐藏软件盘方法的其中一种
	 * 
	 * @param token
	 */
	private void hideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		switch (arg0.getId()) {
		case R.id.write_article_title:
			if (arg1) {
				addExp.setEnabled(false);
				addPic.setEnabled(false);

			} else {
				addExp.setEnabled(true);
				addPic.setEnabled(true);
			}
			break;
		case R.id.write_article_content:
			if (arg1) {
				addExp.setEnabled(true);
				addPic.setEnabled(true);

			} else {
				addExp.setEnabled(false);
				addPic.setEnabled(false);
			}
			break;

		default:
			break;
		}

	}
}
