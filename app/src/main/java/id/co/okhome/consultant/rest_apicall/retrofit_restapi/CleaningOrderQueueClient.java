package id.co.okhome.consultant.rest_apicall.retrofit_restapi;

import java.util.List;
import java.util.Map;

import id.co.okhome.consultant.model.cleaning.order.CleaningReqQueueModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CleaningOrderQueueClient {

    //accept
    //http://localhost:2111/cleaning/65/curated
    @GET("cleaning_order_queue/{consultantId}/curated")
    Call<Map<Integer, List<CleaningReqQueueModel>>> getQuratedCleaningQueueList(
            @Path("consultantId") String consultantId);

    @POST("cleaning_order_queue/accept")
    Call<String> acceptCleaningOrderInQueue(@Query("cleaningQueueId") int cleaningQueueId, @Query("consultantId") String consultantId);

    @POST("cleaning_order_queue/reject")
    Call<String> rejectCleaningOrderInQueue(@Query("cleaningQueueId") int cleaningQueueId, @Query("consultantId") String consultantId);


}
