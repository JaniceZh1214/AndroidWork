package com.zxl.easyapp.common;

import android.os.Environment;

import com.zxl.easyapp.R;

/**
 * Created by 张晓莉 on 2016/8/12.
 * 应用常量配置文件
 */
public class BaseConstant {
    //第三方平台权限配置
    public static final String QQ_APPID = "1104476315";
    public static final String QQ_APPKEY = "ZG1Dvk2i1omZ7uen";
    public static final String WX_APPID = "wxac46bca9dd66efc6";
    public static final String WX_APPSECRET = "63f60e15c0aab892c6428b51309eef7e";
    public static final String SINA_APPKEY = "3287894211";
    public static final String SINA_APPSECRET = "18208c3e6d935b417ad17e3e223902b5";
    public static final String ACCOUNT_REMOVED = "account_removed";
    public static final String ACCOUNT_CONFLICT = "conflict";
    //sd卡路径
    public static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ClassMatch/Portrait/";
    public static final String SDCARD_MNT = "/mnt/sdcard";
    public static final String SDCARD = "/sdcard";
    //请求相册
    public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
    //请求相机
    public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
    //请求裁剪
    public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;
    public static final int DOWN_NOSDCARD = 3;
    public static final int DOWN_UPDATE = 4;
    public static final int DOWN_OVER = 5;
    //上传图片最大张数
    public static final int MAX_IMAGE_NUM = 8;
    //裁剪
    public final static int CROP = 200;
    public static final String[] CHOICE_IMAGE_LIST = {"相册", "拍照"};
    public static final int[] DEFAULT_HEADER = {R.mipmap.touxiang_1, R.mipmap.touxiang_2, R.mipmap.touxiang_3,
            R.mipmap.touxiang_4, R.mipmap.touxiang_5, R.mipmap.touxiang_6};
}