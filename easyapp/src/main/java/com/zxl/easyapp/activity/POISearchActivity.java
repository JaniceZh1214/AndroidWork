package com.zxl.easyapp.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.zxl.easyapp.R;
import com.zxl.easyapp.adapter.POISearchListAdapter;
import com.zxl.easyapp.common.BaseActivity;
import com.zxl.easyapp.ui.OverListView;
import com.zxl.easyapp.utils.DialogUtil;
import com.zxl.easyapp.utils.ScrollUtil;
import com.zxl.easyapp.utils.VerificationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张晓莉 on 2016/9/18.
 * 获取POI数据界面
 */
public class POISearchActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener {
    private ImageButton id_iv_back;
    private LinearLayout id_match_search;
    private EditText id_et_match_search;
    private ImageButton btn_clear;
    private Button id_btn_right;
    private SwipeRefreshLayout id_poi_search_swipe;
    private OverListView id_poi_search_list;
    private TextView id_tv_tip;
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private String keyWord = "";// poi搜索关键字
    private String poiType = "";// poi搜索类型
    private String poiArea = "";// poi搜索区域（空字符串代表全国）
    private PoiResult result; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private int pageSize = 20;// 当前页面，从0开始计数
    private boolean isLastRow = false;
    private boolean isCanLoadData = true;
    private List<PoiItem> poiItemList = new ArrayList<>();
    private POISearchListAdapter poiSearchListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search);
        initView();
    }

    private void initView() {
        id_iv_back = (ImageButton) findViewById(R.id.id_iv_back);
        id_match_search = (LinearLayout) findViewById(R.id.id_match_search);
        id_et_match_search = (EditText) findViewById(R.id.id_et_match_search);
        id_et_match_search.setHint("请输入地址");
        btn_clear = (ImageButton) findViewById(R.id.btn_clear);
        id_btn_right = (Button) findViewById(R.id.id_btn_right);
        id_iv_back.setVisibility(View.GONE);
        id_match_search.setVisibility(View.VISIBLE);
        id_et_match_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    //修改回车键功能
                    if (id_et_match_search.getText().toString().equals("")) {
                        return false;
                    }
                    // 先隐藏键盘
                    hideSoftKeyboard();
                    //开始执行搜索操作
                    if (isCanLoadData) {
                        searchButton(false);
                    }
                }
                return false;
            }
        });
        id_et_match_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!id_et_match_search.getText().toString().equals("") && id_et_match_search.getText().toString().length() > 0) {
                    btn_clear.setVisibility(View.VISIBLE);
                } else {
                    btn_clear.setVisibility(View.GONE);
                }
            }
        });
        btn_clear.setOnClickListener(this);
        id_btn_right.setText("取消");
        id_btn_right.setOnClickListener(this);
        id_poi_search_swipe = (SwipeRefreshLayout) findViewById(R.id.id_poi_search_swipe);
        id_poi_search_swipe.setColorSchemeColors(getResources().getColor(R.color.blue_12b7f5),
                getResources().getColor(R.color.orange_f8a900), getResources().getColor(R.color.green_2ad17b));
        id_poi_search_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新
                isCanLoadData = true;
                id_poi_search_swipe.setRefreshing(false);
                searchButton(false);
            }
        });
        id_poi_search_list = (OverListView) findViewById(R.id.id_poi_search_list);
        id_poi_search_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE://停止滚动
                        if (isLastRow && isCanLoadData) {
                            //加载更多
                            searchButton(true);
                            isLastRow = true;
                        }
                        break;
                }
            }

            /**
             *
             * @param view 滚动时一直回调，直到停止滚动时才停止回调。单击时回调一次
             * @param firstVisibleItem 表示在当前屏幕显示的第一个listItem在整个listView里面的位置（下标从0开始）
             * @param visibleItemCount 表示在现时屏幕可以见到的ListItem(部分显示的ListItem也算)总数
             * @param totalItemCount 表示ListView的ListItem总数
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                ScrollUtil.enableSwipeRefresh(id_poi_search_list, id_poi_search_swipe);
                if (id_poi_search_list.getLastVisiblePosition() + 1 == totalItemCount && firstVisibleItem != 0) {
                    isLastRow = true;
                }
            }
        });
        id_poi_search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final PoiItem poiItem = poiItemList.get(position);
                baseDialog = DialogUtil.showTipDialog(context, poiItem.getProvinceName() + "\t\t" + poiItem.getCityName() + "\t\t" + poiItem.getAdName() + "\t\t" + poiItem.getSnippet(),
                        "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                baseDialog.dismiss();
                                Intent intent = new Intent();
                                intent.putExtra("address", poiItem.getProvinceName() + "\t\t" + poiItem.getCityName() + "\t\t" + poiItem.getAdName() + "\t\t" + poiItem.getSnippet());
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }, "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                baseDialog.dismiss();
                            }
                        });
            }
        });
        id_tv_tip = (TextView) findViewById(R.id.id_tv_tip);
        showSoftKeyboard(id_et_match_search, 1000);
    }

    /**
     * 点击搜索按钮
     */
    public void searchButton(boolean isLoadMore) {
        keyWord = VerificationUtil.checkEditText(id_et_match_search);
        if ("".equals(keyWord)) {
            showToastMessage("请输入搜索关键字");
            return;
        } else {
            if (!isLoadMore) {
                currentPage = 0;
                poiItemList.clear();
                doSearchQuery();
            } else {
                if (query != null && poiSearch != null && result != null) {
                    if (result.getPageCount() - 1 > currentPage) {
                        currentPage++;
                        query.setPageNum(currentPage);// 设置查后一页
                        poiSearch.searchPOIAsyn();
                    }
                }
            }
        }
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        isCanLoadData = false;
        // 显示进度框
        DialogUtil.showProgressDialog((Activity) context);
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query(keyWord, poiType, poiArea);
        // 设置每页最多返回多少条poiitem
        query.setPageSize(pageSize);
        // 设置查第一页
        query.setPageNum(currentPage);
        query.setCityLimit(true);
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.id_btn_right) {
            //取消
            hideSoftKeyboard();
            finish();
        } else if (i == R.id.btn_clear) {
            //清除
            id_et_match_search.setText("");
        }
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        // 隐藏对话框
        DialogUtil.dismissDialog();
        isCanLoadData = true;
        id_tv_tip.setVisibility(View.GONE);
        if (i == 1000) {
            //1000为成功，其他为失败
            if (poiResult != null && poiResult.getQuery() != null) {
                // 搜索poi的结果
                if (poiResult.getQuery().equals(query)) {// 是否是同一条
                    result = poiResult;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = result.getPois();
                    // 取得第一页的poiitem数据，页数从数字0开始
                    if (poiItems != null && poiItems.size() > 0) {
                        poiItemList.addAll(poiItems);
                        if (currentPage == 0) {
                            poiSearchListAdapter = new POISearchListAdapter(context, poiItemList);
                            id_poi_search_list.setAdapter(poiSearchListAdapter);
                        } else {
                            poiSearchListAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }
}