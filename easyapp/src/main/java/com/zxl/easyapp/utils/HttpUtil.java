package com.zxl.easyapp.utils;

import android.content.Context;
import android.content.DialogInterface;

import com.zxl.easyapp.common.BaseDialog;
import com.zxl.easyapp.listener.OnFailureListener;
import com.zxl.easyapp.listener.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by 张晓莉 on 2016/8/12.
 * 网络请求工具类
 */
public class HttpUtil {
    private Context context;
    private HttpMethod httpMethod;
    private String url;
    private BaseDialog baseDialog;
    private OnSuccessListener onSuccessListener;
    private OnFailureListener onFailureListener;

    public HttpUtil(Context context, HttpMethod httpMethod, String url) {
        this.context = context;
        this.httpMethod = httpMethod;
        this.url = url;
    }

    /**
     * 不带缓存的网络请求方式
     *
     * @param params
     */
    public void noCacheHttpUrl(HashMap<String, String> params) {
        LogUtil.d(url + "?" + params.toString());
        x.http().request(httpMethod, createRequestParams(url, params), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
                LogUtil.d(result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    // StatusCode 1成功，0失败
                    if (jsonObject.optInt("status_code") == 1) {
                        //提示常规信息
                        String alert_title = jsonObject.optString("alert_title");
                        String alert_msg = jsonObject.optString("alert_msg");
                        if (!alert_title.equals("") && !alert_msg.equals("")) {
                            baseDialog = DialogUtil.showTipDialog(context, alert_title, alert_msg, "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    baseDialog.dismiss();
                                    if (onSuccessListener != null) {
                                        onSuccessListener.onSuccess(result);
                                    }
                                }
                            });
                            return;
                        }
                        if (onSuccessListener != null) {
                            onSuccessListener.onSuccess(result);
                        }
                    } else {
                        //提示错误信息
                        String return_msg = jsonObject.optString("return_msg");
                        baseDialog = DialogUtil.showTipDialog(context, return_msg, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                baseDialog.dismiss();
                                if (onFailureListener != null) {
                                    onFailureListener.onFailure(result);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    LogUtil.d("JSONException:" + e.getMessage());
                    if (onFailureListener != null) {
                        onFailureListener.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.d("onError:" + ex.getMessage());
                if (onFailureListener != null) {
                    onFailureListener.onFailure(ex.getMessage());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.d("cancelled");
                if (onFailureListener != null) {
                    onFailureListener.onFailure(cex.getMessage());
                }
            }

            //不管成功或者失败最后都会回调该接口
            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 上传单张图片
     *
     * @param imageFile 上传的图片
     */
    public void uploadImage(File imageFile) {
        RequestParams requestParams = new RequestParams(url);
        requestParams.setMultipart(true);
        requestParams.addBodyParameter("art", "5");
        requestParams.addBodyParameter("fi", imageFile);
        LogUtil.d(requestParams.toString() + "&fi=" + imageFile.toString());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
                LogUtil.d(result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    // StatusCode 1成功，0失败
                    if (jsonObject.optInt("status_code") == 1) {
                        //提示常规信息
                        String alert_title = jsonObject.optString("alert_title");
                        String alert_msg = jsonObject.optString("alert_msg");
                        if (!alert_title.equals("") && !alert_msg.equals("")) {
                            baseDialog = DialogUtil.showTipDialog(context, alert_title, alert_msg, "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    baseDialog.dismiss();
                                    if (onSuccessListener != null) {
                                        onSuccessListener.onSuccess(result);
                                    }
                                }
                            });
                            return;
                        }
                        if (onSuccessListener != null) {
                            onSuccessListener.onSuccess(result);
                        }
                    } else {
                        //提示错误信息
                        String return_msg = jsonObject.optString("return_msg");
                        baseDialog = DialogUtil.showTipDialog(context, return_msg, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                baseDialog.dismiss();
                                if (onFailureListener != null) {
                                    onFailureListener.onFailure(result);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    LogUtil.d("JSONException:" + e.getMessage());
                    if (onFailureListener != null) {
                        onFailureListener.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.d("onError:" + ex.getMessage());
                if (onFailureListener != null) {
                    onFailureListener.onFailure(ex.getMessage());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.d("cancelled");
                if (onFailureListener != null) {
                    onFailureListener.onFailure(cex.getMessage());
                }
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 创建RequestParams对象
     *
     * @param url
     * @param params
     * @return
     */
    public RequestParams createRequestParams(String url, HashMap<String, String> params) {
        RequestParams requestParams = new RequestParams(url);
        if (isEmpty(params)) {
            return requestParams;
        }
        Set<String> keys = params.keySet();
        for (String key : keys) {
            requestParams.addQueryStringParameter(key, params.get(key));
        }
        return requestParams;
    }

    /**
     * 检查HashMap是否为空
     *
     * @param sourceMap
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> boolean isEmpty(Map<K, V> sourceMap) {
        return (sourceMap == null || sourceMap.size() == 0);
    }

    public OnSuccessListener getOnSuccessListener() {
        return onSuccessListener;
    }

    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    public OnFailureListener getOnFailureListener() {
        return onFailureListener;
    }

    public void setOnFailureListener(OnFailureListener onFailureListener) {
        this.onFailureListener = onFailureListener;
    }
}