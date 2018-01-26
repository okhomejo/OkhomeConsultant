package id.co.okhome.consultant.view.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.OkHomeParentActivity;
import id.co.okhome.consultant.lib.OkhomeUtil;
import id.co.okhome.consultant.lib.fragment_pager.FragmentTabAdapter;
import id.co.okhome.consultant.view.mainpage_tab.trainee.HomeTabFragment;

public class MainActivity extends OkHomeParentActivity {

    @BindView(R.id.actMain_vp)
    ViewPager vpMain;

    MainTabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        OkhomeUtil.chkException();
        OkhomeUtil.setSystemBarColor(this, Color.parseColor("#000000"));
//                ContextCompat.getColor(this, R.color.colorOkhome));

        init();

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
                    f = new HomeTabFragment();
                    break;
                case 2:
                    f = new HomeTabFragment();
                    break;
                case 3:
                    f = new HomeTabFragment();
                    break;
                case 4:
                    f = new HomeTabFragment();
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
