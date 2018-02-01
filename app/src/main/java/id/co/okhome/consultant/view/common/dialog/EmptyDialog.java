package id.co.okhome.consultant.view.common.dialog;

import android.content.Context;

import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.dialog.DialogParent;

/**
 * Created by jo on 2018-01-24.
 */

public class EmptyDialog extends DialogParent{
    public EmptyDialog(Context context) {
        super(context);
    }

    @Override
    public int onInit() {
        return R.layout.dialog_common_alert;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onShow() {

    }
}
