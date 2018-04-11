package id.co.okhome.consultant.view.dialog;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.exception.OkhomeException;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;

import static id.co.okhome.consultant.lib.dialog.DialogParent.CommonDialogListener.ACTIONCODE_OK;

/**
 * Created by frizurd on 16/03/2018.
 */

public class NikInputDialog extends DialogParent {

    @BindView(R.id.dialogNikInput_etInput1)        EditText etInput1;
    @BindView(R.id.dialogNikInput_etInput2)        EditText etInput2;
    @BindView(R.id.dialogNikInput_etInput3)        EditText etInput3;
    @BindView(R.id.dialogNikInput_etInput4)        EditText etInput4;

    @BindView(R.id.dialogNikInput_vbtnOk)          ViewGroup btnOk;

    public static final String RESULT_NIK = "VALID NIK";
    private String oldNik;

    public NikInputDialog(Activity activity, CommonDialogListener commonDialogListener) {
        super(activity);
        this.commonDialogListener = commonDialogListener;
    }

    public NikInputDialog(Activity activity, CommonDialogListener commonDialogListener, String oldNik) {
        super(activity);
        this.commonDialogListener = commonDialogListener;
        this.oldNik = oldNik;
    }

    @Override
    public int onInit() {
        return R.layout.dialog_update_nik;
    }

    @Override
    public void onCreate() {
        ButterKnife.bind(this, getDecorView());
        restoreOldNik(oldNik);

        initKeyListeners();
    }

    @Override
    public void onShow() {
    }

    private void restoreOldNik(String oldNik) {
        if (oldNik == null) {
            ;
        } else {
            String input1 = oldNik.substring(0,4);
            String input2 = oldNik.substring(4,8);
            String input3 = oldNik.substring(8,12);
            String input4 = oldNik.substring(12,16);

            etInput1.setText(input1);
            etInput2.setText(input2);
            etInput3.setText(input3);
            etInput4.setText(input4);
        }
    }

    private void checkValidInput() {
        String input1 = etInput1.getText().toString();
        String input2 = etInput2.getText().toString();
        String input3 = etInput3.getText().toString();
        String input4 = etInput4.getText().toString();

        String validNik = input1 + input2 + input3 + input4;
        try {
            OkhomeException.chkException(validNik.length() != 16, "NIK must be 16 numbers");
        } catch (OkhomeException e) {
            ToastUtil.showToast(e.getMessage());
            return;
        }
        onNIKDone(validNik);
    }

    private void initKeyListeners() {
        etInput1.addTextChangedListener(new SeparatedEditTextWatcher(etInput1, null, etInput2));
        etInput2.addTextChangedListener(new SeparatedEditTextWatcher(etInput2, etInput1, etInput3));
        etInput3.addTextChangedListener(new SeparatedEditTextWatcher(etInput3, etInput2, etInput4));
        etInput4.addTextChangedListener(new SeparatedEditTextWatcher(etInput4, etInput3, null));
    }

    private void onNIKDone(String validNik) {
        commonDialogListener.onCommonDialogWorkDone(this, ACTIONCODE_OK, OkhomeUtil.makeMap(RESULT_NIK, validNik));
        dismiss();
    }

    @OnClick(R.id.dialogNikInput_vbtnX)
    public void onClose() {
        dismiss();
    }

    @OnClick(R.id.dialogNikInput_vbtnOk)
    public void onSubmit() {
        checkValidInput();
    }


    class SeparatedEditTextWatcher implements TextWatcher {

        private EditText etPrev, etNext, etCur;

        public SeparatedEditTextWatcher(EditText etCur, EditText etPrev, EditText etNext) {
            this.etPrev    = etPrev;
            this.etCur     = etCur;
            this.etNext    = etNext;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (etCur.getText().length() < 1 && etPrev != null) {

                etPrev.requestFocus();
                etPrev.setSelection(etPrev.length());

            } else if (etCur.getText().length() > 3 && etNext != null) {

                etNext.requestFocus();
                etNext.setSelection(0);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) { }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
    }
}