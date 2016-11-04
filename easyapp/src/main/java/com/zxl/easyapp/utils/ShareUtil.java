package com.zxl.easyapp.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.zxl.easyapp.R;

/**
 * Created by 张晓莉 on 2016/9/8.
 * 第三方分享工具类
 */
public class ShareUtil {
    /**
     * 显示分享对话框
     *
     * @param context         上下文环境
     * @param title           分享的标题
     * @param content         分享的内容
     * @param targetUrl       分享的目标链接
     * @param umShareListener 分享操作监听
     */
    public static void showShareDialog(Context context, Bitmap bitmapShare, String title, String content, String targetUrl, UMShareListener umShareListener) {
        UMImage umImage = new UMImage(context, R.mipmap.watermark);
        // 设置分享图片
        if (bitmapShare != null) {
            umImage = new UMImage(context, bitmapShare);
        }
        new ShareAction((Activity) context).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA)
                .withTitle(title)
                .withText(content)
                .withMedia(umImage)
                .withTargetUrl(targetUrl)
                .setCallback(umShareListener)
                .open();
    }
}
