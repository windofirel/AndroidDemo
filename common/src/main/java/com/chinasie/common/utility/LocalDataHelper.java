package com.chinasie.common.utility;

import com.chinasie.common.models.LoginInfo;

/**
 * Created by Administrator on 2016/8/22.
 */
public class LocalDataHelper {

    static
    {
        g_loginInfo = new LoginInfo();
    }

    private static LoginInfo g_loginInfo;
    public static void setRequestUrl(String url){
        g_loginInfo.setUrl(url);
    }

    public static String getRequestUrl(){
        //---作为新旧调用过渡,检测是否为旧的代理调用,并自动调整为新的调用---
        String oldRequestChannel = "AppService.svc/AppEndpoint/Exec";
        String newRequestChannel = "Server.svc/Api/Invoke";
        if(g_loginInfo.getUrl().contains(oldRequestChannel))
            g_loginInfo.setUrl(g_loginInfo.getUrl().replaceFirst(oldRequestChannel,newRequestChannel));

        return g_loginInfo.getUrl();
    }

    public static void setTicket(String ticket){
        g_loginInfo.Ticket = ticket;
    }

    //返回用户名、密码字符数组
    public static LoginInfo GetLoginInfo(){
        return g_loginInfo;
    }

    public static void setLoginInfo(LoginInfo clt_loginInfo) {
        g_loginInfo.LoginName = clt_loginInfo.LoginName;
        g_loginInfo.UserId = clt_loginInfo.UserId;
        g_loginInfo.GroupId = clt_loginInfo.GroupId;
        g_loginInfo.setUrl( clt_loginInfo.getUrl());
        g_loginInfo.Ticket = clt_loginInfo.Ticket;
    }

    public static void setLoginInfo(String loginName,String userId,String groupId){
        g_loginInfo.LoginName = loginName;
        g_loginInfo.UserId = userId;
        g_loginInfo.GroupId = groupId;
    }

    public static String getTicket() {
        return g_loginInfo.Ticket;
    }
}
