package id.co.okhome.consultant.view.dialog;

import android.app.Activity;
import android.app.Dialog;
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
import id.co.okhome.consultant.view.viewholder.StringHolder;

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
    @BindView(R.id.dialogPhoneVerificatoin_tvNationalCode)              TextView tvNationalCode;

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
        String nationalCode = tvNationalCode.getText().toString();

        try {
            OkhomeException.chkException(OkhomeUtil.isEmpty(phone), "Input phone number");

            if(!chkIsValidPhone(nationalCode, phone)){
                throw new OkhomeException(-100, "Check phone number");
            }
        } catch (OkhomeException e){
            ToastUtil.showToast(e.getMessage());
            return;
        }

        vgPhoneVerification.setVisibility(View.VISIBLE);
        vCodeLoading.setVisibility(View.VISIBLE);
        tvSendVerificationCode.setVisibility(View.GONE);
        sendCode = true;

        String realPhone = optimizePhoneByNationalCode(nationalCode, phone);

        OkhomeUtil.Log("real phone : " + realPhone);

        OkhomeRestApi.getValidationClient().issuePhoneValidationCode(realPhone).enqueue(new RetrofitCallback<String>() {
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
        final String nationalCode = tvNationalCode.getText().toString();
        final String phone = etInput.getText().toString();
        final String realPhone = optimizePhoneByNationalCode(nationalCode, phone);

        try{
            OkhomeException.chkException(OkhomeUtil.isEmpty(phone), "Input phone number");
            OkhomeException.chkException(OkhomeUtil.isEmpty(code), "Input code");
        }catch(OkhomeException e){
            ToastUtil.showToast(e.getMessage());
            return;
        }

        final ProgressDialog p = ProgressDialog.show(getContext(), null, "Loading...");
        OkhomeRestApi.getValidationClient().checkPhoneValidationCode(realPhone, code).enqueue(new RetrofitCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if(result){
                    onVerificationSuccess(realPhone, code);
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


    @OnClick(R.id.dialogPhoneVerificatoin_vgNationalCode)
    public void onNationalCodeClick(View v){
        final String[] codes = new String[]{"+62", "+82"};
        final String[] captions = new String[]{"Indonesia(+62)", "Korea(+82)"};


        new CommonListDialog(getContext())
                .setTitle("Choose your national code")
                .setArrItems(captions)
                .setColumnCount(1)
                .setItemClickListener(new StringHolder.ItemClickListener() {
                    @Override
                    public void onItemClick(Dialog dialog, int pos, String value, String tag) {
                        dialog.dismiss();
                        tvNationalCode.setText(codes[pos]);
                    }
                })
                .show();
    }

    //올바른 폰번호인지 리턴
    private boolean chkIsValidPhone(String nationalCode, String phone){
        String realPhone = optimizePhoneByNationalCode(nationalCode, phone);
        //+821093149449 //12개 13개
        if(nationalCode.equals("+82")){
            if(realPhone.length() == 12 || realPhone.length() == 13){
                return true;
            }else{
                return false;
            }
        }

        //+62812922312313
        if(nationalCode.equals("+62")){
            if(realPhone.length() > 10){
                return true;
            }else{
                return false;
            }
        }

        return false;
    }

    //폰코드 최적화
    private String optimizePhoneByNationalCode(String nationalCode, String phone){
        switch(nationalCode){
            case "+62":
            {
                String head3 = phone.substring(0, 3);
                String head2 = phone.substring(0, 2);
                String head1 = phone.substring(0, 1);

                if(head1.equals("0")){
                    phone = phone.substring(1);
                }

                if(head2.equals("62")){
                    phone = phone.substring(2);
                }

                if(head3.equals("+62")){
                    phone = phone.substring(3);
                }

                return nationalCode + phone;
            }

            case "+82":
            {
                //+821093149449 - >109314
                //82109314 -> 109314
                //0109314
                String head3 = phone.substring(0, 3);
                String head2 = phone.substring(0, 2);
                String head1 = phone.substring(0, 1);

                if(head2.equals("01")){
                    phone = phone.substring(1);
                }

                if(head2.equals("82")){
                    phone = phone.substring(2);
                }

                if(head3.equals("+82")){
                    phone = phone.substring(3);
                }
                return nationalCode + phone;
            }
        } return null;
    }

}

