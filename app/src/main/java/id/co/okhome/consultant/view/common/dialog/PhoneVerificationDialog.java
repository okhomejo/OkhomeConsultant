package id.co.okhome.consultant.view.common.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.exception.OkhomeException;
import id.co.okhome.consultant.lib.SmsReceiver;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;

import static id.co.okhome.consultant.lib.dialog.DialogParent.CommonDialogListener.ACTIONCODE_CANCEL;
import static id.co.okhome.consultant.lib.dialog.DialogParent.CommonDialogListener.ACTIONCODE_OK;

/**
 * Created by jo on 2018-01-24.
 */

public class PhoneVerificationDialog extends DialogParent implements SmsReceiver.OnSmsReceivedListener{

    public final static String RESULT_PHONE             = "PHONE";
    public final static String RESULT_VERI_CODE         = "RESULT_VERI_CODE";

    @BindView(R.id.dialogPhoneVerificatoin_vgPhoneVerification)         ViewGroup vgPhoneVerification;
    @BindView(R.id.dialogPhoneVerificatoin_tvCodeNotYet)                TextView tvCodeNotYet;
    @BindView(R.id.dialogPhoneVerificatoin_tvSendVerfificationCode)     TextView tvSendVerificationCode;
    @BindView(R.id.dialogPhoneVerificatoin_etCode)                      EditText etCode;
    @BindView(R.id.dialogCommonInput_etInput)                           EditText etInput;
    @BindView(R.id.dialogPhoneVerificatoin_vCodeLoading)                View vCodeLoading;

    SmsReceiver smsReceiver;
    Activity activity;
    boolean sendCode = false;

    public PhoneVerificationDialog(Activity activity, CommonDialogListener commonDialogListener) {
        super(activity);
        this.activity = activity;
        smsReceiver = new SmsReceiver(activity);
        this.commonDialogListener = commonDialogListener;
    }

    @Override
    public int onInit() {
        return R.layout.dialog_phone_verification;
    }

    @Override
    public void onCreate() {
        ButterKnife.bind(this, getDecorView());
        init();
    }

    @Override
    public void onShow() {

    }

    /** seting phone number*/
    public void setPhoneNumber(String phoneNumber){
        etInput.setText(phoneNumber);
    }

    private void onVerificationSuccess(String phone, String code){
        commonDialogListener.onCommonDialogWorkDone(this, ACTIONCODE_OK, OkhomeUtil.makeMap(RESULT_PHONE, phone, RESULT_VERI_CODE, code));
        dismiss();
    }

    private void init(){
        vgPhoneVerification.setVisibility(View.GONE);
        smsReceiver.init(this);
    }

    //send verification code
    private void sendCode(){
        String phone = etInput.getText().toString();
        try {
            OkhomeException.chkException(OkhomeUtil.isEmpty(phone), "Input phone number");
        } catch (OkhomeException e){
            ToastUtil.showToast(e.getMessage());
            return;
        }

        vgPhoneVerification.setVisibility(View.VISIBLE);
        vCodeLoading.setVisibility(View.VISIBLE);
        tvSendVerificationCode.setVisibility(View.GONE);
        sendCode = true;

        OkhomeRestApi.getValidationClient().issuePhoneValidationCode(phone).enqueue(new RetrofitCallback<String>() {
            @Override
            public void onSuccess(String result) {
            }

            @Override
            public void onFinish() {
                vCodeLoading.setVisibility(View.GONE);
            }

        });
    }

    //check code
    private void checkCode(){
        final String code = etCode.getText().toString();
        final String phone = etInput.getText().toString();

        try{
            OkhomeException.chkException(OkhomeUtil.isEmpty(phone), "Input phone number");
            OkhomeException.chkException(OkhomeUtil.isEmpty(code), "Input code");
        }catch(OkhomeException e){
            ToastUtil.showToast(e.getMessage());
            return;
        }

        final ProgressDialog p = ProgressDialog.show(getContext(), null, "Loading...");
        OkhomeRestApi.getValidationClient().checkPhoneValidationCode(phone, code).enqueue(new RetrofitCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if(result){
                    onVerificationSuccess(phone, code);
                }else{
                    ToastUtil.showToast("Check your verfication code.");
                }
            }

            @Override
            public void onFinish() {
                p.dismiss();
            }
        });
    }

    @Override
    public void onSmsReceive(String message) {
        //do nothing.
    }

    @Override
    public void onOkhomeVerificationSmsReceive(String code) {
        etCode.setText(code);
    }


    @OnClick(R.id.dialogPhoneVerificatoin_tvSendVerfificationCode)
    public void onSendVerificationCode(){
        sendCode();
    }

    @OnClick(R.id.dialogPhoneVerificatoin_tvCodeNotYet)
    public void onResendVerificationCode(){
        sendCode();
    }

    @OnClick(R.id.dialogPhoneVerificatoin_vbtnCancel)
    public void closeDialog() {
        dismiss();
        commonDialogListener.onCommonDialogWorkDone(this, ACTIONCODE_CANCEL, null);
    }

    @OnClick(R.id.dialogPhoneVerificatoin_vbtnOk)
    public void onOk() {
        if(sendCode){
            checkCode();
        }else{
            sendCode();
            etCode.requestFocus();
        }
    }

}

