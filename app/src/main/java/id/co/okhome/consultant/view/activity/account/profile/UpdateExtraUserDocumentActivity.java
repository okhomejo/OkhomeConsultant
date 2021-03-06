package id.co.okhome.consultant.view.activity.account.profile;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
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
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.v2.ProfileModel;
import id.co.okhome.consultant.view.dialog.CommonListDialog;
import id.co.okhome.consultant.view.dialog.NikInputDialog;
import id.co.okhome.consultant.view.viewholder.StringHolder;

public class UpdateExtraUserDocumentActivity extends OkHomeParentActivity implements DialogParent.CommonDialogListener  {

    @BindView(R.id.actUpdateUserExtraDoc_tvNIK)         TextView tvNIK;
    @BindView(R.id.actUpdateUserExtraDoc_tvMarried)     TextView tvMarried;
    @BindView(R.id.actUpdateUserExtraDoc_tvReligion)    TextView tvReligion;
    @BindView(R.id.actUpdateUserExtraDoc_ivChkBikeYES)  ImageView btnBikeYes;
    @BindView(R.id.actUpdateUserExtraDoc_ivChkBikeNO)   ImageView btnBikeNo;
    @BindView(R.id.actUpdateUserExtraDoc_ivChkDogYES)   ImageView btnDogYes;
    @BindView(R.id.actUpdateUserExtraDoc_ivChkDogNO)    ImageView btnDogNo;

    private ProfileModel profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_extra_document);
        ButterKnife.bind(this);
        OkhomeUtil.setWhiteSystembar(this);

        init();
    }

    private void init(){

        if (ConsultantLoggedIn.hasSavedData()) {
            profile = ConsultantLoggedIn.get().profile;

            tvNIK.setText(profile.nik);
            tvReligion.setText(profile.religion);

            if (!TextUtils.isEmpty(profile.marriedYN)) {
                String married = "";
                if (profile.marriedYN.equals("Y")) {
                    if (TextUtils.isEmpty(profile.childrenCnt)) {
                        married = "Married";
                    } else {
                        if (profile.childrenCnt.equals("1")) {
                            married = "Married, 1 child";
                        } if (profile.childrenCnt.equals("4")) {
                            married = "Married, 4+ children";
                        } else {
                            married = "Married, " + profile.childrenCnt + " children";
                        }
                    }
                } else if (profile.marriedYN.equals("N")) {
                    married = "Not yet";
                }
                tvMarried.setText(married);
            }
            if (!TextUtils.isEmpty(profile.bikeYN)) {
                if (profile.bikeYN.equals("Y")) {
                    btnBikeYes.setImageResource(R.drawable.ic_checked);
                } else if (profile.bikeYN.equals("N")) {
                    btnBikeNo.setImageResource(R.drawable.ic_checked);
                }
            }

            if (!TextUtils.isEmpty(profile.likeDogYN)) {
                if (profile.likeDogYN.equals("Y")) {
                    btnDogYes.setImageResource(R.drawable.ic_checked);
                } else if (profile.likeDogYN.equals("N")) {
                    btnDogNo.setImageResource(R.drawable.ic_checked);
                }
            }
        }
    }

    private void updateProfile() {

        final String nik        = tvNIK.getText().toString();
        final String married    = profile.marriedYN;
        final String children   = profile.childrenCnt;
        final String religion   = profile.religion;
        final String hasBike    = profile.bikeYN;
        final String okWithDog  = profile.likeDogYN;

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
        };

        if (TextUtils.isEmpty(children)) {
            ConsultantLoggedIn.updateUserInfo(
                    OkhomeUtil.makeMap(
                            "nik", nik,
                            "married_yn", married,
                            "religion", religion,
                            "bike_yn", hasBike,
                            "like_dog_yn", okWithDog),
                    retrofitCallback
            );
        } else {
            ConsultantLoggedIn.updateUserInfo(
                    OkhomeUtil.makeMap(
                            "nik", nik,
                            "married_yn", married,
                            "children_cnt", children,
                            "religion", religion,
                            "bike_yn", hasBike,
                            "like_dog_yn", okWithDog),
                    retrofitCallback
            );
        }

//        dd/mm/year, Jun 18:00
    }

    @Override
    public void onCommonDialogWorkDone(Dialog dialog, int actionCode, Map<String, Object> mapResult) {
        if(actionCode == ACTIONCODE_OK){
            String validNik = (String) mapResult.get(NikInputDialog.RESULT_NIK);
            tvNIK.setText(validNik);
        }
    }

    //-- onclick methods ---------------------------------
    @OnClick(R.id.actUpdateUserExtraDoc_tvNIK)
    public void onClickNIK(){
        NikInputDialog nikDialog;
        if (tvNIK.getText().length() != 0) {
            nikDialog = new NikInputDialog(this, this, tvNIK.getText().toString());
        } else {
            nikDialog = new NikInputDialog(this, this);
        }
        nikDialog.show();
    }

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
                        profile.marriedYN = tag;
                        if (tag.equals("N")) {
                            tvMarried.setText(value);
                            profile.childrenCnt = "";
                        } else if (tag.equals("Y")) {
                            new CommonListDialog(UpdateExtraUserDocumentActivity.this)
                                    .setTitle("Children?")
                                    .setArrItems("0", "1", "2", "3", "4+")
                                    .setArrItemTag("0", "1", "2", "3", "4")
                                    .setColumnCount(5)
                                    .setItemClickListener(new StringHolder.ItemClickListener() {
                                        @Override
                                        public void onItemClick(Dialog dialog, int pos, String value, String tag) {
                                            dialog.dismiss();

                                            String married;
                                            if (tag.equals("1")) {
                                                married = "1 child";
                                            } else if (tag.equals("4")) {
                                                married = "4+ children";
                                            } else {
                                                married = value + " children";
                                            }
                                            tvMarried.setText(String.format("Married, %s", married));
                                            profile.childrenCnt = tag;
                                        }
                                    })
                                    .show();
                        }
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
                        profile.religion = value;
                    }
                })
                .show();
    }

    @OnClick(R.id.actUpdateUserExtraDoc_vgChkBikeYES)
    public void onClickUserHasBike() {
        btnBikeNo.setImageResource(R.drawable.ic_check_not_deep);
        btnBikeYes.setImageResource(R.drawable.ic_checked);
        profile.bikeYN = "Y";
    }

    @OnClick(R.id.actUpdateUserExtraDoc_vgChkBikeNO)
    public void onClickUserNoBike() {
        btnBikeNo.setImageResource(R.drawable.ic_checked);
        btnBikeYes.setImageResource(R.drawable.ic_check_not_deep);
        profile.bikeYN = "N";
    }

    @OnClick(R.id.actUpdateUserExtraDoc_vgChkDogYES)
    public void onClickUserOKWithDog() {
        btnDogNo.setImageResource(R.drawable.ic_check_not_deep);
        btnDogYes.setImageResource(R.drawable.ic_checked);
        profile.likeDogYN = "Y";
    }

    @OnClick(R.id.actUpdateUserExtraDoc_vgChkDogNO)
    public void onClickUserNotOKWithDog() {
        btnDogNo.setImageResource(R.drawable.ic_checked);
        btnDogYes.setImageResource(R.drawable.ic_check_not_deep);
        profile.likeDogYN = "N";
    }

    @OnClick(R.id.actUpdateUserDocument_vbtnOk)
    public void onSubmitInfo(){
        updateProfile();
    }

    @OnClick(R.id.common_vbtnClose)
    public void onCloseActivity() {
        finish();
    }
}
