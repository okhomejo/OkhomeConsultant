package id.co.okhome.consultant.view.common.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.EditText;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.config.OkhomeRegistryKey;
import id.co.okhome.consultant.exception.OkhomeException;
import id.co.okhome.consultant.lib.JoSharedPreference;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.consultant.model.v2.AccountModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;

/**
 * Created by frizurd on 16/03/2018.
 */

public class UpdatePasswordDialog extends DialogParent {

    @BindView(R.id.dialogUpdatePassword_etPassword1)        EditText etPassword1;
    @BindView(R.id.dialogUpdatePassword_etPassword2)        EditText etPassword2;

    private Context context;
    private String oldPassword;

    public UpdatePasswordDialog(Context context, String oldPassword) {
        super(context);
        this.context = context;
        this.oldPassword = oldPassword;
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

    public void updatePassword(String oldPassword, final String newPassword) {
        final ProgressDialog p = ProgressDialog.show(context, null, "Updating password...");
        int consultantID = Integer.parseInt(ConsultantLoggedIn.get().id);
        OkhomeRestApi.getAccountClient().updatePassword(consultantID, oldPassword, newPassword)
                .enqueue(new RetrofitCallback<String>() {
                    @Override
                    public void onSuccess(String account) {
                        OkhomeUtil.showToast(context, "Password has been changed!");
                        ConsultantLoggedIn.reload(new RetrofitCallback<AccountModel>() {
                            @Override
                            public void onSuccess(AccountModel result) {
                                JoSharedPreference.with().push(OkhomeRegistryKey.PASSWORD_LAST_LOGIN, newPassword);
                            }
                        });
                        dismiss();
                    }

                    @Override
                    public void onJodevError(ErrorModel jodevErrorModel) {
                        super.onJodevError(jodevErrorModel);
                        if (Objects.equals(jodevErrorModel.code, "-101")) {
                            OkhomeUtil.showToast(context, "Make sure your current password is correct.");
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        p.dismiss();
                    }
                });
    }

    private void checkInputFields(){
        final String newPass1   = etPassword1.getText().toString();
        final String newPass2   = etPassword2.getText().toString();

        try {
            OkhomeUtil.chkException(!newPass1.equals(newPass2), "Passwords does not match.");
            OkhomeUtil.isValidPassword(newPass1);
        } catch(OkhomeException e) {
            ToastUtil.showToast(e.getMessage());
            return;
        }
        updatePassword(oldPassword, newPass1);
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
