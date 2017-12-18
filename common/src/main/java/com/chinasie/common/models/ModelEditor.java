package com.chinasie.common.models;

/**
 * Created by GuYongCai on 2017-06-01.
 */

import android.content.Context;
import android.widget.EditText;

/**
 * 处理者
 */
public abstract class ModelEditor {


    /**
     * 上下文
     */
    public Context context;


    /**
     * 初始化界面操作
     */
    public abstract void initView();


    /**
     * 构造函数
     * @param _context
     */
    public ModelEditor(Context _context)
    {
        context = _context;
    }



}
