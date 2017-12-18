package com.chinasie.common.api;

import android.content.Context;
import android.util.Log;

import com.chinasie.common.api.annotation.ApiMethodAnnotation;
import com.chinasie.common.api.annotation.ApiObjectAnnotation;
import com.chinasie.common.api.annotation.ApiParameterAnnotation;
import com.chinasie.common.api.annotation.ApiServiceAnnotation;
import com.chinasie.common.config.OkHttpHelperConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * 事务处理者
 * Created by 古永财 on 2016-10-11.
 */
public class ApiTransactionProcessing {
    private static final String TAG = "控制器事务处理类";
    private ApiRequestController apiRequestController;//---调用者----
    public static final HashMap<String, String> JavaTypeToCSharp;

    static {
        JavaTypeToCSharp = new HashMap<>();
        JavaTypeToCSharp.put("java.lang.Short", "System.Int16");
        JavaTypeToCSharp.put("java.lang.Integer", "System.Int32");
        JavaTypeToCSharp.put("java.lang.Long", "System.Int64");
        JavaTypeToCSharp.put("java.lang.Boolean", "System.Boolean");
        JavaTypeToCSharp.put("java.lang.Double", "System.Double");
        JavaTypeToCSharp.put("java.lang.Float", "System.Single");
        JavaTypeToCSharp.put("java.lang.String", "System.String");
    }

    private static final ApiTransactionProcessing INSTANCE = new ApiTransactionProcessing();

    private ApiTransactionProcessing() {
    }

    public static final ApiTransactionProcessing getInstance() {
        return ApiTransactionProcessing.INSTANCE;
    }

    /**
     * 当前调用方法名称
     * @return
     */
    public String GetCurrentMethod() {
        return Thread.currentThread().getStackTrace()[1].getMethodName();
    }

    public ApiTransactionProcessing(Context context) {
        apiRequestController = new ApiRequestController(context);
    }

    /**
     * 异步调用
     *
     * @param genericityType json解析用type
     * @param apiCallback 回调
     * @param parameters 传递的参数
     * @param <T> json解析用type
     */
    public final <T> void SyncInvoke(Type genericityType, ApiCallback<T> apiCallback, Object... parameters){
        try {
            //---获取参数----------
            ApiMethodParameter[] params = new ApiMethodParameter[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Object p = parameters[i];
                params[i] = new ApiMethodParameter();
                //---翻译成.net的类型----
                if (ApiTransactionProcessing.JavaTypeToCSharp.containsKey(p.getClass().getName()))
                    params[i].Type = ApiTransactionProcessing.JavaTypeToCSharp.get(p.getClass().getName());
                else { //---其它类型直接赋值类型名-----
                    ApiObjectAnnotation apiObjectAnnotation = p.getClass().getAnnotation(ApiObjectAnnotation.class);//---获取类注解--
                    if (apiObjectAnnotation != null) {
                        params[i].Type = apiObjectAnnotation.Type();
                    }
                }
                params[i].Value = p;
            }
            //---获取API类型 如:ApiType="Platform.Domain.Common.Security.UserController, Platform.Domain.Common"---
            String ApiType = "";
            ApiServiceAnnotation apiServiceAnnotation = this.getClass().getAnnotation(ApiServiceAnnotation.class);//---获取类注解--
            if (apiServiceAnnotation == null)
                throw new Exception(this.getClass().getName() + "调用请求体没有注解!");
            ApiType = apiServiceAnnotation.ApiType();
            //---获取调用方法------------
            String ApiMethod = "";//---请求方法---
            StackTraceElement[] stack = new Throwable().getStackTrace();//---程序栈轨迹---
            String objMethodName = stack[1].getMethodName();//----获取上级类调用此方法的方法名----
            Method method;//---方法---
            Class[] parameterTypes = new Class[parameters.length + 1];//---参数表
            if (parameters.length > 0) {//----获取其它参数-----
                for (int i = 0; i < parameters.length; i++) {
                    parameterTypes[i] = parameters[i].getClass();
                }
            }
            parameterTypes[parameters.length] = ApiCallback.class;//---最后为回调参数
            method = this.getClass().getMethod(objMethodName, parameterTypes);//----获取方法----
            ApiMethodAnnotation apiMethodAnnotation = method.getAnnotation(ApiMethodAnnotation.class);//----获取方法注解---
            if (apiMethodAnnotation == null)
                ApiMethod = objMethodName;
            else
                ApiMethod = apiMethodAnnotation.Method().equals("") ? objMethodName : apiMethodAnnotation.Method();
            //---获取自定义超时---
            if(apiMethodAnnotation.ReadTimeOutSeconds() != OkHttpHelperConfig.DEFAULT_READ_TIMEOUT_SECONDS)
                OkHttpHelperConfig.setReadTimeOutSeconds(apiMethodAnnotation.ReadTimeOutSeconds());
            //---找不到类型的参数,通过参数注解来找类型----
            Annotation[][] annotationsParameters = method.getParameterAnnotations();
            for (int i = 0; i < annotationsParameters.length; i++) {
                int length = annotationsParameters[i].length;
                if (length == 0)
                    continue;
                else {
                    //---第一个参数里面的第一个注解----
                    ApiParameterAnnotation apiParameterAnnotation = (ApiParameterAnnotation) annotationsParameters[i][0];
                    if (params[i].Type.equals("")) {
                        params[i].Type = apiParameterAnnotation.ParameterType();
                    }
                }
            }

            Log.i("ApiType", ApiType);
            Log.i("Method", ApiMethod);
            apiRequestController.SyncInvokeUrl(genericityType, ApiType, ApiMethod, apiCallback, params);
        }
        catch (NoSuchMethodException e){
            Log.e(TAG, e.toString());
            apiCallback.Error("反射方法失败，请检查控制器方法的参数是否使用了关键字来声明!\n" + e.toString());
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            apiCallback.Error(e.toString());
        }

    }
}
