package com.zhima.opendoor.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhima.opendoor.R;
import com.zhima.opendoor.application.ZmApplication;
import com.zhima.opendoor.entity.User;
import com.zhima.opendoor.entity.info;
import com.zhima.opendoor.utils.ImageLoaderUtils;
import com.zhima.opendoor.view.CircleImageView;
import com.zhima.opendoor.view.SwipeBackActivity;

public class PersonalInfoActivity extends SwipeBackActivity implements OnClickListener {
	private ImageView editor;
	private TextView level; // 什么芝麻
	private ImageView levelImage;
	private ImageView sex;
	private TextView myFans;
	private TextView myAttention;
	private TextView myModule;
	private TextView myArticle;
	private LinearLayout line;
	private TextView introduce;
	private TextView userName;
	private TextView personalInfo;
	private CircleImageView header;
	private User user;
	private LinearLayout my1;
	private LinearLayout my2;
	private LinearLayout my3;
	private LinearLayout my4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personnal_info_layout);
		editor = (ImageView) findViewById(R.id.editor);
		line = (LinearLayout) findViewById(R.id.line);
		level = (TextView) findViewById(R.id.level2);
		levelImage = (ImageView) findViewById(R.id.level);
		myFans = (TextView) findViewById(R.id.myFans);
		myFans.setOnClickListener(this);
		myAttention = (TextView) findViewById(R.id.myAttention);
		myAttention.setOnClickListener(this);
		myModule = (TextView) findViewById(R.id.myModule);
		myModule.setOnClickListener(this);
		myArticle = (TextView) findViewById(R.id.myAticle);
		myArticle.setOnClickListener(this);
		introduce = (TextView) findViewById(R.id.intruduce);
		userName = (TextView) findViewById(R.id.username);
		header = (CircleImageView) findViewById(R.id.header);
		personalInfo=(TextView) findViewById(R.id.personal_info);
		sex=(ImageView) findViewById(R.id.sex);
		editor.setOnClickListener(this);
		personalInfo.setOnClickListener(this);
		my1=(LinearLayout) findViewById(R.id.my1);
		my1.setOnClickListener(this);
		my2=(LinearLayout) findViewById(R.id.my2);
		my2.setOnClickListener(this);
		my3=(LinearLayout) findViewById(R.id.my3);
		my3.setOnClickListener(this);
		my4=(LinearLayout) findViewById(R.id.my4);
		my4.setOnClickListener(this);
		initData();
	}

	@Override
	protected void onStart() {
		
		super.onStart();
		
	}
	@Override
	protected void onResume() {
		user=ZmApplication.getInstance().getUser();
		super.onResume();
		if ( user!= null) {
			if(user.getHead_img()!=null)
			ImageLoaderUtils.display(ZmApplication.getInstance().getUser()
					.getHead_img(), header);
			userName.setText(user.getUser_name());
			if(user.getUser_desc()!=null){
				introduce.setText(user.getUser_desc());
			}
			if (user.getSex().equals("女")) {
				sex.setImageResource(R.drawable.icon_pop_girl);
				
			} else {
				sex.setImageResource(R.drawable.icon_pop_boy);
			}
			myModule.setText(user.getFavor()+"");
			myFans.setText(user.getBefocus()+"");
			myAttention.setText(user.getFocus()+"");
			myArticle.setText(user.getCollection()+"");
			if(user.getUser_desc()!=null){
				introduce.setText(user.getUser_desc());
			}else{
				introduce.setText("没有留下任何言语");
			}
			
		}
		
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.editor:
			Intent intent = new Intent(PersonalInfoActivity.this,
					EditorActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.personal_info:
			finish();
			break;
		case R.id.my2:
			Intent inten=new Intent();
			inten.setClass(this, ShowInfoActivity.class);
			inten.putExtra("number", 1);
			startActivity(inten);
			break;
		case R.id.my3:
			Intent intentt=new Intent();
			intentt.setClass(this, MyArticleActivity.class);
			startActivity(intentt);
			break;
		case R.id.my1:
			inten=new Intent();
			inten.putExtra("number", 2);
			inten.setClass(this, ShowInfoActivity.class);
			startActivity(inten);
			break;
		case R.id.my4:
			inten=new Intent();
			inten.putExtra("number", 4);
			inten.setClass(this, ShowInfoActivity.class);
			startActivity(inten);
			break;

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == RESULT_OK) {
			info info =  data
					.getParcelableExtra("info");
			if(!info.getName().equals("")){
			userName.setText(info.getName());
			}
			if(!info.getAboutMe().equals("")){
			introduce.setText(info.getAboutMe());
			}
			if(info.getSex()!=null){
				if (info.getSex().equals("男")) {
					sex.setImageResource(R.drawable.icon_pop_boy);
				} else if(info.getSex().equals("女")){
					sex.setImageResource(R.drawable.icon_pop_girl);
				}

			}
			
		}

	}

	private void initData() {
		// 从服务器中读取分数
		int count = 37;
		int level = count / 5;
		if (level <= 5) {
			this.level.setText("白芝麻");
		} else if (level <= 10) {
			this.level.setText("黄芝麻");
		} else if (level <= 15) {
			this.level.setText("绿芝麻");
		} else if (level <= 20) {
			this.level.setText("蓝芝麻");
		} else if (level <= 25) {
			this.level.setText("红芝麻");
		} else if (level <= 30) {
			this.level.setText("黑芝麻");
		} else if (level <= 35) {
			this.level.setText("七彩芝麻");
		}
		// 从网络中读取数据然后更改数据
	}

}
