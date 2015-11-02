package com.zhima.opendoor.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.zhima.opendoor.R;
import com.zhima.opendoor.application.ZmApplication;
import com.zhima.opendoor.entity.Discuss;
import com.zhima.opendoor.entity.User;
import com.zhima.opendoor.utils.ImageLoaderUtils;
import com.zhima.opendoor.utils.StringPostRequest;
import com.zhima.opendoor.utils.urlUtils;
import com.zhima.opendoor.view.CircleImageView;
import com.zhima.opendoor.view.SwipeBackActivity;

public class OtherPersonInfoActivity extends SwipeBackActivity implements
		OnClickListener {
	private TextView level; // ʲô֥��
	private ImageView levelImage;
	private ImageView sex;
	private TextView myFans;
	private TextView myAttention;
	private TextView myModule;
	private TextView myArticle;
	private TextView introduce;
	private TextView userName;
	private TextView personalInfo;
	private CircleImageView header;
	private Button button;
	private Intent intent;
	private Discuss discuss;
	private User user;
	private boolean isFollowed;
	private String userId;
	private ImageView addOtherFavor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_personnal_info_layout);
		intent = getIntent();
		/*
		 * discuss = intent.getParcelableExtra("userId"); if
		 * (intent.getStringExtra("userId3")==null) { userId =
		 * intent.getStringExtra("userId3"); Toast.makeText(this, "����3",
		 * Toast.LENGTH_SHORT).show(); } else if (discuss == null) { userId =
		 * intent.getStringExtra("userId2"); Toast.makeText(this, "����2",
		 * Toast.LENGTH_SHORT).show(); } else { userId = discuss.getUser_id();
		 * Toast.makeText(this, "����1", Toast.LENGTH_SHORT).show(); }
		 */
		userId = intent.getStringExtra("userId");
		level = (TextView) findViewById(R.id.levelOther);
		levelImage = (ImageView) findViewById(R.id.levelO);
		myFans = (TextView) findViewById(R.id.otherFans);
		myAttention = (TextView) findViewById(R.id.otherAttention);
		myModule = (TextView) findViewById(R.id.otherModule);
		myArticle = (TextView) findViewById(R.id.otherAticle);
		introduce = (TextView) findViewById(R.id.intruduceOther);
		addOtherFavor = (ImageView) findViewById(R.id.addFavorOther);
		addOtherFavor.setOnClickListener(this);
		userName = (TextView) findViewById(R.id.usernameOther);
		header = (CircleImageView) findViewById(R.id.headerOther);
		personalInfo = (TextView) findViewById(R.id.ziliao);
		personalInfo.setOnClickListener(this);
		button = (Button) findViewById(R.id.liaotian);
		button.setOnClickListener(this);
		sex = (ImageView) findViewById(R.id.sexOther);
		initData();
	}

	private void initData() {
		// ������˶�ȡ���ݣ����������Ը�ֵ
		StringPostRequest sp = new StringPostRequest(urlUtils.localhost
				+ urlUtils.userInfo, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				Gson gson = new Gson();
				user = gson.fromJson(arg0, User.class);
				int count = user.getScore();
				int levell = count / 5;
				if (levell <= 5) {
					level.setText("��֥��");
				} else if (levell <= 10) {
					level.setText("��֥��");
				} else if (levell <= 15) {
					level.setText("��֥��");
				} else if (levell <= 20) {
					level.setText("��֥��");
				} else if (levell <= 25) {
					level.setText("��֥��");
				} else if (levell <= 30) {
					level.setText("��֥��");
				} else if (levell <= 35) {
					level.setText("�߲�֥��");
				}
				myFans.setText(user.getBefocus() + "");
				myAttention.setText(user.getFocus() + "");
				myModule.setText(user.getCollection() + "");
				myArticle.setText(user.getFavor() + "");
				introduce.setText(user.getUser_desc());
				userName.setText(user.getUser_name());
				if (user.getHead_img() != null) {
					ImageLoaderUtils.display(user.getHead_img(), header);
				} else {
					header.setImageResource(R.drawable.zhima);
				}
				if (user.getIsFocus() == 0) {
					isFollowed = false;
				} else {
					isFollowed = true;
				}
				if (isFollowed) {
					addOtherFavor
							.setImageResource(R.drawable.icon_details_unlikeblack);
				} else {
					addOtherFavor
							.setImageResource(R.drawable.icon_details_likeblack);
				}
				if (user.getSex().equals("��")) {
					sex.setImageResource(R.drawable.icon_pop_boy);
				} else {
					sex.setImageResource(R.drawable.icon_pop_girl);
				}

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

			}
		});
		sp.putParams("requestName", userId);
		sp.putParams("userName", ZmApplication.getInstance().getUser()
				.getUser_id());
		ZmApplication.getInstance().getRequestQueue().add(sp);
		// �ӷ������ж�ȡ����
		// �������ж�ȡ����Ȼ���������

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ziliao:
			finish();
			break;
		case R.id.liaotian:
			// ��ת
			Intent i = new Intent(OtherPersonInfoActivity.this,
					IMChatActivity.class);
			// �������
			i.putExtra("mCustomUserID",user.getUser_id());
			startActivity(i);
			break;
		case R.id.addFavorOther:
			aboutFavor();
			break;

		default:
			break;
		}

	}

	private void aboutFavor() {

		if (isFollowed) {
			StringPostRequest sp = new StringPostRequest(urlUtils.localhost
					+ urlUtils.focus, new Listener<String>() {

				@Override
				public void onResponse(String arg0) {
					if (arg0.equals("success")) {

						addOtherFavor
								.setImageResource(R.drawable.icon_details_likeblack);
						isFollowed = false;
						Toast toast = Toast.makeText(
								OtherPersonInfoActivity.this, "ȡ����ע",
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					} else {
						Toast.makeText(OtherPersonInfoActivity.this, "�Ժ�����",
								Toast.LENGTH_SHORT).show();
					}

				}
			}, new ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {
					Toast.makeText(OtherPersonInfoActivity.this,
							"��������ʧ�ܣ����Ժ�����", Toast.LENGTH_SHORT).show();

				}
			});
			sp.putParams("focus", userId);
			sp.putParams("name", ZmApplication.getInstance().getUser()
					.getUser_id());
			sp.putParams("type", 0 + "");
			ZmApplication.getInstance().getRequestQueue().add(sp);

		} else {
			StringPostRequest sp1 = new StringPostRequest(urlUtils.localhost
					+ urlUtils.focus, new Listener<String>() {

				@Override
				public void onResponse(String arg0) {
					if (arg0.equals("success")) {

						addOtherFavor
								.setImageResource(R.drawable.icon_details_unlikeblack);
						// ����user�����ղ���Ϣ
						isFollowed = true;
						Toast toast = Toast.makeText(
								OtherPersonInfoActivity.this, "��ע�ɹ�",
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						View v = LayoutInflater.from(
								OtherPersonInfoActivity.this).inflate(
								R.layout.favor_toast, null);
						toast.setView(v);
						toast.show();
					} else {
						Toast.makeText(OtherPersonInfoActivity.this, "�Ժ�����",
								Toast.LENGTH_SHORT).show();
					}

				}
			}, new ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {
					Toast.makeText(OtherPersonInfoActivity.this,
							"��������ʧ�ܣ����Ժ�����", Toast.LENGTH_SHORT).show();

				}
			});
			sp1.putParams("focus", userId);
			sp1.putParams("name", ZmApplication.getInstance().getUser()
					.getUser_id());
			sp1.putParams("type", 1 + "");
			ZmApplication.getInstance().getRequestQueue().add(sp1);
			// ���ݿ���favor����һ

		}

	}

}
