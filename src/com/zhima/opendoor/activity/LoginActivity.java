package com.zhima.opendoor.activity;

import imsdk.data.IMMyself;
import imsdk.data.IMMyself.OnActionListener;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.zhima.opendoor.utils.StringPostRequest;
import com.zhima.opendoor.utils.urlUtils;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText userId;
	private EditText pwd;
	private Button btnlog;
	private TextView forgetPwd;
	private TextView register;
	private TextView login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity_layout);
		userId = (EditText) findViewById(R.id.userId);
		pwd = (EditText) findViewById(R.id.pwd);
		btnlog = (Button) findViewById(R.id.btnLog);
		forgetPwd = (TextView) findViewById(R.id.forgetPwd);
		register = (TextView) findViewById(R.id.register);
		login = (TextView) findViewById(R.id.login);

		btnlog.setOnClickListener(this);
		login.setOnClickListener(this);
		register.setOnClickListener(this);
		forgetPwd.setOnClickListener(this);
		// checkBtn();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			finish();
			break;
		case R.id.register:
			startActivity(new Intent(this, RegisterActivity.class));
			finish();
			break;
		case R.id.btnLog:
			// �жϵ�½�ɹ����ķ���
			checkLogin();
		case R.id.forgetPwd:
			break;

		default:
			break;
		}
	}

	private void checkLogin() {
		if (TextUtils.isEmpty(userId.getText().toString())
				&& TextUtils.isEmpty(pwd.getText().toString())) {
			Toast.makeText(this, "�û��������벻��Ϊ�գ�", Toast.LENGTH_SHORT).show();
		} else {
			final ProgressDialog dialog=ProgressDialog.show(LoginActivity.this, "", "��¼��...");
			

			// ʹ��volley��ܶ�ȡ�����������ݿ������ݣ�ƥ���½��Ϣ
			StringPostRequest request = new StringPostRequest(
					urlUtils.loginServlet, new Listener<String>() {

						@Override
						public void onResponse(String result) {
							if (result.length() > 7) {
								Gson gson = new Gson();
								User user = gson.fromJson(result, User.class);
								Toast.makeText(getApplicationContext(), "��½�ɹ�",
										Toast.LENGTH_SHORT).show();
								// ����application�е��û���Ϣ
								ZmApplication.getInstance().setUser(user);

								Set<String> tags = new HashSet<String>();
								if (user.getUser_id() != null
										&& !user.getUser_id().equals("")) {
									tags.add(user.getUser_id());// ��¼��ǰ�û���ID
								}
								if (user.getUser_name() != null
										&& !user.getUser_name().equals("")) {
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
													Log.i("tags",
															tags.toString());
												}
											}
										});

								// ���û���Ϣд�뻺��
								SharedPreferences sp = getSharedPreferences(
										"Setting", MODE_PRIVATE);
								Editor editor = sp.edit();
								editor.putString("userid", userId.getText()
										.toString());
								editor.putString("pwd", pwd.getText()
										.toString());
								// ���û���Ϣд���������ݿ�
								try {
									DataBaseOpenHelper
											.getInstance(LoginActivity.this)
											.getStudentDao().create(user);
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								// ����Ĭ�ϵ��Զ���¼
								editor.putBoolean("autolink", true);
								// �ύ����
								editor.commit();
								Intent intent = new Intent();
								intent.setClass(LoginActivity.this,
										QQSlidingMenuActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// �����Թص���Ҫ���Ľ����м��activity
								startActivity(intent);
								finish();
								dialog.dismiss();
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
							} else {
								Toast.makeText(getApplicationContext(),
										"�û������������", Toast.LENGTH_SHORT).show();
								dialog.dismiss();
							}

						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							Toast.makeText(getApplicationContext(), "�������Ӵ���",
									Toast.LENGTH_SHORT).show();
							dialog.dismiss();

							try {
								List<User> user = DataBaseOpenHelper
										.getInstance(LoginActivity.this)
										.getStudentDao().queryForAll();
								if(user.size()!=0){
									ZmApplication.getInstance()
									.setUser(user.get(0));
								}
								
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});
			request.putParams("username", userId.getText().toString());
			request.putParams("password", pwd.getText().toString());
			request.putParams("type", "0");
			ZmApplication.getInstance().getRequestQueue().add(request);

		}

	}

}
