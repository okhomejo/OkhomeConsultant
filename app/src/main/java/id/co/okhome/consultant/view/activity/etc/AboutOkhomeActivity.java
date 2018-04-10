package id.co.okhome.consultant.view.activity.etc;

import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;

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
}
