package id.co.okhome.consultant.lib;

import android.content.Context;

import id.co.okhome.consultant.lib.app.OkhomeUtil;

/**
 * Created by jo on 2018-01-31.
 */

public class ToastUtil {

    private static Context context;
    public static final void setContext(Context context){
        ToastUtil.context = context;
    }
    public static final void showToast(String msg){
        OkhomeUtil.showToast(context, msg);
    }
}
