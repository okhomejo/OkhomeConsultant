package id.co.okhome.consultant.view.common.dialog;

import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.exception.OkhomeException;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.model.JobExperienceModel;

import static id.co.okhome.consultant.lib.dialog.DialogParent.CommonDialogListener.ACTIONCODE_NO;
import static id.co.okhome.consultant.lib.dialog.DialogParent.CommonDialogListener.ACTIONCODE_OK;

/**
 * Created by frizurd on 16/03/2018.
 */

public class UpdatePasswordDialog extends DialogParent {

    @BindView(R.id.dialogUpdatePassword_etPasswordPrev)     EditText oldPassword;
    @BindView(R.id.dialogUpdatePassword_etPassword1)        EditText etPassword1;
    @BindView(R.id.dialogUpdatePassword_etPassword2)        EditText etPassword2;

    public final static String RESULT_PASSWORD    = "NEW PASSWORD";

    private CommonDialogListener commonDialogListener;

    public UpdatePasswordDialog(Context context, CommonDialogListener commonDialogListener) {
        super(context);
        this.commonDialogListener = commonDialogListener;
    }

    @Override
    public int onInit() {
        return R.layout.dialog_update_password;
    }

    @Override
    public void onCreate() {
        ButterKnife.bind(this, getDecorView());
    }

    @Override
    public void onShow() {
    }

    private void checkInputFields(){
        final String oldPass    = oldPassword.getText().toString();
        final String newPass1   = etPassword1.getText().toString();
        final String newPass2   = etPassword2.getText().toString();

        try {
            OkhomeUtil.chkException(!newPass1.equals(newPass2), "Passwords does not match.");
            OkhomeUtil.isValidPassword(oldPass);
            OkhomeUtil.isValidPassword(newPass1);
        } catch(OkhomeException e) {
            ToastUtil.showToast(e.getMessage());
            return;
        }
        onPasswordChangeDone(newPass1);
    }

    private void onPasswordChangeDone(String password) {
        commonDialogListener.onCommonDialogWorkDone(this, 1, OkhomeUtil.makeMap(RESULT_PASSWORD, password));
        dismiss();
    }

    @Override
    public UpdatePasswordDialog setCommonDialogListener(CommonDialogListener commonDialogListener) {
        this.commonDialogListener = commonDialogListener;
        return this;
    }

    @OnClick(R.id.dialogUpdatePassword_vbtnX)
    public void onClose() {
        dismiss();
    }

    @OnClick(R.id.dialogUpdatePassword_vbtnOK)
    public void onSubmit() {
        checkInputFields();
    }
}
