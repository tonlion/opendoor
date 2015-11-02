package com.zhima.opendoor.fragment;

import java.util.ArrayList;
import java.util.List;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhima.opendoor.R;
import com.zhima.opendoor.activity.ModuleDetailsActivity;
import com.zhima.opendoor.adapter.FinfingAdapter;
import com.zhima.opendoor.application.ZmApplication;
import com.zhima.opendoor.entity.Module;
import com.zhima.opendoor.entity.User;
import com.zhima.opendoor.utils.ImageLoaderUtils;
import com.zhima.opendoor.utils.StringPostRequest;
import com.zhima.opendoor.utils.urlUtils;

public class FindingFragment extends Fragment implements OnClickListener,
		OnRefreshListener2<ListView> {
	private EditText search;
	private PullToRefreshListView moduleContainer;
	private LinearLayout myAttentionModule;
	private TextView addModule;
	private List<Module> myModule = new ArrayList<Module>();
	private List<Module> allModule = new ArrayList<Module>();
	private FinfingAdapter adapter;
	private User user;
	private View sumHeader;
	private LinearLayout cannot;
	private ImageView doSearch;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		user = ZmApplication.getInstance().getUser();

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.finding_layout, null);

		moduleContainer = (PullToRefreshListView) view
				.findViewById(R.id.moduleContainer);
		moduleContainer.setOnRefreshListener(this);
		search = (EditText) view.findViewById(R.id.search);
		doSearch = (ImageView) view.findViewById(R.id.doSearch);
		doSearch.setOnClickListener(this);
		cannot = (LinearLayout) view.findViewById(R.id.welcomelog);
		initData2();
		return view;
	}

	private View initHeader() {
		View v = LayoutInflater.from(getActivity()).inflate(
				R.layout.finding_header, null);
		myAttentionModule = (LinearLayout) v
				.findViewById(R.id.myAttentionModule);
		initMyAttention();
		addModule = (TextView) v.findViewById(R.id.addModule);
		addModule.setOnClickListener(this);
		return v;
	}

	private void initMyAttention() {

		for (int i = 0; i < myModule.size(); i++) {
			final Module m = myModule.get(i);
			View item = LayoutInflater.from(getActivity()).inflate(
					R.layout.finding_item_favor, null);
			TextView title = (TextView) item.findViewById(R.id.title);
			TextView content = (TextView) item.findViewById(R.id.content);
			title.setText(myModule.get(i).getModule_name());
			ImageView img=(ImageView) item.findViewById(R.id.moduleImg);
			ImageLoaderUtils.display(m.getImg(), img);
			content.setText(myModule.get(i).getModule_content());
			item.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(),
							ModuleDetailsActivity.class);
					intent.putExtra("module", m);
					startActivity(intent);

				}
			});
			myAttentionModule.addView(item);
		}

	}

	private void initData2() {
		StringPostRequest sp = new StringPostRequest(urlUtils.localhost
				+ urlUtils.personnalControlServlet, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {

				Gson gson = new Gson();
				List<Module> m = gson.fromJson(arg0,
						new TypeToken<ArrayList<Module>>() {
						}.getType());
				myModule.addAll(m);

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

			}
		});
		if (user == null) {
			sp.putParams("name", "12");

		} else {
			sp.putParams("name", user.getUser_id());
		}

		sp.putParams("type", 4 + "");
		ZmApplication.getInstance().getRequestQueue().add(sp);

		// 从服务器中读取数据
		StringPostRequest sp1 = new StringPostRequest(urlUtils.localhost
				+ urlUtils.personnalControlServlet, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				cannot.setVisibility(View.GONE);
				Gson gson = new Gson();
				List<Module> m = gson.fromJson(arg0,
						new TypeToken<ArrayList<Module>>() {
						}.getType());
				allModule.addAll(m);

				sumHeader = initHeader();

				moduleContainer.getRefreshableView().addHeaderView(sumHeader);
				View footer = LayoutInflater.from(getActivity()).inflate(
						R.layout.footer_view, null);
				moduleContainer.getRefreshableView().addFooterView(footer);
				adapter = new FinfingAdapter(allModule, getActivity());
				moduleContainer.setAdapter(adapter);
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

				cannot.setVisibility(View.VISIBLE);

			}
		});
		if (user == null) {
			sp.putParams("name", "12");

		} else {
			sp.putParams("name", user.getUser_id());
		}
		sp1.putParams("type", 5 + "");
		ZmApplication.getInstance().getRequestQueue().add(sp1);
		// 给allModule赋值

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.addModule:

			break;
		case R.id.doSearch:
			if (search.getText().toString() == null
					|| search.getText().toString().length() == 0) {
				Toast.makeText(getActivity(), "请输入搜索内容！", Toast.LENGTH_SHORT)
						.show();
			} else {
				final ProgressDialog dialog = ProgressDialog.show(
						getActivity(), "", "搜索中...");
				StringPostRequest sp = new StringPostRequest(urlUtils.localhost
						+ urlUtils.search, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						if (arg0.length() < 3) {
							Toast.makeText(getActivity(), "抱歉呢，没有相关模块呀",
									Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						} else {
							Gson gson = new Gson();
							List<Module> m = gson.fromJson(arg0,
									new TypeToken<ArrayList<Module>>() {
									}.getType());
							Intent intent = new Intent();
							intent.putParcelableArrayListExtra("module",
									(ArrayList<? extends Parcelable>) m);
							intent.setClass(getActivity(),
									ModuleSearchActivity.class);
							search.setText("");
							startActivity(intent);
							dialog.dismiss();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(getActivity(), "网络连接失败",
								Toast.LENGTH_SHORT).show();
						dialog.dismiss();

					}
				});
				sp.putParams("search", search.getText().toString());
				ZmApplication.getInstance().getRequestQueue().add(sp);
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// 下拉刷新
		StringPostRequest sp = new StringPostRequest(urlUtils.localhost
				+ urlUtils.personnalControlServlet, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {

				Gson gson = new Gson();
				myModule.clear();
				List<Module> m = gson.fromJson(arg0,
						new TypeToken<ArrayList<Module>>() {
						}.getType());
				myModule.addAll(m);

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT)
						.show();

			}
		});

		sp.putParams("name", user.getUser_id());
		sp.putParams("type", 4 + "");
		ZmApplication.getInstance().getRequestQueue().add(sp);

		// 从服务器中读取数据
		StringPostRequest sp1 = new StringPostRequest(urlUtils.localhost
				+ urlUtils.personnalControlServlet, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				allModule.clear();

				Gson gson = new Gson();
				List<Module> m = gson.fromJson(arg0,
						new TypeToken<ArrayList<Module>>() {
						}.getType());
				allModule.addAll(m);

				moduleContainer.getRefreshableView()
						.removeHeaderView(sumHeader);
				sumHeader = initHeader();
				moduleContainer.getRefreshableView().addHeaderView(sumHeader);

				adapter.notifyDataSetChanged();
				moduleContainer.onRefreshComplete();
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT)
						.show();
				moduleContainer.onRefreshComplete();
			}
		});
		sp1.putParams("name", user.getUser_id());
		sp1.putParams("type", 5 + "");
		ZmApplication.getInstance().getRequestQueue().add(sp1);
		// 给allModule赋值

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// 上拉加载更多

	}

}
