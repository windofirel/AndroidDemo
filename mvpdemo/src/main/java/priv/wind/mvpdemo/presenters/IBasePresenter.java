package priv.wind.mvpdemo.presenters;

import priv.wind.mvpdemo.views.IView;

/**
 * @author Dongbaicheng
 * @version 2017/12/21
 */

public interface IBasePresenter<V extends IView> {
    /**
     * 绑定V层
     *
     * @param view V层对象
     */
    void attach(V view);

    /**
     * 解绑V层
     */
    void detach();

    /**
     * 获取V层
     *
     * @return V层对象
     */
    V getView();
}
