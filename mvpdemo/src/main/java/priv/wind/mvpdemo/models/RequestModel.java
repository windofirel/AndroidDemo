package priv.wind.mvpdemo.models;

import retrofit2.Callback;

/**
 * M层
 *
 * @author Dongbaicheng
 * @version 2017/12/21
 */

public class RequestModel {
    public void request(Callback<String> callback) {
        callback.onResponse(null, null);
        //        callback.onFailure(null, null);
    }
}
