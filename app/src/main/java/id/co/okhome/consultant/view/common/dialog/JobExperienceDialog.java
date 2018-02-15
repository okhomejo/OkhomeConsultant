package id.co.okhome.consultant.view.common.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.EditText;
import android.widget.TextView;

import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.exception.OkhomeException;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.JobExperienceModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;

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

    public void callYearMonthPicker(final TextView tv) {
        YearMonthPickerDialog yearMonthPickerDialog = new YearMonthPickerDialog(activity, new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year, int month) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");

                tv.setText(dateFormat.format(calendar.getTime()));
            }
        }, R.style.MyDialogTheme);
        yearMonthPickerDialog.show();
    }

    private void checkInputFields(){
        final String position   = etPosition.getText().toString();
        final String fromDate   = tvFromDate.getText().toString();
        final String toDate     = tvToDate.getText().toString();

        try{
            OkhomeException.chkException(OkhomeUtil.isEmpty(position), "Please fill in your position");
            OkhomeException.chkException(OkhomeUtil.isEmpty(fromDate), "Please choose a starting date");
            OkhomeException.chkException(OkhomeUtil.isEmpty(toDate), "Please choose an ending date");
        }catch(OkhomeException e){
            ToastUtil.showToast(e.getMessage());
            return;
        }
        newJobExp = new JobExperienceModel((fromDate + " - " + toDate), position);
        onJobExperienceDone(newJobExp);
    }

    private void onJobExperienceDone(JobExperienceModel jobExp){
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
        callYearMonthPicker(tvFromDate);
    }

    @OnClick(R.id.dialogWorkExp_tvToDate)
    public void onEditEndDate() {
        callYearMonthPicker(tvToDate);
    }
}