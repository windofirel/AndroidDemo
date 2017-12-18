package com.chinasie.common.api;

/**
 * 请求参数实体
 * Created by 古永财 on 2016-10-11.
 */

public class ApiMethodParameter {

    public Object Value=null;   //参数值
    /**
     * 参数类型
     * Java所有基本数据类型（int,float,boolean,byte,double,char,long,short) 需要转换为C#类型
     * 如: java的int 这里要设置为C#的System.Int32
     * 其它用户自定义实体类型一般不用转换
     * 如:public class User
     * {
     *     public string UserName;//---名称---
     *     public double weight;//---重量---
     * }
     */
    public String Type = "";

    public ApiMethodParameter(){}

    public ApiMethodParameter(String type,Object value)
    {
        Type = type;
        Value = value;
    }

}
