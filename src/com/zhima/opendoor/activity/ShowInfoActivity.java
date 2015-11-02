package com.zhima.opendoor.activity;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhima.opendoor.R;
import com.zhima.opendoor.adapter.ArticleAdapter;
import com.zhima.opendoor.adapter.FinfingAdapter;
import com.zhima.opendoor.adapter.UserAdapter;
import com.zhima.opendoor.application.ZmApplication;
import com.zhima.opendoor.entity.Article;
import com.zhima.opendoor.entity.Module;
import com.zhima.opendoor.entity.UserFollow;
import com.zhima.opendoor.utils.StringPostRequest;
import com.zhima.opendoor.utils.urlUtils;
import com.zhima.opendoor.view.SwipeBackActivity;

public class ShowInfoActivity extends SwipeBackActivity{
	private TextView wode;
	private ListView showView;
	private Intent intent;
	private int number;
	private List<UserFollow> users=new ArrayList<UserFollow>();
	private List<Article> articles=new ArrayList<Article>();
	private List<Module> modules=new ArrayList<Module>();
	private UserAdapter uAdapter;
	private FinfingAdapter mAdapter;
	private ArticleAdapter aAdapter;
	private LinearLayout showWelcome;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_info_layout);
		intent=getIntent();
		number=intent.getIntExtra("number", 1);
		showView=(ListView) findViewById(R.id.showView);
		showWelcome=(LinearLayout) findViewById(R.id.showWelcome);
		wode=(TextView) findViewById(R.id.wodeshenme);
		wode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		initData();
	}
	private void initData(){
		String url;
		String type;
		String name=ZmApplication.getInstance().getUser().getUser_id();
		switch (number) {
		case 1:
			url=urlUtils.localhost+urlUtils.focusList;
			type="1";
			httpMethod(url,type,name);
			wode.setText("我的关注");
			break;
		case 2:
			url=urlUtils.localhost+urlUtils.focusList;
			type="2";
			httpMethod(url,type,name);
			wode.setText("我的粉丝");
			break;
		case 3:
			url=urlUtils.localhost+urlUtils.personnalControlServlet;
			type="1";
			httpMethod2(url,type,name);
			wode.setText("我的文章");
			break;
		case 4:
			url=urlUtils.localhost+urlUtils.personnalControlServlet;
			type="4";
			httpMethod3(url,type,name);
			wode.setText("我的板块");
			break;

		default:
			break;
		}
	}
	private void httpMethod(String url,String type,String name){
		StringPostRequest sp=new StringPostRequest(url, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {	
				Gson gson=new Gson();
				if(arg0.length()>5){
					List<UserFollow> listt=gson.fromJson(arg0, new TypeToken<ArrayList<UserFollow>>(){}.getType());
					users.addAll(listt);
					uAdapter=new UserAdapter(users,ShowInfoActivity.this);
					showView.setAdapter(uAdapter);
				}else{
					showView.setVisibility(View.GONE);
					showWelcome.setVisibility(View.VISIBLE);
				}
			

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				Toast.makeText(ShowInfoActivity.this, "网络连接问题", Toast.LENGTH_SHORT).show();
				
			}
		});
		sp.putParams("name", name);
		sp.putParams("type", type);
		ZmApplication.getInstance().getRequestQueue().add(sp);
		
	}
	private void httpMethod2(String url,String type,String name){
		StringPostRequest sp=new StringPostRequest(url, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				if(arg0.length()>5){
					Gson gson=new Gson();
					List<Article>listt=gson.fromJson(arg0, new TypeToken<ArrayList<Article>>(){}.getType());
					articles.addAll(listt);
					aAdapter=new ArticleAdapter(articles,ShowInfoActivity.this);
					showView.setAdapter(aAdapter);
				}else{
					showView.setVisibility(View.GONE);
					showWelcome.setVisibility(View.VISIBLE);
				}
				

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				Toast.makeText(ShowInfoActivity.this, "网络连接问题", Toast.LENGTH_SHORT).show();
				
			}
		});
		sp.putParams("name", name);
		sp.putParams("type", type);
		ZmApplication.getInstance().getRequestQueue().add(sp);
		
	}
	private void httpMethod3(String url,String type,String name){
		StringPostRequest sp=new StringPostRequest(url, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				if(arg0.length()>5){
					Gson gson=new Gson();
					List<Module>listt=gson.fromJson(arg0, new TypeToken<ArrayList<Module>>(){}.getType());
					modules.addAll(listt);
					mAdapter=new FinfingAdapter(modules, ShowInfoActivity.this);
					showView.setAdapter(mAdapter);
				}else{
					showView.setVisibility(View.GONE);
					showWelcome.setVisibility(View.VISIBLE);
				}
				

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				Toast.makeText(ShowInfoActivity.this, "网络连接问题", Toast.LENGTH_SHORT).show();
				
			}
		});
		sp.putParams("name", name);
		sp.putParams("type", type);
		ZmApplication.getInstance().getRequestQueue().add(sp);
		
	}


}
