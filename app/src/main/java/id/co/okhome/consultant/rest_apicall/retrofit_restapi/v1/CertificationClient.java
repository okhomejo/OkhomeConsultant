package id.co.okhome.consultant.rest_apicall.retrofit_restapi.v1;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CertificationClient {

    @GET("certification/checking")
    Call<Boolean> chkValidCode(@Query("phone") String phone, @Query("code") String code);

    @POST("certification/sms")
    Call<String> sendCertCode(@Query("phone") String phone, @Query("forUpdate") String forUpdate);


}


