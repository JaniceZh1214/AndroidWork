package com.zxl.easyapp.listener;

import android.view.View;

/**
 * Created by 张晓莉 on 2016/8/15.
 * 滚动改变监听
 */
public interface OnScrollChangedListener {
    void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
}