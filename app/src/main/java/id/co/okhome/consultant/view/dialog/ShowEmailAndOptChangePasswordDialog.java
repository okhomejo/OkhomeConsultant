package id.co.okhome.consultant.view.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.model.v2.AccountModel;

import static id.co.okhome.consultant.lib.dialog.DialogParent.CommonDialogListener.ACTIONCODE_OK;

/**
 * Created by jo on 2018-01-24.
 */

public class ShowEmailAndOptChangePasswordDialog extends DialogParent{

    @BindView(R.id.dialogShowEmailAndOptChangePassword_tvTitle)       TextView tvTitle;
    @BindView(R.id.dialogShowEmailAndOptChangePassword_tvSubTitle)    TextView tvSubTitle;
    @BindView(R.id.dialogShowEmailAndOptChangePassword_tvbtnChangePassword) TextView tvBtnChangePassword;

    CommonDialogListener commonDialogListener;
    AccountModel account;

    public ShowEmailAndOptChangePasswordDialog(Context context, AccountModel account) {
        super(context);
        this.account = account;
    }


    @Override
    public int onInit() {
        return R.layout.dialog_show_email_and_opt_change_password;

    }

    @Override
    public void onCreate() {
        ButterKnife.bind(this, getDecorView());

        //view control
        tvTitle.setText(account.email);
        tvBtnChangePassword.setVisibility(View.GONE);

        if(account.signupBy.equals("EMAIL")){
            tvSubTitle.setText("Sign up by Email");
            tvBtnChangePassword.setVisibility(View.VISIBLE);
        }else if(account.signupBy.equals("GG")){
            tvSubTitle.setText("Sign up by Google");

        }
    }

    @Override
    public void onShow() {

    }

    @Override
    public ShowEmailAndOptChangePasswordDialog setCommonDialogListener(CommonDialogListener commonDialogListener) {
        this.commonDialogListener = commonDialogListener;
        return this;
    }

    @OnClick(R.id.dialogShowEmailAndOptChangePassword_tvbtnChangePassword)
    public void onChangePassword(View v){
        new UpdatePasswordDialog(getContext(), Integer.parseInt(account.id)).show();
    }


    @OnClick(R.id.dialogShowEmailAndOptChangePassword_vbtnOk)
    public void onSubmit() {
        if(commonDialogListener != null){
            commonDialogListener.onCommonDialogWorkDone(this, ACTIONCODE_OK, null);
            dismiss();
        }else{
            dismiss();
        }
    }
}
