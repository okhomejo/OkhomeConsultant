package id.co.okhome.consultant.view.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.config.OkhomeRegistryKey;
import id.co.okhome.consultant.exception.OkhomeException;
import id.co.okhome.consultant.lib.JoSharedPreference;
import id.co.okhome.consultant.lib.PhoneNumberGetter;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.consultant.model.v2.AccountModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;

/**
 * Created by frizurd on 14/03/2018.
 */

public class ForgotLoginActivity extends OkHomeParentActivity {

    @BindView(R.id.actForgotLogin_llHidden)        LinearLayout hiddenContent;
    @BindView(R.id.actForgotLogin_tvEmail)         TextView tvEmail;
    @BindView(R.id.actForgotLogin_tvPhone)         TextView tvPhone;
    @BindView(R.id.actForgotLogin_etPassword1)     EditText firstPassword;
    @BindView(R.id.actForgotLogin_etPassword2)     EditText secondPassword;

    private int accountID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_login);

        ButterKnife.bind(this);
        init();
    }

    private void init() {
        hiddenContent.setAlpha(0.0f);
        hiddenContent.animate().translationY(hiddenContent.getHeight());
    }

    public void getEmailAddress(String phone, String code) {
        OkhomeRestApi.getAccountClient().getInfoByPhone(phone, code).enqueue(new RetrofitCallback<AccountModel>() {
            @Override
            public void onSuccess(AccountModel account) {
                accountID = Integer.parseInt(account.id);

                tvEmail.setText(account.email);
                tvPhone.setText(account.profile.phone);
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                super.onJodevError(jodevErrorModel);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hiddenContent.animate()
                        .translationY(0)
                        .alpha(1.0f)
                        .setDuration(1000);
            }
        });
    }

    private void checkBeforeChangingPassword(){
        final String firstPass  = firstPassword.getText().toString();
        final String secondPass = secondPassword.getText().toString();

        try {
            OkhomeUtil.chkException(!firstPass.equals(secondPass), "Passwords does not match.");
            OkhomeUtil.isValidPassword(firstPass);
        } catch(OkhomeException e) {
            OkhomeUtil.showToast(this, e.getMessage());
            return;
        }
        updatePassword(firstPass);
    }

    public void updatePassword(final String newPassword) {
        OkhomeRestApi.getAccountClient().updatePasswordType2(accountID, newPassword)
                .enqueue(new RetrofitCallback<String>() {
            @Override
            public void onSuccess(String account) {
                OkhomeUtil.showToast(ForgotLoginActivity.this, "Password has been changed!");
                ConsultantLoggedIn.reload(new RetrofitCallback<AccountModel>() {
                    @Override
                    public void onSuccess(AccountModel result) {
                        JoSharedPreference.with().push(OkhomeRegistryKey.PASSWORD_LAST_LOGIN, newPassword);
                    }
                });
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                super.onJodevError(jodevErrorModel);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        PhoneNumberGetter.with(this).destroy();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhoneNumberGetter.with(this).onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.actForgotLogin_vbtnX)
    public void onClickCloseActivity() {
        finish();
    }

    @OnClick(R.id.actForgotLogin_vbtnPhone)
    public void onClickPhone(){
        PhoneNumberGetter.with(this)
                .setPhoneVerificationCallback(new PhoneNumberGetter.PhoneVerificationCallback() {
                    @Override
                    public void onVerificationSuccess(String phone, String code) {
                        getEmailAddress(phone, code);
                    }
                })
                .show();
    }

    @OnClick(R.id.actForgotLogin_btnChange)
    public void onClickChangePassword() {
        checkBeforeChangingPassword();
    }
}
