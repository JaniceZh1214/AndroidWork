package com.zxl.easyapp.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxl.easyapp.R;
import com.zxl.easyapp.adapter.TouchImageAdapter;
import com.zxl.easyapp.common.BaseActivity;
import com.zxl.easyapp.ui.ExtendedViewPager;

import java.util.List;

/**
 * Created by 张晓莉 on 2016/8/19.
 * 可缩放图片组查看界面
 */
public class ViewpagerTouchImageViewActivity extends BaseActivity {
    private LinearLayout id_layout_top;
    private TextView id_tv_bottom;
    private ExtendedViewPager id_image_viewpager;
    private TouchImageAdapter touchImageAdapter;
    private int currentPosition;
    private List<String> images;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_touchimageview);
        systemBarTintManager.setStatusBarTintEnabled(false);
        initView();
    }

    private void initView() {
        currentPosition = 0;
        images = getIntent().getStringArrayListExtra("images");
        id_layout_top = (LinearLayout) findViewById(R.id.id_layout_top);
        id_layout_top.setVisibility(View.GONE);
        id_tv_bottom = (TextView) findViewById(R.id.id_tv_bottom);
        id_tv_bottom.setVisibility(View.VISIBLE);
        id_tv_bottom.setText((currentPosition + 1) + "/" + images.size());
        id_image_viewpager = (ExtendedViewPager) findViewById(R.id.id_image_viewpager);
        touchImageAdapter = new TouchImageAdapter(context, images);
        id_image_viewpager.setAdapter(touchImageAdapter);
        id_image_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                id_tv_bottom.setText((position + 1) + "/" + images.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}