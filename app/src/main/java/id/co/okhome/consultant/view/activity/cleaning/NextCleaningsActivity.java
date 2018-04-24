package id.co.okhome.consultant.view.activity.cleaning;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.fragment.NextCleaningItemFragment;

public class NextCleaningsActivity extends OkHomeParentActivity {

    @BindView(R.id.actNextCleanings_vgContent)     ViewGroup vgContent;
    @BindView(R.id.actNextCleanings_viewPager)     ViewPager viewPager;
    @BindView(R.id.actNextCleanings_tabDots)       TabLayout tabDots;
    @BindView(R.id.actNextCleanings_vProgress)     ProgressBar progressBar;
    @BindView(R.id.actNextCleanings_tvCurrentPage) TextView tvCurrentPage;

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
        viewPager.addOnPageChangeListener(pagerAdapter);
        tabDots.setupWithViewPager(viewPager, true);
    }

    private void getNextCleaningTasks() {
        progressBar.setVisibility(View.VISIBLE);
        OkhomeRestApi.getCleaningTaskClient().getNextCleaningTasks(ConsultantLoggedIn.id(), 0).enqueue(new RetrofitCallback<List<CleaningInfoModel>>() {
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
            Fragment taskFragment = NextCleaningItemFragment.newInstance(task);
            pagerAdapter.addFragment(taskFragment);
        }
        tvCurrentPage.setText(String.format("%s / %s", 1, pagerAdapter.getCount()));
        pagerAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.actNextCleanings_vbtnX)
    public void onBackButtonClicked() {
        finish();
    }

    public class TabsPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {

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

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
//            tvCurrentPage.setText(String.format("%s / %s", position+1, pagerAdapter.getCount()));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

            if (ViewPager.SCROLL_STATE_IDLE == state) {
                tvCurrentPage.setText(String.format("%s / %s", viewPager.getCurrentItem()+1, pagerAdapter.getCount()));
            }
        }
    }
}
