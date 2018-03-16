package id.co.okhome.consultant.rest_apicall.retrofit_restapi.v1;


import java.util.List;
import java.util.Map;

import id.co.okhome.consultant.model.FaqModel;
import id.co.okhome.consultant.model.NewsModel;
import id.co.okhome.consultant.model.WorkingRegionModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommonClient {

    @GET("version/{os}") //map은 id, value로 구성됨.
    Call<Map> getVersion(@Path("os") String os);

    @GET("working_region")
        //The address system has up to two levels.
        // If you want to get first level address list, place -1 on {addressId}.
        // If you want second level address list, place address's id on {addressId}

    Call<List<WorkingRegionModel>> getWorkingRegion(@Query("parentId") int parentId);

    @GET("working_region/all")
    Call<List<WorkingRegionModel>> getAllWorkingRegion();

    @GET("news")
    Call<List<NewsModel>> getAllNews();

    @GET("faq/{faqId}")
    Call<FaqModel> getFaq(@Path("faqId") int faqId);

    @GET("faq")
    Call<List<FaqModel>> getAllFaqs(@Query("parentFaqId") int faqId);
}


