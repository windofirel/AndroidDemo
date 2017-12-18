package com.chinasie.common.tools;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/8/18.
 */
public class ExitApplication  extends Application {
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
