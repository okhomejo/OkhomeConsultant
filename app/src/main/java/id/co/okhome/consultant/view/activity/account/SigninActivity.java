package id.co.okhome.consultant.view.activity.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
import id.co.okhome.consultant.view.dialog.ShowEmailAndOptChangePasswordDialog;

public class SigninActivity extends OkHomeParentActivity {

    @BindView(R.id.actSignIn_etEmail)           EditText etEmail;
    @BindView(R.id.actSignIn_etPassword)        EditText etPassword;
    @BindView(R.id.actSignIn_vLoading)          View vLoading;
    @BindView(R.id.actSignIn_vbtnSignin)        View vBtnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        OkhomeUtil.setWhiteSystembar(this);
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        showLoading(false);

        //set last loggedin email
        String savedEmail = JoSharedPreference.with().get(OkhomeRegistryKey.EMAIL_LAST_LOGIN);
        etEmail.setText(savedEmail);
    }

    private void checkBeforeSignup(){

        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

        try {
            OkhomeUtil.chkException(!OkhomeUtil.isValidEmail(email), "Check your email.");
            OkhomeUtil.isValidPassword(password);

        } catch(OkhomeException e) {
            OkhomeUtil.showToast(this, e.getMessage());
            return;
        }

        login(email, password);

//
//        OkHomeParentActivity.finishAllActivities();
//        startActivity(new Intent(this, TraineeMainActivity.class));
    }

    private void login(final String email, final String password){

        showLoading(true);
        ConsultantLoggedIn.login(this, email, password, new RetrofitCallback<AccountModel>() {
            @Override
            public void onSuccess(AccountModel account) {
                ConsultantLoggedIn.doCommonWorkAfterAcquiringAccount(account, new ConsultantLoggedIn.CommonLoginSuccessImpl(SigninActivity.this, false));
                finishAllActivities();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                showLoading(false);
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
//                super.onJodevError(jodevErrorModel);
                ToastUtil.showToast(jodevErrorModel.message);
            }
        });
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

    //팝업 보여주자
    private void showEmailDialog(AccountModel account){
        new ShowEmailAndOptChangePasswordDialog(this, account).show();
    }

    //폰번호로 사용자 정보 조회
    private void getInfoByPhone(String phone, String code){
        final ProgressDialog p = OkhomeUtil.showLoadingDialog(this);
        OkhomeRestApi.getAccountClient().getInfoByPhone(phone, code).enqueue(new RetrofitCallback<AccountModel>() {
            @Override
            public void onSuccess(AccountModel account) {
                showEmailDialog(account);
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                super.onJodevError(jodevErrorModel);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                p.dismiss();

            }
        });

    }


    @OnClick(R.id.actSignIn_vbtnClose)
    public void onCloseWindow() {
        finish();
    }

    @OnClick(R.id.actSignIn_vbtnSignin)
    public void onSignin(View v){
        checkBeforeSignup();
    }

    @OnClick(R.id.actSignIn_vbtnForgotInfo)
    public void onForgotInfoClick() {

        PhoneNumberGetter.with(this).destroy();
        PhoneNumberGetter.with(this)
                .setPhoneVerificationCallback(new PhoneNumberGetter.PhoneVerificationCallback() {
                    @Override
                    public void onVerificationSuccess(String phone, String code) {
                        //
                        getInfoByPhone(phone, code);
                    }
                })
                .show();

//        startActivity(new Intent(this, ForgotLoginActivity.class));

//        BottomOptionDialog dialog = new BottomOptionDialog(this)
//                .setArrItems("Find Email by phone", "Reset password by phone")
//                .setItemClickListener(new StringHolder.ItemClickListener() {
//                    @Override
//                    public void onItemClick(Dialog dialog, int pos, String value, String tag) {
//                        Intent i = new Intent(SigninActivity.this, ForgotLoginActivity.class);
//                        i.putExtra("actTitle", value);
//                        startActivity(i);
//                        dialog.dismiss();
//                    }
//                });
//        Window window = dialog.getWindow();
//        WindowManager.LayoutParams wlp = window.getAttributes();
//        wlp.gravity = Gravity.BOTTOM;
//        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(wlp);
//
//        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhoneNumberGetter.with(this).onActivityResult(requestCode, resultCode, data);
    }
}
