package priv.wind.mvpdemo.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import priv.wind.mvpdemo.presenters.BasePresenter;

/**
 * @author Dongbaicheng
 * @version 2017/12/21
 */

public abstract class BaseActivity<V extends IView, P extends BasePresenter<V>>
        extends AppCompatActivity implements IView {
    private P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mPresenter == null) {
            mPresenter = createPresenter();
        }

        mPresenter.attach((V) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除绑定
        if (mPresenter != null) {
            mPresenter.detach();
        }
    }

    /**
     * 创建presenter
     *
     * @return 子类对应的presenter
     */
    protected abstract P createPresenter();

    public P getPresenter() {
        return mPresenter;
    }
}
