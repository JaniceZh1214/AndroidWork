package com.zxl.easyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zxl.easyapp.R;

import java.util.List;

/**
 * Created by 张晓莉 on 2016/8/4.
 * 字符串展示数据适配器
 */
public class StringAdapter extends BaseAdapter {
    private Context context;
    private List<String> contentList;

    public StringAdapter(Context context, List<String> contentList) {
        this.context = context;
        this.contentList = contentList;
    }

    @Override
    public int getCount() {
        return contentList.size();
    }

    @Override
    public Object getItem(int position) {
        return contentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.id_tv_content = (TextView) convertView.findViewById(R.id.id_tv_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.id_tv_content.setText(contentList.get(position));
        return convertView;
    }

    class ViewHolder {
        private TextView id_tv_content;
    }
}