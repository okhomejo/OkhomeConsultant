package id.co.okhome.consultant.view.common.dialog;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.DialogParent;
import id.co.okhome.consultant.lib.SmsReceiver;
import id.co.okhome.consultant.view.account.SignupActivity;

/**
 * Created by jo on 2018-01-24.
 */

public class PhoneVerificationDialog extends DialogParent{

    @BindView(R.id.dialogPhoneVerificatoin_vgPhoneVerification)         ViewGroup vgPhoneVerification;
    @BindView(R.id.dialogPhoneVerificatoin_tvCodeNotYet)                TextView tvCodeNotYet;
    @BindView(R.id.dialogPhoneVerificatoin_tvSendVerfificationCode)     TextView tvSendVerificationCode;
    @BindView(R.id.dialogPhoneVerificatoin_etCode)                      EditText etCode;
    @BindView(R.id.dialogCommonInput_etInput)                           EditText etInput;

    private static final String TAG = "PhoneVerification";

    private FirebaseAuth mAuth;
    private Activity activity;
    private boolean mVerificationInProgress = false;
    private boolean isVerified = false;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;
    private String currentPhoneNum;

    public PhoneVerificationDialog(Activity context) {
        super(context);
        this.activity = context;
    }

    @Override
    public int onInit() {
        return R.layout.dialog_phone_verification;
    }

    @Override
    public void onCreate() {
        ButterKnife.bind(this, getDecorView());

        vgPhoneVerification.setVisibility(View.GONE);
        registerSmsReceiver();

        etInput.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        FirebaseApp.initializeApp(activity);
        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                Log.d(TAG, "onVerificationCompleted: " + credential.getSmsCode());
                mVerificationInProgress = false;

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                mVerificationInProgress = false;

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    etInput.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(activity, "Quota exceeded.", Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(activity, "Fail.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);

                mVerificationId = verificationId;
                mResendToken = token;

                tvSendVerificationCode.setVisibility(View.GONE);
                vgPhoneVerification.setVisibility(View.VISIBLE);
                etCode.requestFocus();

                Toast.makeText(activity, "Code has been sent", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");

                            isVerified = true;
                            dismiss();

                        } else {
                            Toast.makeText(activity, "Error, please try again.", Toast.LENGTH_SHORT).show();

                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                etCode.setError("The verification code entered was invalid");
                            }
                            isVerified = false;
                        }
                    }
                });
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    @Override
    public void onShow() {

    }

    public boolean isVerified() {
        return isVerified;
    }

    private void sendSmsCode(){
        currentPhoneNum = etInput.getText().toString();
        if(!currentPhoneNum.contains("+")){
            currentPhoneNum = "+62" + currentPhoneNum.substring(1);
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                currentPhoneNum,
                60,
                TimeUnit.SECONDS,
                activity,
                mCallbacks);

        mVerificationInProgress = true;
    }

    private void resendVerificationCode(PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                currentPhoneNum,
                60,
                TimeUnit.SECONDS,
                activity,
                mCallbacks,
                token);
    }

    //register sms event reciever
    private void registerSmsReceiver(){
        new SmsReceiver(activity).init(new SmsReceiver.OnSmsReceivedListener() {
            @Override
            public void onSmsReceive(String message) {
                //원본
            }

            @Override
            public void onOkhomeVerificationSmsReceive(String message) {
                etCode.setText(message);
            }
        });
    }

    public void setPhoneNumber(String phoneNumber) {
        etInput.setText(phoneNumber);
    }

    public String getCurrentPhoneNum() {
        return currentPhoneNum;
    }

    @OnClick(R.id.dialogPhoneVerificatoin_tvSendVerfificationCode)
    public void onSendVerificationCode(){
        sendSmsCode();
    }

    @OnClick(R.id.dialogPhoneVerificatoin_tvCodeNotYet)
    public void onResendVerificationCode(){
        resendVerificationCode(mResendToken);
    }

    @OnClick(R.id.dialogCommonInput_vbtnX)
    public void closeDialog() {
        isVerified = false;
        dismiss();
    }

    @OnClick(R.id.dialogCommonInput_vbtnOk)
    public void submitPhoneNumber() {
        if (etCode.isShown()) {
            if (etCode.getText().toString().trim().length() > 0) {
                verifyPhoneNumberWithCode(mVerificationId, etCode.getText().toString());
            } else {
                etCode.setError("Please fill in the verification code.");
            }
        } else {
            onSendVerificationCode();
        }
    }
}

