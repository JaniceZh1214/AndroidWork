package com.zxl.easyapp.ui;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 展开的ListView
 */
public class ListViewExtend extends ListView {
    public ListViewExtend(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewExtend(Context context) {
        super(context);
    }

    public ListViewExtend(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}