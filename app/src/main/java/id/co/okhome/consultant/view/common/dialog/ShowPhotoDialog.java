package id.co.okhome.consultant.view.common.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.dialog.DialogParent;

/**
 * Created by jo on 2018-01-24.
 */

public class ShowPhotoDialog extends DialogParent{
    @BindView(R.id.dialogShowPhoto_ivPhoto) ImageView ivPhoto;
    Object imgPath;
    public ShowPhotoDialog(Context context, Object imgPath) {
        super(context);
        this.imgPath = imgPath;
    }

    @Override
    public int onInit() {
        return R.layout.dialog_show_photo;
    }

    @Override
    public void onCreate() {
        ButterKnife.bind(this, getDecorView());
        Glide.with(getContext())
                .load(imgPath)
                .thumbnail(0.5f)
                .dontAnimate()
                .into(ivPhoto);

    }

    @Override
    public void onShow() {

    }

    @OnClick(R.id.dialogShowPhoto_vbtnOk)
    public void onOk(View v){
        dismiss();
    }
}
