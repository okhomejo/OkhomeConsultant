package id.co.okhome.consultant.view.userinfo.trainee;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.exception.OkhomeException;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.jobrowser.callback.ApiResultCallback;
import id.co.okhome.consultant.lib.jobrowser.model.ApiResult;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.rest_apicall.raw_restapi.ImageUploadCall;
import id.co.okhome.consultant.view.common.dialog.ShowPhotoDialog;
import id.co.okhome.consultant.view.photochooser.ImageChooserActivity;

public class UpdateConsultantEducationActivity extends OkHomeParentActivity {

    @BindView(R.id.actUpdateEducation_ivPhotoSD)        ImageView ivPhotoSD;
    @BindView(R.id.actUpdateEducation_ivPhotoSMP)       ImageView ivPhotoSMP;
    @BindView(R.id.actUpdateEducation_ivPhotoSMA)       ImageView ivPhotoSMA;
    @BindView(R.id.actUpdateEducation_ivPhotoUNIV)      ImageView ivPhotoUNIV;

    String sdImgPath, smpImgPath, smaImgPath, univImgPath;
    String sdUrl, smpUrl, smaUrl, univUrl;

    final static int REQ_PHOTO_SD   = 10001;
    final static int REQ_PHOTO_SMP  = 10002;
    final static int REQ_PHOTO_SMA  = 10003;
    final static int REQ_PHOTO_UNV  = 10004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_education);
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        initDefaultPhoto();
    }

    //first visit, set default image.
    private void initDefaultPhoto(){
        sdUrl   = ConsultantLoggedIn.get().sdPhotoUrl;
        smpUrl  = ConsultantLoggedIn.get().smpPhotoUrl;
        smaUrl  = ConsultantLoggedIn.get().smaPhotoURl;
        univUrl = ConsultantLoggedIn.get().univPhotoUrl;

        Glide.with(this).load(sdUrl).thumbnail(0.5f).into(ivPhotoSD);
        Glide.with(this).load(smpUrl).thumbnail(0.5f).into(ivPhotoSMP);
        Glide.with(this).load(smaUrl).thumbnail(0.5f).into(ivPhotoSMA);
        Glide.with(this).load(univUrl).thumbnail(0.5f).into(ivPhotoUNIV);
    }

    //update photo urls.
    private void updatePhotoUrls(){

        /**
         * Note To Fritz
         * When an operation runs in the background, We have to show progress dialog.
         *
         * And it is usually recommended to write callback in an inner class in order to understand the code easily.
         * */

        ConsultantLoggedIn.updateUserInfo(
                OkhomeUtil.makeMap(
                        "sd_photo_url", sdUrl,
                        "smp_photo_url", smpUrl,
                        "sma_photo_url", smaUrl,
                        "univ_photo_url", univUrl),
                new RetrofitCallback<String>() {

                    @Override
                    public void onSuccess(String result) {
                        finish();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                }
        );
    }

    //upload all images and update
    private void postFiles(){

        sdImgPath   = (sdImgPath == null) ? sdUrl : sdImgPath;
        smaImgPath  = (smaImgPath == null) ? smaUrl : smaImgPath;
        smpImgPath  = (smpImgPath == null) ? smpUrl : smpImgPath;
        univImgPath = (univImgPath == null) ? univUrl : univImgPath;

        try{
            OkhomeException.chkException(OkhomeUtil.isEmpty(sdImgPath)
                    && OkhomeUtil.isEmpty(smaImgPath)
                    && OkhomeUtil.isEmpty(smpImgPath)
                    && OkhomeUtil.isEmpty(univImgPath), "Please choose at least 1 photo");
        }catch(Exception e){
            ToastUtil.showToast(e.getMessage());
            return;
        }

        //set params to upload.
        Map<Integer, String> mapFiles = new HashMap<>();
        mapFiles.put(REQ_PHOTO_SD,  sdImgPath);
        mapFiles.put(REQ_PHOTO_SMA, smaImgPath);
        mapFiles.put(REQ_PHOTO_SMP, smpImgPath);
        mapFiles.put(REQ_PHOTO_UNV, univImgPath);


        final int uploadSize = mapFiles.size();
        final ProgressDialog p = ProgressDialog.show(this, null, "Uploading files...");
        @SuppressLint("HandlerLeak") final Handler handlerCheckingcompletion = new Handler() {

            String err = "";

            int count = 0;
            int errCount = 0;
            int ignoreCount = 0;
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                count ++;

                switch(msg.what){
                    case -1:
                        errCount ++;
                        err += (String)msg.obj + "\n";
                        //failed
                        break;

                    case -2:
                        ignoreCount ++;
                        break;

                    case REQ_PHOTO_SD:
                        sdUrl = (String)msg.obj;
                        break;

                    case REQ_PHOTO_SMA:
                        smaUrl = (String)msg.obj;
                        break;

                    case REQ_PHOTO_SMP:
                        smpUrl = (String)msg.obj;
                        break;

                    case REQ_PHOTO_UNV:
                        univUrl = (String)msg.obj;
                        break;
                }

                //finish
                if(count == uploadSize){
                    p.dismiss();
                    updatePhotoUrls();
                }
            }
        };


        //upload.
        Iterator<Map.Entry<Integer, String>> itr = mapFiles.entrySet().iterator();

        while(itr.hasNext()){
            Map.Entry<Integer, String> entry = itr.next();
            final Integer key = entry.getKey();
            final String filePath = entry.getValue();

            // Check if file is not based on the internet.
            if(!URLUtil.isFileUrl(filePath)){

                new ImageUploadCall(filePath).asyncWork(new ApiResultCallback() {
                    @Override
                    public void onFinish(ApiResult apiResult) {
                        Message msg = null;
                        if(apiResult.resultCode == 200){
                            msg = OkhomeUtil.makeHandlerMessage(key, apiResult.object);
                        }else{
                            msg = OkhomeUtil.makeHandlerMessage(-1, apiResult.object);
                        }
                        handlerCheckingcompletion.sendMessage(msg);
                    }
                });
            }else{
                // it occurs when the image is from Web.
                Message msg = OkhomeUtil.makeHandlerMessage(-2, "");
                handlerCheckingcompletion.sendMessage(msg);
            }
        }
    }

    //callback on photo choosed
    private void onPhotoChoosed(int reqCode, String filePath){

        ImageView ivTarget = null;
        switch(reqCode){
            case REQ_PHOTO_SD:
                ivTarget = ivPhotoSD;
                sdImgPath = filePath;
                break;
            case REQ_PHOTO_SMP:
                ivTarget = ivPhotoSMP;
                smpImgPath = filePath;
                break;
            case REQ_PHOTO_SMA:
                ivTarget = ivPhotoSMA;
                smaImgPath = filePath;
                break;
            case REQ_PHOTO_UNV:
                ivTarget = ivPhotoUNIV;
                univImgPath = filePath;
                break;
            default:
                return;
        }

        Glide.with(this)
                .load("file://" + filePath)
                .thumbnail(0.5f)
                .dontAnimate()
                .into(ivTarget);
    }


    //------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            onPhotoChoosed(requestCode, data.getStringExtra(ImageChooserActivity.RESULT_IMAGE_PATH));
        }
    }

    //-----------------------

    @OnClick(R.id.actUpdateEducation_vgPhotoSD)
    public void onSdClick(){
        startActivityForResult(new Intent(this, ImageChooserActivity.class), REQ_PHOTO_SD);
    }

    @OnClick(R.id.actUpdateEducation_vgPhotoSMP)
    public void onSMPClick(){
        startActivityForResult(new Intent(this, ImageChooserActivity.class), REQ_PHOTO_SMP);
    }

    @OnClick(R.id.actUpdateEducation_vgPhotoSMA)
    public void onSMAClick(){
        startActivityForResult(new Intent(this, ImageChooserActivity.class), REQ_PHOTO_SMA);
    }

    @OnClick(R.id.actUpdateEducation_vgPhotoUNIV)
    public void onUNIVClick(){
        startActivityForResult(new Intent(this, ImageChooserActivity.class), REQ_PHOTO_UNV);
    }

    @OnClick(R.id.actUpdateEducation_vgSeeSample)
    public void onClickSeeSample(){
        new ShowPhotoDialog(this, R.drawable.img_model2).show();
    }

    @OnClick(R.id.actUpdateEducation_vbtnOk)
    public void onOkClick() {
        postFiles();
    }

    @OnClick(R.id.actLocation_vbtnX)
    public void onGoBackClick() {
        finish();
    }
}
