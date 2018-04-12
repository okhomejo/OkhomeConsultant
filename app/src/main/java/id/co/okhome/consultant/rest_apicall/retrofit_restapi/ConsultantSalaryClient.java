package id.co.okhome.consultant.rest_apicall.retrofit_restapi;

import java.util.List;

import id.co.okhome.consultant.model.MoneyHistoryModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ConsultantSalaryClient {

    /**type : paid, waiting*/
    @GET("consultant/salary/{consultantId}/{type}")
    Call<List<MoneyHistoryModel>> getTrainingList(
            @Path("consultantId") String consultantId,
            @Path("type") String type,
            @Query("pivotRownum") int rownum
    );

}