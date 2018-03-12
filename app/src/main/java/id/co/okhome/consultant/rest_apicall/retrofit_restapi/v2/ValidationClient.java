package id.co.okhome.consultant.rest_apicall.retrofit_restapi.v2;


import java.util.List;

import id.co.okhome.consultant.model.WorkingRegionModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ValidationClient {

    @POST("validation/phone/issue")
    /**
     * send code for validating phone.
     * */
    Call<String> issuePhoneValidationCode(@Query("phone") String phone);

    @GET("validation/phone/check")
    /**
     * Check if the code is valid
     * */
    Call<List<WorkingRegionModel>> checkPhoneValidationCode(@Query("phone") int phone, @Query("code") int code);

}


