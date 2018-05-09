package id.co.okhome.consultant.lib.firestore_manager.user;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jo on 2018-05-09.
 */

public class UserFSController {

    /**사용자 정보 조회*/
    public static void getUserInfos(final String[] userIds, final UserInfoCallback userInfoCallback){
        final Map<String, Map<String, String>> mapUser = new HashMap<>();

        OnCompleteListener onGetUserInfoCompleteListener = new OnCompleteListener<QuerySnapshot>() {
            int jobCount = 0;

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                jobCount++;
                if(!task.isSuccessful()){
                    ;
                }else{
                    for (DocumentSnapshot document : task.getResult()) {

                        Map<String, String> mapUserInfo = new HashMap<>();
                        mapUserInfo.put("name", document.getString("name"));
                        mapUserInfo.put("photoUrl", document.getString("photoUrl"));

                        mapUser.put(document.getId(), mapUserInfo);
                    }
                }

                if(jobCount >= userIds.length){
                    userInfoCallback.onUserInfoGet(mapUser);
                }


            }
        };

        for(String userId : userIds){
            FirebaseFirestore.getInstance().collection("users").whereEqualTo("id", userId).get().addOnCompleteListener(onGetUserInfoCompleteListener);
        }
    }

    public interface UserInfoCallback{
        void onUserInfoGet(Map<String, Map<String, String>> mapUser);

    }
}
