package id.co.okhome.consultant.view.main.trainee;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.ViewHolderUtil;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.fragment_pager.FragmentTabAdapter;
import id.co.okhome.consultant.view.main.trainee.tab_fragment.ChatTabFragment;
import id.co.okhome.consultant.view.main.trainee.tab_fragment.HomeTabFragment;
import id.co.okhome.consultant.view.main.trainee.tab_fragment.ManualTabFragment;
import id.co.okhome.consultant.view.main.trainee.tab_fragment.SettingTabFragment;
import id.co.okhome.consultant.view.main.trainee.tab_fragment.TrainingTabFragment;

public class TraineeMainActivity extends OkHomeParentActivity {

    @BindView(R.id.actMain_tvTitle)     TextView tvTitle;
    @BindView(R.id.actMain_vgTop)       ViewGroup vgTop;
    @BindView(R.id.actMain_vp)          ViewPager vpMain;

    MainTabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        OkhomeUtil.setWhiteSystembar(this);
        init();
//        TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
//        String mPhoneNumber = tMgr.getLine1Number();
    }

    private void init(){
        OkhomeUtil.setWhiteSystembar(this);
        OkhomeUtil.initTopPadding(tvTitle);
        tabAdapter = new MainTabAdapter(this, vpMain, getSupportFragmentManager());
        tabAdapter.setViewPager(vpMain);
        vpMain.addOnPageChangeListener(tabAdapter);

        ButterKnife.findById(this, R.id.actMain_vgTabForTrainer).setVisibility(View.GONE);
    }


    public class MainTabAdapter extends FragmentTabAdapter implements ViewPager.OnPageChangeListener{

        List<View> listTab = new ArrayList<>();
        List<String> listTitle = new ArrayList<>();
        ViewPager vp;

        public MainTabAdapter(Activity activity, ViewPager vp, FragmentManager fm) {
            super(fm);
            this.vp = vp;

            listTab.add(ButterKnife.findById(activity, R.id.actMain_vgTabBtn1));
            listTab.add(ButterKnife.findById(activity, R.id.actMain_vgTabBtn2));
            listTab.add(ButterKnife.findById(activity, R.id.actMain_vgTabBtn3));
            listTab.add(ButterKnife.findById(activity, R.id.actMain_vgTabBtn4));
            listTab.add(ButterKnife.findById(activity, R.id.actMain_vgTabBtn5));

            listTitle.add("Welcome");
            listTitle.add("Training");
            listTitle.add("Manual");
            listTitle.add("Messages");
            listTitle.add("More");

            for(int i = 0; i < listTab.size(); i ++){
                setOnClick(listTab.get(i), i);
            }

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
                    f = new ManualTabFragment();
                    break;
                case 3:
                    f = new ChatTabFragment();
                    break;
                case 4:
                    f = new SettingTabFragment();
                    break;
            }
            return f;
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);

            //change tab image for indicating page change
            for(int i = 0; i < listTab.size(); i++){
                View vTarget = listTab.get(i);
                ViewGroup vgIcon = ViewHolderUtil.getView(vTarget, R.id.actMain_vgTabIcon);
                TextView tvTabText = ViewHolderUtil.getView(vTarget, R.id.actMain_tvTabText);
                if(i == position){
                    vgIcon.setAlpha(0.7f);
                    tvTabText.setAlpha(1f);
                }else{
                    vgIcon.setAlpha(0.2f);
                    tvTabText.setAlpha(0.5f);
                }
            }

            //change title
            String title = listTitle.get(position);
            tvTitle.setText(title);

        }

        @Override
        public int getCount() {
            return 5;
        }

        private void setOnClick(View v, final int position){
            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    vp.setCurrentItem(position);
                }
            });
        }

    }


}
