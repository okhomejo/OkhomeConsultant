package id.co.okhome.consultant.view.activity.cleaning;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
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
import id.co.okhome.consultant.view.fragment.consultant_tab.PickingCleaningTabFragment;

public class NextCleaningsActivity extends OkHomeParentActivity {

    @BindView(R.id.actNextCleanings_vgContent)     ViewGroup vgContent;
    @BindView(R.id.actNextCleanings_viewPager)     ViewPager viewPager;
    @BindView(R.id.actNextCleanings_tabDots)       TabLayout tabDots;
    @BindView(R.id.actNextCleanings_vProgress)     ProgressBar progressBar;

    private MainPagerAdapter pagerAdapter = null;

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

        pagerAdapter = new MainPagerAdapter();
        viewPager.setAdapter(pagerAdapter);

        tabDots.setupWithViewPager(viewPager, true);

        getNextCleaningTasks();
    }

    private void getNextCleaningTasks() {
        progressBar.setVisibility(View.VISIBLE);
        OkhomeRestApi.getCleaningTaskClient().getNextCleaningTasks(ConsultantLoggedIn.id()).enqueue(new RetrofitCallback<List<CleaningInfoModel>>() {
            @Override
            public void onSuccess(List<CleaningInfoModel> taskList) {
//                addTasksToViewPager(taskList);
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
        int counter = 0;
        for(CleaningInfoModel task : taskList) {

//            PickingCleaningTabFragment taskFrame = new PickingCleaningTabFragment();

            LayoutInflater inflater = getLayoutInflater();
            RelativeLayout taskFrame = (RelativeLayout) inflater.inflate (R.layout.fragment_tab_picking_cleaning, null);
            pagerAdapter.addView (taskFrame, counter);
            counter++;

//            int pageIndex = pagerAdapter.addView (newPage);
//            pagerAdapter.setCurrentItem (pageIndex, true);

        }
    }

    @OnClick(R.id.actNextCleanings_vbtnX)
    public void onBackButtonClicked() {
        finish();
    }


    public class MainPagerAdapter extends PagerAdapter {

        private ArrayList<View> views = new ArrayList<>();

        @Override
        public int getItemPosition (Object object) {
            int index = views.indexOf (object);
            if (index == -1)
                return POSITION_NONE;
            else
                return index;
        }

        @Override
        public Object instantiateItem (ViewGroup container, int position) {
            View v = views.get (position);
            container.addView (v);
            return v;
        }

        @Override
        public void destroyItem (ViewGroup container, int position, Object object) {
            container.removeView (views.get (position));
        }

        @Override
        public int getCount () {
            return views.size();
        }

        @Override
        public boolean isViewFromObject (View view, Object object) {
            return view == object;
        }

        public int addView (View v) {
            return addView (v, views.size());
        }

        public int addView (View v, int position) {
            views.add (position, v);
            return position;
        }

        public int removeView (ViewPager pager, View v) {
            return removeView (pager, views.indexOf (v));
        }

        public int removeView (ViewPager pager, int position) {
            pager.setAdapter (null);
            views.remove (position);
            pager.setAdapter (this);

            return position;
        }

        public View getView (int position) {
            return views.get (position);
        }
    }
}
