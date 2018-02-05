package id.co.okhome.consultant.view.userinfo.trainee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import id.co.okhome.consultant.rest_apicall.raw_restapi.ImageUploadCall;
import id.co.okhome.consultant.view.common.dialog.ShowPhotoDialog;
import id.co.okhome.consultant.view.photochooser.ImageChooserActivity;

public class UpdateConsultantEducationActivity extends OkHomeParentActivity {

    @BindView(R.id.actUpdateEducation_ivPhotoSD)                ImageView ivPhotoSD;

    String sdImgPath, smpImgPath, smaImgPath, univImgPath;
    String sdUrl, smpUrl, smaUrl, univUrl;

    final static int REQ_PHOTO_SD   = 10001;
    final static int REQ_PHOTO_SMP  = 10002;
    final static int REQ_PHOTO_SMA  = 10003;
    final static int REQ_PHOTO_UNV  = 10004;


    /**
     * Note To Fritz
     * Complete the remaining part by referring to this page's code and flow.
     *
     * - upload all photos and update photo url of education ceritification
     * - first visit to this page, previous photo appears on each ImageView
     * - If there are images unchanged, it should be left to update.
     *
     * */

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

    //fitst visitation, set default image.
    private void initDefaultPhoto(){
        sdUrl = ConsultantLoggedIn.get().sdPhotoUrl;
        smpUrl = ConsultantLoggedIn.get().smpPhotoUrl;
        smaUrl = ConsultantLoggedIn.get().smaPhotoURl;
        univUrl = ConsultantLoggedIn.get().univPhotoUrl;


        Glide.with(this).load(sdUrl).thumbnail(0.5f).dontAnimate().into(ivPhotoSD);

        //....

    }

    //update photo urls.
    private void updatePhotoUrls(){

        //        sdImgPath, smpImgPath, smaImgPath, univImgPath;

    }

    //upload all images and update
    private void postFiles(){

        sdImgPath = (sdImgPath == null) ? sdUrl : sdImgPath;
//        smaImgPath = (smaImgPath == null) ? smaImgPath : sdImgPath;
//        smpImgPath = (sdImgPath == null) ? sdUrl : sdImgPath;
//        univImgPath = (sdImgPath == null) ? sdUrl : sdImgPath;

        try{
            OkhomeException.chkException(OkhomeUtil.isEmpty(sdImgPath)
                    && OkhomeUtil.isEmpty(sdImgPath)
                    && OkhomeUtil.isEmpty(sdImgPath)
                    && OkhomeUtil.isEmpty(sdImgPath), "1 photo has to be choosed at least");
        }catch(Exception e){
            ToastUtil.showToast(e.getMessage());
            return;
        }

        //set params to upload.
        Map<Integer, String> mapFiles = new HashMap<>();
        mapFiles.put(REQ_PHOTO_SD, sdImgPath);
        mapFiles.put(REQ_PHOTO_SMA, smaImgPath);
        mapFiles.put(REQ_PHOTO_SMP, smpImgPath);
        mapFiles.put(REQ_PHOTO_UNV, univImgPath);


        final int uploadSize = mapFiles.size();
        final ProgressDialog p = ProgressDialog.show(this, null, "Uploading files...");
        final Handler handlerCheckingcompletion = new Handler(){
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
                        break;

                    case REQ_PHOTO_SMP:
                        break;

                    case REQ_PHOTO_UNV:
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

            if(filePath.contains("file://")){
                //upload file only if the type of file source is from SDcard.

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
                // it may occur when the source is from web.
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

            default:
                return;
        }

        Glide.with(this)
                .load("file://" + filePath)
                .thumbnail(0.5f)
                .dontAnimate()
                .into(ivTarget);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            onPhotoChoosed(requestCode, data.getStringExtra(ImageChooserActivity.RESULT_IMAGE_PATH));
        }
    }

    //-----------------------
    @OnClick(R.id.actUpdateEducation_vgSeeSample)
    public void onClickSeeSample(){
        new ShowPhotoDialog(this, R.drawable.img_model2).show();
    }

    @OnClick(R.id.actUpdateEducation_vgPhotoSD)
    public void onSdClick(){
        startActivityForResult(new Intent(this, ImageChooserActivity.class), REQ_PHOTO_SD);
    }

    @OnClick(R.id.actUpdateEducation_vbtnOk)
    public void onOkClick(){}{
        postFiles();

    }
}
