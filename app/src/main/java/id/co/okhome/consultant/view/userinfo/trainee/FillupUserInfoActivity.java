package id.co.okhome.consultant.view.userinfo.trainee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.view.photochooser.ImageChooserActivity;

public class FillupUserInfoActivity extends OkHomeParentActivity {

    final int REQ_GET_PHOTO_FOR_KTP             = 10001;
    final int REQ_GET_PHOTO_FOR_MYPHOTO         = 10002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fillup_userinfo);

        ButterKnife.bind(this);
    }

    //on photo choosed
    private void onPhotoChoosed(int requestCode, String imgPath){
        switch(requestCode){
            case REQ_GET_PHOTO_FOR_KTP:
                break;

            case REQ_GET_PHOTO_FOR_MYPHOTO:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK ){
            String imgPath = data.getStringExtra(ImageChooserActivity.RESULT_IMAGE_PATH);
            onPhotoChoosed(requestCode, imgPath);
        }
    }

    //--------on click
    @OnClick(R.id.actFillUpUserInfo_vbtnKartuTandaPerduduk)
    public void onClickKTP(View v){
        startActivityForResult(new Intent(this, ImageChooserActivity.class), REQ_GET_PHOTO_FOR_KTP);
    }

    @OnClick(R.id.actFillUpUserInfo_vbtnUploadPhoto)
    public void onClickMyPhoto(View v){
        startActivityForResult(new Intent(this, ImageChooserActivity.class), REQ_GET_PHOTO_FOR_KTP);
    }

    @OnClick({R.id.actFillUpUserInfo_vbtnBasicInformation})
    public void onClickBasicInfo(View v){
        startActivity(new Intent(this, UpdateUserDocumentActivity.class));
    }

    @OnClick({R.id.actFillUpUserInfo_vbtnKartuTandaPerduduk})
    public void onKartuTandaPerdudk(View v){
    }

}
