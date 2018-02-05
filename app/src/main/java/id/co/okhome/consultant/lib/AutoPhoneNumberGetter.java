package id.co.okhome.consultant.lib;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.model.ConsultantModel;
import id.co.okhome.consultant.view.common.dialog.PhoneVerificationDialog;

import static android.app.Activity.RESULT_OK;

/**
 * Created by frizurd on 05/02/2018.
 */

public class AutoPhoneNumberGetter implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private Context context;
    private ConsultantModel consultant;
    private GoogleApiClient mGoogleApiClient;
    private PhoneVerificationDialog verifyDialog;

    public AutoPhoneNumberGetter(Context context) {
        this.context = context;
//        buildGoogleApiClient();
        requestPhoneNumber();
    }

    public void requestPhoneNumber() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(
                mGoogleApiClient, hintRequest);
//        try {
//            startIntentSenderForResult(intent.getIntentSender(),
//                    6, null, 0, 0, 0);
//        } catch (IntentSender.SendIntentException e) {
//            e.printStackTrace();
//        }
    }

//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(Auth.CREDENTIALS_API)
//                .build();
//        mGoogleApiClient.connect();
//    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 6) {
//
//            verifyDialog = new PhoneVerificationDialog(this);
//            verifyDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                @Override
//                public void onDismiss(final DialogInterface arg0) {
//                    if (verifyDialog.isVerified()) {
//                        Toast.makeText(context, "Yups", Toast.LENGTH_SHORT).show();
////                        consultant.phone = verifyDialog.getCurrentPhoneNum();
////                        tvPhone.setText(verifyDialog.getCurrentPhoneNum());
//                    }
//                }
//            });
//            verifyDialog.show();
//
//            if (resultCode == RESULT_OK) {
//                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
//                verifyDialog.setPhoneNumber(credential.getId());
//                verifyDialog.onSendVerificationCode();
//            }
//        }
//    }

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
}
