package com.zxl.easyapp.thread;

import android.os.Environment;
import android.os.Handler;

import com.zxl.easyapp.common.BaseConstant;
import com.zxl.easyapp.model.Version;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by 张晓莉 on 2016/9/12.
 * 版本更新线程
 */
public class UpdateThread extends Thread {
    //终止标记
    public static boolean interceptFlag;
    //进度值
    public static int progress;
    //下载文件大小
    public static String apkFileSize;
    //已下载文件大小
    public static String tmpFileSize;
    //apk保存完整路径
    public static String apkFilePath = "";
    private Version version;
    private Handler updateHandler;
    //下载包保存路径
    private String savePath = "";
    //临时下载文件路径
    private String tmpFilePath = "";

    public UpdateThread(Version version, Handler updateHandler) {
        this.version = version;
        this.updateHandler = updateHandler;
    }

    @Override
    public void run() {
        super.run();
        try {
            String apkName = "ClassMatchApp_" + version.getVersionName() + ".apk";
            String tmpApk = "ClassMatchApp_" + version.getVersionName() + ".tmp";
            //判断是否挂载了SD卡
            String storageState = Environment.getExternalStorageState();
            if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ClassMatch/Update/";
                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                apkFilePath = savePath + apkName;
                tmpFilePath = savePath + tmpApk;
            }
            //没有挂载SD卡，无法下载文件
            if (apkFilePath == null || apkFilePath.equals("")) {
                updateHandler.sendEmptyMessage(BaseConstant.DOWN_NOSDCARD);
                return;
            }
            File ApkFile = new File(apkFilePath);
            //是否已下载更新文件
            if (ApkFile.exists()) {
                ApkFile.delete();
            }
            //输出临时下载文件
            File tmpFile = new File(tmpFilePath);
            FileOutputStream fos = new FileOutputStream(tmpFile);
            URL url = new URL(version.getDownloadUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            int length = conn.getContentLength();
            InputStream is = conn.getInputStream();
            //显示文件大小格式：2个小数点显示
            DecimalFormat df = new DecimalFormat("0.00");
            //进度条下面显示的总文件大小
            apkFileSize = df.format((float) length / 1024 / 1024) + "MB";
            int count = 0;
            byte buf[] = new byte[1024];
            do {
                int numread = is.read(buf);
                count += numread;
                //进度条下面显示的当前下载文件大小
                tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
                //当前进度值
                progress = (int) (((float) count / length) * 100);
                //更新进度
                updateHandler.sendEmptyMessage(BaseConstant.DOWN_UPDATE);
                if (numread <= 0) {
                    //下载完成 - 将临时下载文件转成APK文件
                    if (tmpFile.renameTo(ApkFile)) {
                        //通知安装
                        updateHandler.sendEmptyMessage(BaseConstant.DOWN_OVER);
                    }
                    break;
                }
                fos.write(buf, 0, numread);
            } while (!interceptFlag);//点击取消就停止下载
            fos.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}