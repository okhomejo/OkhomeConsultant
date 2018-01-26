package id.co.okhome.consultant.rest_apicall.retrofit_restapi;


import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ConsultantClient {

    @GET("version/{os}") //map은 id, value로 구성됨.
    Call<Map> getVersion(@Path("os") String os);


}


