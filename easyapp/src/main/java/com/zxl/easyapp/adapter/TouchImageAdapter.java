package com.zxl.easyapp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zxl.easyapp.common.AppManager;
import com.zxl.easyapp.common.ImageLoaderFactory;
import com.zxl.easyapp.ui.TouchImageView;

import java.util.List;

/**
 * Created by 张晓莉 on 2016/8/19.
 * 可缩放图片数据适配器
 */
public class TouchImageAdapter extends PagerAdapter {
    private Context context;
    private List<String> images;

    public TouchImageAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        TouchImageView touchImageView = new TouchImageView(container.getContext());
        ImageLoaderFactory.getImageLoader().displayImage(images.get(position), touchImageView);
        container.addView(touchImageView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        touchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getAppManager().finishActivity();
            }
        });
        return touchImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}