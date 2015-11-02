package com.zhima.opendoor.activity;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhima.opendoor.R;
import com.zhima.opendoor.adapter.UserAdapter;
import com.zhima.opendoor.application.ZmApplication;
import com.zhima.opendoor.entity.Module;
import com.zhima.opendoor.entity.UserFollow;
import com.zhima.opendoor.utils.ImageLoaderUtils;
import com.zhima.opendoor.utils.StringPostRequest;
import com.zhima.opendoor.utils.urlUtils;
import com.zhima.opendoor.view.SwipeBackActivity;

public class AboutModuleActivity extends SwipeBackActivity implements
		OnClickListener {
	private ListView listView;
	private Button enter;
	private Button follow;
	private TextView about_module;
	private Intent intent;
	// header中的
	private TextView jianjie;
	private TextView favorNum;
	private TextView articleNum;
	private TextView name;
	private ImageView image;
	// 判断关注
	private boolean isFollowed;
	private Module module;
	// 加载数据
	private UserAdapter adapter;
	private List<UserFollow> users;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_module_layout);
		intent = getIntent();
		module = intent.getParcelableExtra("this");
		about_module = (TextView) findViewById(R.id.xinzeng);
		about_module.setOnClickListener(this);
		enter = (Button) findViewById(R.id.enter_module);
		enter.setOnClickListener(this);
		follow = (Button) findViewById(R.id.follow_module);
		if (module.getIsFollowed() == 0) {
			isFollowed = false;
			follow.setText("关注板块");
		} else {
			follow.setText("已关注");
			isFollowed = true;
		}
		follow.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.module_followed);
	
		initData();
		
	

	}

	private View initHeader() {
		View v = LayoutInflater.from(this).inflate(
				R.layout.about_module_layout_header, null);
		jianjie = (TextView) v.findViewById(R.id.jianjie);
		jianjie.setText(module.getModule_content());
		favorNum = (TextView) v.findViewById(R.id.favorNum2);
		name = (TextView) v.findViewById(R.id.module_name2);
		name.setText(module.getModule_name());
		favorNum.setText(module.getAttentions() + "");
		articleNum = (TextView) v.findViewById(R.id.articleNum2);
		articleNum.setText(module.getArticle_num() + "");
		image = (ImageView) v.findViewById(R.id.module_img2);
		if (module.getImg() != null) {
			ImageLoaderUtils.display(module.getImg(), image);
		}
		//
		return v;
	}

	private void initData() {
		// 从服务器中加载数据
		StringPostRequest sp = new StringPostRequest(urlUtils.localhost
				+ urlUtils.moduleControl, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				Gson gson = new Gson();
				users=new ArrayList<UserFollow>();
				List<UserFollow> list = gson.fromJson(arg0,
						new TypeToken<ArrayList<UserFollow>>() {
						}.getType());
				users.addAll(list);
				listView.addHeaderView(initHeader());
				View footer=LayoutInflater.from(AboutModuleActivity.this).inflate(R.layout.footer_view_gray, null);
				listView.addFooterView(footer);
				adapter = new UserAdapter(users, AboutModuleActivity.this);
				listView.setAdapter(adapter);

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				Toast.makeText(AboutModuleActivity.this, "网络连接失败",
						Toast.LENGTH_SHORT).show();

			}
		});
		sp.putParams("module", module.getModule_id() + "");
		ZmApplication.getInstance().getRequestQueue().add(sp);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.xinzeng:
			finish();
			break;
		case R.id.enter_module:
			Intent intent = new Intent(this, ModuleDetailsActivity.class);
			intent.putExtra("module", module);
			startActivity(intent);
			finish();
			break;
		case R.id.follow_module:
			initZan();
			break;
		default:
			break;
		}

	}

	private void initZan() {

		if (isFollowed) {
			StringPostRequest sp = new StringPostRequest(urlUtils.localhost
					+ urlUtils.CollectionModuleServlet, new Listener<String>() {

				@Override
				public void onResponse(String arg0) {
					if (arg0.equals("success")) {
						int newFavor = Integer.parseInt(favorNum.getText()
								.toString()) - 1;
						favorNum.setText(newFavor + "");
						follow.setText("关注板块");
						isFollowed = false;
						Toast toast = Toast.makeText(AboutModuleActivity.this,
								"取消关注", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					} else {
						Toast.makeText(AboutModuleActivity.this, "稍后重试",
								Toast.LENGTH_SHORT).show();
					}

				}
			}, new ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {
					Toast.makeText(AboutModuleActivity.this, "网络连接失败，请稍后重试",
							Toast.LENGTH_SHORT).show();

				}
			});
			sp.putParams("module", module.getModule_id() + "");
			sp.putParams("name", ZmApplication.getInstance().getUser()
					.getUser_id());
			sp.putParams("type", 2 + "");
			ZmApplication.getInstance().getRequestQueue().add(sp);

		} else {
			StringPostRequest sp = new StringPostRequest(urlUtils.localhost
					+ urlUtils.CollectionModuleServlet, new Listener<String>() {

				@Override
				public void onResponse(String arg0) {
					if (arg0.equals("success")) {
						int newFavor = Integer.parseInt(favorNum.getText()
								.toString()) + 1;
						favorNum.setText(newFavor + "");
						follow.setText("已关注");
						// 更新user个人收藏信息
						isFollowed = true;
						Toast toast = Toast.makeText(AboutModuleActivity.this,
								"关注成功", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						View v = LayoutInflater.from(AboutModuleActivity.this)
								.inflate(R.layout.favor_toast, null);
						toast.setView(v);
						toast.show();
					} else {
						Toast.makeText(AboutModuleActivity.this, "稍后重试",
								Toast.LENGTH_SHORT).show();
					}

				}
			}, new ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {
					Toast.makeText(AboutModuleActivity.this, "网络连接失败，请稍后重试",
							Toast.LENGTH_SHORT).show();

				}
			});
			sp.putParams("module", module.getModule_id() + "");
			sp.putParams("name", ZmApplication.getInstance().getUser()
					.getUser_id());
			sp.putParams("type", 1 + "");
			ZmApplication.getInstance().getRequestQueue().add(sp);
			// 数据库中favor数加一

		}
	}
}
