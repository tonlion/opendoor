package com.zhima.opendoor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.zhima.opendoor.R;
import com.zhima.opendoor.application.ZmApplication;

public class FirstWelcomeActivity extends Activity implements OnClickListener{
	private Button zhuce;
	private Button denglu;
	private TextView free;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_welcome_layout);
		zhuce=(Button) findViewById(R.id.zhuce);
		denglu=(Button) findViewById(R.id.denglu);
		zhuce.setOnClickListener(this);
		denglu.setOnClickListener(this);
		findViewById(R.id.free).setOnClickListener(this);

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.zhuce:
			startActivity(new Intent(FirstWelcomeActivity.this,RegisterActivity.class));
			finish();
			break;
		case R.id.denglu:
			startActivity(new Intent(FirstWelcomeActivity.this,LoginActivity.class));
			finish();
			break;
		case R.id.free:
			//界面暂时未做好
			startActivity(new Intent(FirstWelcomeActivity.this,QQSlidingMenuActivity.class));
			ZmApplication.getInstance().setUser(null);
			finish();

		default:
			break;
		}
		
	}

}
