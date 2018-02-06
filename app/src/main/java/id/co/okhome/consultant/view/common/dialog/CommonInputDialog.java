package id.co.okhome.consultant.view.common.dialog;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.lib.app.OkhomeUtil;

import static id.co.okhome.consultant.lib.dialog.DialogParent.CommonDialogListener.ACTIONCODE_NO;
import static id.co.okhome.consultant.lib.dialog.DialogParent.CommonDialogListener.ACTIONCODE_OK;

/**
 * Created by jo on 2018-01-24.
 */

public class CommonInputDialog extends DialogParent{

    public final static String TAG_INPUT_TEXT = "INPUT_TEXT";

    @BindView(R.id.dialogCommonInput_etInput)
    EditText etInput;

    @BindView(R.id.dialogCommonInput_tvSubTitle)
    TextView tvSubTitle;

    @BindView(R.id.dialogCommonInput_tvTitle)
    TextView tvTitle;

    @BindView(R.id.dialogCommonInput_tvComment)
    TextView tvComment;

    CommonDialogListener commonDialogListener;
    String title, subTitle, hint, comment, defaultText;


    public CommonInputDialog(Context context, String title, String subTitle, String hint, String defaultText, String comment, CommonDialogListener commonDialogListener) {
        super(context);
        this.commonDialogListener = commonDialogListener;
        this.title = title;
        this.subTitle = subTitle;
        this.hint = hint;
        this.comment = comment;
        this.defaultText = defaultText;
    }


    public CommonInputDialog(Context context){
        super(context);
    }

    //

    public CommonInputDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public CommonInputDialog setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    public CommonInputDialog setHint(String hint) {
        this.hint = hint;
        return this;
    }

    public CommonInputDialog setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public CommonInputDialog setDefaultText(String defaultText) {
        this.defaultText = defaultText;
        return this;
    }

    @Override
    public CommonInputDialog setCommonDialogListener(CommonDialogListener commonDialogListener) {
        this.commonDialogListener = commonDialogListener;
        return this;
    }
    //


    @Override
    public int onInit() {
        return R.layout.dialog_common_input;
    }

    @Override
    public void onCreate() {
        ButterKnife.bind(this, getDecorView());
        tvTitle.setVisibility(View.GONE);
        tvSubTitle.setVisibility(View.GONE);
        tvSubTitle.setVisibility(View.GONE);
        if(title != null){
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
        }

        if(subTitle != null){
            tvSubTitle.setText(subTitle);
            tvSubTitle.setVisibility(View.VISIBLE);
        }

        if(comment != null){
            tvComment.setText(comment);
            tvComment.setVisibility(View.VISIBLE);
        }

        etInput.setHint(hint != null ? hint : "");
        etInput.setText(defaultText != null ? defaultText : "");

    }

    @Override
    public void onShow() {

    }

    public EditText getEtInput() {
        return etInput;
    }

    @OnClick(R.id.dialogCommonInput_vbtnOk)
    public void ok(){

        if(commonDialogListener != null){
            commonDialogListener.onCommonDialogWorkDone(this, ACTIONCODE_OK, OkhomeUtil.makeMap(TAG_INPUT_TEXT, etInput.getText().toString()));
        }else{
            dismiss();
        }
    }

    @OnClick(R.id.dialogCommonInput_vbtnX)
    public void x(){
        dismiss();

        if(commonDialogListener!= null){
            commonDialogListener.onCommonDialogWorkDone(this, ACTIONCODE_NO, null);
        }
    }
}
