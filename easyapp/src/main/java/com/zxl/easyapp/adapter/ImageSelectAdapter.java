package com.zxl.easyapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.zxl.easyapp.R;
import com.zxl.easyapp.common.BaseConstant;
import com.zxl.easyapp.model.ViewHolder;

import org.xutils.common.util.LogUtil;

import java.util.LinkedList;
import java.util.List;

public class ImageSelectAdapter extends CommonAdapter<String> {
    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public static List<String> mSelectedImage = new LinkedList<String>();

    /**
     * 文件夹路径
     */
    private String mDirPath;

    public ImageSelectAdapter(Context context, List<String> mDatas, int itemLayoutId, String dirPath) {
        super(context, mDatas, itemLayoutId);
        this.mDirPath = dirPath;
    }

    @Override
    public void convert(final ViewHolder helper, final String item) {
        //设置no_pic
        helper.setImageResource(R.id.id_item_image, R.mipmap.pictures_no);
        //设置no_selected
        helper.setImageResource(R.id.id_item_select, R.mipmap.picture_unselected);
        //设置图片
        helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);
        final ImageView mImageView = helper.getView(R.id.id_item_image);
        final ImageView mSelect = helper.getView(R.id.id_item_select);
        mImageView.setColorFilter(null);
        //设置ImageView的点击事件
        mImageView.setOnClickListener(new OnClickListener() {
            //选择，则将图片变暗，反之则反之
            @Override
            public void onClick(View v) {
                if (mSelectedImage.contains(mDirPath + "/" + item)) {
                    // 已经选择过该图片
                    mSelectedImage.remove(mDirPath + "/" + item);
                    mSelect.setImageResource(R.mipmap.picture_unselected);
                    mImageView.setColorFilter(null);
                } else {
                    // 未选择该图片
                    if (mSelectedImage.size() > BaseConstant.MAX_IMAGE_NUM - 1) {
                        Toast.makeText(mContext, "您最多可选择" + BaseConstant.MAX_IMAGE_NUM + "张图片", Toast.LENGTH_SHORT).show();
                    } else {
                        mSelectedImage.add(mDirPath + "/" + item);
                        mSelect.setImageResource(R.mipmap.pictures_selected);
                        mImageView.setColorFilter(Color.parseColor("#77000000"));
                    }
                }
            }
        });
        /**
         * 已经选择过的图片，显示出选择过的效果
         */
        if (mSelectedImage.contains(mDirPath + "/" + item)) {
            mSelect.setImageResource(R.mipmap.pictures_selected);
            mImageView.setColorFilter(Color.parseColor("#77000000"));
        }
    }
}