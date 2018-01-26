package id.co.okhome.consultant.view.account;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.OkHomeParentActivity;
import id.co.okhome.consultant.view.common.dialog.PhoneVerificationDialog;
import id.co.okhome.consultant.view.userinfo.trainee.FillupUserInfoActivity;

public class SignupActivity extends OkHomeParentActivity {

    @BindView(R.id.actSignup_etEmail)           EditText etEmail;
    @BindView(R.id.actSignup_etPassword)        EditText etPassword;
    @BindView(R.id.actSignup_etPasswordOneMore) EditText etPasswordOneMore;
    @BindView(R.id.actSignup_tvPhone)           TextView tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);
    }

    //check exception before signup
    private void checkBeforeSignup(){

        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        final String passwordOneMore = etPasswordOneMore.getText().toString();
        final String phone = tvPhone.getText().toString();

//        try{
//            OkhomeUtil.chkException(!OkhomeUtil.isValidEmail(email), "Check your email.");
//            OkhomeUtil.isValidPassword(password);
//            OkhomeUtil.chkException(!password.equals(passwordOneMore), "Passwords do not match.");
//
//        }catch(OkhomeException e){
//            OkhomeUtil.showToast(this, e.getMessage());
//            return;
//        }

        signup();
    }

    private void signup(){
        //connect to server...

        startActivity(new Intent(this, FillupUserInfoActivity.class));
    }

    @OnClick(R.id.actSignUp_vbtnSignup)
    public void onClickSignUp(){
        checkBeforeSignup();
    }

    @OnClick(R.id.actSignup_vbtnGoogle)
    public void onClickGoogle(){

    }

    @OnClick(R.id.actSignup_vbtnTermsAndConditions)
    public void onClickTermsAndConditions(){

    }

    @OnClick(R.id.actSignup_tvPhone)
    public void onClickPhone(){
        new PhoneVerificationDialog(this).show();
    }





}
