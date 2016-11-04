package com.zxl.easyapp.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zxl.easyapp.R;
import com.zxl.easyapp.utils.DoubleClickExit;

import org.xutils.x;

import java.util.HashMap;

/**
 * Created by 张晓莉 on 2016/8/12.
 * 应用中所有Activity必须继承此基类
 */
public class BaseActivity extends FragmentActivity implements View.OnClickListener {
    protected Context context;
    protected Toast toast;
    protected BaseDialog baseDialog;
    protected DoubleClickExit doubleClickExit;
    protected InputMethodManager inputMethodManager;
    protected SystemBarTintManager systemBarTintManager;
    protected HashMap<String, String> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        context = this;
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        systemBarTintManager = new SystemBarTintManager(this);
        systemBarTintManager.setStatusBarTintEnabled(true);
        systemBarTintManager.setStatusBarTintResource(R.color.red_ff4848);//通知栏所需颜色
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        // 创建Toast对话框
        toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        doubleClickExit = new DoubleClickExit(context);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        x.view().inject(this);
    }

    @TargetApi(19)
    protected void setTranslucentStatus() {
        Window window = getWindow();
        // Translucent status bar
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }

    public void showToastMessage(String message) {
        toast.setText(message);
        toast.show();
    }

    public void hideToastMessage() {
        toast.cancel();
    }

    /**
     * 显示软键盘
     *
     * @param editText 适用于这个方法的控件
     */
    protected void showSoftKeyboard(final EditText editText, long delayMillis) {
        //解决因页面加载数据而使键盘无法自动弹出的问题
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        }, delayMillis);
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View v) {
    }
}