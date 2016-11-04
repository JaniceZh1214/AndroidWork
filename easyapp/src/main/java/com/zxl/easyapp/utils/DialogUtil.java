package com.zxl.easyapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;

import com.zxl.easyapp.R;
import com.zxl.easyapp.common.BaseDialog;
import com.zxl.easyapp.listener.OnPrepareListener;

/**
 * Created by 张晓莉 on 2016/8/12.
 * 对话框工具类
 */
public class DialogUtil {

    /**
     * 显示信息提示框
     *
     * @param context    上下文环境
     * @param title      标题
     * @param tip        提示内容
     * @param okText     确认提示信息
     * @param okListener 确认操作
     * @return
     */
    public static BaseDialog showTipDialog(Context context, String title, String tip, String okText, DialogInterface.OnClickListener okListener) {
        BaseDialog baseDialog = new BaseDialog(context);
        baseDialog.setTitle(title);
        baseDialog.setTip(tip);
        baseDialog.setPositive(okText, okListener);
        baseDialog.setNegative("", null);
        baseDialog.show();
        return baseDialog;
    }

    /**
     * 显示信息提示框
     *
     * @param context    上下文环境
     * @param tip        提示内容
     * @param cancelText 取消提示信息
     * @param noListener 取消操作
     * @return
     */
    public static BaseDialog showTipDialog(Context context, String tip, String cancelText, DialogInterface.OnClickListener noListener) {
        BaseDialog baseDialog = new BaseDialog(context);
        baseDialog.setTip(tip);
        baseDialog.setNegative(cancelText, noListener);
        baseDialog.setPositive("", null);
        baseDialog.show();
        return baseDialog;
    }

    /**
     * 显示信息提示框
     *
     * @param context    上下文环境
     * @param title      标题
     * @param tip        提示内容
     * @param okText     确认提示信息
     * @param okListener 确认操作
     * @param cancelText 取消提示信息
     * @param noListener 取消操作
     * @return
     */
    public static BaseDialog showTipDialog(Context context, String title, String tip, String okText, DialogInterface.OnClickListener okListener, String cancelText, DialogInterface.OnClickListener noListener) {
        BaseDialog baseDialog = new BaseDialog(context);
        baseDialog.setTitle(title);
        baseDialog.setTip(tip);
        baseDialog.setPositive(okText, okListener);
        baseDialog.setNegative(cancelText, noListener);
        baseDialog.show();
        return baseDialog;
    }

    /**
     * 显示信息提示框
     *
     * @param context    上下文环境
     * @param tip        提示内容
     * @param okText     确认提示信息
     * @param okListener 确认操作
     * @param cancelText 取消提示信息
     * @param noListener 取消操作
     * @return
     */
    public static BaseDialog showTipDialog(Context context, String tip, String okText, DialogInterface.OnClickListener okListener, String cancelText, DialogInterface.OnClickListener noListener) {
        BaseDialog baseDialog = new BaseDialog(context);
        baseDialog.setTip(tip);
        baseDialog.setPositive(okText, okListener);
        baseDialog.setNegative(cancelText, noListener);
        baseDialog.show();
        return baseDialog;
    }

    /**
     * 显示有内容展示的对话框
     *
     * @param context           上下文环境
     * @param layoutResource    内容布局id
     * @param onPrepareListener 布局加载及其操作
     * @param okText            确认提示信息
     * @param okListener        确认操作
     * @param cancelText        取消提示信息
     * @param noListener        取消操作
     * @return
     */
    public static BaseDialog showContentDialog(Context context, int layoutResource, OnPrepareListener onPrepareListener,
                                               String okText, DialogInterface.OnClickListener okListener, String cancelText, DialogInterface.OnClickListener noListener) {
        final BaseDialog baseDialog = new BaseDialog(context);
        View view = LayoutInflater.from(context).inflate(layoutResource, null);
        if (onPrepareListener != null) {
            onPrepareListener.onPrepare(view);
        }
        baseDialog.setContent(view);
        baseDialog.setPositive(okText, okListener);
        baseDialog.setNegative(cancelText, noListener);
        baseDialog.show();
        return baseDialog;
    }

    /**
     * 显示有内容展示的对话框
     *
     * @param context           上下文环境
     * @param title             标题
     * @param layoutResource    内容布局id
     * @param onPrepareListener 布局加载及其操作
     * @param okText            确认提示信息
     * @param okListener        确认操作
     * @param cancelText        取消提示信息
     * @param noListener        取消操作
     * @return
     */
    public static BaseDialog showContentDialog(Context context, String title, int layoutResource, OnPrepareListener onPrepareListener,
                                               String okText, DialogInterface.OnClickListener okListener, String cancelText, DialogInterface.OnClickListener noListener) {
        final BaseDialog baseDialog = new BaseDialog(context);
        baseDialog.setTitle(title);
        View view = LayoutInflater.from(context).inflate(layoutResource, null);
        if (onPrepareListener != null) {
            onPrepareListener.onPrepare(view);
        }
        baseDialog.setContent(view);
        baseDialog.setPositive(okText, okListener);
        baseDialog.setNegative(cancelText, noListener);
        baseDialog.show();
        return baseDialog;
    }

    /**
     * 显示有内容展示的对话框
     *
     * @param context           上下文环境
     * @param title             标题
     * @param layoutResource    内容布局id
     * @param onPrepareListener 布局加载及其操作
     * @param cancelText        取消提示信息
     * @param noListener        取消操作
     * @return
     */
    public static BaseDialog showContentDialog(Context context, String title, int layoutResource, OnPrepareListener onPrepareListener,
                                               String cancelText, DialogInterface.OnClickListener noListener) {
        final BaseDialog baseDialog = new BaseDialog(context);
        baseDialog.setTitle(title);
        View view = LayoutInflater.from(context).inflate(layoutResource, null);
        if (onPrepareListener != null) {
            onPrepareListener.onPrepare(view);
        }
        baseDialog.setContent(view);
        baseDialog.setNegative(cancelText, noListener);
        baseDialog.setPositive("", null);
        baseDialog.show();
        return baseDialog;
    }

    /**
     * 显示有内容展示的对话框
     *
     * @param context           上下文环境
     * @param layoutResource    内容布局id
     * @param onPrepareListener 布局加载及其操作
     * @param cancelText        取消提示信息
     * @param noListener        取消操作
     * @return
     */
    public static BaseDialog showContentDialog(Context context, int layoutResource, OnPrepareListener onPrepareListener,
                                               String cancelText, DialogInterface.OnClickListener noListener) {
        final BaseDialog baseDialog = new BaseDialog(context);
        View view = LayoutInflater.from(context).inflate(layoutResource, null);
        if (onPrepareListener != null) {
            onPrepareListener.onPrepare(view);
        }
        baseDialog.setContent(view);
        baseDialog.setNegative(cancelText, noListener);
        baseDialog.setPositive("", null);
        baseDialog.show();
        return baseDialog;
    }

    private static Activity mActivity;
    private static View mView;
    /**
     * 对话框
     */
    private static Dialog dialog;
    private static int layoutResource;

    public static void showProgressDialog(Activity activity) {
        if (null != dialog && dialog.isShowing()) {
            return;
        }
        mActivity = activity;
        layoutResource = R.layout.layout_loading_progress_bar;
        initDialog();
        if (null != dialog) {
            /**
             * 等数据加载完毕，显示对话框
             */
            Window window = dialog.getWindow();
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.x = 0;
            wl.y = mActivity.getWindowManager().getDefaultDisplay().getHeight();
            // 以下这两句是为了保证按钮可以水平满屏
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
            wl.height = ViewGroup.LayoutParams.MATCH_PARENT;
            // 设置显示位置
            dialog.onWindowAttributesChanged(wl);
            // 设置点击外围解散
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }
    }

    public static void dismissDialog() {
        if (null != dialog) {
            AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);// 创建一个AlphaAnimation
            // 对象，渐变从1->0
            animation.setDuration(500);// 设置持续时间
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.setFillAfter(true);// 保持最后的渐变状态
            mView.startAnimation(animation);// 启动动画
            mView.setVisibility(View.GONE);
            dialog.dismiss();
        }
    }

    private static void initDialog() {
        mView = mActivity.getLayoutInflater().inflate(layoutResource, null);
        dialog = new Dialog(mActivity, R.style.dialogProgressWindowStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(mView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }
}