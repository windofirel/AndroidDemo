package priv.wind.mvpdemo.presenters;

import android.os.Handler;

import priv.wind.mvpdemo.models.RequestModel;
import priv.wind.mvpdemo.views.IView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * P层
 *
 * @author Dongbaicheng
 * @version 2017/12/21
 */

public class RequestPresenter extends BasePresenter<IView> {
    private RequestModel mModel;

    public RequestPresenter() {
        mModel = new RequestModel();
    }

    public void clickRequest() {
        getView().requestLoading();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mModel.request(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        getView().requestSuccess("成功");
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        getView().requestFailure("失败");
                    }
                });
            }
        }, 1000);
    }
}
