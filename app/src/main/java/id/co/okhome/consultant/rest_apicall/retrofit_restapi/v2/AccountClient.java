package id.co.okhome.consultant.rest_apicall.retrofit_restapi.v2;


import java.util.Map;

import id.co.okhome.consultant.model.v2.AccountModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccountClient {

    @POST("account/signup")
    /**signup. ** signupBy : EMAIL, GOOGLE */
    Call<AccountModel> signup(@Query("email") String email, @Query("password") String password, @Query("signupBy") String signupBy);

    /**sign in*/
    @GET("account/login")
    Call<AccountModel> login(@Query("email") String email, @Query("password") String password);

    /**get account info. it contains Trainee or Consultant info also.*/
    @GET("account/{id}")
    Call<AccountModel> getInfo(@Path("id") int accountId);

    /**get account info. but only for specific fields*/
    @GET("account/{id}")
    Call<Map<String, Object>> getSpecificInfo(@Path("id") int accountId, @Query("keys") int keys);

    /**logout. after calling this, the user can't get push notification and related actions.*/
    @POST("account/logout")
    Call<String> logout(@Path("id") int accountId);

    /**update password only*/
    @PATCH("account/update_password")
    Call<String> updatePassword(@Query("accountId") int accountId, @Query("prevPassword") String prevPassword, @Query("newPassword") int newPassword);

    /**update Account's specific fields at once.(without password, id, email)
     * key which able to be changed : signUpBy, type.
     * jsonReqStr example : jsonReqStr={"phone":"hhh","address":"N"}
     * but now We don't need to use this function.
     * */
    @PATCH("account/update")
    Call<String> update(@Query("accountId") int accountId, @Query("jsonReqStr") int jsonReqStr);

}


