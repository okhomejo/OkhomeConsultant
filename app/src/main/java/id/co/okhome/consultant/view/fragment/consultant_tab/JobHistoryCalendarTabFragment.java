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

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkhomeDateTimeFormatUtil;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.cached_viewpager.CachedPagerAdapter;
import id.co.okhome.consultant.lib.calendar_manager.MonthAdapter;
import id.co.okhome.consultant.lib.calendar_manager.model.YearMonthDay;
import id.co.okhome.consultant.lib.fragment_pager.TabFragmentStatusListener;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.cleaning.order.CleaningDayItemModel;
import id.co.okhome.consultant.model.page.ConsultantPageJobsModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.activity.cleaning.CleaningDetailActivity;
import id.co.okhome.consultant.view.activity.cleaning.CleaningListTabActivity;

/**
 * Created by jo on 2018-04-07.
 */

public class JobHistoryCalendarTabFragment extends Fragment implements TabFragmentStatusListener {

    MonthAdapter monthAdapter = null;

    @BindView(R.id.fragTabJobsCalendar_vgNextJobContents)       ViewGroup vgNextJobContents;
    @BindView(R.id.fragTabJobsCalendar_tvNextJobAddress)        TextView tvAddress;
    @BindView(R.id.fragTabJobsCalendar_tvNextJobWhen)           TextView tvWhen;

    @BindView(R.id.fragTabJobsCalendar_vg)                      ViewPager vp;
    @BindView(R.id.fragTabJobsCalendar_vLoading)                View vLoading;
    @BindView(R.id.fragTabJobsCalendar_tvMonth)                 TextView tvMonth;
    @BindView(R.id.fragTabJobsCalendar_tvYear)                  TextView tvYear;

    int targetYear, targetMonth;
    CachedPagerAdapter<Integer>.ViewHolder targetViewHolder;
    GridLayout targetGridMonth;

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
                clearPreviousCalendarMark(viewHolder);
            }

            @Override
            public void onMonthViewSelected(int year, int month, CachedPagerAdapter<Integer>.ViewHolder viewHolder, GridLayout gridMonth) {

                JobHistoryCalendarTabFragment.this.targetYear = year;
                JobHistoryCalendarTabFragment.this.targetMonth = month;
                targetViewHolder = viewHolder;
                targetGridMonth = gridMonth;

                tvMonth.setText(MonthAdapter.MONTHS[month-1]);
                tvYear.setText(year+"");

                refreshCalendar();
            }

            @Override
            public void onDayClick(int year, int month, int day) {

            }
        });
    }

    //지난 일정 내역 지우기
    private void clearPreviousCalendarMark(CachedPagerAdapter<Integer>.ViewHolder viewHolder){
        if(viewHolder.getTag() != null){
            List<View> listWillGone = (List<View>)viewHolder.getTag();
            for(View v : listWillGone){
                v.setVisibility(View.GONE);
            }
            viewHolder.setTag(null);
        }
    }

    //현재 선택된 달력에서 정보 가져오기
    public void refreshCalendar(){
        //기존 내역 달력 지우기
        clearPreviousCalendarMark(targetViewHolder);

        //
        YearMonthDay firstDay = targetViewHolder.getDays().get(0);
        YearMonthDay lastDay = targetViewHolder.getDays().get(targetViewHolder.getDays().size()-1);

        String from = firstDay.year + OkhomeUtil.getFull2Decimal(firstDay.month) + OkhomeUtil.getFull2Decimal(firstDay.day);
        String end = lastDay.year + OkhomeUtil.getFull2Decimal(lastDay.month) + OkhomeUtil.getFull2Decimal(lastDay.day);

        vLoading.setVisibility(View.VISIBLE);
        final String callId = targetYear + "" + targetMonth;

        OkhomeRestApi.getConsultantClient().getConsultantJobsPage(ConsultantLoggedIn.id(), from, end).enqueue(new RetrofitCallback<ConsultantPageJobsModel>() {
            @Override
            public void onSuccess(ConsultantPageJobsModel consultantPageJobs) {
                String nowYearMonth = targetYear + "" + targetMonth;
                if(!callId.equals(nowYearMonth)){
                    return;
                }

                onPageInfoIncome(consultantPageJobs);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                vLoading.setVisibility(View.GONE);
            }

        });
    }

    //달력 내용 불러옴
    private void onPageInfoIncome(final ConsultantPageJobsModel consultantPageJobs){

        if(consultantPageJobs.nextJob == null){
//            vgNextJobContents.setVisibility(View.GONE);
            tvAddress.setText("Next cleaning is not ready yet");
            tvWhen.setText("Accept cleaning at Request tab menu");
        }else{
            vgNextJobContents.setVisibility(View.VISIBLE);
            vgNextJobContents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CleaningDetailActivity.start(getContext(), consultantPageJobs.nextJob.cleaning.cleaningId);
                }
            });


            DateTime cleaningStart = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.s").parseDateTime(consultantPageJobs.nextJob.cleaning.whenDateTime);
            int durationMin = consultantPageJobs.nextJob.cleaning.totalDurationMinute;
            DateTime cleaningEnd = cleaningStart.plusMinutes(durationMin);

            String whenString = OkhomeDateTimeFormatUtil.printOkhomeType(cleaningStart.getMillis(), "(E) d MMM yy HH:mm");
            String whenStringTail = DateTimeFormat.forPattern("HH:mm").print(cleaningEnd);
            String when = whenString + "-" + whenStringTail;

            tvAddress.setText(consultantPageJobs.nextJob.cleaningOrder.homeAddress);
            tvWhen.setText("Cleaning on " + when);

            //달력처리

            List<View> listWillGone = new ArrayList<>();
            targetViewHolder.setTag(listWillGone);

            //나타타라얍.
            for(final CleaningDayItemModel cleaningDay : consultantPageJobs.cleaningItems){

                DateTime date = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(cleaningDay.whenDate);
                String yearMonth = date.getYear() + "" +  OkhomeUtil.getFull2Decimal(date.getMonthOfYear()) + OkhomeUtil.getFull2Decimal(date.getDayOfMonth());
                View vDaybyTag = targetGridMonth.findViewWithTag(yearMonth);

                View v = vDaybyTag.findViewById(R.id.itemDay_vTag1);
                v.setVisibility(View.VISIBLE);

                listWillGone.add(v);

                //onCLick
                vDaybyTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CleaningDetailActivity.start(getContext(), cleaningDay.cleaningId);
                    }
                });
            }
        }
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
//        View vbtnSetting = (View)param.get("vSetting");
//        vbtnSetting.setVisibility(View.VISIBLE);
//        vbtnSetting.animate().translationX(20).alpha(0f).setDuration(0).start();
//        vbtnSetting.animate().translationX(0).alpha(1f).setDuration(200).start();
    }

    @Override
    public void onDeselect() {
        ;
    }

    @OnClick(R.id.fragTabJobsCalendar_vbtnList)
    public void onclickList(View v){
        startActivity(new Intent(getContext(), CleaningListTabActivity.class));
    }

}