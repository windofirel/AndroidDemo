package com.chinasie.common.tools;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.chinasie.common.models.LoginInfo;
import com.chinasie.common.utility.LocalDataHelper;
import com.chinasie.common.view.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-09-27.
 */

public  class PlatformContextHelper   {

    private Context activity;
    private static class SingletonHolder {
        private static final PlatformContextHelper INSTANCE = new PlatformContextHelper();
    }
    private PlatformContextHelper (){}
    public static final PlatformContextHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 初始化
     */
    public void InitPlatformContextHelper(Context _activity)
    {
        this.activity = _activity;
        GetLoginInfo();
    }

    /**
     * 设置Context
     */
    public void SetContext(Context _activity)
    {
        this.activity = _activity;
    }

    public static final String basicTAG = BaseActivity.class.getSimpleName();
    protected final Uri contentUri = Uri.parse("content://com.chinasie.pluginmain.context.wmscontentprovider/context");
    private static Map<String, String> mapContext = new HashMap<>();


    /**
     * 获取上下文键值
     */
    public Map<String, String> getPlatformContext() {
        try {
            mapContext = new HashMap<>();
            ContentResolver cr = activity.getContentResolver();
            Cursor c = cr.query(contentUri, null, null, null, null);
            while (c.moveToNext()) {
                mapContext.put(c.getString(c.getColumnIndex("key")), c.getString(c.getColumnIndex("value")));
            }
            c.close();
        } catch (Exception ex) {
            String msg = ex.getMessage() == null ? ex.toString() : ex.getMessage();
            Log.i(basicTAG, msg);
        }
        return mapContext;
    }

    /**
     * 向平台上下文中插入或更新数据
     */
    public void insertPlatformContext(String key, String value) {
        try {
            if(key != null && key.length() >0 && value != null && value.length() > 0) {
                ContentResolver cr = activity.getContentResolver();
                ContentValues values = new ContentValues();
                values.put("key", key);
                values.put("value", value);
                cr.insert(contentUri, values);
            }
        } catch (Exception ex) {
            String msg = ex.getMessage() == null ? ex.toString() : ex.getMessage();
            Log.i(basicTAG, msg);
        }
    }

    /**
     *     更新平台上下文
     */
    public void updatePlatformContext(String key, String value) {
        try {
            ContentResolver cr = activity.getContentResolver();
            ContentValues values = new ContentValues();
            values.put("key", key);
            values.put("value", value);
            cr.update(contentUri, values, key, null);
        } catch (Exception ex) {
            String msg = ex.getMessage() == null ? ex.toString() : ex.getMessage();
            Log.i(basicTAG, msg);
        }
    }

    /**
     * 删除平台上下文,key为null则删除全部数据
     */
    public void deletePlatformContext(String key) {
        try {
            ContentResolver cr = activity.getContentResolver();
            if (key == null) {//传入空删除全部数据
                cr.delete(contentUri, null, null);
            } else {
                cr.delete(contentUri, "key=?", new String[]{key});
            }
        } catch (Exception ex) {
            String msg = ex.getMessage() == null ? ex.toString() : ex.getMessage();
            Log.i(basicTAG, msg);
        }
    }

    public void GetLoginInfo()
    {
        Map<String, String> platformContext = this.getPlatformContext();
        LoginInfo clt_LoginInfo  = new LoginInfo();
        clt_LoginInfo.UserId = platformContext.get("userId");
        clt_LoginInfo.GroupId = platformContext.get("groupId");
        clt_LoginInfo.LoginName = platformContext.get("loginName");
        clt_LoginInfo.LoginPwd = "";
        clt_LoginInfo.Url = platformContext.get("requestUrl");
        LocalDataHelper.setLoginInfo(clt_LoginInfo);
    }
}
