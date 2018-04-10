package id.co.okhome.consultant.lib.app;

import android.app.Activity;
import android.content.Intent;

import com.google.gson.Gson;

import java.util.Map;

import id.co.okhome.consultant.config.OkhomeRegistryKey;
import id.co.okhome.consultant.lib.JoSharedPreference;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.consultant.model.v2.AccountModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.etc.BlockedActivity;
import id.co.okhome.consultant.view.main.consultant.ConsultantMainActivity;
import id.co.okhome.consultant.view.main.trainee.TraineeMainActivity;
import id.co.okhome.consultant.view.userinfo.trainee.FillupUserInfoActivity;

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
        JoSharedPreference.with().push(OkhomeRegistryKey.PASSWORD_LAST_LOGIN, null);
    }

    /**login*/
    public final static void login(final Activity activity, final String email, final String pass, final RetrofitCallback<AccountModel> retrofitCallback){
        OkhomeRestApi.getAccountClient().login(email, pass).enqueue(new RetrofitCallback<AccountModel>() {
            @Override
            public void onSuccess(AccountModel result) {
                ConsultantLoggedIn.set(result);
                retrofitCallback.onSuccess(result);

                JoSharedPreference.with().push(OkhomeRegistryKey.EMAIL_LAST_LOGIN, email);
                JoSharedPreference.with().push(OkhomeRegistryKey.PASSWORD_LAST_LOGIN, pass);
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

    /**after acquiring account info*/
    public final static void doCommonWorkAfterAcquiringAccount(AccountModel account, final AfterAcquringAccountInfoListener afterAcquringAccount){

        afterAcquringAccount.beginWork();

        if (account.blocked != null) {
            afterAcquringAccount.onBlocked();
        }

        // or not, have to check consultant's type
        else {

            if(account.type.equals("C")){
                //go to consultant main activity
                afterAcquringAccount.onConsultant();
            }else{
                //check trainee's status by admin.

                if(account.trainee.approveYN.equals("Y")){
                    afterAcquringAccount.onTrainee();
                }else{
                    afterAcquringAccount.onTraineeNotApproved();
                }
            }
        }

        afterAcquringAccount.afterWork();
    }

    public interface AfterAcquringAccountInfoListener{
        void beginWork();
        void onBlocked();
        void onConsultant();
        void onTraineeNotApproved();
        void onTrainee();
        void afterWork();
    }

    public static class CommonLoginSuccessImpl implements AfterAcquringAccountInfoListener{

        OkHomeParentActivity activity;
        boolean finishAllActivity = false;

        public CommonLoginSuccessImpl(OkHomeParentActivity activity, boolean finishAllActivity) {
            this.activity = activity;
            this.finishAllActivity = finishAllActivity;
        }

        @Override
        public void beginWork() {
            if(finishAllActivity){
                OkHomeParentActivity.finishAllActivities();
            }
        }

        @Override
        public void onBlocked() {
            activity.startActivity(new Intent(activity, BlockedActivity.class));
        }

        @Override
        public void onConsultant() {
            activity.startActivity(new Intent(activity, ConsultantMainActivity.class));
        }

        @Override
        public void onTraineeNotApproved() {
            activity.startActivity(new Intent(activity, FillupUserInfoActivity.class));
        }

        @Override
        public void onTrainee() {
            activity.startActivity(new Intent(activity, TraineeMainActivity.class));
        }

        @Override
        public void afterWork() {
            if(!finishAllActivity){
                activity.finish();
            }
        }
    }
}
