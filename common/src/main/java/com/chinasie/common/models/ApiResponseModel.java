package com.chinasie.common.models;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/11.
 */
public class ApiResponseModel {
    public Boolean Success;
    public String Message;
    public HashMap<String,String> Context = new HashMap<>();
    public String Result;
}
