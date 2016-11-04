package com.zxl.easyapp.common;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zxl.easyapp.R;

/**
 * Created by 张晓莉 on 2016/8/12.
 * 应用中的所有的对话框必须继承此基类
 */
public class BaseDialog extends Dialog {
    protected View dialogView;
    protected TextView id_tv_title;
    protected TextView id_tv_tip;
    protected FrameLayout id_layout_content;
    protected TextView id_tv_positive;
    protected View id_view_divider;
    protected TextView id_tv_negative;

    public BaseDialog(Context context) {
        this(context, R.style.TipDialog);
        initView(context);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    private void initView(Context context) {
        setCancelable(false);
        dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_base, null);
        id_tv_title = (TextView) dialogView.findViewById(R.id.id_tv_title);
        id_tv_tip = (TextView) dialogView.findViewById(R.id.id_tv_tip);
        id_layout_content = (FrameLayout) dialogView.findViewById(R.id.id_layout_content);
        id_tv_positive = (TextView) dialogView.findViewById(R.id.id_tv_positive);
        id_view_divider = dialogView.findViewById(R.id.id_view_divider);
        id_tv_negative = (TextView) dialogView.findViewById(R.id.id_tv_negative);
        setContentView(dialogView);
    }

    public void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            id_tv_title.setVisibility(View.GONE);
            return;
        }
        id_tv_title.setVisibility(View.VISIBLE);
        id_tv_title.setText(title);
    }

    public void setTip(String tip) {
        if (TextUtils.isEmpty(tip)) {
            id_tv_tip.setVisibility(View.GONE);
            return;
        }
        id_tv_tip.setVisibility(View.VISIBLE);
        id_tv_tip.setText(tip);
    }

    public void setContent(View view) {
        if (view == null) {
            id_layout_content.setVisibility(View.GONE);
        }
        id_layout_content.setVisibility(View.VISIBLE);
        id_layout_content.removeAllViews();
        id_layout_content.addView(view);
    }

    public void setPositive(String okText, final OnClickListener okListener) {
        if (TextUtils.isEmpty(okText)) {
            id_tv_positive.setVisibility(View.GONE);
            id_view_divider.setVisibility(View.GONE);
            id_tv_negative.setBackgroundResource(R.drawable.bg_btn_white_bottom_radius_10);
            return;
        }
        id_tv_positive.setBackgroundResource(R.drawable.bg_btn_white_bottom_left_radius_10);
        id_tv_positive.setVisibility(View.VISIBLE);
        id_view_divider.setVisibility(View.VISIBLE);
        id_tv_positive.setText(okText);
        id_tv_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (okListener != null) {
                    okListener.onClick(BaseDialog.this, 0);
                } else {
                    BaseDialog.this.dismiss();
                }
            }
        });
    }

    public void setNegative(String cancelText, final OnClickListener noListener) {
        if (TextUtils.isEmpty(cancelText)) {
            id_view_divider.setVisibility(View.GONE);
            id_tv_negative.setVisibility(View.GONE);
            id_tv_positive.setBackgroundResource(R.drawable.bg_btn_white_bottom_radius_10);
            return;
        }
        id_tv_negative.setBackgroundResource(R.drawable.bg_btn_white_bottom_right_radius_10);
        id_view_divider.setVisibility(View.VISIBLE);
        id_tv_negative.setVisibility(View.VISIBLE);
        id_tv_negative.setText(cancelText);
        id_tv_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noListener != null) {
                    noListener.onClick(BaseDialog.this, 0);
                } else {
                    BaseDialog.this.dismiss();
                }
            }
        });
    }
}