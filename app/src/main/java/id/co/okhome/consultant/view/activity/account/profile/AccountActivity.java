package id.co.okhome.consultant.view.activity.account.profile;

import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;

public class AccountActivity extends OkHomeParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ButterKnife.bind(this);
        OkhomeUtil.setWhiteSystembar(this);

        int i = 0;
    }

    private void init(){

    }

    @OnClick(R.id.actAboutOkhome_vbtnX)
    public void onX(){
        finish();
    }
}
