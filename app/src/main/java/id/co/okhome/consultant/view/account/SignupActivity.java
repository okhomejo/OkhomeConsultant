package id.co.okhome.consultant.view.account;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

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
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.AccountClient;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.common.dialog.PhoneVerificationDialog;
import id.co.okhome.consultant.view.main.trainee.TraineeMainActivity;
import id.co.okhome.consultant.view.userinfo.trainee.FillupUserInfoActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends OkHomeParentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.actSignup_etEmail)           EditText etEmail;
    @BindView(R.id.actSignup_etPassword)        EditText etPassword;
    @BindView(R.id.actSignup_etPasswordOneMore) EditText etPasswordOneMore;
    @BindView(R.id.actSignup_tvPhone)           TextView tvPhone;
    @BindView(R.id.actSignup_vLoading)          View vLoading;
    @BindView(R.id.actSignup_vbtnSignup)        View vBtnSignUp;

    private static final int RESOLVE_HINT = 9;
    private GoogleApiClient mGoogleApiClient;
    private PhoneVerificationDialog phoneVerificationDialog;

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

//        try {
//            OkhomeUtil.chkException(!OkhomeUtil.isValidEmail(email), "Check your email.");
//            OkhomeUtil.isValidPassword(password);
//            OkhomeUtil.chkException(!password.equals(passwordOneMore), "Passwords do not match.");
//
//        } catch(OkhomeException e) {
//            OkhomeUtil.showToast(this, e.getMessage());
//            return;
//        }
//
//        if (phoneVerificationDialog != null && phoneVerificationDialog.isVerified()) {
//            signup(email, password);
//        } else {
//            Toast.makeText(this, "Please verify your phone number.", Toast.LENGTH_SHORT).show();
//        }


        // For testing purpose
        OkHomeParentActivity.finishAllActivities();
        startActivity(new Intent(this, FillupUserInfoActivity.class));
    }

    private void signup(final String email, String password) {

        showLoading(true);
        OkhomeRestApi.getAccountClient().signup(email, password).enqueue(new RetrofitCallback<Integer>() {

            @Override
            public void onSuccess(Integer result) {
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
    private void onLoginSuccess(Integer result){

        ConsultantLoggedIn.set(new ConsultantModel());

        OkHomeParentActivity.finishAllActivities();
        startActivity(new Intent(this, FillupUserInfoActivity.class));
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

    // Request phone number connected with Google account
    public void requestPhoneNumber() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(
                mGoogleApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(),
                    RESOLVE_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    // Give phone number to Dialog window
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        phoneVerificationDialog = new PhoneVerificationDialog(this);
        phoneVerificationDialog.show();

        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                phoneVerificationDialog.setPhoneNumber(credential.getId());
                phoneVerificationDialog.onSendVerificationCode();
            }
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
        requestPhoneNumber();
    }

}
