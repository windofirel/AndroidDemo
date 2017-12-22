package priv.wind.mvpdemo.views;

/**
 * @author Dongbaicheng
 * @version 2017/12/21
 */

public interface IView {
    /**
     * 请求时加载动作
     */
    void requestLoading();

    /**
     * 请求成功
     *
     * @param result 返回数据
     */
    void requestSuccess(String result);

    /**
     * 请求失败
     *
     * @param errorMsg 错误信息
     */
    void requestFailure(String errorMsg);
}
