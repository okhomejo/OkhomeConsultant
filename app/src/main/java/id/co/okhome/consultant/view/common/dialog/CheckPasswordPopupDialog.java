package id.co.okhome.consultant.view.common.dialog;

import android.content.Context;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.exception.OkhomeException;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;

import static id.co.okhome.consultant.lib.dialog.DialogParent.CommonDialogListener.ACTIONCODE_OK;

/**
 * Created by frizurd on 19/03/2018.
 */

public class CheckPasswordPopupDialog extends DialogParent {

    @BindView(R.id.dialogCheckPassword_etPasswordPrev)   EditText currentPassword;

    public final static String CUR_PASSWORD    = "CURRENT PASSWORD";

    private CommonDialogListener commonDialogListener;

    public CheckPasswordPopupDialog(Context context, CommonDialogListener commonDialogListener) {
        super(context);
        this.commonDialogListener = commonDialogListener;
    }

    @Override
    public int onInit() {
        return R.layout.dialog_check_password;
    }

    @Override
    public void onCreate() {
        ButterKnife.bind(this, getDecorView());
    }

    @Override
    public void onShow() {

    }

    private void checkPasswordField() {
        final String curPass = currentPassword.getText().toString();

        try {
            OkhomeUtil.isValidPassword(curPass);
        } catch(OkhomeException e) {
            ToastUtil.showToast(e.getMessage());
            return;
        }
        onPasswordCorrect(curPass);
    }

    private void onPasswordCorrect(String password) {
        commonDialogListener.onCommonDialogWorkDone(this, 2, OkhomeUtil.makeMap(CUR_PASSWORD, password));
    }

    public void passwordError(String error) {
        currentPassword.setError(error);
        currentPassword.setText("");
    }

    @Override
    public CheckPasswordPopupDialog setCommonDialogListener(CommonDialogListener commonDialogListener) {
        this.commonDialogListener = commonDialogListener;
        return this;
    }

    @OnClick(R.id.dialogCheckPassword_vbtnX)
    public void onClose() {
        dismiss();
    }

    @OnClick(R.id.dialogCheckPassword_vbtnOK)
    public void onSubmit() {
        checkPasswordField();
    }
}
