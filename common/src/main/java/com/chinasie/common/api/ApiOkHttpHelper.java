package com.chinasie.common.api;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.chinasie.common.config.OkHttpHelperConfig;
import com.chinasie.common.models.BaseCallback;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 古永财 on 2016/10/13.
 */
public class ApiOkHttpHelper {
    public static final String TAG = "http.OkHttpHelper";
    private static ApiOkHttpHelper mInstance;
    private OkHttpClient mHttpClient;
    private Gson mGson;
    private Handler mHandler;
    private OkHttpClentCreator okHttpClentCreator;


    static {
        mInstance = new ApiOkHttpHelper();
    }

    private ApiOkHttpHelper() {
//        mHttpClient = new OkHttpClient
//                .Builder()
//                .connectTimeout(OkHttpHelperConfig.DEFAULT_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
//                .writeTimeout(OkHttpHelperConfig.DEFAULT_WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
//                .readTimeout(OkHttpHelperConfig.DEFAULT_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
//                .build();

        mGson = new Gson();
        mHandler = new Handler(Looper.getMainLooper());
        okHttpClentCreator = new OkHttpClentCreator();

    }

    public static ApiOkHttpHelper getInstance() {
        return mInstance;
    }


    public void get(String url, BaseCallback callback) {
        Request request = buildGetRequest(url);
        request(request, callback);
    }

    public void post(String url, Map<String, String> param, BaseCallback callback) {
        Request request = buildPostRequest(url, param);
        request(request, callback);
    }

    public void post(String url, String jsonString, BaseCallback callback1) {
        Request request = buildPostRequest(url, jsonString);
        request(request, callback1);
    }


    public String post(String url, String jsonString) {
        Request request = buildPostRequest(url, jsonString);
        Response response = null;
        try {
            response = mHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //  var response = JsonConvert.DeserializeObject<ApiResponse<T>>(result);
        if (response.isSuccessful()) {
            String resultStr = null;
            try {
                resultStr = response.body().string();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            return resultStr;
        }
        return "";
    }


    public void request(final Request request, final BaseCallback callback) {
        callback.onBeforeRequest(request);
        //---根据配置,获取请求对象---
        mHttpClient = okHttpClentCreator.getOkHttpClient();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                try {
                    Log.i(TAG, "onFailure method");
                    callbackFailure(callback, request, e);
                } catch (Exception ex) {
                    Log.i(TAG, "onFailure method Exception" + ex.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = "";
                try {
                    callbackResponse(callback, response);
                    //测试用（显示出http的body信息）
                    responseBody = response.body().string();
                    Log.i(TAG, "onResponse method response.body:" + responseBody);
                    Log.i(TAG, "onResponse method");
                    if (response.isSuccessful()) {
                        //                        String resultStr = response.body().string();
                        Log.d(TAG, "onResponse method response.body:" + responseBody);
                        if (callback.mType == String.class) {
                            Log.i(TAG, "onResponse method mType string start");
                            callbackSuccess(callback, response, responseBody);
                            Log.i(TAG, "onResponse method mType string end");
                        } else {
                            try {
                                Log.i(TAG, "onResponse method mType class start");
                                Object obj = mGson.fromJson(responseBody, callback.mType);
                                callbackSuccess(callback, response, obj);
                                Log.i(TAG, "onResponse method mType class end");
                            } catch (com.google.gson.JsonParseException e) { // Json解析的错误
                                Log.e(TAG, "Json解析的错误 " + e.getMessage());
                                Log.i(TAG, "onResponse method mType class exception start");
                                callbackError(callback, response, e, responseBody);
                                Log.i(TAG, "onResponse method mType class exception start");
                            } catch (Exception e) {
                                Log.e(TAG, "服务器返回的错误提示:" + responseBody);
                                Log.e(TAG, "异常信息:" + e.toString());
                            }
                        }
                    } else {
                        callbackError(callback, response, null, responseBody);
                    }
                } catch (Exception ex) {
                    Log.i(TAG, ex.getMessage());
                    Log.e(TAG, "服务器返回的错误提示:" + responseBody);
                    Log.e(TAG, "异常信息:" + ex.toString());
                    //throw new IOException(ex);
                }
            }
        });
    }

    private void callbackSuccess(final BaseCallback callback, final Response response, final Object obj) {
        Log.i(TAG, "callbackSuccess");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, obj);
            }
        });
    }

    private void callbackError(final BaseCallback callback, final Response response, final Exception e, final String responseBody) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG, "callbackError run method start");
                    callback.onError(response, response.code(), e, responseBody);
                    Log.i(TAG, "callbackError run method end");
                } catch (Exception e) {
                    Log.i(TAG, "callbackError run method Exception start");
                    Log.i(TAG, "callbackError run method Exception:" + e.getMessage());
                    Log.i(TAG, "callbackError run method Exception end");
                }
            }
        });
    }

    private void callbackFailure(final BaseCallback callback, final Request request, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG, "callbackFailure run method start");
                    callback.onFailure(request, e);
                    Log.i(TAG, "callbackFailure run method end");
                } catch (Exception e) {
                    Log.i(TAG, "callbackFailure run method Exception start");
                    Log.i(TAG, "callbackFailure run method Exception:" + e.getMessage());
                    Log.i(TAG, "callbackFailure run method Exception end");
                }
            }
        });
    }

    private void callbackResponse(final BaseCallback callback, final Response response) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(response);
            }
        });
    }

    private Request buildPostRequest(String url, Map<String, String> params) {
        return buildRequest(url, HttpMethodType.POST, params, null);
    }

    private Request buildPostRequest(String url, String jsonString) {
        return buildRequest(url, HttpMethodType.POST, null, jsonString);
    }

    private Request buildGetRequest(String url) {
        return buildRequest(url, HttpMethodType.GET, null, null);
    }

    private Request buildRequest(String url, HttpMethodType methodType, Map<String, String> params, String jsonString) {
        Request.Builder builder = new Request.Builder()
                .url(url);
        if (methodType == HttpMethodType.POST) {
            RequestBody body = null;
            if (jsonString == null || jsonString.length() == 0) {//键值对
                body = builderFormData(params);
            } else {//json字符
                body = builderFormData(jsonString);
            }
            builder.post(body);
        } else if (methodType == HttpMethodType.GET) {
            builder.get();
        }
        return builder.build();
    }

    //post Key-value字符串
    private RequestBody builderFormData(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        RequestBody reqBody = builder.build();
        return reqBody;
    }

    //post json格式字符
    private RequestBody builderFormData(String jsonString) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody reqBody = RequestBody.create(mediaType, jsonString);
        return reqBody;
    }

    public void post() {
    }

    enum HttpMethodType {
        GET,
        POST
    }

    /**
     * 用于创建OkHttpClient
     * 由于请求OkHttpClent的是时效,创建后不可修改,所以自定义时效,需要重新创建.
     */
    private final class OkHttpClentCreator
    {
        private OkHttpClient mHttpClient; //---默认请求对象----
        private OkHttpClient mCustomHttpClient;//---自定义请求对象----

        public OkHttpClentCreator()
        {
            mHttpClient = new OkHttpClient
                    .Builder()
                    .connectTimeout(OkHttpHelperConfig.DEFAULT_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(OkHttpHelperConfig.DEFAULT_WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(OkHttpHelperConfig.DEFAULT_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .build();

            mCustomHttpClient = new OkHttpClient
                    .Builder()
                    .connectTimeout(OkHttpHelperConfig.DEFAULT_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(OkHttpHelperConfig.DEFAULT_WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(OkHttpHelperConfig.DEFAULT_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .build();
        }

        /**
         * 根据配置取HttpClent对象
         * @return
         */
        public OkHttpClient getOkHttpClient()
        {
            if(OkHttpHelperConfig.IsDefaultTimeOut())
                return mHttpClient;
            else
            {
                mCustomHttpClient = new OkHttpClient
                        .Builder()
                        .connectTimeout(OkHttpHelperConfig.getConnectTimeoutSeconds(), TimeUnit.SECONDS)
                        .writeTimeout(OkHttpHelperConfig.getWriteTimeOutSeconds(), TimeUnit.SECONDS)
                        .readTimeout(OkHttpHelperConfig.getReadTimeOutSeconds(), TimeUnit.SECONDS)
                        .build();
                OkHttpHelperConfig.resetDefalultTimeOut();//---恢复默认值
                return mCustomHttpClient;
            }
        }

    }

}
