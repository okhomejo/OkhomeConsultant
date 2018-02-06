package id.co.okhome.consultant.view.etc;

import android.content.Intent;
import android.os.Bundle;

import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.PhoneNumberGetter;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;

public class TestActivity extends OkHomeParentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        PhoneNumberGetter
                .with(this)
                .setPhoneVerificationCallback(new PhoneNumberGetter.PhoneVerificationCallback() {
                    @Override
                    public void onVerificationSuccess(String phone, String code) {
                        ToastUtil.showToast(phone + " " + code);
                    }
                })
                .show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhoneNumberGetter.with(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        PhoneNumberGetter.with(this).destroy(); //it is recommended to call it on destroy.
        super.onDestroy();
    }
}
