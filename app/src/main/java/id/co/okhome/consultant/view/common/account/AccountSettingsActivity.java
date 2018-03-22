package id.co.okhome.consultant.view.common.account;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.config.OkhomeRegistryKey;
import id.co.okhome.consultant.lib.JoSharedPreference;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.consultant.model.v2.AccountModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.common.dialog.CheckPasswordPopupDialog;
import id.co.okhome.consultant.view.common.dialog.CommonAlertDialog;
import id.co.okhome.consultant.view.common.dialog.UpdatePasswordDialog;
import id.co.okhome.consultant.view.etc.SplashActivity;

/**
 * Created by frizurd on 16/03/2018.
 */

public class AccountSettingsActivity extends OkHomeParentActivity implements DialogParent.CommonDialogListener  {

    @BindView(R.id.actAccount_vbtnChangePassword)       LinearLayout passwordLayout;

    private CheckPasswordPopupDialog checkPasswordDialog;
    private UpdatePasswordDialog updatePasswordDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        OkhomeUtil.setWhiteSystembar(this);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        if(Objects.equals(ConsultantLoggedIn.get().signupBy, "EMAIL")) {
            passwordLayout.setVisibility(View.VISIBLE);
        }
    }

    public void updatePassword(String oldPassword, final String newPassword) {
        final ProgressDialog p = ProgressDialog.show(AccountSettingsActivity.this, null, "Updating password...");
        int consultantID = Integer.parseInt(ConsultantLoggedIn.get().id);
        OkhomeRestApi.getAccountClient().updatePassword(consultantID, oldPassword, newPassword)
                .enqueue(new RetrofitCallback<String>() {
                    @Override
                    public void onSuccess(String account) {
                        OkhomeUtil.showToast(AccountSettingsActivity.this, "Password has been changed!");
                        ConsultantLoggedIn.reload(new RetrofitCallback<AccountModel>() {
                            @Override
                            public void onSuccess(AccountModel result) {
                                JoSharedPreference.with().push(OkhomeRegistryKey.PASSWORD_LAST_LOGIN, newPassword);
                            }
                        });
                        updatePasswordDialog.dismiss();
                    }

                    @Override
                    public void onJodevError(ErrorModel jodevErrorModel) {
                        super.onJodevError(jodevErrorModel);
                        if (Objects.equals(jodevErrorModel.code, "-101")) {
                            OkhomeUtil.showToast(AccountSettingsActivity.this, "Make sure your current password is correct.");
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        p.dismiss();
                    }
                });
    }

    private void checkPassword(final String password) {
        final ProgressDialog p = ProgressDialog.show(AccountSettingsActivity.this, null, "Checking password...");
        OkhomeRestApi.getAccountClient().login(ConsultantLoggedIn.get().email, password)
                .enqueue(new RetrofitCallback<AccountModel>() {
                    @Override
                    public void onSuccess(AccountModel account) {
                        checkPasswordDialog.dismiss();
                        updatePasswordDialog = new UpdatePasswordDialog(AccountSettingsActivity.this, AccountSettingsActivity.this, password);
                        updatePasswordDialog.show();
                    }

                    @Override
                    public void onJodevError(ErrorModel jodevErrorModel) {
                        super.onJodevError(jodevErrorModel);
                        checkPasswordDialog.passwordError("Incorrect password.");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        p.dismiss();
                    }
                });
    }

    private void deleteAccount(String curPassword) {
        final ProgressDialog p = ProgressDialog.show(AccountSettingsActivity.this, null, "Deleting account...");
        int consultantID = Integer.parseInt(ConsultantLoggedIn.get().id);
        OkhomeRestApi.getAccountClient().deleteAccount(consultantID, curPassword)
                .enqueue(new RetrofitCallback<String>() {
                    @Override
                    public void onSuccess(String account) {
                        ConsultantLoggedIn.clear();
                        OkhomeUtil.showToast(AccountSettingsActivity.this, account + ": Account has been deleted.");
                        finishAllActivities();
                        startActivity(new Intent(AccountSettingsActivity.this, SplashActivity.class));
                    }

                    @Override
                    public void onJodevError(ErrorModel jodevErrorModel) {
                        super.onJodevError(jodevErrorModel);
                        if (Objects.equals(jodevErrorModel.code, "-101")) {
                            checkPasswordDialog.passwordError("Incorrect password.");
                        } else {
                            checkPasswordDialog.passwordError("Error, please try again later.");
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        p.dismiss();
                    }
                });
    }

    @Override
    public void onCommonDialogWorkDone(Dialog dialog, int actionCode, Map<String, Object> mapResult) {
        if(actionCode == 1) {
            String newPassword = (String) mapResult.get(UpdatePasswordDialog.RESULT_PASSWORD);
            String curPassword = (String) mapResult.get(UpdatePasswordDialog.CURRENT_PASSWORD);

            updatePassword(curPassword, newPassword);

        } else if (actionCode == 2) {
            String curPassword = (String) mapResult.get(CheckPasswordPopupDialog.CUR_PASSWORD);
            String action      = (String) mapResult.get(CheckPasswordPopupDialog.DIALOG_ACTION);

            if (Objects.equals(action, "DELETE")) {
                deleteAccount(curPassword);
            } else if (Objects.equals(action, "CHECK")) {
                checkPassword(curPassword);
            }
        }
    }

    @OnClick(R.id.actAccount_vbtnChangePassword)
    public void onClickChangePassword() {

        checkPasswordDialog = new CheckPasswordPopupDialog(this, this, "CHECK");
        checkPasswordDialog.show();
    }

    @OnClick(R.id.actAccount_vbtnDeleteAccount)
    public void onClickDeleteAccount() {
        checkPasswordDialog = new CheckPasswordPopupDialog(this, this, "DELETE");
        checkPasswordDialog.show();
    }

    @OnClick(R.id.actAccount_vbtnLogOut)
    public void onClickLogOut() {
        new CommonAlertDialog(this)
                .setSubTitle("Are you sure you want to log out?")
                .setTitle("LOG OUT")
                .setCommonDialogListener(new DialogParent.CommonDialogListener() {
                    @Override
                    public void onCommonDialogWorkDone(Dialog dialog, int actionCode, Map<String, Object> mapResult) {
                        if(actionCode == ACTIONCODE_OK){
                            final ProgressDialog p = ProgressDialog.show(AccountSettingsActivity.this, null, "Logging out...");
                            int accountID = Integer.parseInt(ConsultantLoggedIn.get().id);
                            OkhomeRestApi.getAccountClient().logout(accountID).enqueue(new RetrofitCallback<String>() {

                                @Override
                                public void onSuccess(final String msg) {
                                    ToastUtil.showToast("Successfully logged out.");
                                    ConsultantLoggedIn.clear();
                                }

                                @Override
                                public void onFinish() {
                                    finishAllActivities();
                                    startActivity(new Intent(AccountSettingsActivity.this, SplashActivity.class));
                                    p.dismiss();
                                }
                            });
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @OnClick(R.id.actAccount_vbtnX)
    public void onClickCloseActivity() {
        finish();
    }
}
