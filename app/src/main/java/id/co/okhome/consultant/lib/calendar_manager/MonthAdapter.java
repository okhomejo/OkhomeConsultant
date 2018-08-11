package id.co.okhome.consultant.lib.calendar_manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.ViewHolderUtil;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.cached_viewpager.CachedPagerAdapter;
import id.co.okhome.consultant.lib.cached_viewpager.SimpleAsyncTask;
import id.co.okhome.consultant.lib.calendar_manager.model.YearMonth;
import id.co.okhome.consultant.lib.calendar_manager.model.YearMonthDay;

/**
 * Created by jo on 2018-04-19.
 */

public class MonthAdapter extends CachedPagerAdapter<Integer> implements ViewPager.OnPageChangeListener{

    public final static String[] MONTHS = new String[]{
            "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    };

    final int MAX_SIZE = 1000;
    final int CENTER_POS = MAX_SIZE / 2;
    int position = CENTER_POS;
    int year, month, pivotYear, pivotMonth;
    ViewPager viewPager;



    List<CalendarCallback> listCalendarCallback = new ArrayList<>();
//    Date

    public MonthAdapter(Context context) {
        super(context);
    }


    //뷰 만들기
    @Override
    public View makeViewItem(LayoutInflater inflater) {
        return inflater.inflate(R.layout.page_month, null);
    }

    //work in background
    @Override
    public void bindViewHolder(ViewHolder viewHolder) {
        viewHolder.initViews(R.id.pageMonth_gridMonth, R.id.itemDay_flDay);
    }

    //main ui thread
    @Override
    public void adaptDataAndViews(ViewHolder viewHolder, Integer data, int position) {

        YearMonth yearMonth = getYearMonth(position);

        //init views
        GridLayout gridMonth = viewHolder.findViewById(R.id.pageMonth_gridMonth);
        List<YearMonthDay> listDay = getMonthCalendar(position);
        viewHolder.setDays(listDay);

        DateTime now = new DateTime();

        long start = System.currentTimeMillis();
        for(int i = 0; i < listDay.size(); i++){
            YearMonthDay day = listDay.get(i);
            View v = gridMonth.getChildAt(i);
            TextView tvDay = ViewHolderUtil.getView(v, R.id.itemDay_tvDay);
            tvDay.setText(day.day +"");
            v.setTag(day.year + "" + OkhomeUtil.getFull2Decimal(day.month) + "" + OkhomeUtil.getFull2Decimal(day.day));
            v.setOnClickListener(new OnDayClickListener(day.year, day.month, day.day));

            //init color
            int dayColor = 0;
            switch(day.dayStatus){
                case YearMonthDay.DAY_NEXT:
                case YearMonthDay.DAY_PREV:
                    switch(day.dayName){
                        case Calendar.SUNDAY:
                            dayColor = ContextCompat.getColor(getContext(), R.color.colorCalendarRedAlpha);
                            break;
                        case Calendar.SATURDAY:
                            dayColor = ContextCompat.getColor(getContext(), R.color.colorCalendarBlueAlpha);
                            break;
                        default:
                            dayColor = ContextCompat.getColor(getContext(), R.color.colorCalendarBlackAlpha);
                    }
                    break;
                default:

                    if(day.year < now.getYear()
                            || (day.year <= now.getYear() && day.month < now.getMonthOfYear())
                            || (day.year <= now.getYear() && day.month <= now.getMonthOfYear() && day.day < now.getDayOfMonth() - 1)){

                        //지난날
                        switch(day.dayName){
                            case Calendar.SUNDAY:
                                dayColor = ContextCompat.getColor(getContext(), R.color.colorCalendarRedAlpha2);
                                break;
                            case Calendar.SATURDAY:
                                dayColor = ContextCompat.getColor(getContext(), R.color.colorCalendarBlueAlpha2);
                                break;
                            default:
                                dayColor = ContextCompat.getColor(getContext(), R.color.colorCalendarBlackAlpha2);
                        }
                    }else{
                        switch(day.dayName){
                            case Calendar.SUNDAY:
                                dayColor = ContextCompat.getColor(getContext(), R.color.colorCalendarRed);
                                break;
                            case Calendar.SATURDAY:
                                dayColor = ContextCompat.getColor(getContext(), R.color.colorCalendarBlue);
                                break;
                            default:
                                dayColor = ContextCompat.getColor(getContext(), R.color.colorCalendarBlack);
                        }
                    }



            }

            tvDay.setTextColor(dayColor);



        }

        long end = System.currentTimeMillis();
        Log.d("MonthAdapter", "adaptDataAndViews : " + (end - start) + "ms");


        for(CalendarCallback calendarCallback :  listCalendarCallback){
            calendarCallback.onMonthViewCreated(yearMonth.year, yearMonth.month, viewHolder, gridMonth);
        }
    }

    /**set current year month*/
    public void setCurrentYearMonth(final int year, final int month){
        this.year = this.pivotYear = year;
        this.month = this.pivotMonth = month;

        viewPager.setCurrentItem(CENTER_POS);


        Handler handler = new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                onPageSelected(CENTER_POS);
                View v = getViewInPosition(position);

                if(v == null){
                    sendEmptyMessageDelayed(0, 100);
                }
            }
        };

        handler.sendEmptyMessageDelayed(0, 200);

    }

    public void addCalendarCallback(CalendarCallback calendarCallback){
        listCalendarCallback.add(calendarCallback);
    }

    /**first load*/
    public void init(final int year, final int month, final ViewPager vp, final OnInitiatingFinishListener onInitiatingFinishListener){
        viewPager = vp;

        new SimpleAsyncTask<List<Integer>>(){
            @Override
            public List<Integer> doBackground() {
                List<Integer> list = new ArrayList<>();
                for(int i = 0; i < MAX_SIZE; i++){
                    list.add(i);
                }

                return list;
            }

            @Override
            public void onFinish(List<Integer> list) {
                asyncAdaptView(vp, list, new OnInitiatingFinishListener() {
                    @Override
                    public void onFinish() {

                        setCurrentYearMonth(year, month);
                        onInitiatingFinishListener.onFinish();
                        vp.addOnPageChangeListener(MonthAdapter.this);
                    }
                });
            }
        }.start();
    }

    //이닛
    private void initCurrentYearMonth(int position){
        YearMonth yearMonth = getYearMonth(position);
        int year = yearMonth.year;
        int month = yearMonth.month;

        MonthAdapter.this.year = year;
        MonthAdapter.this.month = month;
    }

    public YearMonth getYearMonth(int position){

        int pos = position;
        int diffPos = pos - CENTER_POS;
        int diffYear = 0, posMonth, currentYear, currentMonth;

        if(diffPos < 0){
            //작으면
            posMonth = pivotMonth + diffPos;   //-2
            while(posMonth <= 0){
                posMonth = posMonth + 12;
                diffYear ++;
            }
            currentMonth = posMonth;
            currentYear = pivotYear - diffYear;
        }else{
            //크면
            posMonth = pivotMonth + diffPos;
            while(posMonth > 12){
                posMonth = posMonth - 12;
                diffYear ++;
            }
            currentMonth = posMonth;
            currentYear = pivotYear + diffYear;
        }

        year = currentYear;
        month = currentMonth;
//
        YearMonth yearMonth = new YearMonth();
        yearMonth.year = year;
        yearMonth.month = month;
        return yearMonth;
    }


    public int getCurrentYear(){
        return year;
    }

    public int getCurrentMonth(){
        return month;
    }

    //내부처리


    @Override
    public void onPageSelected(int position) {
        initCurrentYearMonth(position);
        View v = getViewInPosition(position);
        if(v == null){
            OkhomeUtil.Log("onPageSelected position " + position + " getView null");
            return;
        }
        ViewHolder viewHolder = getViewHolderFromView(v);

        for(CalendarCallback calendarCallback :  listCalendarCallback){
            calendarCallback.onMonthViewSelected(year, month, viewHolder, (GridLayout) viewHolder.findViewById(R.id.pageMonth_gridMonth));
        }
    }

    // not used
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //달려가져오기
    private List<YearMonthDay> getMonthCalendar(int position){

        long start = System.currentTimeMillis();
        List<YearMonthDay> listYearMonthDay = new ArrayList<>();

        YearMonth yearMonth = getYearMonth(position);
        YearMonth yearMonthNext = getYearMonth(position + 1);
        YearMonth yearMonthPrev = getYearMonth(position - 1);

        //캘린더 정리
        Calendar calThis = Calendar.getInstance();
        calThis.set(yearMonth.year, yearMonth.month-1, 1);

        Calendar calPrev = Calendar.getInstance();
        calPrev.set(yearMonthPrev.year, yearMonthPrev.month-1, 1);

        int lastOfDate = calThis.getActualMaximum(Calendar.DATE);
        int week = calThis.get(Calendar.DAY_OF_WEEK); //해당월의 1일이 무슨요일인가.

        int dayCount = 0;
        for(int i = week; i >= 1; i--){
            int day = calPrev.getActualMaximum(Calendar.DATE) - i + 1;
            listYearMonthDay.add(new YearMonthDay(yearMonthPrev.year, yearMonthPrev.month, day, dayCount % 7 + 1, YearMonthDay.DAY_PREV));
            dayCount ++;
        }
        for(int i = 1; i <= lastOfDate; i++){
            listYearMonthDay.add(new YearMonthDay(yearMonth.year, yearMonth.month, i, dayCount % 7 + 1, YearMonthDay.DAY_THIS));
            dayCount ++;
        }

        for(int i = 1; dayCount <= 41; dayCount++, i++){
            listYearMonthDay.add(new YearMonthDay(yearMonthNext.year, yearMonthNext.month, i, dayCount % 7 + 1, YearMonthDay.DAY_NEXT));
        }

        long end = System.currentTimeMillis();

        Log.d("MonthAdapter", "getMonthCalendar : " + (end - start) + "ms");
        return listYearMonthDay;
    }

    class OnDayClickListener implements View.OnClickListener{
        int year, month, day;
        public OnDayClickListener(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        @Override
        public void onClick(View view) {
            for(CalendarCallback cb : listCalendarCallback){
                cb.onDayClick(year, month, day);
            }
        }
    }

    public interface CalendarCallback{
        void onMonthViewCreated(int year, int month, ViewHolder viewHolder, GridLayout gridMonth);
        void onMonthViewSelected(int year, int month, ViewHolder viewHolder, GridLayout gridMonth);
        void onDayClick(int year, int month, int day);
    }
}
