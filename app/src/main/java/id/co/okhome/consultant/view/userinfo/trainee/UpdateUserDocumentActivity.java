package id.co.okhome.consultant.view.userinfo.trainee;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.messages.internal.Update;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.consultant.model.ConsultantModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.common.dialog.PhoneVerificationDialog;
import id.co.okhome.consultant.view.etc.LocationActivity;

public class UpdateUserDocumentActivity extends OkHomeParentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.actUserInfo_tvName)                   EditText tvName;
    @BindView(R.id.actUserInfo_tvPhone)                  TextView tvPhone;
    @BindView(R.id.actUpdateUserDocument_tvAddress)      TextView tvAddress;
    @BindView(R.id.actUserInfo_tvNameLayout)             RelativeLayout rlName;

    private String address;
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

        // create consultant for testing purpose
        consultant = new ConsultantModel();
        consultant.id = "7";
        consultant.name = "Frizky";
        consultant.address = "Teststreet 6";
        consultant.phone = "123456";
        tvName.setText(consultant.name);
        tvPhone.setText(consultant.phone);
        tvAddress.setText(consultant.address);

        if (ConsultantLoggedIn.hasSavedData()) {
            consultant = ConsultantLoggedIn.get();
            tvName.setText(consultant.name);
            tvPhone.setText(consultant.phone);
            tvAddress.setText(consultant.address);
        }

        tvName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    tvName.clearFocus();
                    rlName.requestFocus();
                    OkhomeUtil.setSoftKeyboardVisiblity(tvName, false);
                    return true;
                }
                return false;
            }
        });


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

    private void updateProfile(final String consultantId, final String jsonParams) {

        OkhomeRestApi.getAccountClient().updateProfile(consultantId, jsonParams).
                enqueue(new RetrofitCallback<String>() {

                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(UpdateUserDocumentActivity.this, "Result: " + result, Toast.LENGTH_SHORT).show();
                        ConsultantLoggedIn.set(consultant);
                        finish();
                    }

                    @Override
                    public void onJodevError(ErrorModel jodevErrorModel) {
                        super.onJodevError(jodevErrorModel);

                        ToastUtil.showToast(jodevErrorModel.message + jodevErrorModel.code);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
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

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @OnClick(R.id.actUserInfo_tvPhone)
    public void onClickPhone(){
        requestPhoneNumber();
    }

    @OnClick(R.id.fragmentMyCleaningCalendar_btnClose)
    public void onSubmitInfo(){
        consultant.name = tvName.getText().toString();
        updateProfile(consultant.id, "{\"name\":\"" + consultant.name + "\", " +
                "\"phone\":\"" + consultant.phone + "\", " +
                "\"address\":\"" + consultant.address + "\"}"
        );
    }
}
