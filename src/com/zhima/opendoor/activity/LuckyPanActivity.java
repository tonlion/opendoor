package com.zhima.opendoor.activity;

import com.zhima.opendoor.R;
import com.zhima.opendoor.utils.LuckyPan;
import com.zhima.opendoor.view.SwipeBackActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class LuckyPanActivity extends SwipeBackActivity{

	private LuckyPan mLuckyPan;
	private ImageView mImageView;
	private boolean firstLucky;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lucky_pan);

		mLuckyPan = (LuckyPan) findViewById(R.id.id_luckyPan);
		mImageView = (ImageView) findViewById(R.id.id_start_btn);
		SharedPreferences sp = getSharedPreferences("setting", MODE_PRIVATE);
		firstLucky = sp.getBoolean("firstLucky", true);

		mImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (firstLucky) {

					if (!mLuckyPan.isStart()) {

						mLuckyPan.LuckyStart(0);
						mImageView.setImageResource(R.drawable.stop);
						SharedPreferences sp = getSharedPreferences("setting",
								MODE_PRIVATE);
						Editor edit = sp.edit();
						edit.putBoolean("firstLucky", false);
					
						edit.commit();
					} else {

						if (!mLuckyPan.isShouldEnd()) {

							mLuckyPan.LuckyEnd();
							mImageView.setImageResource(R.drawable.start);
						
							firstLucky=false;
							Toast.makeText(v.getContext(), "��ϲ�������������",
									Toast.LENGTH_SHORT).show();
						}
					}
				} else {
					Toast.makeText(LuckyPanActivity.this, "�ף�����ֻ��һ�Σ������ظ��齱Ŷ~",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

}
