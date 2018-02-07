package id.co.okhome.consultant.view.etc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.ConsultantModel;
import id.co.okhome.consultant.view.userinfo.trainee.FillupUserInfoActivity;

public class SplashActivity extends OkHomeParentActivity {

    @BindView(R.id.actSplash_vgWhiteLines)                      ViewGroup vgWhiteLines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
        beginLogoAnimation();

        login();
    }

    //login
    private void login(){
        final String email = "hello@gmail.com";
        final String pass = "1234567";

        ConsultantLoggedIn.login(email, pass, new RetrofitCallback<ConsultantModel>() {
            @Override
            public void onSuccess(ConsultantModel result) {
                //consulantant model is automatically stored at ConsultantLoggedIn
                next();
            }
        });
    }

    private void next(){
        new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
//                startActivity(new Intent(SplashActivity.this, AuthActivity.class));

                startActivity(new Intent(SplashActivity.this, FillupUserInfoActivity.class));
                SplashActivity.this.finish();
            }
        }.sendEmptyMessageDelayed(0, 2000);
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
