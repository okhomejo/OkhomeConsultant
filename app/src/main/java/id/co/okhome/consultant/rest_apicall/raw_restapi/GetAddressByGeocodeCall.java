package id.co.okhome.consultant.rest_apicall.raw_restapi;


import org.json.JSONObject;

import id.co.okhome.consultant.lib.jobrowser.api_call.CommonApiCall;
import id.co.okhome.consultant.lib.jobrowser.api_quick_caller.ApiQuickCaller;
import id.co.okhome.consultant.lib.jobrowser.client.CommonApiClient;
import id.co.okhome.consultant.lib.jobrowser.model.ApiResult;
import id.co.okhome.consultant.lib.jobrowser.util.JoBrowserUtil;

/**
 * Created by jo on 2018-01-17.
 */

public class GetAddressByGeocodeCall extends CommonApiCall {

    final String key = "AIzaSyCd1ZjSyh5VN4eO6X48nH7BDrZJuj2mKSQ";

    public GetAddressByGeocodeCall(CommonApiClient commonApiClient) {
        super(commonApiClient);
    }

    @Override
    protected ApiResult<String> work() throws Exception {

        //
        final long lat = getParam(this, "lat");
        final long lng = getParam(this, "lng");

        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=#{lat},#{lng}&key=#{key}";
        url = url.replace("#{lat}", lat+"").replace("#{lng}", lng+"").replace("#{key}", key);

        ApiResult result = ApiQuickCaller.get(
                getCommonApiClient().client
                , url
                , JoBrowserUtil.makeStringMap()
                , JoBrowserUtil.makeStringMap());

        JSONObject jObject = new JSONObject(result.result);
        String address = jObject.getJSONObject("results").getString("formatted_address");


        ApiResult<String> addressResult = new ApiResult<>(100, address, address);
//        result.
        return result;
    }
}
