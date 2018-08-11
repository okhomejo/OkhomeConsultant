package id.co.okhome.consultant.view.dialog;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.cached_viewpager.CachedPagerAdapter;
import id.co.okhome.consultant.lib.calendar_manager.MonthAdapter;
import id.co.okhome.consultant.lib.dialog.DialogParent;

/**
 * Created by jo on 2018-01-24.
 */

public class SetOffDayCleaningDialog extends DialogParent{

    @BindView(R.id.dialogPreferedCleaningArea_vLoading)
    View vLoading;

    @BindView(R.id.dialogPreferedCleaningArea_vp)
    ViewPager vp;

    @BindView(R.id.dialogPreferedCleaningArea_tvMonth)
    TextView tvMonth;

    @BindView(R.id.dialogPreferedCleaningArea_tvYear)
    TextView tvYear;


    MonthAdapter monthAdapter;

    public SetOffDayCleaningDialog(Context context) {
        super(context);
    }

    @Override
    public int onInit() {
        return R.layout.dialog_setoffday;
    }

    @Override
    public void onCreate() {

        ButterKnife.bind(this, getDecorView());
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
//                tvMonth.setText(MonthAdapter.MONTHS[month-1]);
//                tvYear.setText(year+"");
//
//                List<View> listWillGone = new ArrayList<>();
//                viewHolder.setTag(listWillGone);
//
//                View vDaybyTag = gridMonth.findViewWithTag(year+""+month+10);
//                View v = vDaybyTag.findViewById(R.id.itemDay_vTag1);
//                v.setVisibility(View.VISIBLE);
//
//                listWillGone.add(v);
            }

            @Override
            public void onDayClick(int year, int month, int day) {

            }
        });
    }

    @Override
    public void onShow() {

    }
}
