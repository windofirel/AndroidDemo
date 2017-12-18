package com.chinasie.common.api;

import android.content.Context;

/**
 * Created by Administrator on 2016/10/27.
 */

public abstract class ApiCallback<T> {
    public abstract void Callback(T resultObj);//---正确回调----
    public abstract void Error(String errorMsg);//---调用失败---
    public void Failure(Exception e, Context context, String action){};//---通讯失败---
}
