package id.co.okhome.consultant.rest_apicall.retrofit_restapi;

import id.co.okhome.consultant.model.page.ConsultantPageJobsModel;
import id.co.okhome.consultant.model.page.ConsultantPageMainModel;
import id.co.okhome.consultant.model.page.ConsultantPageProgressModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ConsultantClient {

    @GET("consultant/page/progress/{consultantId}")
    Call<ConsultantPageProgressModel> getConsultantProgress(@Path("consultantId") String consultantId);

    @GET("consultant/page/job/{consultantId}")
    Call<ConsultantPageJobsModel> getConsultantJobs(@Path("consultantId") String consultantId);

    @GET("consultant/page/main/{consultantId}")
    Call<ConsultantPageMainModel> getConulstantMainPage(@Path("consultantId") String consultantId);

    @PATCH("consultant/update")
    Call<String> update(@Query("accountId") String accountId, @Query("jsonReqStr") String jsonReqStr);
}