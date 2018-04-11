package id.co.okhome.consultant.view.activity.account.profile;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.exception.OkhomeException;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.lib.jobrowser.callback.ApiResultCallback;
import id.co.okhome.consultant.lib.jobrowser.model.ApiResult;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.v2.AccountModel;
import id.co.okhome.consultant.model.v2.ProfileModel;
import id.co.okhome.consultant.rest_apicall.raw_restapi.ImageUploadCall;
import id.co.okhome.consultant.view.dialog.PhotoDialog;

public class FillupUserInfoActivity extends OkHomeParentActivity {

    final int REQ_GET_PHOTO_FOR_KTP             = 10001;
    final int REQ_GET_PHOTO_FOR_MYPHOTO         = 10002;

    @BindView(R.id.actFillUpUserInfo_vLoading)                          View vLoading;
    @BindView(R.id.actFillupUserInfo_ivBarEducation)                    ImageView ivBarEdu;
    @BindView(R.id.actFillupUserInfo_ivBarJobExp)                       ImageView ivBarJob;
    @BindView(R.id.actFillupUserInfo_ivBarBasicInfo)                    ImageView ivBarBasicInfo;
    @BindView(R.id.actFillupUserInfo_ivBarAdditionalInfo)               ImageView ivBarAdditionalInfo;
    @BindView(R.id.actFillupUserInfo_ivBarKTP)                          ImageView ivBarKTP;
    @BindView(R.id.actFillupUserInfo_ivBarPreferenceArea)               ImageView ivBarPreferenceArea;
    @BindView(R.id.actFillUpUserInfo_tvTitle)                           TextView tvTitle;
    @BindView(R.id.actFillUpUserInfo_tvExtraInfo)                       TextView tvExtraInfo;
    @BindView(R.id.actFillupUserInfo_vgProfileNotUpdatedYet)
    ViewGroup vgProfileNot;
    private boolean accountApproved = false;
    PhotoDialog ktpPhotoDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fillup_userinfo);

        ButterKnife.bind(this);
        OkhomeUtil.setWhiteSystembar(this);

        initViewsAndData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reload();
    }

    private void initViewsAndData() {

        if(ConsultantLoggedIn.get().type.equals("C")){
            tvTitle.setText("Profile information");
            accountApproved = true;

        }else{
            if (ConsultantLoggedIn.get().trainee.approveYN.equals("Y")) {
                accountApproved = true;
                tvTitle.setText("Profile information");
            }
        }


    }

    //check exception. if no error, go to next page.
    private void confirm(){

        ProfileModel profile = ConsultantLoggedIn.get().profile;

        //Section1 data entry completion check
        try{
            OkhomeException.chkException(profile.name == null, "");
            OkhomeException.chkException(profile.phone == null, "");
            OkhomeException.chkException(profile.gender == null, "");
            OkhomeException.chkException(profile.address == null, "");

        }catch(OkhomeException e){
            ToastUtil.showToast("Data entry(Basic Information) must be completed.");
            return;
        }

        //Section2 data entry completion check
        try{
            OkhomeException.chkException(profile.nik == null, "");
            OkhomeException.chkException(profile.marriedYN == null, "");
            OkhomeException.chkException(profile.religion == null, "");
            OkhomeException.chkException(profile.bikeYN == null, "");
            OkhomeException.chkException(profile.likeDogYN == null, "");

        }catch(OkhomeException e){
            ToastUtil.showToast("Data entry(Additional Information) must be completed.");
            return;
        }

        //Section3 data entry completion check
        try{
            OkhomeException.chkException(profile.ktpPhotoUrl == null, "");

        }catch(OkhomeException e){
            ToastUtil.showToast("Data entry(KTP Information) must be completed.");
            return;
        }

        //Section4 data entry completion check
        try{
            OkhomeException.chkException(profile.workingRegions == null, "");

        }catch(OkhomeException e){
            ToastUtil.showToast("Data entry(Preferred area) must be completed.");
            return;
        }

        //Section5 data entry completion check
        try{
            OkhomeException.chkException(OkhomeUtil.isEmpty(profile.sdPhotoUrl)
                    && OkhomeUtil.isEmpty(profile.smaPhotoUrl)
                    && OkhomeUtil.isEmpty(profile.smpPhotoUrl)
                    && OkhomeUtil.isEmpty(profile.univPhotoUrl), "");

        }catch(OkhomeException e){
            ToastUtil.showToast("Data entry(Education) must be completed.");
            return;
        }

        //Section6 data entry completion check
        try{
            OkhomeException.chkException(OkhomeUtil.isEmpty(profile.pastCareers), "");

        }catch(OkhomeException e){
            ToastUtil.showToast("Data entry(Job Experience) must be completed.");
            return;
        }

        // Finish trainee registration process
        startActivity(new Intent(this, TraineeScreeningMornitorActivity.class));
    }

    //check progress to turn on/off left bar lamp.
    private void chkProgress(){

        ProfileModel profile = ConsultantLoggedIn.get().profile;

        //step 1. Basic information
        try{
            OkhomeException.chkException(profile.name == null, "");
            OkhomeException.chkException(profile.phone == null, "");
            OkhomeException.chkException(profile.gender == null, "");
            OkhomeException.chkException(profile.address == null, "");

            ivBarBasicInfo.setBackgroundColor(ContextCompat.getColor(this, R.color.colorOkhome));
        }catch(OkhomeException e){
            ivBarBasicInfo.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightBlueGray2));
        }

        //step 2. Additional information
        try{
            OkhomeException.chkException(profile.nik == null, "");
            OkhomeException.chkException(profile.marriedYN == null, "");
            OkhomeException.chkException(profile.religion == null, "");
            OkhomeException.chkException(profile.bikeYN == null, "");
            OkhomeException.chkException(profile.likeDogYN == null, "");

            ivBarAdditionalInfo.setBackgroundColor(ContextCompat.getColor(this, R.color.colorOkhome));
        }catch(OkhomeException e){
            ivBarAdditionalInfo.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightBlueGray2));
        }

        //step 3. KTP photo
        try{
            OkhomeException.chkException(profile.ktpPhotoUrl == null, "");

            ivBarKTP.setBackgroundColor(ContextCompat.getColor(this, R.color.colorOkhome));
        }catch(OkhomeException e){
            ivBarKTP.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightBlueGray2));
        }

        //step 4. Preferred area for cleaning
        try{
            OkhomeException.chkException(profile.workingRegions == null, "");

            ivBarPreferenceArea.setBackgroundColor(ContextCompat.getColor(this, R.color.colorOkhome));
        }catch(OkhomeException e){
            ivBarPreferenceArea.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightBlueGray2));
        }

        //step 5. Education information
        try{
            OkhomeException.chkException(OkhomeUtil.isEmpty(profile.sdPhotoUrl)
                    && OkhomeUtil.isEmpty(profile.smaPhotoUrl)
                    && OkhomeUtil.isEmpty(profile.smpPhotoUrl)
                    && OkhomeUtil.isEmpty(profile.univPhotoUrl), "");

            ivBarEdu.setBackgroundColor(ContextCompat.getColor(this, R.color.colorOkhome));
        }catch(OkhomeException e){
            ivBarEdu.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightBlueGray2));
        }

        //step 6. Job experience
        try{
            OkhomeException.chkException(OkhomeUtil.isEmpty(profile.pastCareers), "");
            ivBarJob.setBackgroundColor(ContextCompat.getColor(this, R.color.colorOkhome));
        }catch(OkhomeException e){
            ivBarJob.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightBlueGray2));
        }
        if(profile.confirmYN.equals("Y")){
            vgProfileNot.setVisibility(View.GONE);
        }else{
            vgProfileNot.setVisibility(View.VISIBLE);
        }
    }

    //get progress of registering info from server
    private void reload(){
        vLoading.setVisibility(View.VISIBLE);

        ConsultantLoggedIn.reload(new RetrofitCallback<AccountModel>() {
            @Override
            public void onSuccess(AccountModel result) {
                ;
                chkProgress();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                vLoading.setVisibility(View.GONE);
            }
        });
    }

    //update photo field. after that, call reload.
    private void updatePhoto(String photoFieldName, String imgPath){
        vLoading.setVisibility(View.VISIBLE);
        ConsultantLoggedIn.updateUserInfo(OkhomeUtil.makeMap(photoFieldName, imgPath), new RetrofitCallback<String>() {
            @Override
            public void onSuccess(String result) {
                reload();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                vLoading.setVisibility(View.GONE);
            }
        });
    }

    //on photo choosed
    private void onKtpPhotoChoosed(final String imgPath){

        final ProgressDialog p = ProgressDialog.show(this, "", "Upload KTP");
        new ImageUploadCall(imgPath).asyncWork(new ApiResultCallback<String>() {
            @Override
            public void onFinish(ApiResult<String> apiResult) {
                p.dismiss();
                if(apiResult.resultCode == 200){
                    //update photo url
                    updatePhoto("ktp_photo_url", apiResult.object);
                }else{
                    //failed
                    ToastUtil.showToast(apiResult.result);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(ktpPhotoDialog != null){
            ktpPhotoDialog.onActivityResult(requestCode, resultCode, data);
        }
    }

    //--------on click
    @OnClick({R.id.actFillUpUserInfo_vbtnKTP, R.id.actFillUpUserInfo_tvKTP})
    public void onClickKTP(View v){
        if(ktpPhotoDialog == null){
            ktpPhotoDialog = new PhotoDialog(FillupUserInfoActivity.this,
                    "Change KTP photo",
                    ConsultantLoggedIn.get().profile.ktpPhotoUrl,
                    new DialogParent.CommonDialogListener() {
                        @Override
                        public void onCommonDialogWorkDone(Dialog dialog, int actionCode, Map<String, Object> mapResult) {
                            ;
                            if(actionCode == ACTIONCODE_OK){
                                String imgPath = (String)mapResult.get("imgPath");
                                onKtpPhotoChoosed(imgPath);
                            }
                        }
                    });
        }

        ktpPhotoDialog.show();

//        if(TextUtils.isEmpty(ConsultantLoggedIn.get().profile.ktpPhotoUrl)){
//            startActivityForResult(new Intent(this, ImageChooserActivity.class), REQ_GET_PHOTO_FOR_KTP);
//        }else{
//            if(ktpPhotoDialog == null){
//                ktpPhotoDialog = new PhotoDialog(FillupUserInfoActivity.this,
//                        "Change KTP photo",
//                        ConsultantLoggedIn.get().profile.ktpPhotoUrl,
//                        new DialogParent.CommonDialogListener() {
//                            @Override
//                            public void onCommonDialogWorkDone(Dialog dialog, int actionCode, Map<String, Object> mapResult) {
//                                ;
//                                if(actionCode == ACTIONCODE_OK){
//                                    String imgPath = (String)mapResult.get("imgPath");
//                                    onKtpPhotoChoosed(imgPath);
//                                }
//                            }
//                        });
//            }
//
//            ktpPhotoDialog.show();
//
//
//        }
    }

    @OnClick({R.id.actFillUpUserInfo_vbtnBasicInformation})
    public void onClickBasicInfo(View v){
        startActivity(new Intent(this, UpdateUserDocumentActivity.class));
    }

    @OnClick(R.id.actFillUpUserInfo_vbtnAddtionalInformation)
    public void onAdditionalInfoClick(){
        startActivity(new Intent(this, UpdateExtraUserDocumentActivity.class));
    }

    @OnClick(R.id.actFillUpUserInfo_vbtnEducation)
    public void onEducationClick(){
        startActivity(new Intent(this, UpdateConsultantEducationActivity.class));
    }

    @OnClick(R.id.actFillUpUserInfo_vbtnPreferenceArea)
    public void onPreferenceAreaClick(){
        startActivity(new Intent(this, UpdateConsultantAreaActivity.class));
    }

    @OnClick(R.id.actFillUpUserInfo_vbtnJobExperience)
    public void onJobExperienceClick(){
        startActivity(new Intent(this, UpdateJobExperienceActivity.class));
    }

    @OnClick(R.id.actFillUpUserInfo_vgConfirm)
    public void onClickConfirm(){
        if (accountApproved) {
            finish();
        } else {
            confirm();
        }
    }

    @OnClick(R.id.actFillUpUserInfo_vbtnX)
    public void onCloseActivityClick() {
        finish();
    }
}