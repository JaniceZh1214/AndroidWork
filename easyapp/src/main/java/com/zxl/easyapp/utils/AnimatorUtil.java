package com.zxl.easyapp.utils;

import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;

/**
 * Created by 张晓莉 on 2016/8/12.
 * 动画操作工具类
 */
public class AnimatorUtil {
    /**
     * 获取倒计时属性动画
     *
     * @param start    开始数值
     * @param end      结束数值
     * @param duration 动画持续总时间
     * @return 属性动画对象
     */
    public static ValueAnimator getCountDownAnimator(int start, int end, int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        //设置动画持续总时间
        valueAnimator.setDuration(duration);
        //设置属性动画匀速
        valueAnimator.setInterpolator(new LinearInterpolator());
        return valueAnimator;
    }
}