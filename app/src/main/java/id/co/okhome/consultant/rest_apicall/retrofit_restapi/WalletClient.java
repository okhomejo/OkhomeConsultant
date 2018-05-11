package id.co.okhome.consultant.rest_apicall.retrofit_restapi;

import id.co.okhome.consultant.model.wallet.ActivitiesModel;
import id.co.okhome.consultant.model.wallet.BalanceModel;
import id.co.okhome.consultant.model.wallet.TokenModel;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WalletClient {

    @POST("signon")
    Call<TokenModel> signOn(@Query("clientId") String clientId,
                            @Query("clientSecret") String clientSecret,
                            @Query("systrace") String systrace,
                            @Query("words") String words
    );

    @POST("custsourceoffunds")
    Call<BalanceModel> getBalance(@Query("clientId") String clientId,
                                  @Query("accessToken") String accessToken,
                                  @Query("accountId") String accountId,
                                  @Query("words") String words
    );
    @POST("custactivities")
    Call<ActivitiesModel> getActivities(@Query("clientId") String clientId,
                                        @Query("accessToken") String accessToken,
                                        @Query("accountId") String accountId,
                                        @Query("words") String words
    );
}


