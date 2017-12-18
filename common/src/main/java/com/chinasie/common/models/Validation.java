package com.chinasie.common.models;

/**
 * Created by GuYongCai on 2017-06-01.
 */

import android.content.Context;

/**
 * 校验类
 */
public abstract class Validation {

    /**
     * 上级校验对象
     */
    public Validation validation;

    /**
     * 上下文，用于校验界面逻辑
     */
    public Context context;

    /**
     * 校验方法
     * @return
     */
    public abstract boolean checkInput();


    /**
     * 构造函数
     * @param _context
     * @param -validation
     */
    public Validation(Context _context, Validation _validation) {
        context = _context;
        validation = _validation;
    }

    /**
     * 获取上级校验
     * @return
     */
    public boolean checkValidation()
    {
        if(validation == null)
            return true;
        else
            return validation.checkInput();
    }

}
