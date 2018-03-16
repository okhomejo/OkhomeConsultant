package id.co.okhome.consultant.view.userinfo;

import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;

public class AccountActivity extends OkHomeParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ButterKnife.bind(this);
    }

    private void init(){

    }

    @OnClick(R.id.actAboutOkhome_vbtnX)
    public void onX(){
        finish();
    }
}
