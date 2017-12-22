package priv.wind.mvpdemo.views;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import priv.wind.mvpdemo.R;
import priv.wind.mvpdemo.presenters.RequestPresenter;

/**
 * V层
 */
public class MainActivity extends BaseActivity<IView, RequestPresenter> {

    @BindView(R.id.tv_one)
    TextView mTvOne;
    @BindView(R.id.btn_one)
    Button mBtnOne;
    private Button btnOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected RequestPresenter createPresenter() {
        return new RequestPresenter();
    }

    @OnClick(R.id.btn_one)
    public void onViewClicked() {
        getPresenter().clickRequest();
    }

    @Override
    public void requestLoading() {
        mTvOne.setText("加载中……");
    }

    @Override
    public void requestSuccess(String result) {
        mTvOne.setText(result);
    }

    @Override
    public void requestFailure(String errorMsg) {
        mTvOne.setText(errorMsg);
    }
}
