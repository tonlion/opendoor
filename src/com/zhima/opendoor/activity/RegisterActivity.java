package com.zhima.opendoor.activity;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.zhima.opendoor.R;
import com.zhima.opendoor.application.ZmApplication;
import com.zhima.opendoor.db.DataBaseOpenHelper;
import com.zhima.opendoor.entity.User;
import com.zhima.opendoor.utils.StringPostRequest;
import com.zhima.opendoor.utils.urlUtils;

public class RegisterActivity extends Activity implements OnClickListener {
	private TextView register;
	private TextView login;
	private EditText userId;
	private EditText pwd;
	private EditText pwdAgain;
	private Button btnlog;
	private CheckBox check;
	private TextView zcsz;
	private TextView phoneRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity_layout);
		register = (TextView) findViewById(R.id.register_1);
		login = (TextView) findViewById(R.id.login_1);
		userId = (EditText) findViewById(R.id.userId);
		pwd = (EditText) findViewById(R.id.pwd);
		pwdAgain = (EditText) findViewById(R.id.pwdAgain);
		phoneRegister = (TextView) findViewById(R.id.phoneRegister);
		btnlog = (Button) findViewById(R.id.btnReg);
		check = (CheckBox) findViewById(R.id.check);
		zcsz = (TextView) findViewById(R.id.zcsz);
		register.setOnClickListener(this);
		login.setOnClickListener(this);
		phoneRegister.setOnClickListener(this);
		btnlog.setOnClickListener(this);
		SMSSDK.initSDK(this, appKey, appSecret);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_1:
			finish();
			break;
		case R.id.login_1:
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			break;
		case R.id.btnReg:
			// ��ȡ�����������ݣ��ж��û����Ƿ����
			Toast.makeText(this, "����", Toast.LENGTH_SHORT).show();
			// �ж������Ƿ�һ��
			if (TextUtils.isEmpty(userId.getText().toString())
					|| TextUtils.isEmpty(pwd.getText().toString())
					|| TextUtils.isEmpty(pwdAgain.getText().toString())) {
				Toast.makeText(this, "�û��������벻��Ϊ��", Toast.LENGTH_SHORT).show();
			} else if (!pwd.getText().toString()
					.equals(pwdAgain.getText().toString())) {
				Toast.makeText(this, "�����������벻һ�£�����������", Toast.LENGTH_SHORT)
						.show();
				pwd.setText("");
				pwdAgain.setText("");
				// pwd.getFocusables(direction)
			} else {
				// ����ע��

				StringPostRequest sp = new StringPostRequest(
						urlUtils.addUserServlet, new Listener<String>() {

							@Override
							public void onResponse(String arg0) {
								if (arg0.length() < 7) {
									Toast.makeText(RegisterActivity.this,
											"ע���쳣���û����Ѿ�����", Toast.LENGTH_SHORT)
											.show();
								} else {
									Gson gson = new Gson();
									User user = gson.fromJson(arg0, User.class);
									Toast.makeText(RegisterActivity.this,
											"ע��ɹ�,�ѵ�¼", Toast.LENGTH_SHORT)
											.show();

									// startActivity(new
									// Intent(RegisterActivity.this,LoginActivity.class));
									// д�����ݿ�
									try {
										DataBaseOpenHelper
												.getInstance(
														getApplicationContext())
												.getStudentDao().create(user);
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									// ����application�е�user�û�
									ZmApplication.getInstance().setUser(user);
									startActivity(new Intent(
											RegisterActivity.this,
											QQSlidingMenuActivity.class));
									// ��תף����
									finish();
								}

							}
						}, new ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError arg0) {
								Toast.makeText(RegisterActivity.this, "�������Ӵ���",
										Toast.LENGTH_SHORT).show();

							}
						});
				sp.putParams("userID", userId.getText().toString());
				sp.putParams("password", pwd.getText().toString());
				sp.putParams("sex", "��");
				ZmApplication.getInstance().getRequestQueue().add(sp);

			}
			break;
		case R.id.phoneRegister:
			RegisterPage register = new RegisterPage();
			// ע��ص��¼�
			register.setRegisterCallback(new EventHandler() {
				// �¼���ɺ����
				@Override
				public void afterEvent(int event, int result, Object data) {
					// �жϽ���Ƿ��Ѿ����
					if (result == SMSSDK.RESULT_COMPLETE) {
						// ��ȡ����data
						HashMap<String, Object> maps = (HashMap<String, Object>) data;
						// ������Ϣ
						String conntry = (String) maps.get("country");
						// �ֻ���
						String phone = (String) maps.get("phone");

						submitUserInfo(conntry, phone);
					}

				}

			});
			// ��ʾ������
			register.show(RegisterActivity.this);
			// finish();
			break;
		default:
			break;
		}
	}

	private String appKey = "b3090eeefe02"; // �ڷ���˺�̨����
	private String appSecret = "9ad9a2e61e1386b98c6b6b41b7aef723";

	public void submitUserInfo(String country, String phone) {
		Random r = new Random();
		String uid = Math.abs(r.nextInt()) + "";
		String nickName = "liuj";
		SMSSDK.submitUserInfo(uid, nickName, null, country, phone);
	}

}
