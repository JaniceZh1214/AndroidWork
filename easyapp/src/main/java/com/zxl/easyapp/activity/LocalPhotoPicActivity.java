package com.zxl.easyapp.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;

import com.zxl.easyapp.R;
import com.zxl.easyapp.adapter.ImageSelectAdapter;
import com.zxl.easyapp.common.BaseActivity;
import com.zxl.easyapp.model.ImageFloder;

import org.xutils.common.util.LogUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by 张晓莉 on 2016/4/26.
 * 本地图片选择器页面
 */
public class LocalPhotoPicActivity extends BaseActivity implements ListImageDirPopupWindow.OnImageDirSelected {
    private ImageView id_iv_back;
    private TextView id_tv_title;
    private TextView id_btn_right;
    private ProgressDialog mProgressDialog;
    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;
    /**
     * 所有的图片
     */
    private List<String> mImgs;
    private GridView mGirdView;
    private ImageSelectAdapter imageSelectAdapter;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();
    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();
    private RelativeLayout mBottomLy;
    private TextView mChooseDir;
    private TextView mImageCount;
    int totalCount = 0;
    private int mScreenHeight;
    private ListImageDirPopupWindow mListImageDirPopupWindow;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mProgressDialog.dismiss();
            // 为View绑定数据
            data2View();
            // 初始化展示文件夹的popupWindw
            initListDirPopupWindw();
        }
    };
    private boolean isCanUse = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_photo_pic);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
        isCanUse = true;
        initView();
        getImages();
        initEvent();
    }

    /**
     * 初始化View
     */
    private void initView() {
        id_iv_back = (ImageView) findViewById(R.id.id_iv_back);
        id_iv_back.setOnClickListener(this);
        id_tv_title = (TextView) findViewById(R.id.id_tv_title);
        id_tv_title.setText("图片");
        id_btn_right = (TextView) findViewById(R.id.id_btn_right);
        id_btn_right.setText("完成");
        id_btn_right.setOnClickListener(this);
        mGirdView = (GridView) findViewById(R.id.id_gridView);
        mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
        mImageCount = (TextView) findViewById(R.id.id_total_count);
        mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            isCanUse = false;
            showToastMessage("暂无外部存储");
            return;
        }
        // 显示进度条
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String firstImage = null;
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = LocalPhotoPicActivity.this.getContentResolver();
                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    LogUtil.d(path);
                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }
                    if (parentFile.list() == null) {
                        continue;
                    }
                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    }).length;
                    totalCount += picSize;
                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);
                    if (picSize > mPicsSize) {
                        mPicsSize = picSize;
                        mImgDir = parentFile;
                    }
                }
                mCursor.close();
                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;
                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(0x110);
            }
        }).start();
    }

    private void initEvent() {
        /**
         * 为底部的布局设置点击事件，弹出popupWindow
         */
        mBottomLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCanUse) {
                    mListImageDirPopupWindow.setAnimationStyle(R.style.anim_popup_dir);
                    mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);
                    // 设置背景颜色变暗
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = .3f;
                    getWindow().setAttributes(lp);
                }
            }
        });
    }

    /**
     * 为View绑定数据
     */
    private void data2View() {
        if (mImgDir == null) {
            isCanUse = false;
            showToastMessage("暂无照片!");
            return;
        }
        mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        }));
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        imageSelectAdapter = new ImageSelectAdapter(getApplicationContext(), mImgs, R.layout.layout_grid_item, mImgDir.getAbsolutePath());
        mGirdView.setAdapter(imageSelectAdapter);
        mImageCount.setText(totalCount + "张");
    }

    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindw() {
        mListImageDirPopupWindow = new ListImageDirPopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
                mImageFloders, LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_list_dir, null));
        mListImageDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }

    @Override
    public void selected(ImageFloder floder) {
        mImgDir = new File(floder.getDir());
        mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        }));
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        imageSelectAdapter = new ImageSelectAdapter(getApplicationContext(), mImgs, R.layout.layout_grid_item, mImgDir.getAbsolutePath());
        mGirdView.setAdapter(imageSelectAdapter);
        mImageCount.setText(floder.getCount() + "张");
        mChooseDir.setText(floder.getName());
        mListImageDirPopupWindow.dismiss();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.id_iv_back) {
            finish();
        } else if (i == R.id.id_btn_right) {
            if (isCanUse) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}