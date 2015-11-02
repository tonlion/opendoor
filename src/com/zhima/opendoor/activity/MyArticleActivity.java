package com.zhima.opendoor.activity;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.example.swipemenulistview.swipemenulistview.SwipeMenu;
import com.example.swipemenulistview.swipemenulistview.SwipeMenuCreator;
import com.example.swipemenulistview.swipemenulistview.SwipeMenuItem;
import com.example.swipemenulistview.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhima.opendoor.R;
import com.zhima.opendoor.adapter.ArticleAdapter2;
import com.zhima.opendoor.application.ZmApplication;
import com.zhima.opendoor.entity.Article;
import com.zhima.opendoor.utils.StringPostRequest;
import com.zhima.opendoor.utils.urlUtils;

public class MyArticleActivity extends Activity implements OnClickListener{

	private List<Article> mAppList = new ArrayList<Article>();
	private ArticleAdapter2 mAdapter;
	private SwipeMenuListView mListView;
	private String name;
	private TextView delete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_article_history);
		name = ZmApplication.getInstance().getUser().getUser_id();
		mListView = (SwipeMenuListView) findViewById(R.id.id_swipelistview);
		delete=(TextView) findViewById(R.id.deletedArticle);
		delete.setOnClickListener(this);
		mAdapter = new ArticleAdapter2(mAppList, this);
		mListView.setAdapter(mAdapter);
		initData();
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
						0xCE)));
				// set item width
				openItem.setWidth(dp2px(90));
				// set item title
				openItem.setTitle("打开");
				// set item title fontsize
				openItem.setTitleSize(18);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		mListView.setMenuCreator(creator);

		// step 2. listener item click event
		mListView
				.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(int position,
							SwipeMenu menu, int index) {
						Article item = mAppList.get(position);
						switch (index) {
						case 0:
							Intent intent = new Intent(getApplicationContext(),
									DetailsArticleActivity.class);
							intent.putExtra("article", item);
							startActivity(intent);
							break;
						case 1:
							delete(item);

							break;
						}
						return false;
					}
				});

		// set SwipeListener
		mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

		// set MenuStateChangeListener
		mListView
				.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
					@Override
					public void onMenuOpen(int position) {
					}

					@Override
					public void onMenuClose(int position) {
					}
				});
	}

	private void initData() {
		StringPostRequest sp = new StringPostRequest(urlUtils.localhost
				+ urlUtils.personnalControlServlet, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				if (arg0.length() > 5) {
					Gson gson = new Gson();
					List<Article> listt = gson.fromJson(arg0,
							new TypeToken<ArrayList<Article>>() {
							}.getType());
					mAppList.addAll(listt);
					mAdapter.notifyDataSetChanged();

				} else {

				}

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub

			}
		});
		sp.putParams("name", name);
		sp.putParams("type", "6");
		ZmApplication.getInstance().getRequestQueue().add(sp);
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	private void delete(final Article a) {

		StringPostRequest sp = new StringPostRequest(urlUtils.localhost
				+ urlUtils.personnalControlServlet, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				Toast.makeText(MyArticleActivity.this, "删除成功！",
						Toast.LENGTH_SHORT).show();
				mAppList.remove(a);
				mAdapter.notifyDataSetChanged();

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				Toast.makeText(MyArticleActivity.this, "网络连接错误，稍后再试",
						Toast.LENGTH_SHORT).show();

			}
		});
		sp.putParams("type", "7");
		sp.putParams("name", name);
		sp.putParams("article", a.getArticle_id() + "");
		ZmApplication.getInstance().getRequestQueue().add(sp);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_left) {
			mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
			return true;
		}
		if (id == R.id.action_right) {
			mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.deletedArticle:
			finish();
			break;

		default:
			break;
		}
		
	}


}
