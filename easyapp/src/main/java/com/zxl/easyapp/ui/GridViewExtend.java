package com.zxl.easyapp.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 解决Scrollview嵌套GridView事GridView显示不全的问题
 *
 * @author Sco
 */
public class GridViewExtend extends GridView {

    public GridViewExtend(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public GridViewExtend(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewExtend(Context context) {
        super(context);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}