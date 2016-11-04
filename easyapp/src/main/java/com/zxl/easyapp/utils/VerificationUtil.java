package com.zxl.easyapp.utils;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 张晓莉 on 2016/9/28.
 * 验证操作工具类
 */
public class VerificationUtil {

    /**
     * 验证手机号
     *
     * @param phoneNum
     * @return 验证通过返回true
     */
    public static boolean isMobile(String phoneNum) {
        Pattern p = null;
        Matcher m = null;
        boolean isMobile = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(phoneNum);
        isMobile = m.matches();
        return isMobile;
    }

    /**
     * 验证字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    /**
     * 验证edittext是否null
     */
    public static String checkEditText(EditText editText) {
        if (editText != null && editText.getText() != null
                && !(editText.getText().toString().trim().equals(""))) {
            return editText.getText().toString().trim();
        } else {
            return "";
        }
    }
}