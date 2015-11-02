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
	// ����ؼ�
	private IMChatView mChatView;

	// �������жԷ����û�id
	private String mCustomUserID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);// ʹ������������ý������
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// �������жԷ����û�id
		mCustomUserID = getIntent().getStringExtra("mCustomUserID");
		// ʵ��������ؼ�
		mChatView = new IMChatView(this, mCustomUserID);
		// ���������е�����Ϣ��Gif��̬������ٽ�ֵ����������Ϣ�еı�������������ٽ�ֵʱ��
		// ��ñ�����ʾΪ��Gif��̬���飬���Ǿ�̬����--- Ĭ��ֵΪ10��
		mChatView.setMaxGifCountInMessage(10);
		// ������������Ƿ���ʾ����˫����ͷ��
		mChatView.setUserMainPhotoVisible(true);
		// �������������ͷ���Բ�Ƕ���
		mChatView.setUserMainPhotoCornerRadius(10);
		// ������������Ƿ���ʾ����˫����id
		mChatView.setUserNameVisible(false);
		// titlebar�ɼ�
		mChatView.setTitleBarVisible(true);
		// mChatView.setTitleBarBackgroundColor(Color.BLUE);
		mChatView.setTitleBarBackground(R.drawable.bg_bar);
		setContentView(mChatView);
	}

	// ��д onActivityResult����
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mChatView.onActivityResult(requestCode, resultCode, data);
	}

	// ��д onKeyDown����
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mChatView.onKeyDown(keyCode, event)) {
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
