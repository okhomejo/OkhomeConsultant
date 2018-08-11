package id.co.okhome.consultant.rest_apicall.retrofit_restapi;

import id.co.okhome.consultant.model.cleaning.order.CleaningOrderInfoDetailModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CleaningOrderClient {

    @GET("cleaning_order/{cleaningOrderId}")
    Call<CleaningOrderInfoDetailModel> getCleaningDetail(
            @Path("cleaningOrderId") int cleaningOrderId, @Query("consultantId") String consultantId);


}
