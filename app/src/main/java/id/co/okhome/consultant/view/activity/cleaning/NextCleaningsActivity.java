package id.co.okhome.consultant.view.activity.cleaning;

import android.os.Bundle;

import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;

public class NextCleaningsActivity extends OkHomeParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nextcleanings);
        ButterKnife.bind(this);

        OkhomeUtil.setWhiteSystembar(this);
    }
}
