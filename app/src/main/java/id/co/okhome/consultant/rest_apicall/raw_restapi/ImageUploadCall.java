package id.co.okhome.consultant.rest_apicall.raw_restapi;


import org.json.JSONObject;

import java.io.File;

import id.co.okhome.consultant.config.OkhomeConstant;
import id.co.okhome.consultant.lib.jobrowser.api_call.CommonApiCall;
import id.co.okhome.consultant.lib.jobrowser.api_quick_caller.ApiQuickCaller;
import id.co.okhome.consultant.lib.jobrowser.client.CommonApiClient;
import id.co.okhome.consultant.lib.jobrowser.exception.ApiException;
import id.co.okhome.consultant.lib.jobrowser.model.ApiResult;
import id.co.okhome.consultant.lib.jobrowser.util.JoBrowserUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by jo on 2017-11-13.
 */

public class ImageUploadCall extends CommonApiCall {
    final String url = OkhomeConstant.OKHOME_URL + "file/photo";
    String filePath;

    public ImageUploadCall(String filePath) {
        super(new CommonApiClient());
        this.filePath = filePath;
    }


    @Override
    public ApiResult<String> work() throws Exception{

        final String fileName = filePath.substring(filePath.lastIndexOf("/")+1);

        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("photo", fileName, RequestBody.create(MediaType.parse("multipart/form-data"), new File(filePath)))
                .build();

        ApiResult uploadResult = ApiQuickCaller.post(
                getCommonApiClient().client
                , url
                , JoBrowserUtil.makeStringMap(
                        "accept", "*/*"
                        , "user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36"
                ), body);


        if(uploadResult.resultCode != 200){
            throw new ApiException(uploadResult.resultCode, uploadResult.result);
        }

        JSONObject jResult = new JSONObject(uploadResult.result);
        String urlPath = jResult.getString("obj");

        return new ApiResult<>(200, "success", urlPath);
    }

}
