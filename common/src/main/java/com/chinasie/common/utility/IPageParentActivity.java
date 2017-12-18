package com.chinasie.common.utility;

import com.chinasie.common.view.BasePageActivity;

/**
 * 泛型接口
 * Created by 古永财 on 2016-10-06.
 */

public interface IPageParentActivity<T extends BasePageActivity> {

    public T GetParentActivity();
}
