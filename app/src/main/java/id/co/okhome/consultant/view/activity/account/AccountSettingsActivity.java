package id.co.okhome.consultant.view.activity.account;

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
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.dialog.CheckPasswordPopupDialog;
import id.co.okhome.consultant.view.dialog.CommonAlertDialog;
import id.co.okhome.consultant.view.activity.splash.SplashActivity;

/**
 * Created by frizurd on 16/03/2018.
 */

public class AccountSettingsActivity extends OkHomeParentActivity {

    @BindView(R.id.actAccount_vbtnChangePassword)       LinearLayout passwordLayout;

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

    @OnClick(R.id.actAccount_vbtnChangePassword)
    public void onClickChangePassword() {
        CheckPasswordPopupDialog checkPasswordDialog =
                new CheckPasswordPopupDialog(this, "CHECK");
        checkPasswordDialog.show();
    }

    @OnClick(R.id.actAccount_vbtnDeleteAccount)
    public void onClickDeleteAccount() {
        CheckPasswordPopupDialog checkPasswordDialog =
                new CheckPasswordPopupDialog(this, "DELETE");
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