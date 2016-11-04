package com.zxl.easyapp.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 限定输入字数 区分中文英文
 *
 * @author why
 */
public class EditTextLimitNum extends EditText {
    private int maxLen = 10; // the max byte
    private Context mContext;

    public EditTextLimitNum(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        mContext = context;
    }

    public EditTextLimitNum(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        mContext = context;
    }

    public EditTextLimitNum(Context context) {
        super(context);
        init();
        mContext = context;
    }

    public void setLimitNum(int maxLen) {
        this.maxLen = maxLen;
    }

    private void init() {
        addTextChangedListener(tw);
    }

    int numNow = 0;
    int numAfter = 0;
    TextWatcher tw = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            numNow = start;
            numAfter = after;
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public Boolean checkNum(String str) {
        if (!TextUtils.isEmpty(getText())) {
            String etstring = getText().toString().trim();
            if (calculateLength(etstring) > maxLen) {
                int num = maxLen / 2;
                int num2 = maxLen / 4;
                Toast.makeText(mContext, str + "最多输入" + maxLen + "个英文或" + num + "个中文", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public int calculateLength(String etstring) {
        char[] ch = etstring.toCharArray();

        int varlength = 0;
        for (int i = 0; i < ch.length; i++) {
            // changed by zyf 0825 , bug 6918，加入中文标点范围 ， TODO 标点范围有待具体化
            if ((ch[i] >= 0x2E80 && ch[i] <= 0xFE4F)
                    || (ch[i] >= 0xA13F && ch[i] <= 0xAA40) || ch[i] >= 0x80) { // 中文字符范围0x4e00
                // 0x9fbb
                varlength = varlength + 2;
            } else {
                varlength++;
            }
        }
        return varlength;
    }
}