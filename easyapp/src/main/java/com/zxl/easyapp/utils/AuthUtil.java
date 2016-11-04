package com.zxl.easyapp.utils;

import android.app.Activity;
import android.content.Context;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by 张晓莉 on 2016/10/19.
 * 第三方授权工具类
 */
public class AuthUtil {

    public static void showAuthDialog(Context context, SHARE_MEDIA platform, UMAuthListener umAuthListener) {
        UMShareAPI umShareAPI = UMShareAPI.get(context);
        umShareAPI.doOauthVerify((Activity) context, platform, umAuthListener);
    }
}