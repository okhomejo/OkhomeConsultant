package id.co.okhome.consultant.view.activity.cleaning_review;

import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;

public class CleaningReviewListActivity extends OkHomeParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_review_list);

        ButterKnife.bind(this);
        OkhomeUtil.setWhiteSystembar(this);
    }

    @OnClick(R.id.actAccount_vbtnX)
    public void onButtonGoBack() {
        finish();
    }
}
