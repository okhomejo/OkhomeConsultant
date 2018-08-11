package id.co.okhome.consultant.view.activity.etc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.view.activity.agreements.TermAndPrivacyActivity;

public class AboutOkhomeActivity extends OkHomeParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_okhome);
        OkhomeUtil.setWhiteSystembar(this);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.actAboutOkhome_vbtnX)
    public void onClickCloseActivity() {
        finish();
    }

    @OnClick(R.id.actAboutOkhome_vbtnAgreements)
    public void onAgreement(View v){
        startActivity(new Intent(this, TermAndPrivacyActivity.class));
    }

    @OnClick(R.id.actAboutOkhome_vbtnSoftwareLicense)
    public void onSoftwareLicense(View v){
        startActivity(new Intent(this, TermAndPrivacyActivity.class));
    }

    @OnClick(R.id.actAboutOkhome_vbtnTermsAndConditions)
    public void onTermsAndCondition(View v){
        startActivity(new Intent(this, TermAndPrivacyActivity.class));
    }

}
