package id.co.okhome.consultant.lib.firestore_manager.chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryListenOptions;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;

import id.co.okhome.consultant.lib.firestore_manager.chat.adapter.ChatAdapter;
import id.co.okhome.consultant.lib.firestore_manager.chat.model.ChatItem;
import id.co.okhome.consultant.lib.jobrowser.callback.ApiResultCallback;
import id.co.okhome.consultant.lib.jobrowser.model.ApiResult;
import id.co.okhome.consultant.rest_apicall.raw_restapi.ImageUploadCall;
import id.co.okhome.consultant.view.activity.etc.photochooser.ImageChooserActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jo on 2018-05-07.
 */

public class OkhomeChatManager {
    final static String LOGTAG = "CHAT";
    final static int PAGE_SIZE = 60;

    private RecyclerView mRecyclerView;
    private String mRoomNumber, mMyId;

    private CollectionReference mChatsRef;
    private DocumentReference mChatroomRef;

    private ChatAdapter mChatAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private Context mContext;
    private View mViewLoading;

    private boolean mLoadingPrevious = false;
    private int lastLoadSize = 0;
    private ListenerRegistration mListenerRegistration;

    private View.OnLayoutChangeListener mOnLayoutChangeListener;

    public OkhomeChatManager(RecyclerView recyclerView, View vLoading, String roomNumber, String myId) {
        this.mRecyclerView = recyclerView;
        this.mRoomNumber = roomNumber;
        this.mContext = recyclerView.getContext();
        this.mViewLoading = vLoading;
        this.mMyId = "CT_" + myId;

        init();
    }

    //init
    private void init(){
        mChatAdapter = new ChatAdapter(mContext, mMyId, PAGE_SIZE);
        mChatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                if(positionStart == 0){

                }else{
                    mRecyclerView.scrollToPosition(mRecyclerView.getAdapter().getItemCount()-1);
                }
//                Log.d(LOGTAG, String.format("positionStart %d itemCount %d", positionStart, itemCount));

            }
        });

        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mChatAdapter);

        mChatsRef = FirebaseFirestore.getInstance().collection("chatrooms").document(mRoomNumber).collection("chats");
        mChatroomRef = FirebaseFirestore.getInstance().collection("chatrooms").document(mRoomNumber);

    }

    private void setLayoutChangeListener(){
        if(mOnLayoutChangeListener == null){
            mOnLayoutChangeListener = new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int left, int top, int right, int bottom,
                                           int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (bottom < oldBottom) {
                        mRecyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount()-1);
                            }
                        }, 100);
                    }
                }
            };

            mRecyclerView.addOnLayoutChangeListener(mOnLayoutChangeListener);
        }

    }

    //after firstload
    private void afterFirstLoad(){
        mRecyclerView.scrollToPosition(mRecyclerView.getAdapter().getItemCount()-1);

        setChatItemObserver();
        setLoadPrevListener();
        setLayoutChangeListener();
//        loadPrevious();
    }

    private void setLoadPrevListener(){
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy < 0) {
                    int visibleItemCount = mLinearLayoutManager.getChildCount();
                    int totalItemCount = mLinearLayoutManager.getItemCount();
                    int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();

                    if(lastLoadSize >= PAGE_SIZE && firstItem <= PAGE_SIZE){
                        if(!mLoadingPrevious){
                            mLoadingPrevious = true;
                            //do load previous
                            loadPrevious();
                        }
                    }
                }

            }
        });
    }


    private void setChatItemObserver(){
        QueryListenOptions options = new QueryListenOptions().includeDocumentMetadataChanges();
        Query query = mChatsRef.orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(3);


        if(mListenerRegistration != null){
            mListenerRegistration.remove();
        }

        mListenerRegistration = query.addSnapshotListener(options, new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {

                if(e != null){
                    return;
                }

//                        SnapshotMetadata m = snapshots.getMetadata();
//                        if(snapshots.getMetadata().hasPendingWrites()){
//                            //local에서 온거
//                            return;
//                        }
                String source = snapshots != null && snapshots.getMetadata().hasPendingWrites()
                        ? "LOCAL" : "SERVER";

                Log.d(LOGTAG, "data come : " + source);
//                        if(source.equals("LOCAL")){
//                            return;
//                        }


//
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case MODIFIED:
                            break;
//                                    ChatItem chatItemModified = dc.getDocument().toObject(ChatItem.class);
                        case ADDED:
                            ChatItem chatItem = dc.getDocument().toObject(ChatItem.class);
                            chatItem.setId(dc.getDocument().getId());
                            boolean success = mChatAdapter.addItem(chatItem);
                            if(success){
                                mChatAdapter.notifyItemChanged(mChatAdapter.getItemCount()-2);
                                mChatAdapter.notifyItemInserted(mChatAdapter.getItemCount()-1);
                            }
                            break;

                        case REMOVED:
                            break;
                    }
                }
            }
        });
    }

    private void loadPrevious(){
        ChatItem chatItem = mChatAdapter.getChatItemList().get(0);
        mChatsRef.whereLessThan("timestamp", chatItem.getTimestamp()).orderBy("timestamp", Query.Direction.DESCENDING).limit(PAGE_SIZE)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.isSuccessful()){
                            return;
                        }

                        int successCount = 0;
                        for (DocumentSnapshot document : task.getResult()) {
                            ChatItem chatItem = document.toObject(ChatItem.class);
                            chatItem.setId(document.getId());
                            boolean success = mChatAdapter.addItem(0, chatItem);
                            if(success){
//                                mChatAdapter.notifyItemInserted(0);
                            }
                            successCount++;
                        }

                        //사이즈 계산해놓고
                        lastLoadSize = successCount;
                        mChatAdapter.notifyItemRangeInserted(0, lastLoadSize);
                        mChatAdapter.notifyItemChanged(lastLoadSize);

//                        mChatAdapter.notifyDataSetChanged();
                        mLoadingPrevious = false;
                    }
                });

    }


    /**첫 페이지 로드*/
    public void adaptLatestChatItems(){
        mViewLoading.setVisibility(View.VISIBLE);
        mChatsRef.orderBy("timestamp", Query.Direction.DESCENDING).limit(PAGE_SIZE)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.isSuccessful()){
                            return;
                        }

                        for (DocumentSnapshot document : task.getResult()) {
                            ChatItem chatItem = document.toObject(ChatItem.class);
                            chatItem.setId(document.getId());
                            mChatAdapter.addItem(0, chatItem);
                        }

                        //사이즈 계산해놓고
                        lastLoadSize = mChatAdapter.getItemCount();

                        mChatAdapter.notifyDataSetChanged();
                        mViewLoading.setVisibility(View.GONE);

                        afterFirstLoad();
                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == ImageChooserActivity.REQ_GET_IMAGE){
            String imgPath = data.getStringExtra(ImageChooserActivity.RESULT_IMAGE_PATH);
            writePhoto(imgPath);
        }
    }

    /**send photo*/
    public void writePhoto(String photoPath){
        //upload 먼저.
        final ProgressDialog p = ProgressDialog.show(mContext, "Loading", "");
        new ImageUploadCall(photoPath).asyncWork(new ApiResultCallback<String>() {
            @Override
            public void onFinish(ApiResult<String> apiResult) {
                p.dismiss();
                if(apiResult.resultCode == 200){
                    //success
                    write("PHOTO", apiResult.object);
                }
            }
        });
    }

    /**작성*/
    public void write(String messageType, String message){
        if(message.length() <= 0){
            return;
        }

        final Map<String, Object> chat = ChatParamBuilder.makeChatMessage("U", mMyId, messageType, message);
        final Map<String, Object> chatRoomLastMessage = ChatParamBuilder.makeLastMessageInChatRoom("U", mMyId, messageType, message);

        mChatsRef.add(chat)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(LOGTAG, "success");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        mChatroomRef.set(chatRoomLastMessage, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ;
                if(!task.isSuccessful()){
                    Toast.makeText(mContext, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    /**박살*/
    public void finish(){
        mListenerRegistration.remove();
        mRecyclerView.removeOnLayoutChangeListener(mOnLayoutChangeListener);
    }
}
