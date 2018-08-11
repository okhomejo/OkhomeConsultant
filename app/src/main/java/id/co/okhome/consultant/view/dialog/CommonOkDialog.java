package id.co.okhome.consultant.view.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.dialog.DialogParent;

import static id.co.okhome.consultant.lib.dialog.DialogParent.CommonDialogListener.ACTIONCODE_OK;

/**
 * Created by jo on 2018-01-24.
 */

public class CommonOkDialog extends DialogParent{

    @BindView(R.id.dialogCommonOk_tvTitle)       TextView tvTitle;
    @BindView(R.id.dialogCommonOk_tvSubTitle)    TextView tvSubTitle;

    String title, subTitle;
    CommonDialogListener commonDialogListener;
    boolean center = false;

    public CommonOkDialog(Context context) {
        super(context);
    }


    @Override
    public int onInit() {
        return R.layout.dialog_common_ok;

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

    public CommonOkDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public CommonOkDialog setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    @Override
    public CommonOkDialog setCommonDialogListener(CommonDialogListener commonDialogListener) {
        this.commonDialogListener = commonDialogListener;
        return this;
    }


    @OnClick(R.id.dialogCommonOk_vbtnOk)
    public void onSubmit() {
        if(commonDialogListener != null){
            commonDialogListener.onCommonDialogWorkDone(this, ACTIONCODE_OK, null);
            dismiss();
        }else{
            dismiss();
        }
    }
}
