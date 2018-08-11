package id.co.okhome.consultant.view.dialog;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.dialog.DialogParent;

/**
 * Created by jo on 2018-01-24.
 */

public class PhotoShowDialog extends DialogParent{
    @BindView(R.id.dialogShowPhoto_ivPhoto)         ImageView ivPhoto;
    int resId;

    public PhotoShowDialog(Activity activity, int resId) {
        super(activity);
        this.resId = resId;
    }

    @Override
    public int onInit() {
        return R.layout.dialog_show_photoonly;
    }

    @Override
    public void onCreate() {
        ButterKnife.bind(this, getDecorView());

        Glide.with(getContext())
                .load(resId)
                .thumbnail(0.5f)
                .dontAnimate()
                .into(ivPhoto);
    }

    @Override
    public void onShow() {
    }

}
