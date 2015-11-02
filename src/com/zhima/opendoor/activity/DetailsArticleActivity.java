package com.zhima.opendoor.activity;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.RenrenSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.zhima.opendoor.R;
import com.zhima.opendoor.adapter.DiscussedAdapter;
import com.zhima.opendoor.application.ZmApplication;
import com.zhima.opendoor.entity.Article;
import com.zhima.opendoor.entity.Discuss;
import com.zhima.opendoor.entity.DiscussList;
import com.zhima.opendoor.tuisongfenxiang.Constants;
import com.zhima.opendoor.tuisongfenxiang.CustomShareBoard;
import com.zhima.opendoor.utils.ExpressionUtil;
import com.zhima.opendoor.utils.HtmlImageGetter;
import com.zhima.opendoor.utils.ImageLoaderUtils;
import com.zhima.opendoor.utils.StringPostRequest;
import com.zhima.opendoor.utils.urlUtils;
import com.zhima.opendoor.view.SwipeBackActivity;

public class DetailsArticleActivity extends SwipeBackActivity implements
		OnClickListener, OnFocusChangeListener {
	private TextView module;
	private ImageView more;
	private ImageView ownerImg;
	private TextView ownerName;
	private TextView ownerTime;
	private WebView content;
	private ListView discuss;
	private TextView howMany;
	private TextView menu_collection;
	// װ���۵�list
	private List<DiscussList> disList = new ArrayList<DiscussList>();
	// ���۵�adapter
	private DiscussedAdapter disAdapter;
	private Intent intent;
	private Article article; // ����һ���ط�������������ֵ
	private boolean isZan = false;
	// �ı�focus�¼�֮���edit����
	private ImageView dianzan;
	private EditText editReply;
	private ImageView addPic;
	private ImageView addExp;
	private Button sendMessage;
	// ��ӱ������ؿؼ�
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
	private List<Discuss> ceshi = new ArrayList<Discuss>();
	private List<Discuss> ceshi1 = new ArrayList<Discuss>();

	private int useZan;
	private final UMSocialService mController = UMServiceFactory
			.getUMSocialService(Constants.DESCRIPTOR);
	private SHARE_MEDIA mPlatform = SHARE_MEDIA.SINA;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_article_layout);

		intent = getIntent();
		article = intent.getParcelableExtra("article");
		module = (TextView) findViewById(R.id.module_name_article);
		module.setText(article.getArticle_title());
		module.setOnClickListener(this);
		more = (ImageView) findViewById(R.id.more);
		more.setOnClickListener(this);
		// list
		discuss = (ListView) findViewById(R.id.discussList);
		// ���üӵ�headView��
		dianzan = (ImageView) findViewById(R.id.dianzan);
		dianzan.setOnClickListener(this);
		editReply = (EditText) findViewById(R.id.editReply);
		editReply.setOnFocusChangeListener(this);

		// �������
		llBottonLin = (LinearLayout) findViewById(R.id.ll_botton_lin);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		page0 = (ImageView) findViewById(R.id.page0_select);
		page1 = (ImageView) findViewById(R.id.page1_select);
		page2 = (ImageView) findViewById(R.id.page2_select);

		// �ı䲼��֮���edit
		addPic = (ImageView) findViewById(R.id.addPic);
		addPic.setOnClickListener(this);
		addExp = (ImageView) findViewById(R.id.addExp);
		// addExp.setOnClickListener(this);
		sendMessage = (Button) findViewById(R.id.sendMessage);
		sendMessage.setOnClickListener(this);
		// ����Ч��
		showData();
		setListener();
		loadData();
		discuss.addHeaderView(initHeader());
		View footer = LayoutInflater.from(this).inflate(R.layout.footer_view,
				null);
		discuss.addFooterView(footer);
		discuss.setSelector(new ColorDrawable(Color.TRANSPARENT));
		// �õ�adapter
		disAdapter = new DiscussedAdapter(disList, this);
		discuss.setAdapter(disAdapter);
		addScan();
		configPlatforms();

	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// ����������
		loadData();

	}

	/**
	 * �������۵ķ���
	 * 
	 * @return
	 */
	private void sendMessage() {
		final String message = editReply.getText().toString();
		if (message == null || message.length() == 0) {
			Toast.makeText(this, "�������ݲ���Ϊ��", Toast.LENGTH_SHORT).show();
			return;
		}
		StringPostRequest sp = new StringPostRequest(
				urlUtils.articleControlServlet, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						if (!arg0.equals("error")) {
							Toast toast = Toast.makeText(
									DetailsArticleActivity.this, "���۳ɹ�~",
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							View v = LayoutInflater.from(
									DetailsArticleActivity.this).inflate(
									R.layout.favor_toast, null);
							TextView succeed = (TextView) v
									.findViewById(R.id.succeed);
							succeed.setText("���۳ɹ�~");
							toast.setView(v);
							toast.show();
							editReply.setText("");
							DiscussList l = new DiscussList();
							Discuss d = new Discuss();
							d.setArticle_id(article.getArticle_id());
							d.setContent(message);
							d.setDiscussed_id(1000 + (int) (Math.random() * 100));
							d.setDiscussed_item_id(0);
							d.setDiscussed_type(0);
							d.setHead_img(ZmApplication.getInstance().getUser()
									.getHead_img());
							d.setTime(System.currentTimeMillis());
							d.setUser_id(ZmApplication.getInstance().getUser()
									.getUser_id());
							d.setUser_name(ZmApplication.getInstance()
									.getUser().getUser_name());
							l.setMain(d);
							l.setOther(null);
							disList.add(l);

							disAdapter.notifyDataSetChanged();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(DetailsArticleActivity.this, "��������ʧ��",
								Toast.LENGTH_SHORT).show();

					}
				});
		sp.putParams("name", ZmApplication.getInstance().getUser().getUser_id());
		sp.putParams("type", "2");
		sp.putParams("content", message);
		sp.putParams("article", article.getArticle_id() + "");
		ZmApplication.getInstance().getRequestQueue().add(sp);
		// ���¹����ݿ�֮�����͸���
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
		ss.putParams("selStuNos", article.getAuthor());
		ss.putParams("msg", message);
		ss.putParams("title", ZmApplication.getInstance().getUser()
				.getUser_name()
				+ " �ظ�����");
		ZmApplication.getInstance().getRequestQueue().add(ss);
	}

	private View initHeader() {
		// �ӵ�headerview��
		View v = LayoutInflater.from(this).inflate(
				R.layout.discuss_detail_header, null);

		ownerImg = (ImageView) v.findViewById(R.id.ownerImg);
		ownerImg.setOnClickListener(this);
		if (article.getHead_img() != null) {
			ImageLoaderUtils.display(article.getHead_img(), ownerImg);
		} else {
			ownerImg.setImageResource(R.drawable.zhima);
		}

		ownerName = (TextView) v.findViewById(R.id.ownerName);
		ownerName.setText(article.getUser_name());
		ownerTime = (TextView) v.findViewById(R.id.ownerTime);
		howMany = (TextView) v.findViewById(R.id.howManyZan);
		howMany.setText("����" + article.getZan() + "" + "���޹�...");
		SimpleDateFormat sdFormatter = new SimpleDateFormat("MM-dd hh:mm");
		String retStrFormatNowDate = sdFormatter.format(article
				.getCreate_time());
		ownerTime.setText(retStrFormatNowDate);
		content = (WebView) v.findViewById(R.id.article_content);
		/*
		 * HtmlImageGetter ReviewImgGetter = new HtmlImageGetter(
		 * DetailsArticleActivity.this, content);
		 */
		/*
		 * CharSequence text = Html.fromHtml(article.getArticle_content(),
		 * ReviewImgGetter,null);
		 *//*
			 * Pattern pp = Pattern.compile("smiley_[0-9]{1,2}"); Matcher mm =
			 * pp.matcher(text); SpannableString sb = new SpannableString(text);
			 */
		/*
		 * while (mm.find()) { ImageSpan span = new ImageSpan(this,
		 * getResourceId(mm.group())); sb.setSpan(span, mm.start(),
		 * mm.group().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE); }
		 */
		// content.setText(sb);
		// content.setClickable(true);
		// content.setMovementMethod(LinkMovementMethod.getInstance());
		content.loadDataWithBaseURL(null, article.getArticle_content(),
				"text/html", "utf-8", null);

		// ����֮���������
		return v;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.module_name_article:
			finish();
			break;
		case R.id.dianzan:
			if (isZan) {
				Animation animation = AnimationUtils.loadAnimation(this,
						R.anim.dianzan_all);
				animation.setFillAfter(true);
				dianzan.startAnimation(animation);
				dianzan.setImageResource(R.drawable.btn_pb_like_n);
				Toast.makeText(DetailsArticleActivity.this, "ȡ������~",
						Toast.LENGTH_SHORT).show();
				useZan = useZan - 1;
				howMany.setText("����" + useZan + "" + "���޹�...");
				isZan = false;
			} else {
				dianZan();
			}

			break;
		case R.id.addPic:
			// Toast.makeText(this, "�����ܲ��ܵ�", Toast.LENGTH_SHORT).show();
			break;
		case R.id.ownerImg:
			Intent intent = new Intent();
			intent.putExtra("userId", article.getAuthor());
			startActivity(new Intent(DetailsArticleActivity.this,
					OtherPersonInfoActivity.class));
			break;
		case R.id.addExp:
			// Toast.makeText(this, "�ٴβ���", Toast.LENGTH_SHORT).show();
			break;
		case R.id.more:
			View windowView = LayoutInflater.from(this).inflate(
					R.layout.popwindow_layout, null);
			final PopupWindow window = new PopupWindow(windowView,
					(int) (TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 150, getResources()
									.getDisplayMetrics())),
					(int) (TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 150, getResources()
									.getDisplayMetrics())));
			menu_collection = (TextView) windowView
					.findViewById(R.id.menu_collection);
			windowView.findViewById(R.id.menu_collection).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// �ղ�
							if (ZmApplication.getInstance().getUser() != null) {
								CollectionArticle();
							} else {
								Toast.makeText(arg0.getContext(), "��û��¼Ŷ~",
										Toast.LENGTH_SHORT).show();
							}

						}
					});
			windowView.findViewById(R.id.menu_share).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							postShare();

						}
					});
			windowView.findViewById(R.id.menu_jump).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// �ղ�
							Toast.makeText(arg0.getContext(), "��תҳ��~",
									Toast.LENGTH_SHORT).show();
							window.dismiss();
							View jump = LayoutInflater.from(arg0.getContext())
									.inflate(R.layout.jump_layout, null);
							final PopupWindow jumpW = new PopupWindow(jump,
									LayoutParams.MATCH_PARENT,
									LayoutParams.MATCH_PARENT);
							jump.findViewById(R.id.dismiss).setOnClickListener(
									new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											jumpW.dismiss();
										}
									});
							jump.findViewById(R.id.confirm).setOnClickListener(
									new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											// �ϴ�ҳ�룬adapter.notifydatasetchanged
										}
									});
							jumpW.setFocusable(true);
							jumpW.setOutsideTouchable(true);
							jumpW.setBackgroundDrawable(new ColorDrawable());
							jumpW.showAtLocation(arg0, Gravity.CENTER, 0, 0);

						}
					});
			window.setFocusable(true); // ��������,��popuwindow���Ե��������ⲿ��popuwindow��ʧ
			window.setOutsideTouchable(true);
			window.setBackgroundDrawable(new ColorDrawable());
			// window.showAsDropDown(arg0);
			window.showAsDropDown(arg0, 0, 8);// ��λ
			// window.showAtLocation(arg0, Gravity.RIGHT, 0, -100);
			break;
		case R.id.sendMessage:
			sendMessage();
			break;

		default:
			break;
		}

	}

	/**
	 * �������ݵķ���
	 */
	private void loadData() {

		// ������ ,type=0��type=1
		StringPostRequest sp = new StringPostRequest(urlUtils.localhost
				+ urlUtils.DiscussServlet, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				if (arg0.length() > 7) {
					Gson gson = new Gson();
					ceshi.clear();
					List<Discuss> ceshii = gson.fromJson(arg0,
							new TypeToken<ArrayList<Discuss>>() {
							}.getType());
					ceshi.addAll(ceshii);

				} else {

				}
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				Toast.makeText(DetailsArticleActivity.this, "���ݼ����쳣",
						Toast.LENGTH_SHORT).show();

			}
		});
		sp.putParams("article", article.getArticle_id() + "");
		sp.putParams("type", 0 + "");
		ZmApplication.getInstance().getRequestQueue().add(sp);

		StringPostRequest sp1 = new StringPostRequest(urlUtils.localhost
				+ urlUtils.DiscussServlet, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				if (arg0.length() > 7) {
					Gson gson = new Gson();
					disList.clear();
					ceshi1.clear();
					List<Discuss> ceshii = gson.fromJson(arg0,
							new TypeToken<ArrayList<Discuss>>() {
							}.getType());
					ceshi1.addAll(ceshii);
					List<Discuss> other;
					DiscussList list = null;
					for (int i = 0; i < ceshi.size(); i++) {
						other = new ArrayList<Discuss>();
						for (int j = 0; j < ceshi1.size(); j++) {

							if (ceshi1.get(j).getDiscussed_item_id() == ceshi
									.get(i).getDiscussed_id()) {
								other.add(ceshi1.get(j));
							}
						}
						list = new DiscussList();
						list.setMain(ceshi.get(i));
						list.setOther(other);
						disList.add(list);
						disAdapter.notifyDataSetChanged();
					}
				} else {
					disList.clear();
					List<Discuss> other = new ArrayList<Discuss>();
					DiscussList list = null;
					for (int i = 0; i < ceshi.size(); i++) {
						list = new DiscussList();
						list.setMain(ceshi.get(i));
						list.setOther(other);
						disList.add(list);
					}
					disAdapter.notifyDataSetChanged();

				}
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

			}
		});
		sp1.putParams("article", article.getArticle_id() + "");
		sp1.putParams("type", 1 + "");
		ZmApplication.getInstance().getRequestQueue().add(sp1);
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
	 * ������أ�
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
		simpleAdapter = new SimpleAdapter(DetailsArticleActivity.this,
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
						DetailsArticleActivity.this, bitmap);
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
						DetailsArticleActivity.this, bitmap);
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
						DetailsArticleActivity.this, bitmap);
				SpannableString spannableString = new SpannableString(imageName);
				spannableString.setSpan(imageSpan, 0, imageName.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				editReply.append(spannableString);
			}
		});

		// ���ViewPager������������
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

	// ** ָ��ҳ��ļ����� */
	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			System.out.println("ҳ�����" + arg0);

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			System.out.println("��ҳ��" + arg0);
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
				// ����24������
				for (int i = 0; i < 24; i++) {
					Map<String, Object> listItem = new HashMap<String, Object>();
					listItem.put("image", eImages1[i]);
					listItems.add(listItem);
				}

				SimpleAdapter simpleAdapter = new SimpleAdapter(
						DetailsArticleActivity.this, listItems,
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
				// ����24������
				for (int i = 0; i < 24; i++) {
					Map<String, Object> listItem = new HashMap<String, Object>();
					listItem.put("image", eImages2[i]);
					listItems1.add(listItem);
				}

				SimpleAdapter simpleAdapter1 = new SimpleAdapter(
						DetailsArticleActivity.this, listItems1,
						R.layout.single_expression, new String[] { "image" },
						new int[] { R.id.image });
				gView3.setAdapter(simpleAdapter1);
				break;

			}
		}
	}

	// ���������
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {

			// ��õ�ǰ�õ������View��һ������¾���EditText������������ǹ켣�����ʵ�尸�����ƶ����㣩
			View v = getCurrentFocus();

			if (isShouldHideInput(v, ev)) {
				hideSoftInput(v.getWindowToken());
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * ����EditText����������û������������Աȣ����ж��Ƿ����ؼ��̣���Ϊ���û����EditTextʱû��Ҫ����
	 * 
	 * @param v
	 * @param event
	 * @return
	 */
	private boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// ���EditText���¼�����������
				llBottonLin.setVisibility(View.GONE);
				viewPager.setVisibility(View.GONE);
				isShow = false;
				return false;
			} else {
				return true;
			}
		}
		// ������㲻��EditText����ԣ������������ͼ�ջ����꣬��һ�����㲻��EditView�ϣ����û��ù켣��ѡ�������Ľ���
		return false;
	}

	/**
	 * ������������̷���������һ��
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

	private void addScan() {
		StringPostRequest sp = new StringPostRequest(
				urlUtils.articleControlServlet, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				});
		if (ZmApplication.getInstance().getUser() != null) {
			sp.putParams("name", ZmApplication.getInstance().getUser()
					.getUser_id());
		} else {
			sp.putParams("name", "12");
		}
		sp.putParams("article", article.getArticle_id() + "");
		sp.putParams("type", "3");
		ZmApplication.getInstance().getRequestQueue().add(sp);

	}

	private void dianZan() {
		Animation animation = AnimationUtils.loadAnimation(this,
				R.anim.dianzan_all);
		animation.setFillAfter(true);
		dianzan.startAnimation(animation);
		// �ϴ����������и������ݣ��ɹ�֮��
		StringPostRequest sp = new StringPostRequest(
				urlUtils.articleControlServlet, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						dianzan.setImageResource(R.drawable.btn_pb_likeing_n);
						Toast.makeText(DetailsArticleActivity.this, "���޳ɹ�~",
								Toast.LENGTH_SHORT).show();
						isZan = true;
						useZan = article.getZan() + 1;
						howMany.setText("����" + useZan + "" + "���޹�...");

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(DetailsArticleActivity.this, "�Ժ�����",
								Toast.LENGTH_SHORT).show();

					}
				});
		if (ZmApplication.getInstance().getUser() != null) {
			sp.putParams("name", ZmApplication.getInstance().getUser()
					.getUser_id());
		} else {
			sp.putParams("name", "12");
		}
		sp.putParams("article", article.getArticle_id() + "");
		sp.putParams("type", "1");
		ZmApplication.getInstance().getRequestQueue().add(sp);

	}

	private void CollectionArticle() {
		// �ϴ����������и������ݣ��ɹ�֮��
		StringPostRequest sp = new StringPostRequest(
				urlUtils.articleControlServlet, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						if (!arg0.equals("error")) {
							Toast.makeText(DetailsArticleActivity.this,
									"�ղسɹ�~", Toast.LENGTH_SHORT).show();
							menu_collection.setText("ȡ���ղ�");
						} else {
							Toast.makeText(DetailsArticleActivity.this, "�Ժ�����",
									Toast.LENGTH_SHORT).show();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(DetailsArticleActivity.this, "��������ʧ��",
								Toast.LENGTH_SHORT).show();

					}
				});
		if (ZmApplication.getInstance().getUser() != null) {
			sp.putParams("name", ZmApplication.getInstance().getUser()
					.getUser_id());
		} else {
			sp.putParams("name", "12");
		}
		sp.putParams("article", article.getArticle_id() + "");
		sp.putParams("type", "4");
		ZmApplication.getInstance().getRequestQueue().add(sp);

	}

	public int getResourceId(String name) {
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

	private void configPlatforms() {
		// �������SSO��Ȩ
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// �����Ѷ΢��SSO��Ȩ
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		// ���������SSO��Ȩ
		RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(
				DetailsArticleActivity.this, "201874",
				"28401c0964f04a72a14c812d6132fcef",
				"3bf66e42db1e4fa9829b955cc300b737");
		mController.getConfig().setSsoHandler(renrenSsoHandler);

		// ���QQ��QZoneƽ̨
		addQQQZonePlatform();

		// ���΢�š�΢������Ȧƽ̨
		addWXPlatform();
	}

	private void addWXPlatform() {
		// ע�⣺��΢����Ȩ��ʱ�򣬱��봫��appSecret
		// wx967daebe835fbeac������΢�ſ���ƽ̨ע��Ӧ�õ�AppID, ������Ҫ�滻����ע���AppID
		String appId = "wx967daebe835fbeac";
		String appSecret = "5bb696d9ccd75a38c8a0bfe0675559b3";
		// ���΢��ƽ̨
		UMWXHandler wxHandler = new UMWXHandler(DetailsArticleActivity.this,
				appId, appSecret);
		wxHandler.addToSocialSDK();

		// ֧��΢������Ȧ
		UMWXHandler wxCircleHandler = new UMWXHandler(
				DetailsArticleActivity.this, appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	private void addQQQZonePlatform() {
		String appId = "100424468";
		String appKey = "c7394704798a158208a74ab60104f0ba";
		// ���QQ֧��, ��������QQ�������ݵ�target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(
				DetailsArticleActivity.this, appId, appKey);
		qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
		qqSsoHandler.addToSocialSDK();

		// ���QZoneƽ̨
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
				DetailsArticleActivity.this, appId, appKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	private void postShare() {
		CustomShareBoard shareBoard = new CustomShareBoard(
				DetailsArticleActivity.this);
		shareBoard.showAtLocation(DetailsArticleActivity.this.getWindow()
				.getDecorView(), Gravity.BOTTOM, 0, 0);
	}

	/**
	 * ֱ�ӷ����ײ����ӿڡ���������ƽ̨�����ˡ���Ѷ΢�������ꡢ���ˣ���ֱ�ӷ������κν��浯���� ����ƽ̨�ֱ������ͻ��˷���</br>
	 */
	private void directShare() {
		mController.directShare(DetailsArticleActivity.this, mPlatform,
				new SocializeListeners.SnsPostListener() {

					@Override
					public void onStart() {
					}

					@Override
					public void onComplete(SHARE_MEDIA platform, int eCode,
							SocializeEntity entity) {
						String showText = "����ɹ�";
						if (eCode != StatusCode.ST_CODE_SUCCESSED) {
							showText = "����ʧ�� [" + eCode + "]";
						}
						Toast.makeText(DetailsArticleActivity.this, showText,
								Toast.LENGTH_SHORT).show();
					}
				});
	}

}
