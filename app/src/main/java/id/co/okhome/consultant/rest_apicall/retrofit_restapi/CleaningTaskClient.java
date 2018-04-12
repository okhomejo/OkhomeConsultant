package id.co.okhome.consultant.rest_apicall.retrofit_restapi;

import java.util.List;
import id.co.okhome.consultant.model.cleaning.CleaningInfoModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CleaningTaskClient {

    @GET("cleaning/{consultantId}/prev")
    Call<List<CleaningInfoModel>> getPrevCleaningTasks(@Path("consultantId") String consultantId);

    @GET("cleaning/{consultantId}/next")
    Call<List<CleaningInfoModel>> getNextCleaningTasks(@Path("consultantId") String consultantId);

}