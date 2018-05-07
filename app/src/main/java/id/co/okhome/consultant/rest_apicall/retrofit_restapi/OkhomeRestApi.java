package id.co.okhome.consultant.rest_apicall.retrofit_restapi;

import java.util.HashMap;
import java.util.Map;

import id.co.okhome.consultant.lib.retrofit.RetrofitFactory;


/**
 * Created by josongmin on 2016-02-18.
 */

//각 retrofit클라이언트들을 싱글톤으로 관리함.
public class OkhomeRestApi {

    private static Map<String, Object> mapClient = new HashMap<>();
    private static Map<String, Object> mapDokuClient = new HashMap<>();

    public final static <T> T getInstance(final Class<T> service){
        final String key = service.toString();
        T client = (T)mapClient.get(key);
        if(client == null){
            client = RetrofitFactory.getRestClient(service);
            mapClient.put(key, client);
        }
        return client;
    }

    public final static <T> T getDokuInstance(final Class<T> service){
        final String key = service.toString();
        T client = (T)mapDokuClient.get(key);
        if(client == null){
            client = RetrofitFactory.getDokuRestClient(service);
            mapDokuClient.put(key, client);
        }
        return client;
    }

    /**get common restclient*/
    public final static CommonClient getCommonClient(){
        return OkhomeRestApi.getInstance(CommonClient.class);
    }

    /**get consultant api client*/
    public final static AccountClient getAccountClient(){
        return OkhomeRestApi.getInstance(AccountClient.class);
    }
    
    public final static ProfileClient getProfileClient(){
        return OkhomeRestApi.getInstance(ProfileClient.class);
    }

    public final static ValidationClient getValidationClient(){
        return OkhomeRestApi.getInstance(ValidationClient.class);
    }

    public final static TrainingForTraineeClient getTrainingForTraineeClient(){
        return OkhomeRestApi.getInstance(TrainingForTraineeClient.class);
    }

    public final static TraineeClient getTraineeClient(){
        return OkhomeRestApi.getInstance(TraineeClient.class);
    }

    public final static ConsultantClient getConsultantClient() {
        return OkhomeRestApi.getInstance(ConsultantClient.class);
    }

    public final static ConsultantSalaryClient getConsultantSalaryClient() {
        return OkhomeRestApi.getInstance(ConsultantSalaryClient.class);
    }

    public final static CleaningTaskClient getCleaningTaskClient(){
        return OkhomeRestApi.getInstance(CleaningTaskClient.class);
    }

    public final static CleaningReviewClient getCleaningReviewClient(){
        return OkhomeRestApi.getInstance(CleaningReviewClient.class);
    }

    public final static WalletClient getWalletClient(){
        return OkhomeRestApi.getDokuInstance(WalletClient.class);
    }
}
