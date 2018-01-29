package id.co.okhome.consultant.view.common.dialog;

import android.app.Activity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

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
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;

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

                Log.d(TAG, "onVerificationCompleted:" + credential);

                Toast.makeText(activity, "All is good. > " + credential.getSmsCode(), Toast.LENGTH_SHORT).show();
                mVerificationInProgress = false;
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

                Toast.makeText(activity, "Code has been sent ;)", Toast.LENGTH_SHORT).show();
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
    }

    @Override
    public void onShow() {

    }

    private void sendSmsCode(){
        String phoneNum = etInput.getText().toString();
        if(!phoneNum.contains("+")){
            phoneNum = "+62" + phoneNum;
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNum,
                60,
                TimeUnit.SECONDS,
                activity,
                mCallbacks);

        mVerificationInProgress = true;
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

    @OnClick(R.id.dialogPhoneVerificatoin_tvSendVerfificationCode)
    public void onSendVerificationCode(){

        tvSendVerificationCode.setVisibility(View.GONE);
        vgPhoneVerification.setVisibility(View.VISIBLE);
        sendSmsCode();
    }

    @OnClick(R.id.dialogCommonInput_vbtnX)
    public void closeDialog() {
        dismiss();
    }

    @OnClick(R.id.dialogCommonInput_vbtnOk)
    public void submitPhoneNumber() {
        Toast.makeText(activity, "Finito", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}

