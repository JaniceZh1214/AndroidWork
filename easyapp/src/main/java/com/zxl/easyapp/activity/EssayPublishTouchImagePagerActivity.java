package com.zxl.easyapp.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxl.easyapp.R;
import com.zxl.easyapp.adapter.EssayPublishTouchImageAdapter;
import com.zxl.easyapp.adapter.ImageSelectAdapter;
import com.zxl.easyapp.common.BaseActivity;
import com.zxl.easyapp.ui.ExtendedViewPager;
import com.zxl.easyapp.utils.Bimp;
import com.zxl.easyapp.utils.DialogUtil;

/**
 * Created by 张晓莉 on 2016/8/19.
 * 可缩放图片组查看界面(Bitmap)
 */
public class EssayPublishTouchImagePagerActivity extends BaseActivity {
    private ImageButton id_iv_back;
    private TextView id_tv_title;
    private Button id_btn_right;
    private ExtendedViewPager id_image_viewpager;
    private EssayPublishTouchImageAdapter essayPublishTouchImageAdapter;
    private int currentPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_touchimageview);
        initView();
    }

    private void initView() {
        currentPosition = 0;
        id_iv_back = (ImageButton) findViewById(R.id.id_iv_back);
        id_iv_back.setOnClickListener(this);
        id_tv_title = (TextView) findViewById(R.id.id_tv_title);
        id_tv_title.setText((currentPosition + 1) + "/" + Bimp.bmp.size());
        id_btn_right = (Button) findViewById(R.id.id_btn_right);
        id_btn_right.setText("删除");
        id_btn_right.setOnClickListener(this);
        id_image_viewpager = (ExtendedViewPager) findViewById(R.id.id_image_viewpager);
        essayPublishTouchImageAdapter = new EssayPublishTouchImageAdapter(context, Bimp.bmp);
        id_image_viewpager.setAdapter(essayPublishTouchImageAdapter);
        id_image_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                id_tv_title.setText((position + 1) + "/" + Bimp.bmp.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.id_iv_back) {
            setResult(RESULT_OK);
            finish();
        } else if (i == R.id.id_btn_right) {
            baseDialog = DialogUtil.showTipDialog(context, "要删除这张照片吗?", "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    baseDialog.dismiss();
                    ImageSelectAdapter.mSelectedImage.remove(currentPosition);
                    Bimp.drr.remove(currentPosition);
                    Bimp.bmp.remove(currentPosition);
                    Bimp.max--;
                    if (Bimp.bmp.size() > 0) {
                        initView();
                    } else {
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            });
        }
    }
}