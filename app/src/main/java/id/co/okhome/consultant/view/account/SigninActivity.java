package id.co.okhome.consultant.view.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.exception.OkhomeException;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.consultant.model.ConsultantModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.main.trainee.TraineeMainActivity;

public class SigninActivity extends OkHomeParentActivity {

    @BindView(R.id.actSignIn_etEmail)           EditText etEmail;
    @BindView(R.id.actSignIn_etPassword)        EditText etPassword;
    @BindView(R.id.actSignIn_vLoading)          View vLoading;
    @BindView(R.id.actSignIn_vbtnSignin)        View vBtnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        showLoading(false);
    }

    private void checkBeforeSignup(){

        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

        /**Note for Fritz :
         * To validate parameters, Use Try~Catch like below before calling APIs.
         * */
        try {
            OkhomeUtil.chkException(!OkhomeUtil.isValidEmail(email), "Check your email.");
            OkhomeUtil.isValidPassword(password);

        } catch(OkhomeException e) {
            OkhomeUtil.showToast(this, e.getMessage());
            return;
        }

        login(email, password);
    }

    private void login(final String email, final String password){

        /**Note for Fritz :
         When calling API, Use it like below.
         RetrofitCallback class is i made.
         The order of work is as follows.

         1. onFinish() is called first
         2-1.if success, onSuccess(T t) is called
         2-2.if failed, onJodevError(ErrorModel e) is called

         note) By IDE, only onSuccess(T obj) is made by default. If necessary, override the two methods(onSuccess, onJodevError).
         note) onFailure and onResponse are raw method of upper class. We don't use them.
         */

        showLoading(true);
        OkhomeRestApi.getAccountClient().signin(email, password).enqueue(new RetrofitCallback<ConsultantModel>() {
            /**Note for Fritz :
             * Successfully getting result from our API Server, it is called.
             * */
            @Override
            public void onSuccess(ConsultantModel result) {
                onLoginSuccess(result);
            }

            /**Note for Fritz :
             * When error take place, below bethod is called back.
             * jodevErrorModel.code is error code.
             * jodevErrorModel.msg is error message by server or application exception error.
             * jodevErrorModel.obj is error object. but usually we don't need use it.
            */
            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                super.onJodevError(jodevErrorModel);
                ToastUtil.showToast(jodevErrorModel.message);
            }

            /**Note for Fritz :
             * Whatever result is success or failed, It is called back first.
             * So, usually I place the code for toggling loading visivlity.
             * */
            @Override
            public void onFinish() {
                super.onFinish();
                showLoading(false);
            }
        });
    }

    // on login success
    private void onLoginSuccess(ConsultantModel result){
        /**Note for Fritz :
         * To access user information simply, We need to store user data like Cookie of web.
         * Check ConsultantLoggedIn class.
         * */

        ConsultantLoggedIn.set(result);

        /**Note for Fritz :
         * We need to check consultants's type. then behave differently depending on it.
         * Actually, We are still middle of thinking about details.
         * So go to TraineeMainActivity for now.
         * */

        OkHomeParentActivity.finishAllActivities();
        startActivity(new Intent(this, TraineeMainActivity.class));

    }

    //Loading toggle
    private void showLoading(boolean on){
        if(on){
            vBtnSignIn.animate().translationX(-100).alpha(0f).setDuration(300).start();
            vLoading.animate().translationX(0).alpha(1f).setDuration(300).start();
        }else{
            vBtnSignIn.animate().translationX(0).alpha(1f).setDuration(300).start();
            vLoading.animate().translationX(100).alpha(0f).setDuration(300).start();
        }
    }

    /*deprecated*/
//    private void loginProcessWithRetrofit(final String email, String password){
//
//
//        AccountClient mApiService = OkhomeRestApi.getAccountClient();
//
//        Call<ConsultantModel> mService = mApiService.signin(email, password);
//        mService.enqueue(new Callback<ConsultantModel>() {
//            @Override
//            public void onResponse(Call<ConsultantModel> call, Response<ConsultantModel> response) {
//
//                if(response.isSuccessful()){
//
//                    int consultantId = Integer.parseInt(response.body().id);
//
//                    Toast.makeText(SigninActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
//
//                    Intent loginIntent = new Intent(SigninActivity.this, TraineeMainActivity.class);
//                    loginIntent.putExtra("EMAIL", email);
//                    loginIntent.putExtra("ID", consultantId);
//                    startActivity(loginIntent);
//                } else {
//
//                    Toast.makeText(SigninActivity.this, "Login fail", Toast.LENGTH_SHORT).show();
//
//                    etPassword.setText("");
//
//                    Log.e("Error Code", String.valueOf(response.code()));
//                    Log.e("Error Body", response.errorBody().toString());
//                }
//            }
//            @Override
//            public void onFailure(Call<ConsultantModel> call, Throwable t) {
//                call.cancel();
//                Toast.makeText(SigninActivity.this, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();
//            }
//        });
//    }

    @OnClick(R.id.actSignIn_vbtnClose)
    public void onCloseWindow() {
        finish();
    }

    @OnClick(R.id.actSignIn_vbtnSignin)
    public void onSignin(View v){

        checkBeforeSignup();

//        startActivity(new Intent(this, TraineeMainActivity.class));
    }
}
