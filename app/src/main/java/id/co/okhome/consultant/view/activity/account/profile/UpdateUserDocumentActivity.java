package id.co.okhome.consultant.view.activity.account.profile;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.exception.OkhomeException;
import id.co.okhome.consultant.lib.PhoneNumberGetter;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeDateTimeFormatUtil;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.jobrowser.callback.ApiResultCallback;
import id.co.okhome.consultant.lib.jobrowser.model.ApiResult;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.consultant.model.v2.ProfileModel;
import id.co.okhome.consultant.rest_apicall.raw_restapi.ImageUploadCall;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.activity.etc.photochooser.ImageChooserActivity;
import id.co.okhome.consultant.view.dialog.CommonListDialog;
import id.co.okhome.consultant.view.viewholder.StringHolder;

public class UpdateUserDocumentActivity extends OkHomeParentActivity {

    @BindView(R.id.actUpdateUserDocument_tvName)        TextView tvName;
    @BindView(R.id.actUpdateUserDocument_tvPhone)       TextView tvPhone;
    @BindView(R.id.actUpdateUserDocument_tvBirthDate)   TextView tvBirthDate;
    @BindView(R.id.actUpdateUserDocument_tvAddress)     TextView tvAddress;
    @BindView(R.id.actUpdateUserDocument_tvGender)      TextView tvGender;
    @BindView(R.id.actUpdateUserDocument_llName)        LinearLayout vgName;
    @BindView(R.id.actUpdateUserDocument_ivPhoto)       ImageView ivPhoto;

    private String address;
    private String photoFilePath = null;

    private ProfileModel profile;
    private String accountId;
    private String phoneCode;

    private Bundle previousBundle = null;
    private boolean isActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_document);
        OkhomeUtil.setWhiteSystembar(this);
        ButterKnife.bind(this);
        init();
    }

    private void init(){

        // Load saved consultant data
        profile = ConsultantLoggedIn.get().profile;
        accountId = ConsultantLoggedIn.get().id;

        tvName.setText(profile.name);
        tvPhone.setText(profile.phone);
        tvAddress.setText(profile.address);

        Glide.with(this)
                .load(profile.photoUrl)
                .thumbnail(0.5f)
                .placeholder(R.drawable.img_user_blank)
                .dontAnimate()
                .into(ivPhoto);



        if (!TextUtils.isEmpty(profile.birthdate)) {
            DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTime birthDate = dtf.parseDateTime(profile.birthdate);

            tvBirthDate.setText(OkhomeDateTimeFormatUtil.printOkhomeType(profile.birthdate, "yyyy-MM-dd", "d MMM yyyy"));

        }

        if (!TextUtils.isEmpty(profile.gender)) {
            String consultantGender = "";
            if (profile.gender.equals("M")) {
                consultantGender = "Male";
            } else if (profile.gender.equals("F")) {
                consultantGender = "Female";
            }
            tvGender.setText(consultantGender);
        }
    }

    //profile update.
    private void updateProfile() {

        final String name           = tvName.getText().toString();
        final String phone          = profile.phone;
        final String gender         = profile.gender;
        final String address        = profile.address;
        final String birthdate      = profile.birthdate;

        boolean photoEmpty = true;
        if (!OkhomeUtil.isEmpty(profile.photoUrl) || !OkhomeUtil.isEmpty(photoFilePath)) {
            photoEmpty = false;
        }

        try {
            OkhomeException.chkException(photoEmpty, "Photo must be chosen");
            OkhomeException.chkException(name.length() <= 2, "Name must be more than 2");
            OkhomeException.chkException(OkhomeUtil.isEmpty(phone), "Please verify your phone number");
            OkhomeException.chkException(OkhomeUtil.isEmpty(gender), "Please state your gender");
            OkhomeException.chkException(OkhomeUtil.isEmpty(address), "Please state your address");
            OkhomeException.chkException(OkhomeUtil.isEmpty(birthdate), "Please select your birth date");

        } catch (OkhomeException e) {
            ToastUtil.showToast(e.getMessage());
            return;
        }

        final ProgressDialog p = ProgressDialog.show(this, "", "Loading");
        final RetrofitCallback<String> retrofitCallback = new RetrofitCallback<String>() {

            @Override
            public void onSuccess(String result) {
                if (phoneCode != null) {
                    savePhoneNumber();
                }
                finish();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                p.dismiss();
            }
        };

        // Don't start if the user already uploaded photo and does not want to change his photo
        if (photoFilePath != null) {
            //updating. first upload photo, and then update info.
            new ImageUploadCall(photoFilePath).asyncWork(new ApiResultCallback<String>() {
                @Override
                public void onFinish(ApiResult<String> apiResult) {
                    if (apiResult.resultCode == 200) { //success
                        // User wants to upload new image or edit old one
                        ConsultantLoggedIn.updateUserInfo(
                                OkhomeUtil.makeMap(
                                        "name", name,
                                        "gender", gender,
                                        "address", address,
                                        "birthdate", birthdate,
                                        "photo_url", apiResult.object),
                                retrofitCallback
                        );
                    } else {
                        p.dismiss();
                    }
                }
            });
        } else {
            // Image already exists and user does not want to edit it
            ConsultantLoggedIn.updateUserInfo(
                    OkhomeUtil.makeMap(
                            "name", name,
                            "gender", gender,
                            "address", address,
                            "birthdate", birthdate),
                    retrofitCallback
            );
        }
    }

    private void savePhoneNumber() {
        final ProgressDialog p = ProgressDialog.show(this, "", "Loading");
        OkhomeRestApi.getValidationClient().updatePhoneNumber(accountId, profile.phone, phoneCode)
                .enqueue(new RetrofitCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                    }

                    @Override
                    public void onJodevError(ErrorModel jodevErrorModel) {
                        super.onJodevError(jodevErrorModel);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        p.dismiss();
                    }
                });
    }

    //photo load
    private void onPhotoChoosed(String imgPath){
        Glide.with(this)
                .load("file://" + imgPath)
                .thumbnail(0.5f)
                .dontAnimate()
                .into(ivPhoto);
        photoFilePath = imgPath;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data.getExtras() == null) {
                    address = "No address";
                } else {
                    address = data.getStringExtra("address");
                    previousBundle = data.getExtras();
                }
                profile.address = address;
                tvAddress.setText(address);
            }
            isActive = false;

        } else if (requestCode == 1001 && resultCode == RESULT_OK) {
            String imgPath = data.getStringExtra(ImageChooserActivity.RESULT_IMAGE_PATH);
            onPhotoChoosed(imgPath);
        }
        PhoneNumberGetter.with(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        PhoneNumberGetter.with(this).destroy();
        super.onDestroy();
    }

    @OnClick(R.id.actUpdateUserDocument_vgbtnAddress)
    public void onAddressClick(View v){
        if (!isActive) {
            if (previousBundle != null) {
                Intent editAddressActivity = new Intent(this, LocationActivity.class);
                editAddressActivity.putExtras(previousBundle);
                startActivityForResult(editAddressActivity, 1);
            } else {
                if (TextUtils.isEmpty(tvAddress.getText().toString())) {
                    startActivityForResult(new Intent(this, LocationActivity.class), 1);
                } else {
                    Intent editAddressActivity = new Intent(this, LocationActivity.class);
                    editAddressActivity.putExtra("address", tvAddress.getText().toString());
                    startActivityForResult(editAddressActivity, 1);
                }
            }
            isActive = true;
        }
    }

    @OnClick(R.id.actUpdateUserDocument_vgPhoto)
    public void onClickPhoto(){
        startActivityForResult(new Intent(this, ImageChooserActivity.class), 1001);
    }

    @OnClick({R.id.actUpdateUserDocument_vgbtnPhone, R.id.actUpdateUserDocument_tvPhone})
    public void onClickPhone(){
        PhoneNumberGetter.with(this)
                .setPhoneVerificationCallback(new PhoneNumberGetter.PhoneVerificationCallback() {
                    @Override
                    public void onVerificationSuccess(String phone, String code) {
                        tvPhone.setText(phone);
                        profile.phone = phone;
                        phoneCode = code;
                    }
                })
                .show();
    }

    @OnClick(R.id.actUpdateUserDocument_vgbtnBirthDate)
    public void onClickBirthDay() {
//        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
//        if (profile.birthdate != null) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//            try {
//                cal.setTime(sdf.parse(profile.birthdate));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        DatePickerDialog datePicker = new DatePickerDialog(this,
//                datePickerListener,
//                cal.get(Calendar.YEAR),
//                cal.get(Calendar.MONTH),
//                cal.get(Calendar.DAY_OF_MONTH));
//        datePicker.setCancelable(true);
//        datePicker.setTitle("Select your birthday");
//        datePicker.show();

        //year, month, day

//        profile.birthdate = birthday.toString(dtf);
        final List<String> years = new ArrayList<>();
        for(int i = 1950; i < 2000; i++){
            years.add(i+"");
        }

        final List<String> months = new ArrayList<>();
        for(int i = 1; i <= 12; i++){
            months.add(i+"");
        }

        final List<String> days = new ArrayList<>();
        for(int i = 1; i <= 31; i++){
            days.add(i+"");
        }


        new CommonListDialog(this)
                .setTitle("Choose year of Birthday")
                .setArrItems(years.toArray(new String[years.size()]))
                .setColumnCount(3)
                .setItemClickListener(new StringHolder.ItemClickListener() {
                    @Override
                    public void onItemClick(Dialog dialog, int pos, String value, String tag) {
                        dialog.dismiss();

                        final String year = value;

                        new CommonListDialog(UpdateUserDocumentActivity.this)
                                .setTitle("Choose month of Birthday")
                                .setArrItems(months.toArray(new String[months.size()]))
                                .setColumnCount(3)
                                .setItemClickListener(new StringHolder.ItemClickListener() {
                                    @Override
                                    public void onItemClick(Dialog dialog, int pos, String value, String tag) {
                                        dialog.dismiss();

                                        final String month = value;
                                        new CommonListDialog(UpdateUserDocumentActivity.this)
                                                .setTitle("Choose day of Birthday")
                                                .setArrItems(days.toArray(new String[days.size()]))
                                                .setColumnCount(4)
                                                .setItemClickListener(new StringHolder.ItemClickListener() {
                                                    @Override
                                                    public void onItemClick(Dialog dialog, int pos, String value, String tag) {
                                                        dialog.dismiss();
                                                        final String day = value;

                                                        //선택됨
                                                        profile.birthdate = year + "-" + OkhomeUtil.getFull2Decimal(Integer.parseInt(month)) + "-" + OkhomeUtil.getFull2Decimal(Integer.parseInt(day));
                                                        tvBirthDate.setText(OkhomeDateTimeFormatUtil.printOkhomeType(profile.birthdate, "yyyy-MM-dd", "d MMM yyyy"));
                                                    }
                                                })
                                                .show();
                                    }
                                })
                                .show();

                    }
                })
                .show();


    }

    @OnClick(R.id.actUpdateUserDocument_vgbtnGender)
    public void onClickGender(){
        new CommonListDialog(this)
                .setTitle("Choose your gender")
                .setArrItems("Male", "Female") //value displayed on list
                .setArrItemTag("M", "F") // value will be sent to server
                .setColumnCount(2)
                .setItemClickListener(new StringHolder.ItemClickListener() {
                    @Override
                    public void onItemClick(Dialog dialog, int pos, String item, String tag) {
                        tvGender.setText(item);
                        profile.gender = tag;
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @OnClick(R.id.actUpdateUserDocument_vbtnOk)
    public void onSubmitInfo(){
        updateProfile();
    }

    @OnClick({R.id.common_vbtnClose})
    public void onCloseActivity() {
        finish();
    }
}