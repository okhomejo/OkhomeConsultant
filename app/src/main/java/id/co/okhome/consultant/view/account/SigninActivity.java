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

//        try {
//            OkhomeUtil.chkException(!OkhomeUtil.isValidEmail(email), "Check your email.");
//            OkhomeUtil.isValidPassword(password);
//
//        } catch(OkhomeException e) {
//            OkhomeUtil.showToast(this, e.getMessage());
//            return;
//        }
//
//        login(email, password);


        OkHomeParentActivity.finishAllActivities();
        startActivity(new Intent(this, TraineeMainActivity.class));
    }

    private void login(final String email, final String password){

        showLoading(true);
        OkhomeRestApi.getAccountClient().signin(email, password).enqueue(new RetrofitCallback<ConsultantModel>() {

            @Override
            public void onSuccess(ConsultantModel result) {
                onLoginSuccess(result);
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                super.onJodevError(jodevErrorModel);
                ToastUtil.showToast(jodevErrorModel.message);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                showLoading(false);
            }
        });
    }

    // on login success
    private void onLoginSuccess(ConsultantModel result){

        ConsultantLoggedIn.set(result);

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

    @OnClick(R.id.actSignIn_vbtnClose)
    public void onCloseWindow() {
        finish();
    }

    @OnClick(R.id.actSignIn_vbtnSignin)
    public void onSignin(View v){
        checkBeforeSignup();
    }
}
