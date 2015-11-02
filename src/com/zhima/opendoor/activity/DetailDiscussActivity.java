package com.zhima.opendoor.activity;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Gravity;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.zhima.opendoor.R;
import com.zhima.opendoor.adapter.SmallDiscussAdapter;
import com.zhima.opendoor.adapter.SmallDiscussAdapter.smallListener;
import com.zhima.opendoor.application.ZmApplication;
import com.zhima.opendoor.entity.Discuss;
import com.zhima.opendoor.entity.DiscussList;
import com.zhima.opendoor.utils.ExpressionUtil;
import com.zhima.opendoor.utils.ImageLoaderUtils;
import com.zhima.opendoor.utils.StringPostRequest;
import com.zhima.opendoor.utils.urlUtils;
import com.zhima.opendoor.view.MyListView;
import com.zhima.opendoor.view.SwipeBackActivity;

public class DetailDiscussActivity extends SwipeBackActivity implements OnClickListener,
		OnFocusChangeListener ,smallListener{
	private Intent intent;
	private Discuss main;
	private List<Discuss> other;
	private MyListView list;
	private ImageView head;
	private TextView name;
	private TextView floor;
	private TextView homeFloor;
	private TextView time;
	private ImageButton send;
	private TextView content;
	private int floorNum;
	// 加载下面的输入框
	private ImageView dianzan;
	private EditText editReply;
	private ImageView addPic;
	private ImageView addExp;
	private Button sendMessage;
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
	private SmallDiscussAdapter smallAdapter;
	private String replyId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_disuss_layout);
		list = (MyListView) findViewById(R.id.sencondPinglun);
		// 获取数据
		intent = getIntent();
		main = intent.getParcelableExtra("main");
		other = intent.getParcelableArrayListExtra("other");
		floorNum = intent.getIntExtra("floor", 1);
		homeFloor=(TextView) findViewById(R.id.someFloor);
		homeFloor.setOnClickListener(this);
		homeFloor.setText("第"+floorNum+""+"楼");
		// 输入框相关布局的加载
		// 不用加到headView中
		dianzan = (ImageView) findViewById(R.id.dianzan);
		dianzan.setVisibility(View.GONE);
		editReply=(EditText) findViewById(R.id.editReply);
		editReply.setOnFocusChangeListener(this);
		editReply.setFocusable(true);
		editReply.setFocusableInTouchMode(true);
	
		// 表情相关
		llBottonLin = (LinearLayout) findViewById(R.id.ll_botton_lin);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		page0 = (ImageView) findViewById(R.id.page0_select);
		page1 = (ImageView) findViewById(R.id.page1_select);
		page2 = (ImageView) findViewById(R.id.page2_select);

		// 改变布局之后的edit
		addPic = (ImageView) findViewById(R.id.addPic);
		addPic.setVisibility(View.VISIBLE);
		addPic.setOnClickListener(this);
		addExp = (ImageView) findViewById(R.id.addExp);
		addExp.setVisibility(View.VISIBLE);
		sendMessage = (Button) findViewById(R.id.sendMessage);
		sendMessage.setOnClickListener(this);
		sendMessage.setVisibility(View.VISIBLE);
		showData();
		setListener();
		list.addHeaderView(initHead());
		smallAdapter=new SmallDiscussAdapter(other, this);
		smallAdapter.setListener(this);
		list.setAdapter(smallAdapter);

	}
	private View initHead() {
		View v = LayoutInflater.from(this).inflate(
				R.layout.detail_discuss_header, null);
		name = (TextView) v.findViewById(R.id.floorName1);
		time = (TextView) v.findViewById(R.id.floorTime1);
		floor = (TextView) v.findViewById(R.id.ownerFloor1);
		head = (ImageView) v.findViewById(R.id.floorImg1);
		if(main.getHead_img()==null||main.getHead_img().length()==0){
			head.setImageResource(R.drawable.zhima);
		}else{
			ImageLoaderUtils.display(main.getHead_img(), head);
		}
		content = (TextView) v.findViewById(R.id.contentDis1);
		send = (ImageButton) v.findViewById(R.id.sendDis1);
		name.setText(main.getUser_name());
		SimpleDateFormat sdFormatter = new SimpleDateFormat("MM-dd hh:mm");
		String retStrFormatNowDate = sdFormatter.format(main.getTime());
		time.setText(retStrFormatNowDate);
		floor.setText("第" + floorNum + "" + "楼");
		
		Pattern pp=Pattern.compile("smiley_[0-9]{1,2}");
		Matcher mm=pp.matcher(main.getContent());
		SpannableString sb=new SpannableString(main.getContent());
		while(mm.find()){
			ImageSpan span=new ImageSpan(DetailDiscussActivity.this,getResourceId(mm.group()));
			sb.setSpan(span, mm.start(), mm.start()+mm.group().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		}
		content.setText(sb);
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
			}
		});

		return v;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.someFloor:
			finish();
			break;
		case R.id.sendMessage:
			updateDiscuss();
			break;

		default:
			break;
		}
		
	}

	private void updateDiscuss() {
		final String message=editReply.getText().toString();
		if(message==null||message.length()==0){
			Toast.makeText(this, "评论内容不可为空", Toast.LENGTH_SHORT).show();
			return;
		}
		StringPostRequest sp=new StringPostRequest(urlUtils.articleControlServlet, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				if(!arg0.equals("error")){
					Toast toast = Toast.makeText(DetailDiscussActivity.this,
							"评论成功~", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					View v = LayoutInflater.from(DetailDiscussActivity.this).inflate(
							R.layout.favor_toast, null);
					TextView succeed=(TextView) v.findViewById(R.id.succeed);
					succeed.setText("评论成功~");
					toast.setView(v);
					toast.show();
					editReply.setText("");
					DiscussList l=new DiscussList();
					Discuss d=new Discuss();
					d.setArticle_id(main.getArticle_id());
					d.setContent(message);
					d.setDiscussed_id(1000);
					d.setDiscussed_item_id(0);
					d.setDiscussed_type(0);
					d.setHead_img(ZmApplication.getInstance().getUser().getHead_img());
					d.setTime(System.currentTimeMillis());
					d.setUser_id(ZmApplication.getInstance().getUser().getUser_id());
					d.setUser_name(ZmApplication.getInstance().getUser().getUser_name());
					other.add(d);		
					smallAdapter.notifyDataSetChanged();
				}
				
				
				
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				Toast.makeText(DetailDiscussActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
			
				
			}
		});
		sp.putParams("name", ZmApplication.getInstance().getUser().getUser_id());
		sp.putParams("type", "2");
		sp.putParams("content", message);
		sp.putParams("article", main.getArticle_id()+"");
		sp.putParams("itemID",main.getDiscussed_id()+"");
		ZmApplication.getInstance().getRequestQueue().add(sp);
		
		StringPostRequest ss = new StringPostRequest(urlUtils.localhost
				+ urlUtils.messagePush, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				
			}
		});
		if(replyId==null||replyId.equals("")){
			ss.putParams("selStuNos",main.getUser_id());
		}else{
			ss.putParams("selStuNos",replyId);
		}
		
		ss.putParams("msg",message);
		ss.putParams("title", ZmApplication.getInstance().getUser().getUser_name()+" 回复了您");
		ZmApplication.getInstance().getRequestQueue().add(ss);
		
	}
	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		if (arg1) {
			dianzan.setVisibility(View.GONE);
			addPic.setVisibility(View.VISIBLE);
			addExp.setVisibility(View.VISIBLE);
			sendMessage.setVisibility(View.VISIBLE);
		} else {
			dianzan.setVisibility(View.VISIBLE);
			addPic.setVisibility(View.GONE);
			addExp.setVisibility(View.GONE);
			sendMessage.setVisibility(View.GONE);
		}

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
		simpleAdapter = new SimpleAdapter(DetailDiscussActivity.this,
				listItems, R.layout.single_expression,
				new String[] { "image" }, new int[] { R.id.image });
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
				ImageSpan imageSpan = new ImageSpan(
						DetailDiscussActivity.this, bitmap);
				SpannableString spannableString = new SpannableString(imageName);
				spannableString.setSpan(imageSpan, 0, imageName.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				editReply.append(spannableString);
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
				ImageSpan imageSpan = new ImageSpan(
						DetailDiscussActivity.this, bitmap);
				SpannableString spannableString = new SpannableString(imageName);
				spannableString.setSpan(imageSpan, 0, imageName.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				editReply.append(spannableString);
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
				ImageSpan imageSpan = new ImageSpan(
						DetailDiscussActivity.this, bitmap);
				SpannableString spannableString = new SpannableString(imageName);
				spannableString.setSpan(imageSpan, 0, imageName.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				editReply.append(spannableString);
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
						DetailDiscussActivity.this, listItems,
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
						DetailDiscussActivity.this, listItems1,
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
		if (v != null && (v instanceof EditText||v instanceof MyListView)) {
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
	private String floorNameReply;
	@Override
	public void getItemContent(String name,String id) {
		floorNameReply=name;
		replyId=id;
		editReply.setText("回复"+" "+floorNameReply+":");
		InputMethodManager im = (InputMethodManager) editReply.getContext().
				getSystemService(Context.INPUT_METHOD_SERVICE);
		editReply.setFocusable(true);
		editReply.setFocusableInTouchMode(true);
		editReply.requestFocus();
		im.showSoftInput(editReply, 0);
		
	}
	public int getResourceId(String name){
		Field filed;
		try {
			filed = R.drawable.class.getField(name);
			return Integer.parseInt(filed.get(null).toString());
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		
		
	}

}
