package id.co.okhome.consultant.view.account;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.config.OkhomeRegistryKey;
import id.co.okhome.consultant.exception.OkhomeException;
import id.co.okhome.consultant.lib.JoSharedPreference;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.consultant.model.v2.AccountModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.account.reset.ForgotLoginActivity;
import id.co.okhome.consultant.view.common.dialog.BottomOptionDialog;
import id.co.okhome.consultant.view.etc.BlockedActivity;
import id.co.okhome.consultant.view.main.trainee.TraineeMainActivity;
import id.co.okhome.consultant.view.userinfo.trainee.FillupUserInfoActivity;
import id.co.okhome.consultant.view.viewholder.StringHolder;

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
        OkhomeRestApi.getAccountClient().login(email, password).enqueue(new RetrofitCallback<AccountModel>() {

            @Override
            public void onSuccess(AccountModel result) {
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
    private void onLoginSuccess(AccountModel account){

        ConsultantLoggedIn.set(account);
        OkHomeParentActivity.finishAllActivities();

        if(account.blocked != null) {
            startActivity(new Intent(this, BlockedActivity.class));
        } else if (account.trainee.approveYN.equals("N")) {
            startActivity(new Intent(this, FillupUserInfoActivity.class));
        } else {
            startActivity(new Intent(this, TraineeMainActivity.class));
        }
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

    @OnClick(R.id.actSignIn_vbtnForgotInfo)
    public void onForgotInfoClick() {
        BottomOptionDialog dialog = new BottomOptionDialog(this)
                .setArrItems("Find Email by phone", "Reset password by phone")
                .setItemClickListener(new StringHolder.ItemClickListener() {
                    @Override
                    public void onItemClick(Dialog dialog, int pos, String value, String tag) {
                        Intent i = new Intent(SigninActivity.this, ForgotLoginActivity.class);
                        i.putExtra("actTitle", value);
                        startActivity(i);
                        dialog.dismiss();
                    }
                });
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);

        dialog.show();
    }
}
