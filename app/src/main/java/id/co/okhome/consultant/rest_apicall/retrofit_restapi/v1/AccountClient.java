package id.co.okhome.consultant.rest_apicall.retrofit_restapi.v1;


import id.co.okhome.consultant.model.AccountStatusModel;
import id.co.okhome.consultant.model.ConsultantModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccountClient {

    @POST("consultant/account/signup")
    /**signup. **Callback : Integer(consultant id)*/
    Call<Integer> signup(@Query("email") String email, @Query("password") String password);

    @GET("consultant/account/signin")
    /**signin. */
    Call<ConsultantModel> signin(@Query("email") String email, @Query("password") String password);

    @GET("consultant/account/{consultantId}")
    /**signin. */
    Call<ConsultantModel> getConsultantInfo(@Path("consultantId") String consultantId);

    @PATCH("consultant/account/update_profile")
    /**
     * @Param String jsonParams Sending param with json form. ex) {"gender":"F"}
     * @return If success, Meaningless String will arrive.
     *
     * */
    Call<String> updateProfile(@Query("consultantId") String consultantId, @Query("jsonParams") String jsonParams);

    @GET("consultant/account/check_status/{consultantId}")
    /**signin. */
        Call<AccountStatusModel> getAccountStatus(@Path("consultantId") String consultantId);


}


