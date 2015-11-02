package com.zhima.opendoor.activity;

import com.zhima.opendoor.R;
import com.zhima.opendoor.view.SwipeBackActivity;

import imsdk.views.IMChatView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

public class IMChatActivity extends Activity {
	// 聊天控件
	private IMChatView mChatView;

	// 聊天室中对方的用户id
	private String mCustomUserID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 聊天室中对方的用户id
		mCustomUserID = getIntent().getStringExtra("mCustomUserID");
		// 实例化聊天控件
		mChatView = new IMChatView(this, mCustomUserID);
		// 设置聊天中单条消息中Gif动态表情的临界值（当单条消息中的表情个数超过该临界值时，
		// 则该表情显示为非Gif动态表情，而是静态表情--- 默认值为10）
		mChatView.setMaxGifCountInMessage(10);
		// 设置聊天界面是否显示聊天双方的头像
		mChatView.setUserMainPhotoVisible(true);
		// 设置聊天界面中头像的圆角度数
		mChatView.setUserMainPhotoCornerRadius(10);
		// 设置聊天界面是否显示聊天双方的id
		mChatView.setUserNameVisible(false);
		// titlebar可见
		mChatView.setTitleBarVisible(true);
		// mChatView.setTitleBarBackgroundColor(Color.BLUE);
		mChatView.setTitleBarBackground(R.drawable.bg_bar);
		setContentView(mChatView);
	}

	// 重写 onActivityResult方法
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mChatView.onActivityResult(requestCode, resultCode, data);
	}

	// 重写 onKeyDown方法
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mChatView.onKeyDown(keyCode, event)) {
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
