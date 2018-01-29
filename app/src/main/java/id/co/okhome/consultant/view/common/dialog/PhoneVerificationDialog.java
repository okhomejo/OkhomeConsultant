package id.co.okhome.consultant.view.common.dialog;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.DialogParent;
import id.co.okhome.consultant.lib.SmsReceiver;

/**
 * Created by jo on 2018-01-24.
 */

public class PhoneVerificationDialog extends DialogParent{

    @BindView(R.id.dialogPhoneVerificatoin_vgPhoneVerification)         ViewGroup vgPhoneVerification;
    @BindView(R.id.dialogPhoneVerificatoin_tvCodeNotYet)                TextView tvCodeNotYet;
    @BindView(R.id.dialogPhoneVerificatoin_tvSendVerfificationCode)     TextView tvSendVerificationCode;
    @BindView(R.id.dialogPhoneVerificatoin_etCode)                      EditText etCode;

    Activity activity;

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
    }

    @Override
    public void onShow() {

    }


    //send sms code to phone wrote down
    private void sendSmsCode(){


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


    @OnClick(R.id.dialogPhoneVerificatoin_tvSendVerfificationCode)
    public void onSendVerificationCode(){

        tvSendVerificationCode.setVisibility(View.GONE);
        vgPhoneVerification.setVisibility(View.VISIBLE);
        sendSmsCode();
    }
}

