package com.zhima.opendoor.activity;

import java.util.HashMap;
import java.util.Random;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import com.zhima.opendoor.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FastRegisterActivity extends Activity implements OnClickListener{
	private Button message;
	private Button fast_register;
	private String appKey="b3090eeefe02"; //在服务端后台生成
	private String appSecret="9ad9a2e61e1386b98c6b6b41b7aef723";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fast_register_activity_layout);
		message=(Button) findViewById(R.id.getMessage);
		message.setOnClickListener(this);
		fast_register=(Button) findViewById(R.id.fast_register);
		fast_register.setOnClickListener(this);
		//初始化sdk
		SMSSDK.initSDK(this, appKey, appSecret);
		//配置信息,在清单文件中
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.getMessage:
			RegisterPage register=new RegisterPage();
			//注册回调事件
			register.setRegisterCallback(new EventHandler(){
				//事件完成后调用
				@Override
				public void afterEvent(int event, int result, Object data) {
					//判断结果是否已经完成
					if(result==SMSSDK.RESULT_COMPLETE){
						//获取数据data
						HashMap<String, Object> maps=(HashMap<String, Object>)data;
								//国家信息
						String conntry=(String) maps.get("country");
								//手机号
						String phone=(String) maps.get("phone");
						
						submitUserInfo(conntry, phone);
					}
					
				}
				
			});
			//显示祖册界面
			register.show(FastRegisterActivity.this);
			
			break;
		case R.id.fast_register:
			break;

		default:
			break;
		}
		
	}
	/**
	 * 提交用户信息
	 * @param country
	 * @param phone
	 */
	public void submitUserInfo(String country,String phone){
		Random r=new Random();
		String uid=Math.abs(r.nextInt())+"";
		String nickName="liuj";
		SMSSDK.submitUserInfo(uid, nickName, null, country, phone);
	}

}
