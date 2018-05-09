package id.co.okhome.consultant.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "OKHOME";

    public MyFirebaseInstanceIDService() {
    }

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);


    }

    private void sendRegistrationToServer(String token) {
        //ctTFr1m0nio:APA91bHYymejv348MKw6WeLjsaHBpfIQ7Yh-qZ7puF48e0NzEW94RVh-xjWHQiggZXYsYsApZDVV8M1WSi3_0BK5MHa-LGbquHLgzEsaSLViTeVQGpjw91-3Fdh_-BKZLRfEU4eRxX0D

//        Util.showToast(this, token);
    }

}
