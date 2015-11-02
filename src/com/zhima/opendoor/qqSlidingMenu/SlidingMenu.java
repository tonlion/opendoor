package com.zhima.opendoor.qqSlidingMenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;
import com.zhima.opendoor.R;

/**
 * Created by Owner on 2015/9/19.
 */

public class SlidingMenu extends HorizontalScrollView {

    /**
     * �Զ���view
     * 1��onMeasure
     * �����ڲ�view(��view)�Ŀ�͸ߣ��Լ��Լ��Ŀ�͸�
     * 2��onLayout
     * ������view�ķ��õ�λ��
     * 3����дontouchevent
     */
    private LinearLayout mWapper;
    private ViewGroup mMenu;
    private ViewGroup mContent;

    private int mScreenWidth;
    private int mMenuRightPadding = 250;

    private boolean once;//��ֹonMeasure���ö��

    private int mMenuWidth;

    private boolean isOpen;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    /**
     * ��ʹ�����Զ�������ʱ������ô˹��캯��
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //��ȡ�Զ�������ֵ
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SlidingMenu, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.SlidingMenu_rightPadding:
                    mMenuRightPadding = a.getDimensionPixelSize(attr,
                            //��dpת����px
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250,
                                    context.getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
    }

    /**
     * AttributeSet δʹ���Զ�������ʱ����
     *
     * @param context
     * @param attrs
     */
    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * ������view�Ŀ�͸�
     * �����Լ��Ŀ�͸�
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!once) {
            mWapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWapper.getChildAt(0);
            mContent = (ViewGroup) mWapper.getChildAt(1);
            mMenuWidth = mMenu.getLayoutParams().width =
                    mScreenWidth - mMenuRightPadding;
            mContent.getLayoutParams().width = mScreenWidth;
            once = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * ͨ������ƫ��������menu����
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed)
            this.scrollTo(mMenuWidth, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            //�û�̧��Ķ���
            case MotionEvent.ACTION_UP:
                //��������ߵĿ��
                int scrollX = getScrollX();
                if (scrollX >= mMenuWidth / 2) {
                    //��ʾ������С��1/2ʱ�����ز˵�
                    this.smoothScrollTo(mMenuWidth, 0);
                    isOpen = false;
                } else {
                    //��ʾ������С��1/2ʱ���˵���ʾ
                    this.smoothScrollTo(0, 0);
                    isOpen = true;
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * �򿪲˵�
     */
    public void openMenu() {
        if (isOpen)
            return;
        this.smoothScrollTo(0, 0);
        isOpen = true;
    }

    /**
     * �رղ˵�
     */
    public void closeMenu() {
        if (!isOpen)
            return;
        this.smoothScrollTo(mMenuWidth, 0);
        isOpen = false;
    }

    /**
     * �л��˵�
     */
    public void toggle() {
        if (isOpen)
            closeMenu();
        else
            openMenu();
    }

    /**
     * ��������ʱ�������ֶ����Զ�
     *
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //3.0��������
        float scale = (float) (l * 1.0 / mMenuWidth);
        /**
         * ��QQ������
         *
         * ����1����������1.0~0.7 ���ŵ�Ч�� scale : 1.0~0.0 0.7 + 0.3 * scale
         *
         * ����2���˵���ƫ������Ҫ�޸�
         *
         * ����3���˵�����ʾʱ�������Լ�͸���ȱ仯 ���ţ�0.7 ~1.0 1.0 - scale * 0.3 ͸���� 0.6 ~ 1.0
         * 0.6+ 0.4 * (1- scale) ;
         *
         */
        float rightScale = 0.8f + 0.2f * scale;
        float leftScale = 1.0f - scale * 0.3f;
        float leftAlpha = 1.0f - 0.4f * scale;
        //�������Զ���������translaX
        ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.8f);
        //Ϊ�˵����ö���
        ViewHelper.setScaleX(mMenu, leftScale);
        ViewHelper.setScaleY(mMenu, leftScale);
        ViewHelper.setAlpha(mMenu, leftAlpha);
        //����content���������ĵ�
        ViewHelper.setPivotX(mContent, 0);
        ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
        ViewHelper.setScaleX(mContent, rightScale);
        ViewHelper.setScaleY(mContent, rightScale);
    }
}
