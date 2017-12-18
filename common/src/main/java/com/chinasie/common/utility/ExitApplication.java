package com.chinasie.common.utility;

import android.app.Activity;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/8/22.
 */
public class ExitApplication {
    private LinkedList<Activity> activityList = new LinkedList<Activity>();

    private static ExitApplication instance = null;

    private ExitApplication(){

    }

    //单例模式中获取唯一的ExitApplication实例
    public static ExitApplication getInstance()
    {
        if(null == instance)
        {
            instance = new ExitApplication();
        }
        return instance;
    }

    //添加Activity到容器中
    public void addActivity(Activity activity)
    {
        activityList.add(activity);
    }
    //遍历所有Activity并finish
    public void exit()
    {
        for(Activity act:activityList)
        {
            act.finish();
        }
        System.exit(0);
    }
}
