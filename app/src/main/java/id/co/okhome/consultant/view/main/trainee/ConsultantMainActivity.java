package id.co.okhome.consultant.view.main.trainee;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;

public class ConsultantMainActivity extends OkHomeParentActivity {

    @BindView(R.id.actMain_tvTitle)     TextView tvTitle;
    @BindView(R.id.actMain_vgTop)       ViewGroup vgTop;
    @BindView(R.id.actMain_vp)          ViewPager vpMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_main);

        ButterKnife.bind(this);
        init();
    }

    private void init(){
        OkhomeUtil.setWhiteSystembar(this);
        initTopPadding();
    }

    //init top pading
    private void initTopPadding(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tvTitle.getLayoutParams();
            layoutParams.topMargin = layoutParams.topMargin - OkhomeUtil.getPixelByDp(this, 3);
            tvTitle.setLayoutParams(layoutParams);
        }
    }


}
