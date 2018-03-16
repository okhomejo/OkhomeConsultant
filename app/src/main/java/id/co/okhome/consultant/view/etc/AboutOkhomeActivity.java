package id.co.okhome.consultant.view.etc;

import android.os.Bundle;

import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;

public class AboutOkhomeActivity extends OkHomeParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_okhome);
        OkhomeUtil.setWhiteSystembar(this);
    }
}