package id.co.okhome.consultant.view.dialog;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.view.activity.etc.photochooser.ImageChooserActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jo on 2018-01-24.
 */

public class PhotoDialog extends DialogParent{
    @BindView(R.id.dialogShowPhoto_ivPhoto)         ImageView ivPhoto;
    @BindView(R.id.dialogShowPhoto_tvSubTitle)      TextView tvSubTitle;
    String imgPath;
    Activity activity;
    String subTitle;
    String choosedImagePath = null;
    CommonDialogListener dialogListener;

    public PhotoDialog(Activity activity, String subTitle, String imgPath, CommonDialogListener dialogListener) {
        super(activity);
        this.imgPath = imgPath;
        this.subTitle = subTitle;
        this.activity = activity;
        this.dialogListener = dialogListener;
    }

    @Override
    public int onInit() {
        return R.layout.dialog_show_photo;
    }

    @Override
    public void onCreate() {
        ButterKnife.bind(this, getDecorView());

        tvSubTitle.setText(subTitle);
        if(imgPath == null || imgPath.equals("")){
            ;
        }else{
            onPhotoChoosed(imgPath);
        }

    }

    @Override
    public void onShow() {
        ;
        if(TextUtils.isEmpty(imgPath)){
            activity.startActivityForResult(new Intent(activity, ImageChooserActivity.class), 10102);
            dismiss();
        }
    }

    private void onPhotoChoosed(String imgPath){
        Glide.with(getContext())
                .load(imgPath)
                .thumbnail(0.5f)
                .dontAnimate()
                .into(ivPhoto);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == 10102 || requestCode == 10101) {
                String imgPath = data.getStringExtra(ImageChooserActivity.RESULT_IMAGE_PATH);
                choosedImagePath = imgPath;
                onPhotoChoosed(imgPath);
                if(requestCode == 10102) {
                    dialogListener.onCommonDialogWorkDone(this, CommonDialogListener.ACTIONCODE_OK, OkhomeUtil.makeMap("imgPath", choosedImagePath));
                    dismiss();
                }
            }
        }
//        if(requestCode == 10101 && resultCode == RESULT_OK){
//            String imgPath = data.getStringExtra(ImageChooserActivity.RESULT_IMAGE_PATH);
//            choosedImagePath = imgPath;
//            onPhotoChoosed(imgPath);
//        }
    }

    @OnClick({R.id.dialogShowPhoto_ivPhoto, R.id.dialogShowPhoto_vbtnChangePhoto})
    public void onClickChoosePhoto(View v){
        activity.startActivityForResult(new Intent(activity, ImageChooserActivity.class), 10101);
    }

    @OnClick(R.id.dialogShowPhoto_vbtnX)
    public void onX(View v){
        dismiss();
    }

    @OnClick(R.id.dialogShowPhoto_vbtnOk)
    public void onOk(View v){
        if(choosedImagePath == null){
            ;
        }else{
            dialogListener.onCommonDialogWorkDone(this, CommonDialogListener.ACTIONCODE_OK, OkhomeUtil.makeMap("imgPath", choosedImagePath));
        }
        dismiss();
    }
}
