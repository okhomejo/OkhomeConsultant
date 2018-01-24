package id.co.okhome.consultant.view.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.OkHomeParentActivity;
import id.co.okhome.consultant.view.userinfo.trainee.FillupUserInfoActivity;

public class SignupActivity extends OkHomeParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.actSignUp_vbtnSignup)
    public void onSignUp(View v){
        startActivity(new Intent(this, FillupUserInfoActivity.class));
        finish();
    }


}
