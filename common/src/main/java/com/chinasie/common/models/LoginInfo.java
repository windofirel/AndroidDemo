package com.chinasie.common.models;

/**
 * Created by Administrator on 2016/8/23.
 */
public class LoginInfo {
    public String UserId;
    public String LoginName;
    public String LoginPwd;
    public String GroupId;
    public String Url;
    public String Ticket = "";

    public String getUrl() {

        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getUserId() {
        return UserId;
    }
    public String getLoginName(){
        return LoginName;
    }
    public String getLoginPwd(){
        return LoginPwd;
    }
    public String getGroupId(){
        return GroupId;
    }

}
