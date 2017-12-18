package com.chinasie.common.api;

import java.util.HashMap;

/**
 * Created by 古永财 on 2016-10-12.
 */

public class ApiResponse<T> {

    public Boolean Success;
    public String Message;
    public HashMap<String,String> Context = new HashMap<>();
    public T Result;

}
