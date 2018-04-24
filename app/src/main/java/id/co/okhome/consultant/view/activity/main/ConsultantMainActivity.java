package id.co.okhome.consultant.view.activity.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.DelayedFinish;
import id.co.okhome.consultant.lib.ViewHolderUtil;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.app.main.CleaningProgressIconManager;
import id.co.okhome.consultant.lib.fragment_pager.FragmentTabAdapter;
import id.co.okhome.consultant.lib.fragment_pager.TabFragmentStatusListener;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.page.ConsultantPageMainModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.fragment.consultant_tab.JobHistoryCalendarTabFragment;
import id.co.okhome.consultant.view.fragment.consultant_tab.PickingCleaningTabFragment;
import id.co.okhome.consultant.view.fragment.consultant_tab.ProgressTabFragment;
import id.co.okhome.consultant.view.fragment.consultant_tab.SettingForConsultantTabFragment;
import id.co.okhome.consultant.view.fragment.trainee_tab.ChatTabFragment;


public class ConsultantMainActivity extends OkHomeParentActivity{

    @BindView(R.id.actMain_tvTitle)     TextView tvTitle;
    @BindView(R.id.actMain_vgTop)       ViewGroup vgTop;
    @BindView(R.id.actMain_vp)          ViewPager vpMain;
    @BindView(R.id.actMain_vbtnRequestSetting) View vbtnSetting;

    ConsultantMainActivity.MainTabAdapter tabAdapter;
    CleaningProgressIconManager cleaningProgressIconManager;

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

    @Override
    protected void onResume() {
        super.onResume();
        adaptMainPageInfoFromServer();

    }

    private void init(){
        OkhomeUtil.setWhiteSystembar(this);
        OkhomeUtil.initTopPadding(tvTitle);
        tabAdapter = new ConsultantMainActivity.MainTabAdapter(this, vpMain, getSupportFragmentManager());
        tabAdapter.setViewPager(vpMain);
        vpMain.addOnPageChangeListener(tabAdapter);

        ButterKnife.findById(this, R.id.actMain_vgTabForTrainee).setVisibility(View.GONE);

        cleaningProgressIconManager = new CleaningProgressIconManager(
                (ImageView)ButterKnife.findById(this, R.id.actMain_ivCleaningIcon),
                ButterKnife.findById(this, R.id.actMain_vCleaningIconDot),
                ButterKnife.findById(this, R.id.actMain_vgTabIcons));

    }

    private void adaptMainPageInfoFromServer(){
        OkhomeRestApi.getConsultantClient().getConulstantMainPage(ConsultantLoggedIn.id()).enqueue(new RetrofitCallback<ConsultantPageMainModel>() {
            @Override
            public void onSuccess(ConsultantPageMainModel consultantPageMain) {
                cleaningProgressIconManager.check(consultantPageMain.onCleaning);
            }
        });
    }



    @Override
    public void onBackPressed() {
        DelayedFinish.delayedFinish(this, "App will close if pressed one more");
    }

    //
    public class MainTabAdapter extends FragmentTabAdapter implements ViewPager.OnPageChangeListener{

        List<View> listTab = new ArrayList<>();
        List<String> listTitle = new ArrayList<>();
        ViewPager vp;

        public MainTabAdapter(Activity activity, ViewPager vp, FragmentManager fm) {
            super(fm);
            this.vp = vp;

            listTab.add(ButterKnife.findById(activity, R.id.actMain_vgTabBtn1Trainer));
            listTab.add(ButterKnife.findById(activity, R.id.actMain_vgTabBtn2Trainer));
            listTab.add(ButterKnife.findById(activity, R.id.actMain_vgTabBtn3Trainer));
            listTab.add(ButterKnife.findById(activity, R.id.actMain_vgTabBtn4Trainer));
            listTab.add(ButterKnife.findById(activity, R.id.actMain_vgTabBtn5Trainer));

            listTitle.add("Request");
            listTitle.add("Schedule");
            listTitle.add("Progress");
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
                    f = new PickingCleaningTabFragment();
                    break;
                case 1:
                    f = new JobHistoryCalendarTabFragment();
                    break;
                case 2:
                    f = new ProgressTabFragment();
                    break;
                case 3:
                    f = new ChatTabFragment();
                    break;
                case 4:
                    f = new SettingForConsultantTabFragment();
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


            notifyCurrentItemChange(position);
        }

        private void notifyCurrentItemChange(int position){

            vbtnSetting.setVisibility(View.GONE);
            for(int i = 0; i < getSupportFragmentManager().getFragments().size(); i++){
                Fragment f = getSupportFragmentManager().getFragments().get(i);
                if(position == i){
                    if(f instanceof TabFragmentStatusListener){
                        ((TabFragmentStatusListener) f).onSelect();
                        ((TabFragmentStatusListener) f).onSelectWithData(OkhomeUtil.makeMap("vSetting", vbtnSetting));
                    }
                }else{
                    if(f instanceof TabFragmentStatusListener){
                        ((TabFragmentStatusListener) f).onDeselect();
                    }
                }
            }
        }

        @Override
        public int getCount() {
            return listTitle.size();
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
