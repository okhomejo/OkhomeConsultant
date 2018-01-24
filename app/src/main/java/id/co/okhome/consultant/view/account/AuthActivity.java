package id.co.okhome.consultant.view.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.OkHomeParentActivity;
import id.co.okhome.consultant.view.userinfo.trainee.UpdateUserDocumentActivity;

public class AuthActivity extends OkHomeParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        ButterKnife.bind(this);
        startActivity(new Intent(this, UpdateUserDocumentActivity.class));


        //android:windowLightStatusBar

    }

    private void init(){

    }

    @OnClick(R.id.activityAuth_vbtnSignUp)
    public void signup(View v){
        startActivity(new Intent(this, SignupActivity.class));
    }

    @OnClick(R.id.activityAuth_vbtnSignIn)
    public void signin(View v){
        startActivity(new Intent(this, SigninActivity.class));
    }


}
