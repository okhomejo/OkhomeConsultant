package id.co.okhome.consultant.lib.firestore_manager.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryListenOptions;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import id.co.okhome.consultant.lib.firestore_manager.chat.adapter.ChatRoomAdapter;
import id.co.okhome.consultant.lib.firestore_manager.chat.model.ChatRoomItem;
import id.co.okhome.consultant.lib.firestore_manager.user.UserFSController;

/**
 * Created by jo on 2018-05-07.
 */

public class OkhomeChatRoomManager {
    final static String LOGTAG = "CHAT";

    private RecyclerView mRecyclerView;
    private String mMyId;

    private CollectionReference mChatroomRef;

    private ChatRoomAdapter mChatRoomAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private Context mContext;
    private View mViewLoading;

    private boolean mLoadingPrevious = false;
    private int lastLoadSize = 0;
    private ListenerRegistration mListenerRegistration;

    private View.OnLayoutChangeListener mOnLayoutChangeListener;

    public OkhomeChatRoomManager(RecyclerView recyclerView, View vLoading, String myId) {
        this.mRecyclerView = recyclerView;
        this.mContext = recyclerView.getContext();
        this.mViewLoading = vLoading;
        this.mMyId = "CT_" + myId;

        init();
    }

    //init
    private void init(){
        mChatRoomAdapter = new ChatRoomAdapter(mContext, mMyId);

        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mChatRoomAdapter);

        mChatroomRef = FirebaseFirestore.getInstance().collection("chatrooms");

    }

    public void adapt(){
        setChatRoomItemChangeObserver();
    }

    private void setChatRoomItemChangeObserver(){
        QueryListenOptions options = new QueryListenOptions().includeDocumentMetadataChanges();
        Query query = mChatroomRef
                .whereLessThan("members." + mMyId, "Z");

        if(mListenerRegistration != null){
            mListenerRegistration.remove();
        }

        mListenerRegistration = query.addSnapshotListener(options, new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {

                if(e != null){
                    return;
                }

                String source = snapshots != null && snapshots.getMetadata().hasPendingWrites() ? "LOCAL" : "SERVER";
//                if(source.equals("LOCAL")){
//                    return;
//                }

                adaptLatestChatItems();
            }
        });
    }


    /**첫 페이지 로드*/
    private void adaptLatestChatItems(){
//        mViewLoading.setVisibility(View.VISIBLE);
        mChatroomRef
                .whereLessThan("members." + mMyId, "Z").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(!task.isSuccessful()){
                    return;
                }

                //마지막에 작성한 사용자 정보 가져오기 준비
                Set<String> userIdSets= new HashSet<>();

                final List<ChatRoomItem> chatRoomItems = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    ChatRoomItem chatRoomItem = document.toObject(ChatRoomItem.class);
                    chatRoomItem.id=document.getId();
                    chatRoomItems.add(chatRoomItem);
                    userIdSets.add(chatRoomItem.lastMessage.writerId);
                }

                String[] ids = userIdSets.toArray(new String[userIdSets.size()]);

                //사용자 이름, 사진 정보 가져오기
                UserFSController.getUserInfos(ids, new UserFSController.UserInfoCallback() {
                    @Override
                    public void onUserInfoGet(Map<String, Map<String, String>> mapUser) {

                        List<ChatRoomItem> chatRoomItemsResult =  new ArrayList<>();

                        for(ChatRoomItem chatRoom : chatRoomItems){
                            if(mapUser.containsKey(chatRoom.lastMessage.writerId)){

                                Map<String, String> mapUserInfo = mapUser.get(chatRoom.lastMessage.writerId);

                                String name = mapUserInfo.get("name");
                                String photoUrl = mapUserInfo.get("photoUrl");

                                chatRoom.lastWriterName = name;
                                chatRoom.lastWriterPhotoUrl = photoUrl;

                                chatRoomItemsResult.add(chatRoom);

                            }
                        }

                        mChatRoomAdapter.setList(chatRoomItemsResult);
                        mChatRoomAdapter.notifyDataSetChanged();
                        mViewLoading.setVisibility(View.GONE);
                    }
                });

//                mChatRoomAdapter.addItem(chatRoomItem);

            }
        });

    }


    /**박살*/
    public void finish(){
        if(mListenerRegistration!= null){
            mListenerRegistration.remove();
        }
    }
}
