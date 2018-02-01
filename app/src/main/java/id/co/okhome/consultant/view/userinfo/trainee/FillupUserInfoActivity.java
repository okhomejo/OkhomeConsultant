package id.co.okhome.consultant.view.userinfo.trainee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.exception.OkhomeException;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.ConsultantModel;

public class FillupUserInfoActivity extends OkHomeParentActivity {

    final int REQ_GET_PHOTO_FOR_KTP             = 10001;
    final int REQ_GET_PHOTO_FOR_MYPHOTO         = 10002;

    @BindView(R.id.actFillUpUserInfo_vLoading)                          View vLoading;
    @BindView(R.id.actFillupUserInfo_ivBarEducation)                    ImageView ivBarEdu;
    @BindView(R.id.actFillupUserInfo_ivBarJobExp)                       ImageView ivBarJob;
    @BindView(R.id.actFillupUserInfo_ivBarBasicInfo)                    ImageView ivBarBasicInfo;
    @BindView(R.id.actFillupUserInfo_ivBarKTP)                          ImageView ivBarKTP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fillup_userinfo);

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        reload();
    }


    //check exception. if no error, go to next page.
    private void confirm(){

        /**
         * Note To Fritz
         * Check data entry completion
         * If All sections are completed, Customer request it to server and can go TraineeScreeningMornitorActivity.
         * */
        ConsultantModel consultant = ConsultantLoggedIn.get();

        //Section1 data entry completion check
        try{
            OkhomeException.chkException(consultant.name == null, "");
            OkhomeException.chkException(consultant.phone == null, "");
            OkhomeException.chkException(consultant.gender == null, "");
            OkhomeException.chkException(consultant.address == null, "");

        }catch(OkhomeException e){
            ToastUtil.showToast("Data entry(Basic Informations) must be completed.");
            return;
        }

        //Section1 data entry completion check
        startActivity(new Intent(this, TraineeScreeningMornitorActivity.class));
    }

    //check progress to turn on/off left bar lamp.
    private void chkProgress(){
        /**
         * Note To Fritz
         * Check data entry completion and as a result, turn on/off left bar lamp.
         *
         * */
        ConsultantModel consultant = ConsultantLoggedIn.get();

        //step 1.
        try{
            OkhomeException.chkException(consultant.name == null, "");
            OkhomeException.chkException(consultant.phone == null, "");
            OkhomeException.chkException(consultant.gender == null, "");
            OkhomeException.chkException(consultant.address == null, "");

            ivBarBasicInfo.setBackgroundColor(ContextCompat.getColor(this, R.color.colorOkhome));
        }catch(OkhomeException e){
            ivBarBasicInfo.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightBlueGray2));
        }

        //step 2. ~~~


        //step. ktp photo
        try{
            OkhomeException.chkException(consultant.ktpPhotoUrl == null, "");

            ivBarKTP.setBackgroundColor(ContextCompat.getColor(this, R.color.colorOkhome));
        }catch(OkhomeException e){
            ivBarKTP.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightBlueGray2));
        }

    }

    //get progress of registering info from server
    private void reload(){
        vLoading.setVisibility(View.VISIBLE);

        ConsultantLoggedIn.reload(new RetrofitCallback<ConsultantModel>() {
            @Override
            public void onSuccess(ConsultantModel result) {
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
    private void onPhotoChoosed(final int requestCode, final String imgPath){

//        final ProgressDialog p = ProgressDialog.show(this, "", "Upload photo");
//        new ImageUploadCall(imgPath).asyncWork(new ApiResultCallback<String>() {
//            @Override
//            public void onFinish(ApiResult<String> apiResult) {
//                p.dismiss();
//                if(apiResult.resultCode == 200){
//
//                    //update photo url
//                    switch(requestCode){
//                        case REQ_GET_PHOTO_FOR_KTP:
//                            updatePhoto("ktp_photo_url", imgPath);
//                            break;
//
//                    }
//
//                }else{
//                    //faild
//                    ToastUtil.showToast(apiResult.result);
//                }
//            }
//        });
    }

    //--------on click
    @OnClick(R.id.actFillUpUserInfo_vbtnKartuTandaPerduduk)
    public void onClickKTP(View v){
//        startActivityForResult(new Intent(this, ImageChooserActivity.class), REQ_GET_PHOTO_FOR_KTP);
    }

    @OnClick({R.id.actFillUpUserInfo_vbtnBasicInformation})
    public void onClickBasicInfo(View v){
        startActivity(new Intent(this, UpdateUserDocumentActivity.class));
    }

    @OnClick(R.id.actFillUpUserInfo_vbtnAddtionalInformation)
    public void onAdditionalInfoClick(){
        startActivity(new Intent(this, UpdateExtraUserDocumentActivity.class));
    }

    @OnClick(R.id.actFillUpUserInfo_vgConfirm)
    public void onClickConfirm(){
        confirm();

    }

}
