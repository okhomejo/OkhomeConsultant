package id.co.okhome.consultant.view.activity.chatting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.firestore_manager.user.UserFSController;

public class ChattingTestActivity extends OkHomeParentActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_test);

        ButterKnife.bind(this);

        UserFSController.getUserInfos(new String[]{"CT_1", "CT_2", "CT_0"}, new UserFSController.UserInfoCallback() {
            @Override
            public void onUserInfoGet(Map<String, Map<String, String>> mapUser) {
                Log.d("CHATROOM", mapUser.toString());
            }
        });


    }



    //write test
    private void doWriteTest(){
    }

    @OnClick(R.id.actChattingTest_btnOk)
    public void onOk(View v){
        doWriteTest();
    }

    @OnClick(R.id.actChattingTest_btnReadAll)
    public void readAll(View v){
        db.collection("chatrooms").whereEqualTo("members.CT_1", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(!task.isSuccessful()){
                    return;
                }

                int successCount = 0;
                for (DocumentSnapshot document : task.getResult()) {
                    Log.d("CHATROOM", document.getId()  + " " + document.getData().toString());
                }

            }
        });
    }

    @OnClick(R.id.actChattingTest_btnWaitEvent)
    public void onWaitClick(View v){

    }

}
