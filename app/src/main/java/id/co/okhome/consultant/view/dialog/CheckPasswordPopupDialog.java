package id.co.okhome.consultant.view.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.exception.OkhomeException;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.consultant.model.v2.AccountModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.activity.splash.SplashActivity;
import static id.co.okhome.consultant.lib.app.OkHomeParentActivity.finishAllActivities;

/**
 * Created by frizurd on 19/03/2018.
 */

public class CheckPasswordPopupDialog extends DialogParent {

    @BindView(R.id.dialogCheckPassword_etPasswordPrev)   EditText currentPassword;

    private String action;
    private Context context;

    public CheckPasswordPopupDialog(Context context, String action) {
        super(context);
        this.context = context;
        this.action = action;
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

    private void checkPassword(final String password) {
        final ProgressDialog p = ProgressDialog.show(context, null, "Checking password...");
        OkhomeRestApi.getAccountClient().login(ConsultantLoggedIn.get().email, password)
                .enqueue(new RetrofitCallback<AccountModel>() {
                    @Override
                    public void onSuccess(AccountModel account) {
                        dismiss();
                        switch (action) {
                            case "CHECK":
                                UpdatePasswordDialog updatePasswordDialog = new UpdatePasswordDialog(context, password);
                                updatePasswordDialog.show();
                                break;
                            case "DELETE":
                                deleteAccount(password);
                                break;
                        }
                    }

                    @Override
                    public void onJodevError(ErrorModel jodevErrorModel) {
                        super.onJodevError(jodevErrorModel);
                        passwordError("Incorrect password.");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        p.dismiss();
                    }
                });
    }

    private void deleteAccount(String curPassword) {
        final ProgressDialog p = ProgressDialog.show(context, null, "Deleting account...");
        int consultantID = Integer.parseInt(ConsultantLoggedIn.get().id);
        OkhomeRestApi.getAccountClient().deleteAccount(consultantID, curPassword)
                .enqueue(new RetrofitCallback<String>() {
                    @Override
                    public void onSuccess(String account) {
                        ConsultantLoggedIn.clear();
                        OkhomeUtil.showToast(context, account + ": Account has been deleted.");
                        finishAllActivities();
                        context.startActivity(new Intent(context, SplashActivity.class));
                    }

                    @Override
                    public void onJodevError(ErrorModel jodevErrorModel) {
                        super.onJodevError(jodevErrorModel);
                        if (Objects.equals(jodevErrorModel.code, "-101")) {
                            passwordError("Incorrect password.");
                        } else {
                            passwordError("Error, please try again later.");
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        p.dismiss();
                    }
                });
    }

    private void checkPasswordField() {
        final String curPass = currentPassword.getText().toString();

        try {
            OkhomeUtil.isValidPassword(curPass);
        } catch(OkhomeException e) {
            ToastUtil.showToast(e.getMessage());
            return;
        }
        checkPassword(curPass);
    }

    public void passwordError(String error) {
        currentPassword.setError(error);
        currentPassword.setText("");
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
