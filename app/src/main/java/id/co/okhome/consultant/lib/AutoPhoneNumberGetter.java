package id.co.okhome.consultant.lib;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.api.GoogleApiClient;

import id.co.okhome.consultant.view.common.dialog.PhoneVerificationDialog;

/**
 * Created by frizurd on 05/02/2018.
 */

public class AutoPhoneNumberGetter {

    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private PhoneVerificationDialog verifyDialog;
    private boolean isVerified = false;
    private String verifiedPhoneNumber;
    private PhoneNumCallback callback;

    @Deprecated
    public AutoPhoneNumberGetter(Context context, GoogleApiClient mGoogleApiClient, PhoneNumCallback callback) {
        this.context = context;
        this.mGoogleApiClient = mGoogleApiClient;
        this.callback = callback;

        requestPhoneNumber();
    }

    @Deprecated
    public void requestPhoneNumber() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(
                mGoogleApiClient, hintRequest);
        try {
            Activity activity = (Activity) context;
            activity.startIntentSenderForResult(intent.getIntentSender(),
                    6, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
//
//        if (requestCode == 6) {
//            final Activity activity = (Activity) context;
//            verifyDialog = new PhoneVerificationDialog(activity);
//            verifyDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                @Override
//                public void onDismiss(final DialogInterface arg0) {
//                    if (verifyDialog.isVerified()) {
//
//                        isVerified = true;
//                        verifiedPhoneNumber = verifyDialog.getCurrentPhoneNum();
//
//                        callback.sendVerifiedPhoneNumber(verifiedPhoneNumber);
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
    }

    public interface PhoneNumCallback {
        void sendVerifiedPhoneNumber(String phoneNum);
    }
}
