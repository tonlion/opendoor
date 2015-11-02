package com.zhima.opendoor.activity;

import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.zhima.opendoor.R;
import com.zhima.opendoor.view.SwipeBackActivity;

public class MessageShowActivity extends SwipeBackActivity{
	private LinearLayout message;
	private Bundle b;
	private LinearLayout l;
	private TextView xiaoxi;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_show_activity_layout);
		message=(LinearLayout) findViewById(R.id.show_message);
		l=(LinearLayout) findViewById(R.id.messageCenter);
		xiaoxi=(TextView) findViewById(R.id.xiaoxizhongxin);
		xiaoxi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
		b=getIntent().getExtras();
		if(b!=null){
			 initMessage();
		}else{
			message.setVisibility(View.GONE);
			l.setVisibility(View.VISIBLE);
		}
		
	}
	private void initMessage(){
		View v=LayoutInflater.from(this).inflate(R.layout.article_list_item_layout2, null);
		TextView title=(TextView) v.findViewById(R.id.list_title2);
		title.setText("新消息回复");
		TextView time=(TextView) v.findViewById(R.id.list_time2);
		SimpleDateFormat sdFormatter = new SimpleDateFormat("MM-dd hh:mm");
		  String retStrFormatNowDate = sdFormatter.format(System.currentTimeMillis());
		  time.setText(retStrFormatNowDate);
		TextView Isay=(TextView) v.findViewById(R.id.Isay);
		TextView what=(TextView) v.findViewById(R.id.whatIsay);
		what.setText("回复了我： ");
		Isay.setText(b.getString(JPushInterface.EXTRA_MESSAGE));
		message.addView(v);
	}

}
