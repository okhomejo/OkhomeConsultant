package id.co.okhome.consultant.lib.jobrowser.callback;


import id.co.okhome.consultant.lib.jobrowser.model.ApiResult;

/**
 * Created by jo on 2017-11-21.
 */

public interface ApiResultCallback<T> {
    void onFinish(ApiResult<T> apiResult);
}
