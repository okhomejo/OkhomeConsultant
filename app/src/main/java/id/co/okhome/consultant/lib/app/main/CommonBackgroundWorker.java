package id.co.okhome.consultant.lib.app.main;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by jo on 2018-05-09.
 */

public class CommonBackgroundWorker {

    public void onResume(){

    }

    private void updateFcmToken(){
        String token = FirebaseInstanceId.getInstance().getToken();
        if(token != null){
//            OkhomeRestApi.getAccountClient().update()
        }
    }
}
