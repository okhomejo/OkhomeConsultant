package id.co.okhome.consultant.rest_apicall.retrofit_restapi;

import java.util.List;

import id.co.okhome.consultant.model.cleaning.CleaningDetailWithOrderInfoModel;
import id.co.okhome.consultant.model.cleaning.order.CleaningDayItemModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CleaningTaskClient {

    @GET("cleaning/{consultantId}/prev")
    Call<List<CleaningDayItemModel>> getPrevCleaningTasks(
            @Path("consultantId") String consultantId,
            @Query("pivotRownum") int rownum);

    @GET("cleaning/{consultantId}/next")
    Call<List<CleaningDayItemModel>> getNextCleaningTasks(
            @Path("consultantId") String consultantId,
            @Query("pivotRownum") int rownum);


    //청소 큐레이팅된 내역 불러오기
    @GET("/cleaning/{cleaningId}")
    Call<CleaningDetailWithOrderInfoModel> getCleaningDetailWithOrderDetail(@Path("cleaningId") int cleaningId, @Query("consultantId") String consultantId);

    //
    @POST("/cleaning/begin_cleaning")
    Call<String> notifyBeginCleaning(@Query("consultantId") String consultantId, @Query("cleaningId") int cleaningId);

    //end_cleaning
    @POST("/cleaning/end_cleaning")
    Call<String> notifyEndCleaning(@Query("consultantId") String consultantId, @Query("cleaningId") int cleaningId);

}
