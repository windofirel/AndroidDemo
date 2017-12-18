package com.chinasie.common.api;

import java.util.HashMap;

/**
 * Created by 古永财 on 2016-10-12.
 */

public class ApiRequest {

    /// <summary>
    /// 请求类型
    /// </summary>
    public String ApiType;

    /// <summary>
    /// 所有的参数。
    /// </summary>
    public ApiMethodParameter[] Parameters;

    /// <summary>
    /// 方法名称。
    /// </summary>
    public String Method;

    /// <summary>
    /// 上下文
    /// </summary>
    public HashMap<String,String> Context= new HashMap<>();


}
