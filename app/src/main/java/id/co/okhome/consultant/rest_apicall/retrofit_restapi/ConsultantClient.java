package id.co.okhome.consultant.rest_apicall.retrofit_restapi;

import id.co.okhome.consultant.model.page.ConsultantPageJobsModel;
import id.co.okhome.consultant.model.page.ConsultantPageProgressModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ConsultantClient {

    @GET("consultant/page/progress/{consultantId}")
    Call<ConsultantPageProgressModel> getConsultantProgress(@Path("consultantId") String consultantId);

    @GET("consultant/page/job/{consultantId}")
    Call<ConsultantPageJobsModel> getConsultantJobs(@Path("consultantId") String consultantId);

}