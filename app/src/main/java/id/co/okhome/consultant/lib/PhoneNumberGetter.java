package id.co.okhome.consultant.lib;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;
import java.util.Map;

import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.view.common.dialog.PhoneVerificationDialog;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jo on 2018-02-07.
 */

public class PhoneNumberGetter  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DialogParent.CommonDialogListener {

    private static Map<Activity, PhoneNumberGetter> mapInstance = new HashMap<>();
    private final static int REQ_HINT = 6;

    private Activity activity;
    private GoogleApiClient googleApiClient;
    private PhoneVerificationDialog verifyDialog = null;
    private PhoneVerificationCallback phoneVerificationCallback;

    public static PhoneNumberGetter with(Activity activity){
        PhoneNumberGetter phoneNumberGetter = mapInstance.get(activity);
        if(phoneNumberGetter == null){
            phoneNumberGetter = new PhoneNumberGetter(activity);
            phoneNumberGetter.init();
            mapInstance.put(activity, phoneNumberGetter);
        }
        return phoneNumberGetter;
    }

    public PhoneNumberGetter(Activity activity) {
        this.activity = activity;
    }

    //-------public methods---------------------
    /**show. */
    public void show() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest);
        try {
            activity.startIntentSenderForResult(intent.getIntentSender(),
                    REQ_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            onGettingPhoneNumberFailed(e.getMessage());
        }
    }

    /**it called by activity's onActivityResult*/
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == REQ_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                String phoneNumber = credential.getId();
                onGettingPhoneNumber(phoneNumber);
            } else if (resultCode == 1001) {
                onGettingPhoneNumberFailed("User don't want use it");
            } else {
                onGettingPhoneNumberFailed("error occurs in onActivityResult");
            }
        }
    }

    /**it must be called when activity finish*/
    public void destroy(){
        googleApiClient = null;
        verifyDialog    = null;
        mapInstance.remove(activity);
    }

    /**set param for callback*/
    public PhoneNumberGetter setPhoneVerificationCallback(PhoneVerificationCallback callback){
        this.phoneVerificationCallback = callback;
        return this;
    }

    //
    public void init() {
        initGoogleApiClient();
        initPhoneVerificationDialog();
    }

    //--------private methods -------------------
    //init google api client
    private void initGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.CREDENTIALS_API)
                .build();
        googleApiClient.connect();

    }

    //init phonenumber verification dialog
    private void initPhoneVerificationDialog(){
        verifyDialog = new PhoneVerificationDialog(activity, this);
    }


    //verification success.
    private void onVerificationSuccess(String phone, String code){
        if(phoneVerificationCallback != null){
            phoneVerificationCallback.onVerificationSuccess(phone, code);
        }
    }

    //success getting phone number.
    private void onGettingPhoneNumber(String phone){
        //if success, show dialog and input phone number
        verifyDialog.show();
        verifyDialog.setPhoneNumber(phone);
    }
    // failed1.
    private void onGettingPhoneNumberFailed(String message){
        //If phone number could not be optained, just show Verify dialog without any actions.
        OkhomeUtil.Log("onGettingPhoneNumberFailed : " + message);
        verifyDialog.show();
        ;
    }

    @Override
    public void onCommonDialogWorkDone(Dialog dialog, int actionCode, Map<String, Object> mapResult) {
        if(actionCode == ACTIONCODE_OK){

            String phone    = (String) mapResult.get(PhoneVerificationDialog.RESULT_PHONE);
            String veriCode = (String) mapResult.get(PhoneVerificationDialog.RESULT_VERI_CODE);

            onVerificationSuccess(phone, veriCode);
        }
    }

    // on activity result which is called by this class.
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public interface PhoneVerificationCallback{
        void onVerificationSuccess(String phone, String code);
    }
}
