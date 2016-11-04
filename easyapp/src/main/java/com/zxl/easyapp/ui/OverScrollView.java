package com.zxl.easyapp.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ScrollView;

import com.zxl.easyapp.listener.OnScrollChangedListener;

/**
 * Created by 张晓莉 on 2016/8/15.
 * 可以滑动监听并且带有上下拉伸回弹阻尼效果的ScrollView
 */
public class OverScrollView extends ScrollView {
    private static final int MAX_Y_OVERSCROLL_DISTANCE = 150;
    private static final float SCROLL_RATIO = 0.5f;// 阻尼系数
    public int maxYOverscrollDistance;
    private int flag = 0;
    private OnScrollChangedListener onScrollChangeListener;

    public OverScrollView(Context context) {
        super(context);
        initOverScrollView(context);
    }

    public OverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initOverScrollView(context);
    }

    public OverScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initOverScrollView(context);
    }

    private void initOverScrollView(Context context) {
        //get the density of the screen and do some maths with it on the max overscroll distance
        //variable so that you get similar behaviors no matter what the screen size
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        final float density = metrics.density;
        maxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        //This is where the magic happens, we have replaced the incoming maxOverScrollY with our own custom variable maxYOverscrollDistance;
        int newDeltaY = deltaY;
        int delta = (int) (deltaY * SCROLL_RATIO);
        if (delta != 0) newDeltaY = delta;
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxYOverscrollDistance, isTouchEvent);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollChangeListener != null && flag == 0) {
            onScrollChangeListener.onScrollChange(this, l, t, oldl, oldt);
        }
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setOnScrollChangeListener(OnScrollChangedListener onScrollChangeListener) {
        this.onScrollChangeListener = onScrollChangeListener;
    }
}