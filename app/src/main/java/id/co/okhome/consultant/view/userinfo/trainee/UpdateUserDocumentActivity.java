package id.co.okhome.consultant.view.userinfo.trainee;

import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

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
import id.co.okhome.consultant.model.ConsultantModel;
import id.co.okhome.consultant.rest_apicall.raw_restapi.ImageUploadCall;
import id.co.okhome.consultant.view.common.dialog.CommonListDialog;
import id.co.okhome.consultant.view.common.dialog.PhoneVerificationDialog;
import id.co.okhome.consultant.view.etc.LocationActivity;
import id.co.okhome.consultant.view.photochooser.ImageChooserActivity;
import id.co.okhome.consultant.view.viewholder.StringHolder;

public class UpdateUserDocumentActivity extends OkHomeParentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.actUpdateUserDocument_tvName)        TextView tvName;
    @BindView(R.id.actUpdateUserDocument_tvPhone)       TextView tvPhone;
    @BindView(R.id.actUpdateUserDocument_tvAddress)     TextView tvAddress;
    @BindView(R.id.actUpdateUserDocument_tvGender)      TextView tvGender;
    @BindView(R.id.actUpdateUserDocument_llName)        LinearLayout vgName;
    @BindView(R.id.actUpdateUserDocument_ivPhoto)       ImageView ivPhoto;

    private String address;
    private String photoFilePath = null;
    private Bundle previousBundle = null;
    private ConsultantModel consultant;
    private boolean isActive = false;

    private GoogleApiClient mGoogleApiClient;
    private PhoneVerificationDialog verifyDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_document);
        OkhomeUtil.setSystemBarColor(this,

//                Color.parseColor("#29313a"));
                ContextCompat.getColor(this, R.color.colorOkhome));

        buildGoogleApiClient();
        ButterKnife.bind(this);
        init();
    }

    private void init(){

        // Load saved consultant data
        if (ConsultantLoggedIn.hasSavedData()) {
            consultant = ConsultantLoggedIn.get();

            tvName.setText(consultant.name);
            tvPhone.setText(consultant.phone);
            tvAddress.setText(consultant.address);

            Glide.with(this)
                    .load(consultant.photoUrl)
                    .thumbnail(0.5f)
                    .dontAnimate()
                    .into(ivPhoto);

            if (!TextUtils.isEmpty(consultant.gender)) {
                String consultantGender = "";
                if (consultant.gender.equals("M")) {
                    consultantGender = "Male";
                } else if (consultant.gender.equals("F")) {
                    consultantGender = "Female";
                }
                tvGender.setText(consultantGender);
            }
        }
    }

    //profile update.
    private void updateProfile() {

        final String name       = tvName.getText().toString();
        final String phone      = consultant.phone;
        final String gender     = consultant.gender;
        final String address    = consultant.address;

        boolean photoEmpty = true;
        if (!OkhomeUtil.isEmpty(consultant.photoUrl) || !OkhomeUtil.isEmpty(photoFilePath)) {
            photoEmpty = false;
        }

        try {
            OkhomeException.chkException(photoEmpty, "Photo must be chosen");
            OkhomeException.chkException(name.length() <= 2, "Name must be more than 2");
            OkhomeException.chkException(OkhomeUtil.isEmpty(phone), "Please verify your phone number");
            OkhomeException.chkException(OkhomeUtil.isEmpty(gender), "Please state your gender");
            OkhomeException.chkException(OkhomeUtil.isEmpty(address), "Please state your address");

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


        // Don't start if the user already uploaded photo and does not want to change his photo
        if (photoFilePath != null) {
            //updating. first upload photo, and then update info.
            new ImageUploadCall(photoFilePath).asyncWork(new ApiResultCallback<String>() {
                @Override
                public void onFinish(ApiResult<String> apiResult) {
                    if (apiResult.resultCode == 200) { //success
                        // User wants to upload new image or edit old one
                        ConsultantLoggedIn.updateUserInfo(
                                OkhomeUtil.makeMap("name", name, "gender", gender, "phone", phone, "address", address, "photo_url", apiResult.object), retrofitCallback
                        );
                    } else {
                        p.dismiss();
                    }
                }
            });
        } else {
            // Image already exists and user does not want to edit it
            ConsultantLoggedIn.updateUserInfo(
                    OkhomeUtil.makeMap("name", name, "gender", gender, "phone", phone, "address", address), retrofitCallback
            );
        }
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

    public void requestPhoneNumber() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(
                mGoogleApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(),
                    6, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.CREDENTIALS_API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data.getExtras() == null) {
                    address = "No address";
                } else {
                    address = data.getStringExtra("address");
                    previousBundle = data.getExtras();
                }
                consultant.address = address;
                tvAddress.setText(address);
            }
            isActive = false;

        } else if (requestCode == 6) {

            verifyDialog = new PhoneVerificationDialog(this);
            verifyDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(final DialogInterface arg0) {
                    if (verifyDialog.isVerified()) {
                        consultant.phone = verifyDialog.getCurrentPhoneNum();
                        tvPhone.setText(verifyDialog.getCurrentPhoneNum());
                    }
                }
            });
            verifyDialog.show();

            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                verifyDialog.setPhoneNumber(credential.getId());
                verifyDialog.onSendVerificationCode();
            }
        } else if(requestCode == 1001 && resultCode == RESULT_OK){
            String imgPath = data.getStringExtra(ImageChooserActivity.RESULT_IMAGE_PATH);
            onPhotoChoosed(imgPath);
        }
    }

    @OnClick(R.id.actUpdateUserDocument_vgbtnAddress)
    public void onAddressClick(View v){
        if (!isActive) {
            if (previousBundle != null) {
                Intent editAddressActivity = new Intent(this, LocationActivity.class);
                editAddressActivity.putExtras(previousBundle);
                startActivityForResult(editAddressActivity, 1);
            } else {
                startActivityForResult(new Intent(this, LocationActivity.class), 1);
            }
            isActive = true;
        }
    }

    @OnClick(R.id.actUpdateUserDocument_vgPhoto)
    public void onClickPhoto(){
        startActivityForResult(new Intent(this, ImageChooserActivity.class), 1001);
    }

    @OnClick(R.id.actUpdateUserDocument_vgbtnPhone)
    public void onClickPhone(){
        requestPhoneNumber();
    }

    @OnClick(R.id.actUpdateUserDocument_vgbtnGender)
    public void onClickGender(){
        new CommonListDialog(this)
                .setTitle("Choose your gender")
                .setArrItems("Male", "Female") //value displayd on list
                .setArrItemTag("M", "F") // value will be sent to server
                .setColumnCount(2)
                .setItemClickListener(new StringHolder.ItemClickListener() {
                    @Override
                    public void onItemClick(Dialog dialog, int pos, String item, String tag) {
                        tvGender.setText(item);
                        consultant.gender = tag;
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @OnClick(R.id.actUpdateUserDocument_vbtnOk)
    public void onSubmitInfo(){
        updateProfile();
    }

    @OnClick({R.id.actLocation_vbtnX})
    public void onCloseActivity() {
        finish();
    }
}
