package com.zhima.opendoor.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.zhima.opendoor.R;
import com.zhima.opendoor.application.ZmApplication;
import com.zhima.opendoor.db.DataBaseOpenHelper;
import com.zhima.opendoor.entity.Chanel;
import com.zhima.opendoor.entity.User;
import com.zhima.opendoor.fragment.FindingFragment;
import com.zhima.opendoor.fragment.LifeFragment;
import com.zhima.opendoor.fragment.SelectedFragment;
import com.zhima.opendoor.qqSlidingMenu.SlidingMenu;
import com.zhima.opendoor.tuisongfenxiang.ExampleUtil;
import com.zhima.opendoor.utils.FragmentTabHost;
import com.zhima.opendoor.utils.ImageLoaderUtils;
import com.zhima.opendoor.view.CircleImageView;

/**
 * Created by Owner on 2015/9/19.
 */
public class QQSlidingMenuActivity extends FragmentActivity implements
		OnClickListener {

	private SlidingMenu menu;
	private ImageView level;
	private TextView username;
	// private ImageView headImage;
	private RelativeLayout myAttention;
	private RelativeLayout myCollection;
	private RelativeLayout history;
	private RelativeLayout personalInfo;
	private RelativeLayout messageCenter;
	private RelativeLayout aboutId;
	private RelativeLayout exit;
	private CircleImageView toggle;
	private FragmentTabHost host;
	private List<Chanel> chanel;
	private LinearLayout line;// ����ʵ��ǰ�����û��������ת
	private User user;
	private CircleImageView headImage;
	public static boolean isForeground = false;
	private ImageView messageCenterXF;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.qq_main_layout);
		menu = (SlidingMenu) findViewById(R.id.id_menu);
		level = (ImageView) findViewById(R.id.level);
		username = (TextView) findViewById(R.id.username);
		headImage = (CircleImageView) findViewById(R.id.head_image);
		myAttention = (RelativeLayout) findViewById(R.id.myattention);
		myCollection = (RelativeLayout) findViewById(R.id.mycollection);
		history = (RelativeLayout) findViewById(R.id.history);
		personalInfo = (RelativeLayout) findViewById(R.id.personalInfo);
		messageCenter = (RelativeLayout) findViewById(R.id.messageCenter);
		aboutId = (RelativeLayout) findViewById(R.id.aboutid);
		exit = (RelativeLayout) findViewById(R.id.exit);
		line = (LinearLayout) findViewById(R.id.lineMain);
		toggle = (CircleImageView) findViewById(R.id.toggle);
		host = (FragmentTabHost) findViewById(R.id.tabhost);
		messageCenterXF=(ImageView) findViewById(R.id.messageCenterXF);
		messageCenterXF.setOnClickListener(this);
		host.setup(this, getSupportFragmentManager(), R.id.useFrame);
		initTabs();
		headImage.setOnClickListener(this);
		myAttention.setOnClickListener(this);
		myCollection.setOnClickListener(this);
		history.setOnClickListener(this);
		personalInfo.setOnClickListener(this);
		aboutId.setOnClickListener(this);
		exit.setOnClickListener(this);
		toggle.setOnClickListener(this);
		messageCenter.setOnClickListener(this);

		registerMessageReceiver();
		JPushInterface.init(getApplicationContext());
		JPushInterface.setDebugMode(true);

	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		initData();
		if (user != null) {
			initSlideMenu();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_image:
			if (user != null) {
				// ��ת���������ý���
				startActivity(new Intent(QQSlidingMenuActivity.this,
						PersonalInfoActivity.class));
			} else {
				Toast.makeText(this, "���ȵ�¼Ŷ~", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(this, LoginActivity.class));
			}

			break;
		case R.id.personalInfo:
			if (user != null) {
				// ��ת���������ý���
				startActivity(new Intent(QQSlidingMenuActivity.this,
						PersonalInfoActivity.class));
			} else {
				Toast.makeText(this, "���ȵ�¼Ŷ~", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(this, LoginActivity.class));
			}
			break;
		case R.id.aboutid:
			// ��ת���˺����ý���
			startActivity(new Intent(QQSlidingMenuActivity.this,
					AboutIdActivity.class));
			break;
		case R.id.exit:
			// �˳���ǰ�˺ŵ�½
			AlertDialog.Builder builder;
			builder = new AlertDialog.Builder(this);
			builder.setTitle("��ʾ").setMessage("��ȷ��Ҫ�˳��˺��� ");
			builder.setPositiveButton("�ǵģ�",
					new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// ��application�е�user����Ϊ��
							SharedPreferences sp = getSharedPreferences(
									"Setting", MODE_PRIVATE);
							Editor e = sp.edit();
							e.remove("userid");
							e.remove("pwd");
							e.commit();
							try {
								DataBaseOpenHelper
										.getInstance(getApplicationContext())
										.getStudentDao().delete(user);
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							ZmApplication.getInstance().setUser(null);

							user = null;
							initSlideMenu2();
							username.setText("���¼");
							headImage.setImageResource(R.drawable.zhima);
						}
					});
			builder.setNegativeButton("ѽ�����",
					new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							arg0.dismiss();

						}
					});
			AlertDialog dialog = builder.create();
			dialog.setCancelable(false);
			dialog.show();
			break;
		case R.id.messageCenter:
			// ��ת����Ϣ���Ľ���
			startActivity(new Intent(QQSlidingMenuActivity.this,
					MessageShowActivity.class));
			break;
		case R.id.toggle:
			menu.toggle();
			break;
		case R.id.messageCenterXF:
			startActivity(new Intent(QQSlidingMenuActivity.this,MessageShowActivity.class));
			break;
		default:
			break;
		}

	}

	/**
	 * ʵ���ҵĹ�ע���ҵ��ղأ������ʷ����״
	 */
	private void initSlideMenu() {
		chanel = new ArrayList<Chanel>();
		chanel.add(new Chanel(1, "�ҹ�ע��", 0));
		chanel.add(new Chanel(2, "��ע�ҵ�", 0));
		chanel.add(new Chanel(3, "�ղ�����", 1));
		chanel.add(new Chanel(4, "�ղذ��", 1));
		chanel.add(new Chanel(5, "������", 2));
		chanel.add(new Chanel(6, "����ظ�", 2));

		for (int i = 0; i < line.getChildCount(); i++) {
			line.getChildAt(i).setId(i);
			line.getChildAt(i).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(QQSlidingMenuActivity.this,
							MyAttentionActivity.class);
					intent.putParcelableArrayListExtra("data",
							(ArrayList<? extends Parcelable>) chanel);
					intent.putExtra("num", arg0.getId());
					startActivity(intent);

				}
			});
		}
	}

	private void initSlideMenu2() {
		chanel = new ArrayList<Chanel>();
		chanel.add(new Chanel(1, "��ע����", 0));
		chanel.add(new Chanel(2, "��ע���", 0));
		chanel.add(new Chanel(3, "�ղ�����", 1));
		chanel.add(new Chanel(4, "�ղذ��", 1));
		chanel.add(new Chanel(5, "������", 2));
		chanel.add(new Chanel(6, "����ظ�", 2));

		for (int i = 0; i < line.getChildCount(); i++) {
			line.getChildAt(i).setId(i);
			line.getChildAt(i).setEnabled(false);
		}
	}

	/**
	 * ʵ�ֵ�������˳���¼
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitByTowClick();
		}
		return false;
	}

	private static Boolean isExit = false;

	private void exitByTowClick() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true;
			Toast.makeText(this, "�ٰ�һ���˳�֥�鿪��", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;
				}

			}, 2 * 1000);
		} else {
			finish();
		}
	}

	private void addTabs(String tag, String labelValue, int resourse,
			Class clzss, Bundle args) {
		FragmentTabHost.TabSpec tab = host.newTabSpec(tag);
		View v = LayoutInflater.from(this).inflate(R.layout.tab_item_op, null);
		TextView label = (TextView) v.findViewById(R.id.text);
		ImageView icon = (ImageView) v.findViewById(R.id.icon);
		label.setText(labelValue);
		icon.setImageResource(resourse);
		tab.setIndicator(v);
		host.addTab(tab, clzss, args);
	}

	private void initTabs() {
		addTabs("selected", "�Ƽ�", R.drawable.select_background,
				SelectedFragment.class, null);
		addTabs("find", "����", R.drawable.finding_background,
				FindingFragment.class, null);
		addTabs("life", "����", R.drawable.life_backfround, LifeFragment.class,
				null);
	}

	/**
	 * ��application�еõ����ݣ�����д����
	 */
	private void initData() {
		user = ZmApplication.getInstance().getUser();
		if (user != null) {
			username.setText(user.getUser_name());
			// ����ͷ��
			if (user.getHead_img() != null) {
				ImageLoaderUtils.display(user.getHead_img(), headImage);
				ImageLoaderUtils.display(user.getHead_img(), toggle);
			}

		}

	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}

	// for receive customer msg from jpush server
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
				if (!ExampleUtil.isEmpty(extras)) {
					showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
				}
				setCostomMsg(showMsg.toString());
			}
		}

		private void setCostomMsg(String msg) {
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
					.show();
			// if (null != msgText) {
			// msgText.setText(msg);
			// msgText.setVisibility(android.view.View.VISIBLE);
			// }
		}
	}

	@Override
	protected void onResume() {
		isForeground = true;
		super.onResume();
	}

	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();
	}
}
