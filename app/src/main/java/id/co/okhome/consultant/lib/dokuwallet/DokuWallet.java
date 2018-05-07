package id.co.okhome.consultant.lib.dokuwallet;

import id.co.okhome.consultant.config.OkhomeConstant;
import id.co.okhome.consultant.lib.EncryptionUtils;
import id.co.okhome.consultant.model.wallet.ActivitiesModel;
import id.co.okhome.consultant.model.wallet.BalanceModel;
import id.co.okhome.consultant.model.wallet.TokenModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import retrofit2.Call;

public class DokuWallet {
    public static final Call<TokenModel> signOn(String systrace){
        return OkhomeRestApi.getWalletClient().signOn(
                OkhomeConstant.CLIENT_ID,
                OkhomeConstant.CLIENT_SECRET,
                systrace,
                EncryptionUtils.hmacSha1(OkhomeConstant.CLIENT_ID+OkhomeConstant.SHARED_KEY+systrace)
        );
    }

    public static final Call<BalanceModel> getBalance(String accessToken, String systrace, String accountId){
        return OkhomeRestApi.getWalletClient().getBalance(
                OkhomeConstant.CLIENT_ID,
                accessToken,
                accountId,
                EncryptionUtils.hmacSha1(OkhomeConstant.CLIENT_ID+systrace+OkhomeConstant.SHARED_KEY+accountId)
        );
    }

    public static final Call<ActivitiesModel> getActivities(String accessToken, String systrace, String accountId){
        return OkhomeRestApi.getWalletClient().getActivities(
                OkhomeConstant.CLIENT_ID,
                accessToken,
                accountId,
                EncryptionUtils.hmacSha1(OkhomeConstant.CLIENT_ID+systrace+OkhomeConstant.SHARED_KEY+accountId)
        );
    }
}