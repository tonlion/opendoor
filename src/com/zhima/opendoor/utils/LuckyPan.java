package com.zhima.opendoor.utils;

import java.lang.reflect.TypeVariable;
import java.util.concurrent.Delayed;
import com.zhima.opendoor.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.renderscript.Type;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.Toast;

public class LuckyPan extends SurfaceView implements Callback, Runnable {

	private SurfaceHolder mHolder;
	
	/**
	 * 与SurfaceHolder绑定的Canvas
	 */
	private Canvas mCanvas;
	
	/**
	 * 用于绘制的线程
	 */
	private Thread t;
	
	/**
	 * 线程的控制开关
	 */
	private boolean isRunning;
	
	/**
	 * 抽奖的文字
	 */
	private String[] name = new String[]{"单反相机", "IPAD", "恭喜发财", "IPHONE",  
            "妹子一只", "恭喜发财"};
	
	/**
	 * 与文字对应图片
	 */
	private int[] Imgs = new int[]{R.drawable.danfan, R.drawable.ipad,  
            R.drawable.f040, R.drawable.iphone, R.drawable.meizi,  
            R.drawable.f040 };
	
	/**
	 * 每个盘块的颜色
	 */
	private int[] colors = new int[]{0xFFFFC300, 0xFFF17E01, 0xFFFFC300,  
            0xFFF17E01, 0xFFFFC300, 0xFFF17E01};
	
	/**
	 * 与文字对应图片的bitmap数组
	 */
	private Bitmap [] mImgsBitmaps;
	
	/**
	 * 盘块的个数
	 */
	private int mItemCounts = 6;
	
	/**
	 * 绘制盘块的范围
	 */
	private RectF mRange = new RectF();
	
	/**
	 * 圆的直径
	 */
	private int mRadius;
	
	/**
	 * 绘制文字的画笔
	 */
	private Paint mTextPaint;
	
	/**
	 * 绘制盘块的画笔
	 */
	private Paint mArcPaint;
	
	/**
	 * 滚动的速度
	 */
	private double mSpeed;
	private volatile float mStartAngle = 0;
	
	/**
	 * 控件的中心位置
	 */
	private int mCenter;
	
	/** 
     * 控件的padding，这里我们认为4个padding的值一致，以paddingleft为标准 
     */  
	private int mPadding;
	
	/**
	 * 背景图的bitmap
	 */
	private Bitmap mBgBitmap  = BitmapFactory.decodeResource(getResources(), R.drawable.bg3);
	
	/**
	 * 文字的大小
	 */
	private float mtextSize  = TypedValue.applyDimension(TypedValue.DENSITY_DEFAULT, 20, getResources().getDisplayMetrics());
	
	/**
	 * 
	 */
	private boolean isShouldEnd;
	
	public LuckyPan(Context context) {
		super(context,null);
		
	}

	public LuckyPan(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		
		//设置可获取焦点
		setFocusable(true);
		setFocusableInTouchMode(true);
		
		//设置常量
		this.setKeepScreenOn(true);
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
		mRadius = width - getPaddingLeft() - getPaddingRight();
		mPadding = getPaddingLeft();
		mCenter = width / 2;
		
		setMeasuredDimension(width, width);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		//初始化绘制盘块的画笔
		mArcPaint = new Paint();
		mArcPaint.setAntiAlias(true);//设置去锯齿
		mArcPaint.setDither(true);//设置递色
		
		//初始化绘制文字的画笔
		mTextPaint = new Paint();
		mTextPaint.setColor(0xffffffff);
		mTextPaint.setTextSize(mtextSize);
		
		//绘制圆盘的范围
		mRange = new RectF(mPadding, mPadding, mPadding + mRadius, mPadding + mRadius);
		
		//初始化图片
		mImgsBitmaps = new Bitmap[mItemCounts];
		for(int i=0;i<mItemCounts;i++){
			
			mImgsBitmaps[i] = BitmapFactory.decodeResource(getResources(), Imgs[i]);
		}
		
		isRunning = true;
		t = new Thread(this);
		t.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		
		isRunning = false;
		
	}

	@Override
	public void run() {
		
		while(isRunning){
			
			long start = System.currentTimeMillis();
			darw();
			long end = System.currentTimeMillis();
			
			if(end - start < 100){
				
				try {
					Thread.sleep(100 -(end - start));
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}
		}
		
	}

	private void darw() {
		
		try {
			mCanvas = mHolder.lockCanvas();
			if(mCanvas != null){
				
				//绘制背景图
				drawBg();
				
				
				float tmpAngle = mStartAngle;  //起始角度
                float sweepAngle = (float) (360 / mItemCounts);//转动角度
                
				for(int i=0;i<mItemCounts;i++){
					//绘制盘块
					mArcPaint.setColor(colors[i]);
					mCanvas.drawArc(mRange, tmpAngle, sweepAngle, true, mArcPaint);
					
					//绘制文本
					darwText(tmpAngle,sweepAngle,name[i]);
					
					//绘制退片
					darwIcon(tmpAngle,mImgsBitmaps[i]);
					
					tmpAngle += sweepAngle;
					mStartAngle += mSpeed;
					
					//判断是否点击了停止按钮
					if(isShouldEnd){
						
						mSpeed -= 1;
					}
					
					if(mSpeed <=0){
						mSpeed = 0;
						isShouldEnd = false;
					}
				}
				
				
			}
		} catch (Exception e) {
		}
		finally{
			
			if(mCanvas != null){
				mHolder.unlockCanvasAndPost(mCanvas);
			}
		}
	}

	/**
	 * 绘制图片
	 * @param tmpAngle
	 * @param bitmap
	 */
	private void darwIcon(float startAngle, Bitmap bitmap) {
		
		//控制图片显示大小
		int imgWidth = mRadius / 8;
		
		float  angle = (float) ((30 + startAngle) *(Math.PI /180));
		
		float x = (float) (mCenter + mRadius/4 * Math.cos(angle));
		float y = (float) (mCenter + mRadius/4 * Math.sin(angle));
		
		//确定图片的位置
		RectF rectf = new RectF(x - imgWidth/2, y - imgWidth / 2, x + imgWidth/2, y + imgWidth / 2);
		
		mCanvas.drawBitmap(bitmap, null, rectf, null);
		
	}

	/**
	 * 绘制文本
	 * @param stratAngle
	 * @param sweepAngle
	 * @param string
	 */
	private void darwText(float stratAngle, float sweepAngle, String string) {
		
		Path path = new Path();
		path.addArc(mRange, stratAngle, sweepAngle);
		//文字的长度
		float textWidth = mTextPaint.measureText(string);
		//设置水平偏移量使文字居中显示
		float hOffset = (float) (mRadius * Math.PI / mItemCounts / 2 - textWidth / 2);
		//设置垂直偏移量
		float vOffset = mRadius / 2 / 6 ;
		mCanvas.drawTextOnPath(string, path, hOffset, vOffset, mTextPaint);
		
	}

	/**
	 * 绘制背景图
	 */
	private void drawBg() {
		
		mCanvas.drawColor(0xffffffff);
		mCanvas.drawBitmap(mBgBitmap, null, new RectF(mPadding / 2, mPadding / 2, 
				getMeasuredWidth() - mPadding /2, getMeasuredWidth() - mPadding / 2), null);
		
	}
	
	/**
	 * 点击开始转动按钮
	 */
	public void LuckyStart(int index){
		
		float angle = (360 / mItemCounts);
		
		//中奖的角度范围，第一项为210—270
		float from = 270 - (index + 1) * angle;
		float to = from + angle;
		
		//停止时的旋转距离
		float targetFrom = 6 * 360 + from;
		float targetTo = 6 * 360 + to;
		
		/** 
         * <pre> 
         *  (v1 + 0) * (v1+1) / 2 = target ; 
         *  v1*v1 + v1 - 2target = 0 ; 
         *  v1=-1+(1*1 + 8 *1 * target)/2; 
         * </pre> 
         */  
        float v1 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetFrom) - 1) / 2;  
        float v2 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetTo) - 1) / 2;  
        
        mSpeed = v1 + Math.random() * (v2 - v1);
		isShouldEnd = false;
	}
	
	public void LuckyEnd(){
		
		isShouldEnd = true;
		mStartAngle = 0;
	}
	
	/**
	 * 判断是否在转动
	 * @return
	 */
	public boolean isStart(){
		return mSpeed != 0;
	}
	
	public boolean isShouldEnd(){
		return isShouldEnd;
	}

}
