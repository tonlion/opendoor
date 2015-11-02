package com.zhima.opendoor.activity;

import imsdk.data.IMMyself;
import imsdk.data.IMMyself.OnActionListener;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.zhima.opendoor.R;
import com.zhima.opendoor.application.ZmApplication;
import com.zhima.opendoor.db.DataBaseOpenHelper;
import com.zhima.opendoor.entity.User;
import com.zhima.opendoor.utils.FileUitlity;
import com.zhima.opendoor.utils.StringPostRequest;
import com.zhima.opendoor.utils.urlUtils;

public class SplushActivity extends Activity {
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splush_layout);
		SharedPreferences sp = getSharedPreferences("Setting", MODE_PRIVATE);
		// Editor editor=sp.edit();
		final String userId = sp.getString("userid", null);
		final String pwd = sp.getString("pwd", null);
		boolean autolink = sp.getBoolean("autolink", true);
		if (userId != null && pwd != null && autolink) {
			handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// ִ�е�¼
					StringPostRequest rq = new StringPostRequest(
							urlUtils.loginServlet, new Listener<String>() {

								@Override
								public void onResponse(String result) {
									if (result.length() < 7) {
										Toast.makeText(getApplicationContext(),
												"��¼�쳣��", Toast.LENGTH_SHORT)
												.show();
										startActivity(new Intent(
												SplushActivity.this,
												LoginActivity.class));
									} else {
										Gson gson = new Gson();
										User user = gson.fromJson(result,
												User.class);
										ZmApplication.getInstance().setUser(
												user);
										// ���ü������ͱ���
										Set<String> tags = new HashSet<String>();
										if (user.getUser_id() != null
												&& !user.getUser_id()
														.equals("")) {
											tags.add(user.getUser_id());// ��¼��ǰ�û���ID
										}
										if (user.getUser_name() != null
												&& !user.getUser_name().equals(
														"")) {
											tags.add(user.getUser_name());// ��¼��ǰ�û����û���
										}
										// ��ѧ�����õ��༶��רҵ�飬�����ñ���Ϊѧ����ѧ��
										JPushInterface.setAliasAndTags(
												getApplicationContext(),
												user.getUser_id(), tags,
												new TagAliasCallback() {
													@Override
													public void gotResult(
															int responseCode,
															String alias,
															Set<String> tags) {
														// TODO
														if (responseCode == 0) {
															Log.i("tags", tags
																	.toString());
														}
													}
												});
										// ��¼
										IMMyself.setCustomUserID(user.getUser_id());
										IMMyself.setPassword(user.getPassword());
										// ���ó�ʱʱ��Ϊ5��
										IMMyself.login(false, 5, new OnActionListener() {

											@Override
											public void onSuccess() {
												/*Toast.makeText(LoginActivity.this, "��¼�ɹ�",
														Toast.LENGTH_SHORT).show();*/
												/*startActivity(new Intent(LoginActivity.this,
														IMChatActivity.class));*/
											}

											@Override
											public void onFailure(String error) {
												/*if (error.equals("Timeout")) {
													error = "��¼��ʱ";
												} else if (error.equals("Wrong Password")) {
													error = "�������";
												}

												Toast.makeText(LoginActivity.this, error,
														Toast.LENGTH_SHORT).show();*/
											}
										});

										startActivity(new Intent(
												SplushActivity.this,
												QQSlidingMenuActivity.class));
										finish();
									}

								}
							}, new ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError arg0) {
									Toast.makeText(getApplicationContext(),
											"�޷����ӵ���������������������",
											Toast.LENGTH_SHORT).show();

									// �ӱ������ݿ���ȡ����Ϣ

									try {
										List<User> users = DataBaseOpenHelper
												.getInstance(
														SplushActivity.this)
												.getStudentDao().queryForAll();
										User user = users.get(0);
										ZmApplication.getInstance().setUser(
												users.get(0));
										Set<String> tags = new HashSet<String>();
										if (user.getUser_id() != null
												&& !user.getUser_id()
														.equals("")) {
											tags.add(user.getUser_id());// ��¼��ǰ�û���ID
										}
										if (user.getUser_name() != null
												&& !user.getUser_name().equals(
														"")) {
											tags.add(user.getUser_name());// ��¼��ǰ�û����û���
										}
										// ��ѧ�����õ��༶��רҵ�飬�����ñ���Ϊѧ����ѧ��
										JPushInterface.setAliasAndTags(
												getApplicationContext(),
												user.getUser_id(), tags,
												new TagAliasCallback() {
													@Override
													public void gotResult(
															int responseCode,
															String alias,
															Set<String> tags) {
														// TODO
														if (responseCode == 0) {
															Log.i("tags", tags
																	.toString());
														}
													}
												});

									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									startActivity(new Intent(
											SplushActivity.this,
											QQSlidingMenuActivity.class));
									finish();

								}
							});
					rq.putParams("username", userId);
					rq.putParams("password", pwd);
					rq.putParams("type", "0");
					ZmApplication.getInstance().getRequestQueue().add(rq);
				}
			}, 3 * 1000);
		} else {
			// ���û����޵�¼��Ϣ��ʱ����ת����һ��¼ӭ�ӽ���
			// ������ʱδд
			handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					startActivity(new Intent(SplushActivity.this,
							FirstWelcomeActivity.class));
					finish();
				}
			}, 3 * 1000);
		}

	}

}
