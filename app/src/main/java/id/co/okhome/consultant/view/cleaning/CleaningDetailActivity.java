package id.co.okhome.consultant.view.cleaning;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.mrjodev.jorecyclermanager.QuickReturnViewInitializor;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;

public class CleaningDetailActivity extends OkHomeParentActivity {

    @BindView(R.id.actCleaningInfo_svItem)          ScrollView svItem;
    @BindView(R.id.actCleaningInfo_vgActions)       ViewGroup vgActions;
    @BindView(R.id.actCleaningInfo_vLoading)        View vLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_info);
        ButterKnife.bind(this);

        OkhomeUtil.setWhiteSystembar(this);

        QuickReturnViewInitializor.init(svItem, vgActions);

        vLoading.setVisibility(View.GONE);
    }
}
