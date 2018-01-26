package id.co.okhome.consultant.view.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.OkHomeParentActivity;
import id.co.okhome.consultant.view.main.MainActivity;

public class SigninActivity extends OkHomeParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.actSignIn_vbtnSignin)
    public void onSignin(View v){
        startActivity(new Intent(this, MainActivity.class));
    }
}
