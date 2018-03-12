package id.co.okhome.consultant.view.etc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.ConsultantModel;
import id.co.okhome.consultant.view.account.AuthActivity;
import id.co.okhome.consultant.view.userinfo.trainee.FillupUserInfoActivity;

public class SplashActivity extends OkHomeParentActivity {

    @BindView(R.id.actSplash_vgWhiteLines)                      ViewGroup vgWhiteLines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
        beginLogoAnimation();

        chkCachedAccount();
    }

    private void chkCachedAccount(){
        if(ConsultantLoggedIn.hasSavedData()){

//            ConsultantModel consultantModel = ConsultantLoggedIn.get();
            login("hello@gmail.com", "1234567");
        }else{
            startActivity(new Intent(this, AuthActivity.class));
            finish();
            //go to login activity.
        }
    }

    //login
    private void login(final String email, final String password){
        ConsultantLoggedIn.login(this, email, password, new RetrofitCallback<ConsultantModel>() {
            @Override
            public void onSuccess(ConsultantModel result) {
                gotoNext();
            }
        });
    }

    private void gotoNext(){
        startActivity(new Intent(SplashActivity.this, FillupUserInfoActivity.class));
        SplashActivity.this.finish();
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
