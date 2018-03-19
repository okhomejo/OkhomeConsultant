package id.co.okhome.consultant.view.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.config.OkhomeRegistryKey;
import id.co.okhome.consultant.exception.OkhomeException;
import id.co.okhome.consultant.lib.JoSharedPreference;
import id.co.okhome.consultant.lib.PhoneNumberGetter;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.consultant.model.v2.AccountModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;

public class SignupActivity extends OkHomeParentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.actSignup_etEmail)           EditText etEmail;
    @BindView(R.id.actSignup_etPassword)        EditText etPassword;
    @BindView(R.id.actSignup_etPasswordOneMore) EditText etPasswordOneMore;
    @BindView(R.id.actSignup_tvPhone)           TextView tvPhone;
    @BindView(R.id.actSignup_vLoading)          View vLoading;
    @BindView(R.id.actSignup_vbtnSignup)        View vBtnSignUp;

    private GoogleApiClient mGoogleApiClient;
    private String phoneNum, phoneCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        buildGoogleApiClient();
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        showLoading(false);
    }

    //check exception before signup
    private void checkBeforeSignup(){
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        final String passwordOneMore = etPasswordOneMore.getText().toString();
        final String phone = tvPhone.getText().toString();

        try {
            OkhomeUtil.chkException(!OkhomeUtil.isValidEmail(email), "Check your email.");
            OkhomeUtil.isValidPassword(password);
            OkhomeUtil.chkException(!password.equals(passwordOneMore), "Passwords do not match.");
            OkhomeUtil.chkException(phone.equals(""), "Please verify your phone number.");

        } catch(OkhomeException e) {
            OkhomeUtil.showToast(this, e.getMessage());
            return;
        }

        signup(email, password);

        // Skip login check -- Only for testing purpose
//        OkHomeParentActivity.finishAllActivities();
//        startActivity(new Intent(this, FillupUserInfoActivity.class));
    }

    private void signup(final String email, final String password) {
        showLoading(true);

        OkhomeRestApi.getAccountClient().signup(email, password, "EMAIL").enqueue(new RetrofitCallback<AccountModel>() {
            @Override
            public void onSuccess(AccountModel account) {
                savePhoneNumber(account.id);
                onSignUpSuccess(account, email, password);
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
    private void onSignUpSuccess(AccountModel account, String email, String password){
        ConsultantLoggedIn.set(account);
        JoSharedPreference.with().push(OkhomeRegistryKey.EMAIL_LAST_LOGIN, email);
        JoSharedPreference.with().push(OkhomeRegistryKey.PASSWORD_LAST_LOGIN, password);

        ConsultantLoggedIn.doCommonWorkAfterAcquiringAccount(account, new ConsultantLoggedIn.CommonLoginSuccessImpl(this, true));
    }

    private void savePhoneNumber(final String id) {
        showLoading(true);
        OkhomeRestApi.getValidationClient().updatePhoneNumber(id, phoneNum, phoneCode).enqueue(new RetrofitCallback<String>() {
            @Override
            public void onSuccess(String result) {
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                super.onJodevError(jodevErrorModel);
                if (Objects.equals(jodevErrorModel.code, "-102")) {
                    tvPhone.setError("Make sure the phone number is unique.");
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                showLoading(false);
            }
        });
    }

    //Loading toggle
    private void showLoading(boolean on){
        if(on){
            vBtnSignUp.animate().translationX(-100).alpha(0f).setDuration(300).start();
            vLoading.animate().translationX(0).alpha(1f).setDuration(300).start();
        }else{
            vBtnSignUp.animate().translationX(0).alpha(1f).setDuration(300).start();
            vLoading.animate().translationX(100).alpha(0f).setDuration(300).start();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.CREDENTIALS_API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhoneNumberGetter.with(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        PhoneNumberGetter.with(this).destroy();
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @OnClick(R.id.actSignup_vbtnSignup)
    public void onClickSignUp(){
        checkBeforeSignup();
    }

    @OnClick(R.id.actSignup_vbtnGoogle)
    public void onClickGoogle(){

    }

    @OnClick(R.id.actSignup_vbtnClose)
    public void onCloseActivity() {
        finish();
    }

    @OnClick(R.id.actSignup_vbtnTermsAndConditions)
    public void onClickTermsAndConditions(){

    }

    @OnClick(R.id.actSignup_tvPhone)
    public void onClickPhone(){
        PhoneNumberGetter.with(this)
                .setPhoneVerificationCallback(new PhoneNumberGetter.PhoneVerificationCallback() {
                    @Override
                    public void onVerificationSuccess(String phone, String code) {
                        tvPhone.setText(phone);
                        phoneNum = phone;
                        phoneCode = code;
                        vBtnSignUp.requestFocus();
                    }
                })
                .show();
    }
}