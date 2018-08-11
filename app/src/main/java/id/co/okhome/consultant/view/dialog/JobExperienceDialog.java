package id.co.okhome.consultant.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.widget.EditText;
import android.widget.TextView;

import org.joda.time.format.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.exception.OkhomeException;
import id.co.okhome.consultant.lib.CustomDatePickerDialog;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.model.JobExperienceModel;
import id.co.okhome.consultant.view.viewholder.StringHolder;

import static id.co.okhome.consultant.lib.dialog.DialogParent.CommonDialogListener.ACTIONCODE_OK;

/**
 * Created by frizurd on 13/02/2018.
 */

public class JobExperienceDialog extends DialogParent {

    @BindView(R.id.dialogWorkExp_etPosition)      EditText etPosition;
    @BindView(R.id.dialogWorkExp_tvFromDate)      TextView tvFromDate;
    @BindView(R.id.dialogWorkExp_tvToDate)        TextView tvToDate;

    public final static String RESULT_POSITION    = "JOB EXPERIENCE";
    private Activity activity;
    private JobExperienceModel newJobExp;

    String endYearMonth, beginYearMonth;

    public JobExperienceDialog(Activity activity, CommonDialogListener commonDialogListener) {
        super(activity);
        this.activity = activity;
        this.commonDialogListener = commonDialogListener;
    }

    public void callYearMonthPicker(final TextView tv, String fromTo) {
        CustomDatePickerDialog yearMonthPickerDialog = new CustomDatePickerDialog(activity, new CustomDatePickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year, int month) {
                Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
                tv.setText(dateFormat.format(calendar.getTime()));

                if(!tvFromDate.getText().toString().matches("") && tvToDate.getText().toString().matches("")) {
                    onEditEndDate();
                }
            }
        }, R.style.MonthYearDialogPicker, fromTo);

        yearMonthPickerDialog.show();
    }

    private void checkInputFields(){
        final String position   = etPosition.getText().toString();
        final String fromDate   = beginYearMonth;
        final String toDate     = endYearMonth;

        try {
            OkhomeException.chkException(OkhomeUtil.isEmpty(position), "Please fill in your position");
            OkhomeException.chkException(OkhomeUtil.isEmpty(fromDate), "Please choose a starting date");
            OkhomeException.chkException(OkhomeUtil.isEmpty(toDate), "Please choose an ending date");
        } catch(OkhomeException e) {
            ToastUtil.showToast(e.getMessage());
            return;
        }

        newJobExp = new JobExperienceModel(
                (fromDate + "," + toDate), position
        );
        onJobExperienceDone(newJobExp);
    }

    private void onJobExperienceDone(JobExperienceModel jobExp) {
        commonDialogListener.onCommonDialogWorkDone(this, ACTIONCODE_OK, OkhomeUtil.makeMap(RESULT_POSITION, jobExp));
        dismiss();
    }

    @Override
    public int onInit() {
        return R.layout.dialog_work_experience;
    }

    @Override
    public void onCreate() {
        ButterKnife.bind(this, getDecorView());
    }

    @Override
    public void onShow() {
    }

    @OnClick(R.id.dialogWorkExp_vBtnOk)
    public void onSend() {
        checkInputFields();
    }

    @OnClick(R.id.dialogWorkExp_vBtnCancel)
    public void onCancel() {
        dismiss();
    }

    @OnClick(R.id.dialogWorkExp_tvFromDate)
    public void onEditStartDate() {
        showYearMonthPicker("When begin this job?", new YearMonthChooseListener() {
            @Override
            public void onChoosed(String year, String month) {
                beginYearMonth = year +"-" + month;
                String parsedString = DateTimeFormat.forPattern("MMM yy").withLocale(new Locale("id")).print(DateTimeFormat.forPattern("yyyy-MM").parseDateTime(beginYearMonth));
                tvFromDate.setText(parsedString);
            }
        });
    }

    @OnClick(R.id.dialogWorkExp_tvToDate)
    public void onEditEndDate() {
        showYearMonthPicker("When finish this job?", new YearMonthChooseListener() {
            @Override
            public void onChoosed(String year, String month) {
                endYearMonth = year +"-" + month;
                String parsedString = DateTimeFormat.forPattern("MMM yy").withLocale(new Locale("id")).print(DateTimeFormat.forPattern("yyyy-MM").parseDateTime(endYearMonth));
                tvToDate.setText(parsedString);
            }
        });
    }


    private void showYearMonthPicker(final String titleHeader, final YearMonthChooseListener yearMonthChooseListener){
        final List<String> years = new ArrayList<>();
        for(int i = 1950; i < 2018; i++){
            years.add(i+"");
        }

        final List<String> months = new ArrayList<>();
        for(int i = 1; i <= 12; i++){
            months.add(i+"");
        }


        new CommonListDialog(getContext())
                .setTitle(titleHeader + "(year)")
                .setArrItems(years.toArray(new String[years.size()]))
                .setColumnCount(3)
                .setItemClickListener(new StringHolder.ItemClickListener() {
                    @Override
                    public void onItemClick(Dialog dialog, int pos, String value, String tag) {
                        dialog.dismiss();

                        final String year = value;

                        new CommonListDialog(getContext())
                                .setTitle(titleHeader + "(month)")
                                .setArrItems(months.toArray(new String[months.size()]))
                                .setColumnCount(3)
                                .setItemClickListener(new StringHolder.ItemClickListener() {
                                    @Override
                                    public void onItemClick(Dialog dialog, int pos, String value, String tag) {
                                        dialog.dismiss();
                                        final String month = OkhomeUtil.fillupWith2Zero(value);

                                        yearMonthChooseListener.onChoosed(year, month);

                                    }
                                })
                                .show();

                    }
                })
                .show();
    }

    interface YearMonthChooseListener{
        public void onChoosed(String year, String month);
    }
}
