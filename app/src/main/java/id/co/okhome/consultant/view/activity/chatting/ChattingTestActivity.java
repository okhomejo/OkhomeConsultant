package id.co.okhome.consultant.view.activity.chatting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryListenOptions;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;

public class ChattingTestActivity extends OkHomeParentActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_test);

        ButterKnife.bind(this);

        db = FirebaseFirestore.getInstance();
    }

    //write test
    private void doWriteTest(){
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", new Random().nextInt(100) + "");
        user.put("born", new Random().nextInt(1000));
        user.put("timestamp", OkhomeUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));


        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        OkhomeUtil.Log("ChattingTestActivity" + " DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        OkhomeUtil.Log("Error adding document" +  e.toString());
                    }
                });
    }

    @OnClick(R.id.actChattingTest_btnOk)
    public void onOk(View v){
        doWriteTest();
    }

    @OnClick(R.id.actChattingTest_btnReadAll)
    public void readAll(View v){
        db.collection("users")
                .whereEqualTo("first", "Ada")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                OkhomeUtil.Log(document.getId() + " => " + document.getData());
                            }
                        } else {
                            OkhomeUtil.Log("Error getting documents.", task.getException());
                        }
                    }
                });

    }

    @OnClick(R.id.actChattingTest_btnWaitEvent)
    public void onWaitClick(View v){

        QueryListenOptions options = new QueryListenOptions().includeDocumentMetadataChanges();

        db.collection("users")
                .whereEqualTo("first", "Ada")
                .orderBy("timestamp", Query.Direction.DESCENDING).limit(3)
                .addSnapshotListener(options, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                        if (e == null) {

                            OkhomeUtil.Log("from check.. isFromCache : " + snapshots.getMetadata().isFromCache() + " hasPendingWrites : " + snapshots.getMetadata().hasPendingWrites());
                            if(!snapshots.getMetadata().hasPendingWrites()){
                                for (DocumentSnapshot document : snapshots) {
                                    OkhomeUtil.Log(document.getId() + " => " + document.getData());
                                }
                            }


                        } else {
                            OkhomeUtil.Log(ChattingTestActivity.this, e);
                        }
                    }


                });
    }

}
