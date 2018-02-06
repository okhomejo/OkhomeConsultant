package id.co.okhome.consultant.lib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

import id.co.okhome.consultant.R;


/**
 * Created by jo on 2018-01-24.
 */

public abstract class DialogParent extends Dialog {




    ViewGroup vgContainer;
    View contentView;
    Context context;
    Map<String, Object> params = new HashMap<>();
    protected CommonDialogListener commonDialogListener;
    public DialogParent(Context context) {
        super(context);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //기본설정
        init();
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_parent_bg, null);
        vgContainer = contentView.findViewById(R.id.dialogBg_vgContent);

        View v = LayoutInflater.from(context).inflate(onInit(), null);
        vgContainer.addView(v);

        setContentView(contentView);

        onCreate();
    }

    private void init(){

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN,
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN );
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setCancelable(true);
    }

    public View getDecorView(){
        return contentView;
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);

        onShow();
    }

    public DialogParent putParam(String key, Object value){
        params.put(key, value);
        return this;
    }

    public <E> E getParam(String key){
        return (E)params.get(key);
    }

    public <E> E getParam(String key, E defaultValue){
        if(params.get(key) == null){
            return defaultValue;
        }else{
            return (E)params.get(key);
        }

    }

    public DialogParent setCommonDialogListener(CommonDialogListener commonDialogListener) {
        this.commonDialogListener = commonDialogListener;
        return this;
    }

    public CommonDialogListener getCommonDialogListener() {
        return commonDialogListener;
    }

    public abstract int onInit();
    public abstract void onCreate();
    public abstract void onShow();

    public interface CommonDialogListener{
        public final static int ACTIONCODE_OK = 1;
        public final static int ACTIONCODE_NO = 2;
        public final static int ACTIONCODE_CANCEL = -1;

        void onCommonDialogWorkDone(Dialog dialog, int actionCode, Map<String, Object> mapResult);
    }
}
