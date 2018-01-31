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
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.view.account.AuthActivity;

public class SplashActivity extends OkHomeParentActivity {

    @BindView(R.id.actSplash_vgWhiteLines)                      ViewGroup vgWhiteLines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
        beginLogoAnimation();

        next();
    }

    private void next(){
        new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                SplashActivity.this.finish();
                startActivity(new Intent(SplashActivity.this, AuthActivity.class));

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
