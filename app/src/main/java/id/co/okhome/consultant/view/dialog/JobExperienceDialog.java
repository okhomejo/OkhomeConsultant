package id.co.okhome.consultant.view.dialog;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        final String fromDate   = tvFromDate.getText().toString();
        final String toDate     = tvToDate.getText().toString();

        try {
            OkhomeException.chkException(OkhomeUtil.isEmpty(position), "Please fill in your position");
            OkhomeException.chkException(OkhomeUtil.isEmpty(fromDate), "Please choose a starting date");
            OkhomeException.chkException(OkhomeUtil.isEmpty(toDate), "Please choose an ending date");
        } catch(OkhomeException e) {
            ToastUtil.showToast(e.getMessage());
            return;
        }
        DateTimeFormatter dtf1 = DateTimeFormat.forPattern("MMMM yyyy");
        DateTimeFormatter dtf2 = DateTimeFormat.forPattern("yyyy-MM");

        DateTime start  = dtf1.parseDateTime(fromDate);
        DateTime end    = dtf1.parseDateTime(toDate);

        newJobExp = new JobExperienceModel(
                (start.toString(dtf2) + "," + end.toString(dtf2)), position
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
        callYearMonthPicker(tvFromDate, "From");
    }

    @OnClick(R.id.dialogWorkExp_tvToDate)
    public void onEditEndDate() {
        callYearMonthPicker(tvToDate, "To");
    }
}
