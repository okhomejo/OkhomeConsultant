package id.co.okhome.consultant.rest_apicall.retrofit_restapi.v2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
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
    Call<Boolean> checkPhoneValidationCode(@Query("phone") String phone, @Query("code") String code);

    @PATCH("profile/update/phone")
    Call<String> updatePhoneNumber(@Query("accountId") String accountId, @Query("phone") String phone, @Query("code") String code);
}