package com.chinasie.common.models;

import android.content.Context;

import com.chinasie.common.api.ApiResponse;

import java.lang.reflect.Type;

import dmax.dialog.SpotsDialog;
import okhttp3.Request;
import okhttp3.Response;

import static com.google.gson.internal.$Gson$Types.canonicalize;

/**
 * Created by Administrator on 2016/8/19.
 */
public abstract class SpotsCallBack<T> extends BaseCallback<T> {
    private Context mContext;
    private SpotsDialog mDialog;
    Type type;
    public SpotsCallBack(Context context){
        mContext = context;
        initSpotsDialog();
    }
    public SpotsCallBack(Context context, Type genericityType){
        mContext = context;
        initSpotsDialog();
        type = genericityType;
        Type t =  new ParameterizedTypeImpl(getClass(), ApiResponse.class, type);
        mType = canonicalize(t);
    }

    private  void initSpotsDialog(){
        mDialog = new SpotsDialog(mContext,"拼命加载中...");
        mDialog.setCancelable(false);
    }

    public  void showDialog(){
        mDialog.show();
    }

    public  void dismissDialog(){
        mDialog.dismiss();
    }

    public void setLoadMessage(int resId){
        mDialog.setMessage(mContext.getString(resId));
    }

    @Override
    public void onFailure(Request request, Exception e) {
        dismissDialog();
    }

    @Override
    public void onBeforeRequest(Request request) {

        showDialog();
    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }
}
