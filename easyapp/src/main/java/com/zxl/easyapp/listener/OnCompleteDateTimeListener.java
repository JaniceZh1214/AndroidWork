package com.zxl.easyapp.listener;

import java.util.Calendar;

/**
 * Created by 张晓莉 on 2016/10/24.
 * 时间选择完成监听接口
 */
public interface OnCompleteDateTimeListener {

    void onComplete(Calendar calendar, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute);
}