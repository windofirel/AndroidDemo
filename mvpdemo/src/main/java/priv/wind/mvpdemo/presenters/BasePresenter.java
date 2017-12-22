package priv.wind.mvpdemo.presenters;

import priv.wind.mvpdemo.views.IView;

/**
 * P层基类
 *
 * @author Dongbaicheng
 * @version 2017/12/21
 */

public abstract class BasePresenter<V extends IView> {
    private V mBaseView;

    /**
     * 绑定V层
     *
     * @param view V层对象
     */
    public void attach(V view) {
        mBaseView = view;
    }

    /**
     * 解绑V层
     */
    public void detach() {
        mBaseView = null;
    }

    /**
     * 获取V层
     *
     * @return V层对象
     */
    public V getView() {
        return mBaseView;
    }
}
