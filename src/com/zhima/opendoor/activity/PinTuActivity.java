package com.zhima.opendoor.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.TextView;

import com.zhima.opendoor.R;
import com.zhima.opendoor.view.GamePintuLayout;
import com.zhima.opendoor.view.GamePintuLayout.GamePintuListener;


public class PinTuActivity extends Activity
{

	private GamePintuLayout mGamePintuLayout;
	private TextView mLevel ; 
	private TextView mTime;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pintu_layout);

		mTime = (TextView) findViewById(R.id.id_time);
		mLevel = (TextView) findViewById(R.id.id_level);
		
		mGamePintuLayout = (GamePintuLayout) findViewById(R.id.id_gamepintu);
		mGamePintuLayout.setTimeEnabled(true);
		
		mGamePintuLayout.setOnGamePintuListener(new GamePintuListener()
		{
			@Override
			public void timechanged(int currentTime)
			{
				mTime.setText(""+currentTime);
			}

			@Override
			public void nextLevel(final int nextLevel)
			{
				new AlertDialog.Builder(PinTuActivity.this)
						.setTitle("Game Info").setMessage("LEVEL UP !!!")
						.setPositiveButton("NEXT LEVEL", new OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog,
									int which)
							{
								mGamePintuLayout.nextLevel();
								mLevel.setText(""+nextLevel);
							}
						}).show();
			}

			@Override
			public void gameover()
			{
				new AlertDialog.Builder(PinTuActivity.this)
				.setTitle("Game Info").setMessage("Game over !!!")
				.setPositiveButton("RESTART", new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog,
							int which)
					{
						mGamePintuLayout.restart();
					}
				}).setNegativeButton("QUIT",new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						finish();
					}
				}).show();
			}
		});

	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		
		mGamePintuLayout.pause();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		mGamePintuLayout.resume();
	}

}
