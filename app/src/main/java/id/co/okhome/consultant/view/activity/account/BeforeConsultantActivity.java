package id.co.okhome.consultant.view.activity.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.v2.AccountModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.activity.account.profile.FillupUserInfoActivity;
import id.co.okhome.consultant.view.activity.etc.AboutOkhomeActivity;
import id.co.okhome.consultant.view.activity.faq.FaqActivity;
import id.co.okhome.consultant.view.activity.main.ConsultantMainActivity;

public class BeforeConsultantActivity extends OkHomeParentActivity {



    @BindView(R.id.actBeforeConsultant_tvAlert)
    TextView tvAlert;

    @BindView(R.id.actBeforeConsultant_tvProfileInfo)
    TextView tvProfileInfo;

    @BindView(R.id.actBeforeConsultant_tvAccountEmail)
    TextView tvAccountEmail;

    @BindView(R.id.actBeforeConsultant_ivProfileImage)
    ImageView ivProfile;

    @BindView(R.id.actBeforeConsultant_vbtnManual)
    ViewGroup vgManual;

    @BindView(R.id.actBeforeConsultant_pbLoading)
    ProgressBar pbLoading;

    AccountModel account = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_consultant);
        OkhomeUtil.setWhiteSystembar(this);
        ButterKnife.bind(this);
        //트레이니 승인대기중 및 프로필 업데이트 화면 보여줘야할때
//        FillupUserInfoActivity

        //블락먹었을때

        //트레이니일때 이렇게 들어옴.
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    private void adaptDataAndViews(){

        //도중에 업데이트 되었으면 컨설턴트 메인으로 고고
        if(account.consultant != null){
            startActivity(new Intent(this, ConsultantMainActivity.class));
            finish();
            return;
        }

        vgManual.setVisibility(View.GONE);

        if(account.blocked != null){
            tvAlert.setText(account.blocked.toString());
        }

        else if(account.trainee.approveYN.equals("Y")){
            tvAlert.setText("You are under consultant training.");
            vgManual.setVisibility(View.VISIBLE);
        }

        else{
            tvAlert.setText("You has not been approved yet");
        }



        String accountType = "", gender = "";
        if (account.type == null){
            ;
        }
        else if (account.type.equals("T")) {
            accountType = "Trainee";
        } else if (account.type.equals("C")) {
            accountType = "Consultant";
        }

        if (account.profile.gender == null){

        }else if (account.profile.gender.equals("M")) {
            gender = "Male";
        }else if (account.profile.gender.equals("F")) {
            gender = "Female";
        }

        String personInfo = "";

        if(account.profile.name != null){
            personInfo += account.profile.name;
        }

        if(!accountType.equals("")){
            personInfo += ", " + accountType;
        }
        if(!gender.equals("") ){
            personInfo += ", " + gender;
        }

        if(personInfo ==null){
            personInfo = "Update your profile.";
        }

        if(personInfo.equals("")){
            tvProfileInfo.setText("Set profile");
        }else{
            tvProfileInfo.setText(personInfo.substring(2));
        }

        tvAccountEmail.setText(account.email);

        Glide.with(this).load(account.profile.photoUrl).error(R.drawable.img_user_blank).thumbnail(0.5f).into(ivProfile);
    }

    private void getUserInfo(){
        final ProgressDialog p = OkhomeUtil.showLoadingDialog(this);
        OkhomeRestApi.getAccountClient().getInfo(Integer.parseInt(ConsultantLoggedIn.id())).enqueue(new RetrofitCallback<AccountModel>() {
            @Override
            public void onSuccess(AccountModel account) {
                BeforeConsultantActivity.this.account = account;
                adaptDataAndViews();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                p.dismiss();
            }
        });

    }

    @OnClick(R.id.actBeforeConsultant_vbtnProfile)
    public void onProfile(View v){
        startActivity(new Intent(this, FillupUserInfoActivity.class));
    }

    @OnClick(R.id.actBeforeConsultant_vbtnAccount)
    public void onAccount(View v){
        startActivity(new Intent(this, AccountSettingsActivity.class));
    }

    @OnClick(R.id.actBeforeConsultant_vbtnAbout)
    public void onAbout(View v){
        startActivity(new Intent(this, AboutOkhomeActivity.class));
    }
    @OnClick(R.id.actBeforeConsultant_vbtnManual)
    public void onFaq(View v){
        FaqActivity.startFaqActivity(this, "MANUAL", "MANUAL_TRAINEE", 0);
    }


}
