package com.zhima.opendoor.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.zhima.opendoor.R;
import com.zhima.opendoor.adapter.aboutIdAdapter;
import com.zhima.opendoor.adapter.aboutIdAdapter.onContactSlectedListener;
import com.zhima.opendoor.application.ZmApplication;
import com.zhima.opendoor.db.DataBaseOpenHelper;
import com.zhima.opendoor.entity.AboutIdEntity;
import com.zhima.opendoor.entity.User;
import com.zhima.opendoor.utils.StringPostRequest;
import com.zhima.opendoor.utils.urlUtils;
import com.zhima.opendoor.view.SwipeBackActivity;

public class AboutIdActivity extends SwipeBackActivity implements OnClickListener,onContactSlectedListener,OnItemClickListener {
	private ListView line;
	private List<AboutIdEntity> ids;
	private List<User> users;
	private TextView addUser;
	private TextView aboutId;
	private TextView edit;
	private aboutIdAdapter adapter;

	// 这个以后再弄
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutid_layout);
		users = new ArrayList<User>();
		ids = new ArrayList<AboutIdEntity>();
		line = (ListView) findViewById(R.id.userContainer);
		addUser = (TextView) findViewById(R.id.addUser);
		aboutId = (TextView) findViewById(R.id.aboutId);
		edit = (TextView) findViewById(R.id.edit);
		edit.setOnClickListener(this);
		adapter = new aboutIdAdapter(ids, this);
		line.setOnItemClickListener(this);
		adapter.setListener(this);
		aboutId.setOnClickListener(this);
		addUser.setOnClickListener(this);
		initUsers();
		line.setAdapter(adapter);
	}

	private void initUsers() {
		// 从数据库中读取users
		try {
			users.addAll(DataBaseOpenHelper.getInstance(this).getStudentDao().queryForAll());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 假装从applica中读取的user
           User app=ZmApplication.getInstance().getUser();
           if(app!=null){
        		for (int i = 0; i < users.size(); i++) {
        			AboutIdEntity e = new AboutIdEntity();
        			if (users.get(i).getUser_id().equals(app.getUser_id())) {
        				e.setSelect(true);

        			} else {
        				e.setSelect(false);
        			}
        			e.setType(0);
        			e.setU(users.get(i));
        			ids.add(e);
        		}
           }
	

	}

	

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.aboutId:
			finish();
			break;
		case R.id.edit:
			if(edit.getText().toString().equals("编辑")){
				edit.setText("完成");
				edit.setTextColor(Color.YELLOW);
			for(int i=0;i<ids.size();i++){
				ids.get(i).setType(1);
			}
			adapter.notifyDataSetChanged();}
			else{
				edit.setText("编辑");
				edit.setTextColor(Color.WHITE);
				for(int i=0;i<ids.size();i++){
					ids.get(i).setType(0);
				}
				adapter.notifyDataSetChanged();
			}
			break;
		case R.id.addUser:
			startActivity(new Intent(AboutIdActivity.this,LoginActivity.class));
			break;
		default:
			break;
		}

	}

	@Override
	public void onContactSelectedChanged(AboutIdEntity e) {
		ids.remove(e);
		try {
			DataBaseOpenHelper.getInstance(this).getStudentDao().delete(e.getU());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(arg3==-1){
			return;
		}
		int positon=(int)arg3;
		final AboutIdEntity e=(AboutIdEntity) arg0.getItemAtPosition(positon);
		
		StringPostRequest request = new StringPostRequest(
				urlUtils.loginServlet, new Listener<String>() {

					@Override
					public void onResponse(String result) {
						if (result.length() > 7) {
							Gson gson = new Gson();
							User user = gson.fromJson(result,
									User.class);
							Toast.makeText(getApplicationContext(),
									"账号切换成功", Toast.LENGTH_SHORT).show();
							//设置application中的用户信息
							ZmApplication.getInstance().setUser(user);
							//将用户信息写入缓存
							SharedPreferences sp=getSharedPreferences("Setting", MODE_PRIVATE);
							Editor editor=sp.edit();
							editor.putString("userid",e.getU().getUser_id());
							editor.putString("pwd",e.getU().getPassword());
							//保存默认的自动登录
							editor.putBoolean("autolink", true);
							//提交数据
							editor.commit();
							Intent intent = new Intent();
							intent.setClass(AboutIdActivity.this,
									QQSlidingMenuActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
							startActivity(intent);
							finish();
						} else {
							Toast.makeText(getApplicationContext(),
									"连接错误", Toast.LENGTH_SHORT)
									.show();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(getApplicationContext(),
								"网络连接错误", Toast.LENGTH_SHORT).show();

					}
				});
		request.putParams("username", e.getU().getUser_id());
		request.putParams("password", e.getU().getPassword());
		request.putParams("type", "0");
		ZmApplication.getInstance().getRequestQueue().add(request);
	
		
		
	}

}
