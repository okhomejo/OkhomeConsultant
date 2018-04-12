package id.co.okhome.consultant.view.activity.cleaning;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.cleaning.CleaningInfoModel;
import id.co.okhome.consultant.model.page.ConsultantPageProgressModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.fragment.consultant_tab.NextCleaningTabFragment;
import id.co.okhome.consultant.view.fragment.consultant_tab.PickingCleaningTabFragment;

public class NextCleaningsActivity extends OkHomeParentActivity {

    @BindView(R.id.actNextCleanings_vgContent)     ViewGroup vgContent;
    @BindView(R.id.actNextCleanings_viewPager)     ViewPager viewPager;
    @BindView(R.id.actNextCleanings_tabDots)       TabLayout tabDots;
    @BindView(R.id.actNextCleanings_vProgress)     ProgressBar progressBar;

    private TabsPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nextcleanings);
        OkhomeUtil.setWhiteSystembar(this);

        ButterKnife.bind(this);
        init();
    }

    private void init() {
        vgContent.setVisibility(View.GONE);

        pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        getNextCleaningTasks();

        viewPager.setAdapter(pagerAdapter);

//        tabDots.setupWithViewPager(viewPager, true);

    }

    private void getNextCleaningTasks() {
        progressBar.setVisibility(View.VISIBLE);
        OkhomeRestApi.getCleaningTaskClient().getNextCleaningTasks(ConsultantLoggedIn.id()).enqueue(new RetrofitCallback<List<CleaningInfoModel>>() {
            @Override
            public void onSuccess(List<CleaningInfoModel> taskList) {
                addTasksToViewPager(taskList);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressBar.setVisibility(View.GONE);
                vgContent.setVisibility(View.VISIBLE);
            }
        });
    }

    private void addTasksToViewPager(List<CleaningInfoModel> taskList) {
        for(CleaningInfoModel task : taskList) {
            Fragment taskFragment = NextCleaningTabFragment.newInstance(task);
            pagerAdapter.addFragment(taskFragment);
        }
        pagerAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.actNextCleanings_vbtnX)
    public void onBackButtonClicked() {
        finish();
    }

    public class TabsPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment) {
            fragments.add(fragment);
        }
    }
}
