package id.co.okhome.consultant.rest_apicall.retrofit_restapi;

import java.util.List;

import id.co.okhome.consultant.model.training.TrainingModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TrainingForTraineeClient {

    @GET("training/trainee/{traineeId}")
    Call<List<TrainingModel>> getTrainingList(@Path("traineeId") String traineeId);

    @GET("training/trainee/{traineeId}")
    Call<TrainingModel> getTrainingDetail(@Path("traineeId") String traineeId, @Query("trainingId") String trainingId);
}