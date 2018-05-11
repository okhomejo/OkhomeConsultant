package id.co.okhome.consultant.lib.app.main;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;

/**
 * Created by jo on 2018-05-09.
 */

public class CommonBackgroundWorker {

    public static void onResume(){
        updateFcmToken();
    }

    private static void updateFcmToken(){
        String token = FirebaseInstanceId.getInstance().getToken();
        if(token != null){
            String param = new Gson().toJson(OkhomeUtil.makeMap("fcmToken", token));
            OkhomeRestApi.getAccountClient().update(ConsultantLoggedIn.id(), param).enqueue(new RetrofitCallback<String>() {
                @Override
                public void onSuccess(String result) {

                }
            });
        }
    }
}
