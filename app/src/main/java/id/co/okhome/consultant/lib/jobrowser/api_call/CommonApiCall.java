package id.co.okhome.consultant.lib.jobrowser.api_call;


import id.co.okhome.consultant.lib.jobrowser.client.CommonApiClient;

/**
 * Created by jo on 2017-11-21.
 */

public abstract class CommonApiCall extends ApiCall{

    CommonApiClient commonApiClient;
    public CommonApiCall(CommonApiClient commonApiClient) {
        this.commonApiClient = commonApiClient;
    }

    public CommonApiClient getCommonApiClient() {
        return commonApiClient;
    }
}
