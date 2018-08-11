package id.co.okhome.consultant.view.activity.agreements;

import android.os.Bundle;

import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;

public class TermAndPrivacyActivity extends OkHomeParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_privacy);
        OkhomeUtil.setWhiteSystembar(this);
    }
}
