package com.zxl.easyapp.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.x;

import java.util.HashMap;

/**
 * Created by 张晓莉 on 2016/8/12.
 * 应用中所有Fragment必须继承此基类
 */
public class BaseFragment extends Fragment implements View.OnClickListener {
    private boolean injected = false;
    protected Context context;
    protected BaseDialog baseDialog;
    protected ImageLoader imageLoader;
    protected HashMap<String, String> params = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        injected = true;
        context = getContext();
        imageLoader = ImageLoaderFactory.getImageLoader();
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

    @Override
    public void onClick(View v) {
    }
}