package id.co.okhome.consultant.lib.app;

import android.app.Activity;
import com.google.gson.Gson;
import java.util.Map;
import id.co.okhome.consultant.config.OkhomeRegistryKey;
import id.co.okhome.consultant.lib.JoSharedPreference;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.consultant.model.v2.AccountModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;

/**
 * Created by jo on 2018-01-28.
 */

public class ConsultantLoggedIn {
    /**set consultant information on sdcard*/
    private static AccountModel consultant;

    public final static void set(AccountModel consultant){
        ConsultantLoggedIn.consultant = consultant;
        JoSharedPreference.with().push(OkhomeRegistryKey.LOGIN_CONSULTANT, consultant);
    }

    /**get consultant information from sdcard*/
    public final static AccountModel get(){
        if(consultant == null){
            consultant = JoSharedPreference.with().get(OkhomeRegistryKey.LOGIN_CONSULTANT);
        }
        return consultant;
    }

    public final static String id(){
        if(consultant == null){
            consultant = JoSharedPreference.with().get(OkhomeRegistryKey.LOGIN_CONSULTANT);
        }
        return consultant.id;
    }


    /**if there is saved consultant data logged-in, return true.*/
    public static boolean hasSavedData(){
        return JoSharedPreference.with().get(OkhomeRegistryKey.LOGIN_CONSULTANT) == null ? false : true;
    }

    /**clear all data. It may be called on logout*/
    public final static void clear(){
        JoSharedPreference.with().push(OkhomeRegistryKey.LOGIN_CONSULTANT, null);
    }

    /**login*/
    public final static void login(final Activity activity, final String email, final String pass, final RetrofitCallback<AccountModel> retrofitCallback){
        OkhomeRestApi.getAccountClient().login(email, pass).enqueue(new RetrofitCallback<AccountModel>() {
            @Override
            public void onSuccess(AccountModel result) {
                ConsultantLoggedIn.set(result);
                retrofitCallback.onSuccess(result);

                if(result.blocked != null){
                    //블럭화면으로
                }else if(!result.trainee.approveYN.equals("N")){
//                }else if(!result.accountStatus.isDoumentCompleted){
                    //문서 완료 안됬으면
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                retrofitCallback.onFinish();
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                super.onJodevError(jodevErrorModel);
                retrofitCallback.onJodevError(jodevErrorModel);
            }
        });
    }

    /**refresh consultant data*/
    public final static void reload(final RetrofitCallback<AccountModel> retrofitCallback){
        String consultantId = get().id;
        OkhomeRestApi.getAccountClient().getInfo(Integer.parseInt(consultantId)).enqueue(
                new RetrofitCallback<AccountModel>() {

                    @Override
                    public void onSuccess(AccountModel result) {
                        ConsultantLoggedIn.set(result);
                        retrofitCallback.onSuccess(result);
                    }

                    @Override
                    public void onJodevError(ErrorModel jodevErrorModel) {
                        super.onJodevError(jodevErrorModel);
                        ToastUtil.showToast(jodevErrorModel.message);
                        retrofitCallback.onJodevError(jodevErrorModel);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        retrofitCallback.onFinish();
                    }
                });
    }

    /**update user profile*/
    public final static void updateUserInfo(Map param, RetrofitCallback<String> callback){
        String jsonParam = new Gson().toJson(param);
        OkhomeRestApi.getProfileClient().update(get().id, jsonParam).enqueue(callback);
    }
}
