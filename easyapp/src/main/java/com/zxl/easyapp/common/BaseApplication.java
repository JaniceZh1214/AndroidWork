package com.zxl.easyapp.common;

import android.app.Application;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

/**
 * Created by 张晓莉 on 2016/8/12.
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this); // 这一步之后, 我们就可以在任何地方使用x.app()来获取Application的实例了.
        x.Ext.setDebug(true); // 是否输出debug日志
        //各个平台的配置，建议放在全局Application或者程序入口
        //QQ
        PlatformConfig.setQQZone(BaseConstant.QQ_APPID, BaseConstant.QQ_APPKEY);
        //微信
        PlatformConfig.setWeixin(BaseConstant.WX_APPID, BaseConstant.WX_APPSECRET);
        //新浪微博
        PlatformConfig.setSinaWeibo(BaseConstant.SINA_APPKEY, BaseConstant.SINA_APPSECRET);
        //初始化SDK
        UMShareAPI.get(this);
    }
}