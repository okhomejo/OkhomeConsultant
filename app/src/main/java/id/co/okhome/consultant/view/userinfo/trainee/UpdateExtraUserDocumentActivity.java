package id.co.okhome.consultant.view.userinfo.trainee;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.exception.OkhomeException;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.jobrowser.callback.ApiResultCallback;
import id.co.okhome.consultant.lib.jobrowser.model.ApiResult;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.consultant.model.ConsultantModel;
import id.co.okhome.consultant.rest_apicall.raw_restapi.ImageUploadCall;
import id.co.okhome.consultant.view.common.dialog.CommonListDialog;
import id.co.okhome.consultant.view.viewholder.StringHolder;

public class UpdateExtraUserDocumentActivity extends OkHomeParentActivity {

    @BindView(R.id.actUpdateUserExtraDoc_etNIK)         EditText etNIK;
    @BindView(R.id.actUpdateUserExtraDoc_tvMarried)     TextView tvMarried;
    @BindView(R.id.actUpdateUserExtraDoc_tvReligion)    TextView tvReligion;
    @BindView(R.id.actUpdateUserExtraDoc_ivChkBikeYES)  ImageView btnBikeYes;
    @BindView(R.id.actUpdateUserExtraDoc_ivChkBikeNO)   ImageView btnBikeNo;
    @BindView(R.id.actUpdateUserExtraDoc_ivChkDogYES)   ImageView btnDogYes;
    @BindView(R.id.actUpdateUserExtraDoc_ivChkDogNO)    ImageView btnDogNo;

    private ConsultantModel consultant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_extra_document);
        ButterKnife.bind(this);
        OkhomeUtil.setSystemBarColor(this,

//                Color.parseColor("#29313a"));
                ContextCompat.getColor(this, R.color.colorOkhome));

        init();
    }

    private void init(){

        if (ConsultantLoggedIn.hasSavedData()) {
            consultant = ConsultantLoggedIn.get();

            etNIK.setText(consultant.nik);
            tvReligion.setText(consultant.religion);

            if (!TextUtils.isEmpty(consultant.marriedYN)) {
                String married = "";
                if (consultant.marriedYN.equals("Y")) {
                    married = "Married";
                } else if (consultant.marriedYN.equals("N")) {
                    married = "Not yet";
                }
                tvMarried.setText(married);
            }
            if (!TextUtils.isEmpty(consultant.bikeYN)) {
                if (consultant.bikeYN.equals("Y")) {
                    btnBikeYes.setImageResource(R.drawable.ic_checked);
                } else if (consultant.bikeYN.equals("N")) {
                    btnBikeNo.setImageResource(R.drawable.ic_checked);
                }
            }

            if (!TextUtils.isEmpty(consultant.likeDogYN)) {
                if (consultant.likeDogYN.equals("Y")) {
                    btnDogYes.setImageResource(R.drawable.ic_checked);
                } else if (consultant.likeDogYN.equals("N")) {
                    btnDogNo.setImageResource(R.drawable.ic_checked);
                }
            }
        }
    }

    private void updateProfile() {

        final String nik        = etNIK.getText().toString();
        final String married    = consultant.marriedYN;
        final String religion   = consultant.religion;
        final String hasBike    = consultant.bikeYN;
        final String okWithDog  = consultant.likeDogYN;

        try {
            OkhomeException.chkException(nik.length() != 16, "NIK must be 16 numbers");
            OkhomeException.chkException(OkhomeUtil.isEmpty(married), "Please state if you are married");
            OkhomeException.chkException(OkhomeUtil.isEmpty(religion), "Please state your religion");
            OkhomeException.chkException(OkhomeUtil.isEmpty(hasBike), "Please state if you have a motorbike");
            OkhomeException.chkException(OkhomeUtil.isEmpty(okWithDog), "Please state if you are OK with cleaning a house with a dog");

        } catch (OkhomeException e) {
            ToastUtil.showToast(e.getMessage());
            return;
        }

        final ProgressDialog p = ProgressDialog.show(this, "", "Loading");
        final RetrofitCallback retrofitCallback = new RetrofitCallback<String>() {

            @Override
            public void onSuccess(String result) {
                finish();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                p.dismiss();
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                super.onJodevError(jodevErrorModel);

                ToastUtil.showToast(jodevErrorModel.message + jodevErrorModel.code);
            }
        };

        ConsultantLoggedIn.updateUserInfo(
                OkhomeUtil.makeMap("nik", nik, "married_yn", married, "religion", religion, "bike_yn", hasBike, "like_dog_yn", okWithDog), retrofitCallback
        );
    }

    //-- onclick methods ---------------------------------
    @OnClick(R.id.actUpdateUserExtraDoc_vgMarried)
    public void onClickMarried(){

        new CommonListDialog(this)
                .setTitle("Married?")
                .setArrItems("Married", "Not yet")
                .setArrItemTag("Y", "N")
                .setItemClickListener(new StringHolder.ItemClickListener() {
                    @Override
                    public void onItemClick(Dialog dialog, int pos, String value, String tag) {
                        dialog.dismiss();
                        tvMarried.setText(value);
                        consultant.marriedYN = tag;
                    }
                })
                .show();
    }


    @OnClick(R.id.actUpdateUserExtraDoc_vgReligion)
    public void onClickReligion(){
        new CommonListDialog(this)
                .setTitle("Religion?")
                .setArrItems("Islam", "Christian", "Catholic", "Buddhism", "Hinduism", "Confucius")
                .setColumnCount(2)
                .setItemClickListener(new StringHolder.ItemClickListener() {
                    @Override
                    public void onItemClick(Dialog dialog, int pos, String value, String tag) {
                        dialog.dismiss();
                        tvReligion.setText(value);
                        consultant.religion = value;
                    }
                })
                .show();
    }

    @OnClick(R.id.actUpdateUserExtraDoc_vgChkBikeYES)
    public void onClickUserHasBike() {
        btnBikeNo.setImageResource(R.drawable.ic_check_not_deep);
        btnBikeYes.setImageResource(R.drawable.ic_checked);
        consultant.bikeYN = "Y";
    }

    @OnClick(R.id.actUpdateUserExtraDoc_vgChkBikeNO)
    public void onClickUserNoBike() {
        btnBikeNo.setImageResource(R.drawable.ic_checked);
        btnBikeYes.setImageResource(R.drawable.ic_check_not_deep);
        consultant.bikeYN = "N";
    }

    @OnClick(R.id.actUpdateUserExtraDoc_vgChkDogYES)
    public void onClickUserOKWithDog() {
        btnDogNo.setImageResource(R.drawable.ic_check_not_deep);
        btnDogYes.setImageResource(R.drawable.ic_checked);
        consultant.likeDogYN = "Y";
    }

    @OnClick(R.id.actUpdateUserExtraDoc_vgChkDogNO)
    public void onClickUserNotOKWithDog() {
        btnDogNo.setImageResource(R.drawable.ic_checked);
        btnDogYes.setImageResource(R.drawable.ic_check_not_deep);
        consultant.likeDogYN = "N";
    }

    @OnClick(R.id.actUpdateUserDocument_vbtnOk)
    public void onSubmitInfo(){
        updateProfile();
    }

    @OnClick({R.id.actLocation_vbtnX, R.id.actUpdateUserDocument_vbtnCancel})
    public void onCloseActivity() {
        finish();
    }

}
