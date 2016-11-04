package com.zxl.easyapp.utils;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

/**
 * Created by 张晓莉 on 2016/8/12.
 * 滑动操作工具类
 */
public class ScrollUtil {
    private ScrollUtil() {
    }

    /**
     * 确定是否可以刷新
     *
     * @param listView
     * @param swipeRefreshLayout
     */
    public static void enableSwipeRefresh(ListView listView, SwipeRefreshLayout swipeRefreshLayout) {
        boolean enable = false;
        if (listView != null && listView.getChildCount() > 0) {
            // check if the first item of the list is visible
            boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
            // check if the top of the first item is visible
            boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
            // enabling or disabling the refresh layout
            enable = firstItemVisible && topOfFirstItemVisible;
        }
        swipeRefreshLayout.setEnabled(enable);
    }

    /**
     * 获取当前ListViewY轴滚动过的距离
     *
     * @param listView
     * @return
     */
    public static int getCurrentScrollY(ListView listView) {
        int scrolly = 0;
        View childView = listView.getChildAt(0);
        if (childView != null) {
            int firstVisiblePosition = listView.getFirstVisiblePosition();
            int top = childView.getTop();
            scrolly = -top + firstVisiblePosition * childView.getHeight();
        }
        return scrolly;
    }
}