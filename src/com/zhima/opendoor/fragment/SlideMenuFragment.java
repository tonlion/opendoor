package com.zhima.opendoor.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhima.opendoor.R;
import com.zhima.opendoor.adapter.ArticleListAdapter;
import com.zhima.opendoor.adapter.FinfingAdapter;
import com.zhima.opendoor.adapter.RecentDiscussAdapter;
import com.zhima.opendoor.adapter.UserAdapter;
import com.zhima.opendoor.application.ZmApplication;
import com.zhima.opendoor.entity.Article;
import com.zhima.opendoor.entity.Chanel;
import com.zhima.opendoor.entity.Discuss;
import com.zhima.opendoor.entity.Module;
import com.zhima.opendoor.entity.User;
import com.zhima.opendoor.entity.UserFollow;
import com.zhima.opendoor.utils.StringPostRequest;
import com.zhima.opendoor.utils.urlUtils;

public class SlideMenuFragment extends Fragment {
	private ListView listView;
	private List<User> list;
	private Bundle b;
	private Chanel c;
	private List<UserFollow> users = new ArrayList<UserFollow>();
	private List<Article> articles = new ArrayList<Article>();
	private List<Module> modules = new ArrayList<Module>();
	private List<Discuss> discuss=new ArrayList<Discuss>();
	private UserAdapter uAdapter;
	private FinfingAdapter mAdapter;
	private RecentDiscussAdapter rAdapter;
	private LinearLayout showWelcome;
	private ArticleListAdapter lAdapter;
	private TextView yihan;
	private int pageNum;
	private Intent intent;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		b = getArguments();
		if(b.getInt("type", 1)==1){
			c = b.getParcelable("pageCount");
			pageNum=c.getChanel_id();
		}else{
			pageNum=b.getInt("page", 7);
		}
		
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.select_fragment_layout, null);
		listView = (ListView) v.findViewById(R.id.listview);
		showWelcome = (LinearLayout) v.findViewById(R.id.showWelcomee);
		yihan=(TextView) v.findViewById(R.id.yihan);
		initData();
		return v;
	}

	/**
	 * 根据传来的频道的ID从网络端读取数据
	 */
	private void initData() {
		String url;
		String type;
		String name = ZmApplication.getInstance().getUser().getUser_id();
		switch (pageNum) {
		case 1:
			// 关注的人，从网络断读取
			url = urlUtils.localhost + urlUtils.focusList;
			type = "1";
			httpMethod(url, type, name);
			break;
		case 2:
			// 关注我的
			url = urlUtils.localhost + urlUtils.focusList;
			type = "2";
			httpMethod(url, type, name);
			break;
		case 3:
			// 收藏的文章
			url = urlUtils.localhost + urlUtils.personnalControlServlet;
			type = "1";
			httpMethod2(url, type, name);
			break;
		case 4:
			// 收藏的板块
			url = urlUtils.localhost + urlUtils.personnalControlServlet;
			type = "4";
			
			httpMethod3(url, type, name);
			break;
		case 5:
			// 最近浏览的帖子
			url = urlUtils.localhost + urlUtils.personnalControlServlet;
			type = "2";
			httpMethod2(url, type, name);
			break;
		case 6:
			// 最近回复的帖子
			url = urlUtils.localhost + urlUtils.personnalControlServlet;
			type = "3";
			httpMethod4(url, type, name);
			break;
		default:
			break;
		}

	}

	private void httpMethod(String url, String type, String name) {
		StringPostRequest sp = new StringPostRequest(url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						Gson gson = new Gson();
						if (arg0.length() > 5) {
							List<UserFollow> listt = gson.fromJson(arg0,
									new TypeToken<ArrayList<UserFollow>>() {
									}.getType());
							users.addAll(listt);
							uAdapter = new UserAdapter(users, getActivity());
							listView.setAdapter(uAdapter);
						} else {
							listView.setVisibility(View.GONE);
							showWelcome.setVisibility(View.VISIBLE);
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						yihan.setText("哎呀，网络连接失败");
						showWelcome.setVisibility(View.VISIBLE);
					
					}
				});
		sp.putParams("name", name);
		sp.putParams("type", type);
		ZmApplication.getInstance().getRequestQueue().add(sp);

	}

	private void httpMethod2(String url, String type, String name) {
		StringPostRequest sp = new StringPostRequest(url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						if (arg0.length() > 5) {
							Gson gson = new Gson();
							List<Article> listt = gson.fromJson(arg0,
									new TypeToken<ArrayList<Article>>() {
									}.getType());
							articles.addAll(listt);
							lAdapter = new ArticleListAdapter(articles,
									getActivity());
							listView.setAdapter(lAdapter);
						} else {
							listView.setVisibility(View.GONE);
							showWelcome.setVisibility(View.VISIBLE);
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						yihan.setText("哎呀，网络连接失败");

						showWelcome.setVisibility(View.VISIBLE);
					
					}
				});
		sp.putParams("name", name);
		sp.putParams("type", type);
		ZmApplication.getInstance().getRequestQueue().add(sp);

	}
	private void httpMethod3(String url, String type, String name) {
		StringPostRequest sp = new StringPostRequest(url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						if (arg0.length() > 5) {
							Gson gson = new Gson();
							List<Module> listt = gson.fromJson(arg0,
									new TypeToken<ArrayList<Module>>() {
									}.getType());
							modules.addAll(listt);
							mAdapter = new FinfingAdapter(modules,
									getActivity());
							listView.setAdapter(mAdapter);
						} else {
							listView.setVisibility(View.GONE);
							showWelcome.setVisibility(View.VISIBLE);
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						yihan.setText("哎呀，网络连接失败");

						showWelcome.setVisibility(View.VISIBLE);

					}
				});
		sp.putParams("name", name);
		sp.putParams("type", type);
		ZmApplication.getInstance().getRequestQueue().add(sp);

	}
	private void httpMethod4(String url, String type, String name) {
		StringPostRequest sp = new StringPostRequest(url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						if (arg0.length() > 5) {
							Gson gson = new Gson();
							List<Discuss> listt = gson.fromJson(arg0,
									new TypeToken<ArrayList<Discuss>>() {
									}.getType());
							discuss.addAll(listt);
							rAdapter = new RecentDiscussAdapter(discuss,
									getActivity());
							listView.setAdapter(rAdapter);
						} else {
							listView.setVisibility(View.GONE);
							showWelcome.setVisibility(View.VISIBLE);
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						yihan.setText("哎呀，网络连接失败");

						showWelcome.setVisibility(View.VISIBLE);
					
					}
				});
		sp.putParams("name", name);
		sp.putParams("type", type);
		ZmApplication.getInstance().getRequestQueue().add(sp);

	}

}
