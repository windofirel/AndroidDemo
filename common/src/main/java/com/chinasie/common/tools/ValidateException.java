package com.chinasie.common.tools;

import android.util.Log;

/**
 * 验证异常（待完善）
 * @author Dongbaicheng
 * @version 2017/6/1
 */

public class ValidateException extends RuntimeException {
    private static final String TAG = "验证异常";

    public ValidateException(String message){
        super(message);
        Log.i(TAG, message);
    }
}
