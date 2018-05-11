package id.co.okhome.consultant.service;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import id.co.okhome.consultant.lib.app.OkhomeUtil;

/**
 * Created by jo on 2018-05-09.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        OkhomeUtil.Log("From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            OkhomeUtil.Log("Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            OkhomeUtil.Log("Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        //푸시 처리
//        patchMessage(remoteMessage.getData());

    }
}
