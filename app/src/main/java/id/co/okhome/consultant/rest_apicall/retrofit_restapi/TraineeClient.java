package id.co.okhome.consultant.rest_apicall.retrofit_restapi;

import id.co.okhome.consultant.model.page.TraineePageHomeModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TraineeClient {

    @GET("trainee/page/home/{traineeId}")
    Call<TraineePageHomeModel> getTraineePageHome(@Path("traineeId") String traineeId);

}