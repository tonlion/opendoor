package com.zhima.opendoor.view;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zhima.opendoor.R;
import com.zhima.opendoor.utils.ImagePiece;
import com.zhima.opendoor.utils.ImageSplitterUtil;


public class GamePintuLayout extends RelativeLayout implements OnClickListener
{

	private int mColumn = 3;
	/**
	 * 瀹瑰櫒鐨勫唴杈硅窛
	 */
	private int mPadding;
	/**
	 * 姣忓紶灏忓浘涔嬮棿鐨勮窛绂伙紙妯�佺旱锛� dp
	 */
	private int mMargin = 3;

	private ImageView[] mGamePintuItems;

	private int mItemWidth;

	/**
	 * 娓告垙鐨勫浘鐗�
	 */
	private Bitmap mBitmap;

	private List<ImagePiece> mItemBitmaps;

	private boolean once;

	/**
	 * 娓告垙闈㈡澘鐨勫搴�
	 */
	private int mWidth;

	private boolean isGameSuccess;
	private boolean isGameOver;

	public interface GamePintuListener
	{
		void nextLevel(int nextLevel);

		void timechanged(int currentTime);

		void gameover();
	}

	public GamePintuListener mListener;

	/**
	 * 璁剧疆鎺ュ彛鍥炶皟
	 * 
	 * @param mListener
	 */
	public void setOnGamePintuListener(GamePintuListener mListener)
	{
		this.mListener = mListener;
	}

	private int mLevel = 1;
	private static final int TIME_CHANGED = 0x110;
	private static final int NEXT_LEVEL = 0x111;

	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			switch (msg.what)
			{
			case TIME_CHANGED:
				if (isGameSuccess || isGameOver || isPause)
					return;
				if (mListener != null)
				{
					mListener.timechanged(mTime);
				}
				if (mTime == 0)
				{
					isGameOver = true;
					mListener.gameover();
					return;
				}
				mTime--;
				mHandler.sendEmptyMessageDelayed(TIME_CHANGED, 1000);

				break;
			case NEXT_LEVEL:
				mLevel = mLevel + 1;
				if (mListener != null)
				{
					mListener.nextLevel(mLevel);
				} else
				{
					nextLevel();
				}
				break;

			}
		};
	};

	private boolean isTimeEnabled = false;
	private int mTime;

	/**
	 * 璁剧疆鏄惁寮�鍚椂闂�
	 * 
	 * @param isTimeEnabled
	 */
	public void setTimeEnabled(boolean isTimeEnabled)
	{
		this.isTimeEnabled = isTimeEnabled;
	}

	public GamePintuLayout(Context context)
	{
		this(context, null);
	}

	public GamePintuLayout(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public GamePintuLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	private void init()
	{
		mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				3, getResources().getDisplayMetrics());
		mPadding = min(getPaddingLeft(), getPaddingRight(), getPaddingTop(),
				getPaddingBottom());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 鍙栧鍜岄珮涓殑灏忓��
		mWidth = Math.min(getMeasuredHeight(), getMeasuredWidth());

		if (!once)
		{
			// 杩涜鍒囧浘锛屼互鍙婃帓搴�
			initBitmap();
			// 璁剧疆ImageView(Item)鐨勫楂樼瓑灞炴��
			initItem();
			// 鍒ゆ柇鏄惁寮�鍚椂闂�
			checkTimeEnable();

			once = true;
		}

		setMeasuredDimension(mWidth, mWidth);

	}

	private void checkTimeEnable()
	{
		if (isTimeEnabled)
		{
			// 鏍规嵁褰撳墠绛夌骇璁剧疆鏃堕棿
			countTimeBaseLevel();
			mHandler.sendEmptyMessage(TIME_CHANGED);
		}

	}

	private void countTimeBaseLevel()
	{
		mTime = (int) Math.pow(2, mLevel) * 60;
	}

	/**
	 * 杩涜鍒囧浘锛屼互鍙婃帓搴�
	 */
	private void initBitmap()
	{
		if (mBitmap == null)
		{
			mBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.image);
		}
		mItemBitmaps = ImageSplitterUtil.splitImage(mBitmap, mColumn);

		// 浣跨敤sort瀹屾垚鎴戜滑鐨勪贡搴�
		Collections.sort(mItemBitmaps, new Comparator<ImagePiece>()
		{
			@Override
			public int compare(ImagePiece a, ImagePiece b)
			{
				return Math.random() > 0.5 ? 1 : -1;
			}
		});

	}

	/**
	 * 璁剧疆ImageView(Item)鐨勫楂樼瓑灞炴��
	 */
	private void initItem()
	{
		mItemWidth = (mWidth - mPadding * 2 - mMargin * (mColumn - 1))
				/ mColumn;
		mGamePintuItems = new ImageView[mColumn * mColumn];
		// 鐢熸垚鎴戜滑鐨処tem锛岃缃甊ule
		for (int i = 0; i < mGamePintuItems.length; i++)
		{
			ImageView item = new ImageView(getContext());
			item.setOnClickListener(this);
			item.setImageBitmap(mItemBitmaps.get(i).getBitmap());

			mGamePintuItems[i] = item;
			item.setId(i + 1);

			// 鍦↖tem鐨則ag涓瓨鍌ㄤ簡index
			item.setTag(i + "_" + mItemBitmaps.get(i).getIndex());

			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					mItemWidth, mItemWidth);

			// 璁剧疆Item闂存í鍚戦棿闅欙紝閫氳繃rightMargin
			// 涓嶆槸鏈�鍚庝竴鍒�
			if ((i + 1) % mColumn != 0)
			{
				lp.rightMargin = mMargin;
			}
			// 涓嶆槸绗竴鍒�
			if (i % mColumn != 0)
			{
				lp.addRule(RelativeLayout.RIGHT_OF,
						mGamePintuItems[i - 1].getId());
			}
			// 濡傛灉涓嶆槸绗竴琛� , 璁剧疆topMargin鍜宺ule
			if ((i + 1) > mColumn)
			{
				lp.topMargin = mMargin;
				lp.addRule(RelativeLayout.BELOW,
						mGamePintuItems[i - mColumn].getId());
			}
			addView(item, lp);
		}

	}

	public void restart()
	{
		isGameOver = false;
		mColumn--;
		nextLevel();
	}
	
	private boolean isPause ; 
	
	public void pause()
	{
		isPause = true ; 
		mHandler.removeMessages(TIME_CHANGED);
	}
	
	public void resume()
	{
		if(isPause)
		{
			isPause = false ;
			mHandler.sendEmptyMessage(TIME_CHANGED);
		}
	}

	public void nextLevel()
	{
		this.removeAllViews();
		mAnimLayout = null;
		mColumn++;
		isGameSuccess = false;
		checkTimeEnable();
		initBitmap();
		initItem();
	}

	/**
	 * 鑾峰彇澶氫釜鍙傛暟鐨勬渶灏忓��
	 */
	private int min(int... params)
	{
		int min = params[0];

		for (int param : params)
		{
			if (param < min)
				min = param;
		}
		return min;
	}

	private ImageView mFirst;
	private ImageView mSecond;

	@Override
	public void onClick(View v)
	{
		if (isAniming)
			return;

		// 涓ゆ鐐瑰嚮鍚屼竴涓狪tem
		if (mFirst == v)
		{
			mFirst.setColorFilter(null);
			mFirst = null;
			return;
		}
		if (mFirst == null)
		{
			mFirst = (ImageView) v;
			mFirst.setColorFilter(Color.parseColor("#55FF0000"));
		} else
		{
			mSecond = (ImageView) v;
			// 浜ゆ崲鎴戜滑鐨処tem
			exchangeView();
		}

	}

	/**
	 * 鍔ㄧ敾灞�
	 */
	private RelativeLayout mAnimLayout;
	private boolean isAniming;

	/**
	 * 浜ゆ崲鎴戜滑鐨処tem
	 */
	private void exchangeView()
	{
		mFirst.setColorFilter(null);
		// 鏋勯�犳垜浠殑鍔ㄧ敾灞�
		setUpAnimLayout();

		ImageView first = new ImageView(getContext());
		final Bitmap firstBitmap = mItemBitmaps.get(
				getImageIdByTag((String) mFirst.getTag())).getBitmap();
		first.setImageBitmap(firstBitmap);
		LayoutParams lp = new LayoutParams(mItemWidth, mItemWidth);
		lp.leftMargin = mFirst.getLeft() - mPadding;
		lp.topMargin = mFirst.getTop() - mPadding;
		first.setLayoutParams(lp);
		mAnimLayout.addView(first);

		ImageView second = new ImageView(getContext());
		final Bitmap secondBitmap = mItemBitmaps.get(
				getImageIdByTag((String) mSecond.getTag())).getBitmap();
		second.setImageBitmap(secondBitmap);
		LayoutParams lp2 = new LayoutParams(mItemWidth, mItemWidth);
		lp2.leftMargin = mSecond.getLeft() - mPadding;
		lp2.topMargin = mSecond.getTop() - mPadding;
		second.setLayoutParams(lp2);
		mAnimLayout.addView(second);

		// 璁剧疆鍔ㄧ敾
		TranslateAnimation anim = new TranslateAnimation(0, mSecond.getLeft()
				- mFirst.getLeft(), 0, mSecond.getTop() - mFirst.getTop());
		anim.setDuration(300);
		anim.setFillAfter(true);
		first.startAnimation(anim);

		TranslateAnimation animSecond = new TranslateAnimation(0,
				-mSecond.getLeft() + mFirst.getLeft(), 0, -mSecond.getTop()
						+ mFirst.getTop());
		animSecond.setDuration(300);
		animSecond.setFillAfter(true);
		second.startAnimation(animSecond);

		// 鐩戝惉鍔ㄧ敾
		anim.setAnimationListener(new AnimationListener()
		{
			@Override
			public void onAnimationStart(Animation animation)
			{
				mFirst.setVisibility(View.INVISIBLE);
				mSecond.setVisibility(View.INVISIBLE);

				isAniming = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation)
			{

				String firstTag = (String) mFirst.getTag();
				String secondTag = (String) mSecond.getTag();

				mFirst.setImageBitmap(secondBitmap);
				mSecond.setImageBitmap(firstBitmap);

				mFirst.setTag(secondTag);
				mSecond.setTag(firstTag);

				mFirst.setVisibility(View.VISIBLE);
				mSecond.setVisibility(View.VISIBLE);

				mFirst = mSecond = null;
				mAnimLayout.removeAllViews();
				// 鍒ゆ柇鐢ㄦ埛娓告垙鏄惁鎴愬姛
				checkSuccess();
				isAniming = false;
			}
		});

	}

	/**
	 * 鍒ゆ柇鐢ㄦ埛娓告垙鏄惁鎴愬姛
	 */
	private void checkSuccess()
	{
		boolean isSuccess = true;

		for (int i = 0; i < mGamePintuItems.length; i++)
		{
			ImageView imageView = mGamePintuItems[i];
			if (getImageIndexByTag((String) imageView.getTag()) != i)
			{
				isSuccess = false;
			}
		}

		if (isSuccess)
		{
			isGameSuccess = true;
			mHandler.removeMessages(TIME_CHANGED);
			mHandler.sendEmptyMessage(NEXT_LEVEL);
		}

	}

	/**
	 * 鏍规嵁tag鑾峰彇Id
	 * 
	 * @param tag
	 * @return
	 */
	public int getImageIdByTag(String tag)
	{
		String[] split = tag.split("_");
		return Integer.parseInt(split[0]);
	}

	public int getImageIndexByTag(String tag)
	{
		String[] split = tag.split("_");
		return Integer.parseInt(split[1]);
	}

	/**
	 * 鏋勯�犳垜浠殑鍔ㄧ敾灞�
	 */
	private void setUpAnimLayout()
	{
		if (mAnimLayout == null)
		{
			mAnimLayout = new RelativeLayout(getContext());
			addView(mAnimLayout);
		}
	}

}
