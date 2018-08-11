package id.co.okhome.consultant.view.activity.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.config.OkhomeRegistryKey;
import id.co.okhome.consultant.lib.DelayedWorkRepeator;
import id.co.okhome.consultant.lib.JoSharedPreference;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.consultant.model.v2.AccountModel;
import id.co.okhome.consultant.view.activity.account.AuthActivity;

public class SplashActivity extends OkHomeParentActivity {

    @BindView(R.id.actSplash_vgWhiteLines)                      ViewGroup vgWhiteLines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        OkhomeUtil.setWhiteSystembar(this);
        ButterKnife.bind(this);

        beginLogoAnimation();

        chkAutoLogin();
    }


    //chk if there are saved email and password.
    private void chkAutoLogin(){
        final String emailLastLogin = JoSharedPreference.with().get(OkhomeRegistryKey.EMAIL_LAST_LOGIN);
        final String passwordLastLogin = JoSharedPreference.with().get(OkhomeRegistryKey.PASSWORD_LAST_LOGIN);

        if(emailLastLogin != null && passwordLastLogin != null){
            //
            DelayedWorkRepeator.with("1").setJob(new DelayedWorkRepeator.Job() {
                @Override
                public void work() {
                    autologin(emailLastLogin, passwordLastLogin);
                }
            }).setDelay(1400).work();

        }else{

            //delayed start
            DelayedWorkRepeator.with("2").setJob(new DelayedWorkRepeator.Job() {
                @Override
                public void work() {
                    gotoAuthActivity();
                }
            }).setDelay(2000).work();
        }
    }

    private void gotoAuthActivity(){
        JoSharedPreference.with().push(OkhomeRegistryKey.PASSWORD_LAST_LOGIN, null);
        startActivity(new Intent(SplashActivity.this, AuthActivity.class));
        finish();
    }

    //login
    private void autologin(final String email, final String password){
        ConsultantLoggedIn.login(this, email, password, new RetrofitCallback<AccountModel>() {
            @Override
            public void onSuccess(AccountModel account) {
                ConsultantLoggedIn.doCommonWorkAfterAcquiringAccount(account, new ConsultantLoggedIn.CommonLoginSuccessImpl(SplashActivity.this, false));
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                super.onJodevError(jodevErrorModel);


                gotoAuthActivity();
            }
        });
    }

    //begin logo animation
    private void beginLogoAnimation(){

        int delay = 0;
        for(int i = 0; i < vgWhiteLines.getChildCount(); i++){
            View vTarget = vgWhiteLines.getChildAt(i);

            delay += 50;
            vTarget.animate().translationX(-700f).alpha(0f).setDuration(500).setStartDelay(delay).start();
        }
    }
}
