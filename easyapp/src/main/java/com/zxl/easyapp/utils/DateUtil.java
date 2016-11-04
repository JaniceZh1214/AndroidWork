package com.zxl.easyapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.zxl.easyapp.R;
import com.zxl.easyapp.common.BaseDialog;
import com.zxl.easyapp.listener.OnCompleteDateTimeListener;
import com.zxl.easyapp.listener.OnPrepareListener;

import org.xutils.common.util.LogUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 张晓莉 on 2016/8/12.
 * 时间操作工具类
 */
public class DateUtil {
    private static BaseDialog baseDialog;
    private static int choiceYear;
    private static int choiceMonth;
    private static int choiceDay;
    private static int choiceHour;
    private static int choiceMinute;
    public static OnCompleteDateTimeListener onCompleteDateTimeListener;

    /**
     * 日期时间选择对话框
     *
     * @param context      上下文环境
     * @param initYear     初始化年
     * @param initMonth    初始化月
     * @param initDay      初始化日
     * @param initHour     初始化小时
     * @param initMinute   初始化分钟
     * @param isDateTime   是否是日期+时间模式
     * @param is24HourView 是否是24小时制
     */
    public static void showDateTimerPickerDialog(final Context context, int initYear, int initMonth, int initDay,
                                                 int initHour, int initMinute, final boolean isDateTime, final boolean is24HourView) {
        final Calendar calendar = Calendar.getInstance();
        if (initYear == 0) {
            initYear = calendar.get(Calendar.YEAR);
        }
        if (initMonth == -1) {
            initMonth = calendar.get(Calendar.MONTH);
        }
        if (initDay == 0) {
            initDay = calendar.get(Calendar.DAY_OF_MONTH);
        }
        if (initHour == -1) {
            initHour = calendar.get(Calendar.HOUR_OF_DAY);
        }
        if (initMinute == -1) {
            initMinute = calendar.get(Calendar.MINUTE);
        }
        choiceYear = initYear;
        choiceMonth = initMonth;
        choiceDay = initDay;
        choiceHour = initHour;
        choiceMinute = initMinute;
        final int finalInitYear = initYear;
        final int finalInitMonth = initMonth;
        final int finalInitDay = initDay;
        final int finalInitHour = initHour;
        final int finalInitMinute = initMinute;
        baseDialog = DialogUtil.showContentDialog(context, R.layout.layout_date_time_picker, new OnPrepareListener() {
            @Override
            public void onPrepare(View view) {
                DatePicker datePicker = (DatePicker) view.findViewById(R.id.id_data_picker);
                datePicker.init(finalInitYear, finalInitMonth, finalInitDay, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        choiceYear = year;
                        choiceMonth = monthOfYear;
                        choiceDay = dayOfMonth;
                    }
                });
                TimePicker timePicker = (TimePicker) view.findViewById(R.id.id_time_picker);
                if (!isDateTime) {
                    timePicker.setVisibility(View.GONE);
                }
                timePicker.setIs24HourView(is24HourView);
                timePicker.setCurrentHour(finalInitHour);
                timePicker.setCurrentMinute(finalInitMinute);
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        choiceHour = hourOfDay;
                        choiceMinute = minute;
                    }
                });
            }
        }, "完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                baseDialog.dismiss();
                if (onCompleteDateTimeListener != null) {
                    onCompleteDateTimeListener.onComplete(calendar, choiceYear, choiceMonth, choiceDay, choiceHour, choiceMinute);
                }
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                baseDialog.dismiss();
            }
        });
    }

    /**
     * 比较当前时间是否大于所传时间3小时以上
     *
     * @param time
     * @return
     */
    public static boolean compareToCurrentTime3H(String time) {
        boolean isTrue = false;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date1 = new Date();
            Date date2 = dateFormat.parse(time);
            long timeDif = date1.getTime() - date2.getTime();
            long dayDif = timeDif / (24 * 60 * 60 * 1000);
            if (dayDif > 0) {
                isTrue = true;
            }
            long hourDif = (timeDif / (60 * 60 * 1000) - dayDif * 24);
            if (hourDif > 3) {
                isTrue = true;
            }
            long minDif = ((timeDif / (60 * 1000)) - dayDif * 24 * 60 - hourDif * 60);
            long secondDif = (timeDif / 1000 - dayDif * 24 * 60 * 60 - hourDif * 60 * 60 - minDif * 60);
            LogUtil.d("dayDif=" + dayDif + "  hourDif=" + hourDif + "  minDif=" + minDif + "  secondDif=" + secondDif);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isTrue;
    }
}