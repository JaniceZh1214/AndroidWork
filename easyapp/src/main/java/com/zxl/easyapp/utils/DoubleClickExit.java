package com.zxl.easyapp.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.Toast;

import com.zxl.easyapp.common.AppManager;

/**
 * Created by 张晓莉 on 2016/8/17.
 * 双击退出应用
 */
public class DoubleClickExit {
    private Context context;
    private Handler doubleClickHandler;
    private boolean isOnKeyBacking;
    private Toast backTipToast;

    public DoubleClickExit(Context context) {
        this.context = context;
        doubleClickHandler = new Handler(Looper.getMainLooper());
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return false;
        }
        if (isOnKeyBacking) {
            doubleClickHandler.removeCallbacks(onBackTimeRunnable);
            if (backTipToast != null) {
                backTipToast.cancel();
            }
            // 退出
            AppManager.getAppManager().AppExit(context);
            return true;
        } else {
            isOnKeyBacking = true;
            if (backTipToast == null) {
                backTipToast = Toast.makeText(context, "再按一次退出班级赛", Toast.LENGTH_SHORT);
            }
            backTipToast.show();
            doubleClickHandler.postDelayed(onBackTimeRunnable, 1000);
            return true;
        }
    }

    private Runnable onBackTimeRunnable = new Runnable() {
        @Override
        public void run() {
            isOnKeyBacking = false;
            if (backTipToast != null) {
                backTipToast.cancel();
            }
        }
    };
}