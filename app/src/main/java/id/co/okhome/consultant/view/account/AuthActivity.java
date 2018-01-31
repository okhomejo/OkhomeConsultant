package id.co.okhome.consultant.view.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;

public class AuthActivity extends OkHomeParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        ButterKnife.bind(this);


        //<uses-permission android:name="android/.permission.READ_CONTACTS" />
//<uses-permission android:name="android.permission.READ_PROFILE" />
//        PermissionUtil.requestPermission(this, new String[]{
//                Manifest.permission.READ_PHONE_STATE});
//
//        TelephonyManager telMgr =
//                (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//
//        String deviceID = telMgr.getDeviceId();
//        String simSerialNumber = telMgr.getSimSerialNumber();

    }

    private void init(){

    }


    @OnClick(R.id.activityAuth_vbtnSignUp)
    public void onClickSignup(View v){
        startActivity(new Intent(this, SignupActivity.class));
    }

    @OnClick(R.id.activityAuth_vbtnSignIn)
    public void onClickSignin(View v){
        startActivity(new Intent(this, SigninActivity.class));
    }


}
