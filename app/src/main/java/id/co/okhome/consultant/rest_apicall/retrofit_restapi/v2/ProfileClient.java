package id.co.okhome.consultant.rest_apicall.retrofit_restapi.v2;


import java.util.Map;

import id.co.okhome.consultant.model.v2.ProfileModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProfileClient {


    @GET("profile/{accountId}")
    /**
     * To get profile by id.
     * ** Recommend to use AccountClient.getInfo instead of this.
     * */
    Call<ProfileModel> getProfile(@Path("accountId") int accountId);


    @GET("profile/{accountId}")
    /**
     * To get only specific profile's field value by id.
     * keys sample : keys=phone,address,photoUrl,ktpPhotoUrl
     * Use params name with camelStyle. Our db use under_score style. but param and return json result is composed of camelStyle
     * */
    Call<Map<String, Object>> getProfileSpecificFieldValue(@Path("accountId") int accountId, @Query("keys") int keys);


    @PATCH("profile/update")
    /**update Account Profile's specific fields at once.(without accountId, phone)
     * key which able to be changed : signUpBy, type.
     * jsonReqStr example : jsonReqStr={"address":"hhh","ktpPhotoUrl":"http://aAAAa.jpg"}
     * */
    Call<String> update(@Path("accountId") int accountId, @Query("jsonReqStr") int jsonReqStr);
}


