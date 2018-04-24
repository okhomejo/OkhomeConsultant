package id.co.okhome.consultant.view.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.view.activity.account.profile.UpdateConsultantAreaActivity;

/**
 * Created by jo on 2018-01-24.
 */

public class ConsultantCleaningQueueSettingDialog extends DialogParent{
    public ConsultantCleaningQueueSettingDialog(Context context) {
        super(context);
    }

    @Override
    public int onInit() {
        return R.layout.dialog_consultant_cleaningqueue_setting;
    }

    @Override
    public void onCreate() {
        ButterKnife.bind(this, getDecorView());
    }

    @Override
    public void onShow() {

    }

    @OnClick(R.id.dialogConsultantCleaningQueueSetting_vbtnSetOffday)
    public void onSetOffDayClick(View v){
//        getContext().startActivity(new Intent(getContext(), UpdateConsultantAreaActivity.class));
        new SetOffDayCleaningDialog(getContext()).show();
    }

    @OnClick(R.id.dialogConsultantCleaningQueueSetting_vbtnPreferredArea)
    public void onPreferredAreaClick(View v){
        getContext().startActivity(new Intent(getContext(), UpdateConsultantAreaActivity.class));
    }
}
