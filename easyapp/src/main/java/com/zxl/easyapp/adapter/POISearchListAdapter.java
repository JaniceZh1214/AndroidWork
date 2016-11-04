package com.zxl.easyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.zxl.easyapp.R;

import java.util.List;

/**
 * Created by 张晓莉 on 2016/9/21.
 * poi搜索数据适配器
 */
public class POISearchListAdapter extends BaseAdapter {
    private Context context;
    private List<PoiItem> poiItemList;

    public POISearchListAdapter(Context context, List<PoiItem> poiItemList) {
        this.context = context;
        this.poiItemList = poiItemList;
    }

    @Override
    public int getCount() {
        return poiItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return poiItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_poi_search_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.id_poi_title = (TextView) convertView.findViewById(R.id.id_poi_title);
            viewHolder.id_poi_address = (TextView) convertView.findViewById(R.id.id_poi_address);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PoiItem poiItem = poiItemList.get(position);
        viewHolder.id_poi_title.setText(poiItem.getTitle());
        viewHolder.id_poi_address.setText(poiItem.getProvinceName() + "\t\t" + poiItem.getCityName() + "\t\t" + poiItem.getAdName() + "\t\t" + poiItem.getSnippet());
        return convertView;
    }

    class ViewHolder {
        TextView id_poi_title;
        TextView id_poi_address;
    }
}