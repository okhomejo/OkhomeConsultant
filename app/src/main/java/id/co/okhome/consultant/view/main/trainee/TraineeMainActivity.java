package id.co.okhome.consultant.view.main.trainee;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.fragment_pager.FragmentTabAdapter;
import id.co.okhome.consultant.view.main.trainee.fragment.ChatTabFragment;
import id.co.okhome.consultant.view.main.trainee.fragment.HomeTabFragment;
import id.co.okhome.consultant.view.main.trainee.fragment.ManualTabFragment;
import id.co.okhome.consultant.view.main.trainee.fragment.SettingTabFragment;
import id.co.okhome.consultant.view.main.trainee.fragment.TrainingTabFragment;

public class TraineeMainActivity extends OkHomeParentActivity {

    @BindView(R.id.actMain_tvTitle)
    TextView tvTitle;

    @BindView(R.id.actMain_vgTop)
    ViewGroup vgTop;

    @BindView(R.id.actMain_vp)
    ViewPager vpMain;

    MainTabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

//        OkhomeUtil.setSystemBarColor(this, Color.parseColor("#000000"));
//                ContextCompat.getColor(this, R.color.colorOkhome));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            OkhomeUtil.setSystemBarColor(this, Color.parseColor("#ffffff"));

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tvTitle.getLayoutParams();
            layoutParams.topMargin = layoutParams.topMargin - OkhomeUtil.getPixelByDp(this, 3);
            tvTitle.setLayoutParams(layoutParams);


        }


        init();

        vpMain.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
//        TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
//        String mPhoneNumber = tMgr.getLine1Number();


    }

    private void init(){
        tabAdapter = new MainTabAdapter(getSupportFragmentManager());
        tabAdapter.setViewPager(vpMain);

    }

    public class MainTabAdapter extends FragmentTabAdapter {

        public MainTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = null;
            switch(position){
                case 0:
                    f = new HomeTabFragment();
                    break;
                case 1:
                    f = new TrainingTabFragment();
                    break;
                case 2:
                    f = new ChatTabFragment();
                    break;
                case 3:
                    f = new ManualTabFragment();
                    break;
                case 4:
                    f = new SettingTabFragment();
                    break;
            }
            return f;
        }

        @Override
        public int getCount() {
            return 5;
        }
    }


}
