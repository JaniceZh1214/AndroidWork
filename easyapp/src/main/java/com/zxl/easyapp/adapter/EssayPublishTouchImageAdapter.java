package com.zxl.easyapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zxl.easyapp.ui.TouchImageView;

import java.util.List;

/**
 * Created by 张晓莉 on 2016/8/19.
 * 可缩放图片数据适配器
 */
public class EssayPublishTouchImageAdapter extends PagerAdapter {
    private Context context;
    private List<Bitmap> images;

    public EssayPublishTouchImageAdapter(Context context, List<Bitmap> images) {
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
        touchImageView.setImageBitmap(images.get(position));
        container.addView(touchImageView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
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