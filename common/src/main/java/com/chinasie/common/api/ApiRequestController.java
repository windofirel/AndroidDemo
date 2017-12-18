package com.chinasie.common.api;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.chinasie.common.config.OkHttpHelperConfig;
import com.chinasie.common.models.LoginInfo;
import com.chinasie.common.models.SpotsCallBack;
import com.chinasie.common.tools.PlatformContextHelper;
import com.chinasie.common.tools.ValidateException;
import com.chinasie.common.utility.LocalDataHelper;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.config.PropertyConfigurator;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 古永财 on 2016-09-10.
 */

public class ApiRequestController {
    public static final String TAG = ApiRequestController.class.getSimpleName();
    private SpotsDialog mDialog;
    protected static final ApiOkHttpHelper httpHelper = ApiOkHttpHelper.getInstance();
    protected Context context; //----上下文--
    protected static Gson gson = new Gson();//---Gson对象------
    private MyHandler handler = new MyHandler();
    private Thread thread;
    private static String ticket = null;//记录ticket,如果不为空，每次访问请求头信息中带上ticket lijiantong 2016-11-11
    private boolean isCalling = false; //---是否正在请求---
    private static final Logger logger = LoggerFactory.getLogger(ApiRequestController.class);//日志记录对象

    public ApiRequestController(Context _context) {
        context = _context;
        mDialog = new SpotsDialog(context, "拼命加载中...");
        thread = new Thread(waitInvoke);

        PropertyConfigurator.getConfigurator(_context).configure();
    }

    /**
     * 改变执行标识
     */
    private Runnable waitInvoke = new Runnable() {
        @Override
        public void run() {
            if(isCalling) {
                isCalling = false;
            }
        }
    };

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 0)
                mDialog.dismiss();
        }
    }

    public String getRequestURL() {
        //从上下文中获取ticket
        //        PlatformContextHelper platformContextHelper = PlatformContextHelper.getInstance();
        //        platformContextHelper.SetContext(this.context);
        //        Map<String, String> platContext =  platformContextHelper.getPlatformContext();
        //        return platContext.get("requestUrl");
        return LocalDataHelper.getRequestUrl();
    }

    protected <T> void actionUrl(Type genericityType, ApiRequest appRequest, final ApiCallback<T> callback) {
        final String action = appRequest.Method;//----执行的操作-----
        String requestJsonData = gson.toJson(appRequest);
        String requestUrl = this.getRequestURL();
        Log.d(TAG, "请求服务URL：" + requestUrl);
        Log.d(TAG, "请求JSON数据：" + requestJsonData);
        //---是否正在请求,PDA暂时不允许并发-----
        if (isCalling){
            Log.d(TAG, "已拦截并发操作");
            return;
        }
        //在线程中记录日志存在问题，会出现乱码
        logger.debug(String.format("\n\t请求JSON数据:%s,\n\t请求服务URL:%s\n", requestJsonData, requestUrl));

        handler.removeCallbacks(waitInvoke);
        handler.postDelayed(waitInvoke, OkHttpHelperConfig.getReadTimeOutSeconds() * 1000);//---超时触发----
        httpHelper.post(requestUrl, requestJsonData, new SpotsCallBack<ApiResponse>(context, genericityType) {
            @Override
            public void onSuccess(Response response, ApiResponse apiResponse) {
                try {
                    isCalling = false;
                    Log.d(action, "post onSuccess method start");
                    //获取Ticket lijiantong 2016-11-11
                    ApiRequestController.this.readTicket(apiResponse);

                    //success为false时，表示后台返回验证异常，前台处理
                    if (apiResponse.Success == null || apiResponse.Success == false) {
                        Log.e(TAG, "请求服务失败，错误信息" + apiResponse.Message);
//                        logger.debug(String.format("\n\t错误信息:%s", apiResponse.Message));

                        throw new ValidateException(apiResponse.Message);
                    }
                    callback.Callback((T) apiResponse.Result);
                }
                catch (ValidateException e){
                    callback.Error(e.getMessage());
                }
                catch (Exception e) {
                    Log.e(action, "post onSuccess method exception:" + e.toString());
                    callback.Error(e.toString());
//                    logger.error(String.format("\n\t异常信息:%s", e.toString()));
                }
            }

            @Override
            public void onError(Response response, int code, Exception e, String responseBody) {
                isCalling = false;
                if (responseBody != null) {
                    Log.e(TAG, "请求服务失败，错误信息" + responseBody);
                    callback.Error("错误信息: " + response.message() + code + "\n" + responseBody);
//                    logger.error(String.format("\n\t服务内部错误信息:%s", responseBody));
                    return;
                }
                if (e != null) {
                    callback.Error("错误信息:" + response.message() + "\n异常信息:" + e.getMessage());
//                    logger.error(String.format("\n\t异常信息:%s", e.getMessage()));
                    return;
                }
                callback.Error("错误信息:" + response.message() + code);
//                logger.error(String.format("\n\t错误信息:%s", response.message()));
            }

            @Override
            public void onFailure(Request request, Exception e) {
                isCalling = false;
                super.onFailure(request, e);
                //requestFailure(request, e, action);
                Log.e(TAG, "连接超时,请检查网络连接!" + e.getMessage());
                callback.Error("连接超时,请检查网络连接!");
                //                callback.Failure(e.getMessage());
            }
        });
    }

    //读取ticket，并保存到上下文中 2016-12-19
    private void readTicket(ApiResponse apiResponse) {
        if (apiResponse != null && apiResponse.Context != null && apiResponse.Context.containsKey("Ticket")) {
            ticket = (String) apiResponse.Context.get("Ticket");
            if (ticket == null) return;
            if (ticket.equals(LocalDataHelper.getTicket()))//---票据变更才重新缓存--
                return;
            LocalDataHelper.setTicket(ticket);//---更新到本地缓存
            //更新ticket到上下文,公共资源
            PlatformContextHelper platformContextHelper = PlatformContextHelper.getInstance();
            platformContextHelper.deletePlatformContext("Ticket");
            platformContextHelper.insertPlatformContext("Ticket", ticket);
        }
    }

    /**
     * 异步执行泛型
     *
     * @param apiType    方法类型
     * @param method     方法名
     * @param callback   回调函数
     * @param parameters 可变参数
     */
    public <T> void SyncInvokeUrl(Type genericityType, String apiType, String method, final ApiCallback<T> callback, ApiMethodParameter[] parameters) {

        ApiRequest request = new ApiRequest();
        request.ApiType = apiType;
        request.Method = method;
        request.Parameters = parameters;
        this.initRequestContext(request);
        actionUrl(genericityType, request, callback);
    }

    /**
     * 向ApiRequest中插入Context lijiantong 2016-11-11
     * @param request 请求对象
     */
    private void initRequestContext(ApiRequest request) {
        PlatformContextHelper platformContextHelper = PlatformContextHelper.getInstance();
        Map<String, String> platContext = platformContextHelper.getPlatformContext();
        if (platContext.containsKey("Ticket")) {
            ticket = platContext.get("Ticket");
            request.Context.put("Ticket", ticket);
        }

        LoginInfo loginInfo = LocalDataHelper.GetLoginInfo();
        if (loginInfo.UserId != null) {
            request.Context.put("userId", loginInfo.UserId);
        }
        if (loginInfo.GroupId != null) {
            //因为后台组织ID为整形，故强制转换
            double groupId = Double.parseDouble(loginInfo.GroupId);
            request.Context.put("InvOrgId", String.valueOf((int) groupId));
        }
    }

    /**
     * 请求失败,前端可以复写
     *
     * @param request
     * @param e
     * @param action
     */
    protected void requestFailure(Request request, Exception e, String action) {
        try {
            //调用父类中的方法，关闭加载中弹出框
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception errExc) {
            Log.e(action, "onFailure method exception" + errExc.getMessage());
            Toast.makeText(context, "连接网络失败!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 请求错误,前端可以复写
     *
     * @param code
     * @param e
     * @param context
     * @param action
     */
    protected void requestError(int code, Exception e, Context context, String action) {
        try {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i(action, "onError method Response.Code:" + code);
            Log.i(action, "onError method Response.Msg" + e.getMessage());
        } catch (Exception errExc) {
            Log.e(action, "onError method exception" + errExc.getMessage());
            Toast.makeText(context, "连接网络失败!", Toast.LENGTH_SHORT).show();
        }
    }
}