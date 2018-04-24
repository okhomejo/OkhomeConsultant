package id.co.okhome.consultant.view.fragment.consultant_tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.cached_viewpager.CachedPagerAdapter;
import id.co.okhome.consultant.lib.calendar_manager.MonthAdapter;
import id.co.okhome.consultant.lib.fragment_pager.TabFragmentStatusListener;
import id.co.okhome.consultant.view.activity.cleaning.CleaningListTabActivity;

/**
 * Created by jo on 2018-04-07.
 */

public class JobHistoryCalendarTabFragment extends Fragment implements TabFragmentStatusListener {

    MonthAdapter monthAdapter = null;

    @BindView(R.id.fragTabJobsCalendar_vg)
    ViewPager vp;
    @BindView(R.id.fragTabJobsCalendar_vLoading)    View vLoading;
    @BindView(R.id.fragTabJobsCalendar_tvMonth)     TextView tvMonth;
    @BindView(R.id.fragTabJobsCalendar_tvYear)      TextView tvYear;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_jobs_calendar, null);
        initView(v);
        return v;
    }

    private void initView(View v) {
        ButterKnife.bind(this, v);
        vLoading.setVisibility(View.VISIBLE);

        monthAdapter = new MonthAdapter(getContext());
        monthAdapter.init(OkhomeUtil.getCurrentYear(), OkhomeUtil.getCurrentMonth(), vp, new CachedPagerAdapter.OnInitiatingFinishListener() {
            @Override
            public void onFinish() {
                vLoading.setVisibility(View.GONE);
            }
        });

        //listner
        monthAdapter.addCalendarCallback(new MonthAdapter.CalendarCallback() {


            @Override
            public void onMonthViewCreated(int year, int month, CachedPagerAdapter<Integer>.ViewHolder viewHolder, GridLayout gridMonth) {
                if(viewHolder.getTag() != null){
                    List<View> listWillGone = (List<View>)viewHolder.getTag();
                    for(View v : listWillGone){
                        v.setVisibility(View.GONE);
                    }

                    viewHolder.setTag(null);
                }
            }

            @Override
            public void onMonthViewSelected(int year, int month, CachedPagerAdapter<Integer>.ViewHolder viewHolder, GridLayout gridMonth) {
                tvMonth.setText(MonthAdapter.MONTHS[month-1]);
                tvYear.setText(year+"");

                List<View> listWillGone = new ArrayList<>();
                viewHolder.setTag(listWillGone);

                View vDaybyTag = gridMonth.findViewWithTag(year+""+month+10);
                View v = vDaybyTag.findViewById(R.id.itemDay_vTag1);
                v.setVisibility(View.VISIBLE);

                listWillGone.add(v);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSelect() {
    }

    @Override
    public void onSelectWithData(Map<String, Object> param) {
        View vbtnSetting = (View)param.get("vSetting");
        vbtnSetting.setVisibility(View.VISIBLE);
        vbtnSetting.animate().translationX(20).alpha(0f).setDuration(0).start();
        vbtnSetting.animate().translationX(0).alpha(1f).setDuration(200).start();

    }

    @Override
    public void onDeselect() {

    }

    @OnClick(R.id.fragTabJobsCalendar_vbtnList)
    public void onclickList(View v){
        startActivity(new Intent(getContext(), CleaningListTabActivity.class));

    }


}
