package id.co.okhome.consultant.view.common.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.view.viewholder.StringHolder;

import static id.co.okhome.consultant.lib.dialog.DialogParent.CommonDialogListener.ACTIONCODE_NO;
import static id.co.okhome.consultant.lib.dialog.DialogParent.CommonDialogListener.ACTIONCODE_OK;

/**
 * Created by jo on 2018-01-24.
 */

public class CommonAlertDialog extends DialogParent{

    @BindView(R.id.dialogCommonAlert_tvTitle)       TextView tvTitle;
    @BindView(R.id.dialogCommonAlert_tvSubTitle)    TextView tvSubTitle;

    String title, subTitle;
    CommonDialogListener commonDialogListener;

    public CommonAlertDialog(Context context) {
        super(context);
    }

    @Override
    public int onInit() {
        return R.layout.dialog_common_alert;
    }

    @Override
    public void onCreate() {
        ButterKnife.bind(this, getDecorView());

        //view control
        tvTitle.setVisibility(View.GONE);
        tvSubTitle.setVisibility(View.GONE);
        tvSubTitle.setVisibility(View.GONE);
        if(title != null){
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
        }

        if(subTitle != null){
            tvSubTitle.setText(subTitle);
            tvSubTitle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onShow() {

    }

    public CommonAlertDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public CommonAlertDialog setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    @Override
    public CommonAlertDialog setCommonDialogListener(CommonDialogListener commonDialogListener) {
        this.commonDialogListener = commonDialogListener;
        return this;
    }

    @OnClick(R.id.dialogCommonAlert_vbtnX)
    public void onClose() {
        dismiss();

        if(commonDialogListener!= null){
            commonDialogListener.onCommonDialogWorkDone(this, ACTIONCODE_NO, null);
        }
    }

    @OnClick(R.id.dialogCommonAlert_vbtnOk)
    public void onSubmit() {
        if(commonDialogListener != null){
            commonDialogListener.onCommonDialogWorkDone(this, ACTIONCODE_OK, null);
        }else{
            dismiss();
        }
    }
}
