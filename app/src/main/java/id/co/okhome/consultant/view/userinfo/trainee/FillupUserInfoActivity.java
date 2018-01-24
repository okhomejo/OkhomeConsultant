package id.co.okhome.consultant.view.userinfo.trainee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.OkHomeParentActivity;

public class FillupUserInfoActivity extends OkHomeParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fillup_userinfo);

        ButterKnife.bind(this);
    }


    @OnClick({R.id.actFillUpUserInfo_vbtnKartuTandaPerduduk, R.id.actFillUpUserInfo_tvKartuTandaPerduduk})
    public void onKartuTandaPerdudk(View v){
        startActivity(new Intent(this, UpdateUserDocumentActivity.class));
    }

}
