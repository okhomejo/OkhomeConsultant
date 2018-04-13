package id.co.okhome.consultant.rest_apicall.retrofit_restapi;

import java.util.List;

import id.co.okhome.consultant.model.cleaning.CleaningReviewModel;
import id.co.okhome.consultant.model.cleaning.CleaningReviewPageModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CleaningReviewClient {

    @GET("cleaning_review/{consultantId}/summary")
    Call<CleaningReviewPageModel> getReviewPageModel (
            @Path("consultantId") String consultantId);

    @GET("cleaning_review/{consultantId}")
    Call<List<CleaningReviewModel>> getCleaningReviews(
            @Path("consultantId") String consultantId,
            @Query("pivotRownum") int rownum);


}
