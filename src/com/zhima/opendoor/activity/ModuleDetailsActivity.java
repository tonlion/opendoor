package com.zhima.opendoor.activity;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhima.opendoor.R;
import com.zhima.opendoor.adapter.ArticleAdapter;
import com.zhima.opendoor.application.ZmApplication;
import com.zhima.opendoor.entity.Article;
import com.zhima.opendoor.entity.Module;
import com.zhima.opendoor.utils.ImageLoaderUtils;
import com.zhima.opendoor.utils.StringPostRequest;
import com.zhima.opendoor.utils.urlUtils;
import com.zhima.opendoor.view.SwipeBackActivity;

public class ModuleDetailsActivity extends SwipeBackActivity implements OnClickListener,OnRefreshListener2<ListView> {
	private PullToRefreshListView articleList;
	private TextView moduleTitle;
	private ImageView writeArticle;
	// 从header中得到的数据
	private ImageView moduleImage;
	private TextView favorNum;
	private TextView articleNum;
	private ImageView addFavor;
	private TextView paomadeng;
	private TextView moduleName;
	private boolean isFollowed;
	// 从上一个界面得到数据
	private Intent intent;
	// 当前的模块信息
	private Module module;
	private ArticleAdapter adapter;
	private List<Article> articles;
	private int pageNum=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.module_home_layout);
		// 从上一个activity中得到Module的信息
		intent = getIntent();
		module = intent.getParcelableExtra("module");
		// 初始化list
		articles = new ArrayList<Article>();
		articleList = (PullToRefreshListView) findViewById(R.id.article_List);
		moduleTitle = (TextView) findViewById(R.id.moduleTitle);
		moduleTitle.setOnClickListener(this);
		moduleTitle.setText(module.getModule_name());
		writeArticle = (ImageView) findViewById(R.id.writeModule);
		writeArticle.setOnClickListener(this);
		View header = addHeader();
		articleList.getRefreshableView().addHeaderView(header);
		adapter = new ArticleAdapter(articles, this);
		articleList.setAdapter(adapter);
		articleList.setMode(Mode.BOTH);
		articleList.setOnRefreshListener(this);
		
	}
	@Override
	protected void onStart() {
		super.onStart();
		initData();
		
	
	}
	@Override
	protected void onResume() {
		super.onResume();
		
		initData();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	// 加载头部布局，设置控件属性
	private View addHeader() {
		View v = LayoutInflater.from(this).inflate(R.layout.module_home_header,
				null);
		moduleImage = (ImageView) v.findViewById(R.id.module_img);
		moduleImage.setOnClickListener(this);
		if(module.getImg()!=null){
			ImageLoaderUtils.display(module.getImg(), moduleImage);
		}
		favorNum = (TextView) v.findViewById(R.id.favorNum);
		favorNum.setText(module.getAttentions() + "");
		articleNum = (TextView) v.findViewById(R.id.articleNum);
		articleNum.setText(module.getArticle_num() + "");
		paomadeng = (TextView) v.findViewById(R.id.paomadeng);
		paomadeng.setOnClickListener(this);
		moduleName = (TextView) v.findViewById(R.id.module_name);
		addFavor = (ImageView) v.findViewById(R.id.addFavor);
		if (module.getIsFollowed() == 0) {
			isFollowed = false;
			addFavor.setImageResource(R.drawable.icon_details_likeblack);
		} else {
			addFavor.setImageResource(R.drawable.icon_details_unlikeblack);
			isFollowed = true;
		}
		addFavor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isFollowed) {
					StringPostRequest sp = new StringPostRequest(
							urlUtils.localhost
									+ urlUtils.CollectionModuleServlet,
							new Listener<String>() {

								@Override
								public void onResponse(String arg0) {
									if(arg0.equals("success")){
										int newFavor = Integer.parseInt(favorNum.getText()
												.toString()) - 1;
										favorNum.setText(newFavor + "");
										addFavor.setImageResource(R.drawable.icon_details_likeblack);
										isFollowed = false;
										Toast toast = Toast.makeText(ModuleDetailsActivity.this,
												"取消关注", Toast.LENGTH_SHORT);
										toast.setGravity(Gravity.CENTER, 0, 0);
										toast.show();
									}else{
										Toast.makeText(ModuleDetailsActivity.this, "稍后重试", Toast.LENGTH_SHORT).show();
									}
									

								}
							}, new ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError arg0) {
								Toast.makeText(ModuleDetailsActivity.this, "网络连接失败，请稍后重试", Toast.LENGTH_SHORT).show();

								}
							});
					sp.putParams("module", module.getModule_id()+"");
					sp.putParams("name", ZmApplication.getInstance().getUser().getUser_id());
					sp.putParams("type", 2+"");
					ZmApplication.getInstance().getRequestQueue().add(sp);
					
				} else {
					StringPostRequest sp = new StringPostRequest(
							urlUtils.localhost
									+ urlUtils.CollectionModuleServlet,
							new Listener<String>() {

								@Override
								public void onResponse(String arg0) {
									if(arg0.equals("success")){
										int newFavor = Integer.parseInt(favorNum.getText()
												.toString()) + 1;
										favorNum.setText(newFavor + "");
										addFavor.setImageResource(R.drawable.icon_details_unlikeblack);
										// 更新user个人收藏信息
										isFollowed = true;
										Toast toast = Toast.makeText(ModuleDetailsActivity.this,
												"关注成功", Toast.LENGTH_SHORT);
										toast.setGravity(Gravity.CENTER, 0, 0);
										View v = LayoutInflater.from(ModuleDetailsActivity.this).inflate(
												R.layout.favor_toast, null);
										toast.setView(v);
										toast.show();
									}else{
										Toast.makeText(ModuleDetailsActivity.this, "稍后重试", Toast.LENGTH_SHORT).show();
									}
									

								}
							}, new ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError arg0) {
								Toast.makeText(ModuleDetailsActivity.this, "网络连接失败，请稍后重试", Toast.LENGTH_SHORT).show();

								}
							});
					sp.putParams("module", module.getModule_id()+"");
					sp.putParams("name", ZmApplication.getInstance().getUser().getUser_id());
					sp.putParams("type", 1+"");
					ZmApplication.getInstance().getRequestQueue().add(sp);
					// 数据库中favor数加一
					
				}

			}
		});
		moduleName.setText(module.getModule_name());
		return v;

	}

	private void initData() {
		// 从网络端读取listView的数据
		StringPostRequest sp=new StringPostRequest(urlUtils.articleServlet, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				articles.clear();
				Gson gson=new Gson();
				List<Article> a=gson.fromJson(arg0,new TypeToken<ArrayList<Article>>(){}.getType());
				articles.addAll(a);	
				adapter.notifyDataSetChanged();
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				Toast.makeText(ModuleDetailsActivity.this,"网络连接失败", Toast.LENGTH_SHORT).show();
				
			}
		});
		sp.putParams("type", module.getModule_id()+"");
		sp.putParams("pageNum", "1");
		ZmApplication.getInstance().getRequestQueue().add(sp);
	
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.moduleTitle:
			finish();
			break;
		case R.id.writeModule:
			Intent intent=new Intent(this, WriteArticleActivity.class);
			intent.putExtra("module", module);
			startActivity(intent);
			break;
		case R.id.module_img:
		intent = new Intent(ModuleDetailsActivity.this,
					AboutModuleActivity.class);
			intent.putExtra("this", module);
			startActivity(intent);

			break;
		case R.id.paomadeng:
			startActivity(new Intent(ModuleDetailsActivity.this,LuckyPanActivity.class));
			break;

		default:
			break;
		}

	}
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		//下来刷新
		StringPostRequest sp=new StringPostRequest(urlUtils.articleServlet, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				articles.clear();
				Gson gson=new Gson();
				List<Article> a=gson.fromJson(arg0,new TypeToken<ArrayList<Article>>(){}.getType());
				articles.addAll(a);	
				adapter.notifyDataSetChanged();
			articleList.onRefreshComplete();
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				Toast.makeText(ModuleDetailsActivity.this,"网络连接失败", Toast.LENGTH_SHORT).show();
				
			}
		});
		sp.putParams("type", module.getModule_id()+"");
		sp.putParams("pageNum", "1");
		ZmApplication.getInstance().getRequestQueue().add(sp);
		
	}
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		//上拉加载更多
		// 从网络端读取listView的数据
				StringPostRequest sp=new StringPostRequest(urlUtils.articleServlet, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						//articles.clear();
						Gson gson=new Gson();
						List<Article> a=gson.fromJson(arg0,new TypeToken<ArrayList<Article>>(){}.getType());
						articles.addAll(a);	
						adapter.notifyDataSetChanged();
					articleList.onRefreshComplete();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(ModuleDetailsActivity.this,"网络连接失败", Toast.LENGTH_SHORT).show();
						
					}
				});
				sp.putParams("type", module.getModule_id()+"");
				++pageNum;
				sp.putParams("pageNum", pageNum+"");
				ZmApplication.getInstance().getRequestQueue().add(sp);
		
	}

}
