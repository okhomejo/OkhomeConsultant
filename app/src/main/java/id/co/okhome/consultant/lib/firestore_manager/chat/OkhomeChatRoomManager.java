package id.co.okhome.consultant.lib.firestore_manager.chat;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.okhome.consultant.lib.JoSharedPreference;
import id.co.okhome.consultant.lib.firestore_manager.chat.adapter.ChatRoomAdapter;
import id.co.okhome.consultant.lib.firestore_manager.chat.model.ChatRoomItem;
import id.co.okhome.consultant.lib.firestore_manager.user.UserFSController;

/**
 * Created by jo on 2018-05-07.
 */

public class OkhomeChatRoomManager {


    public final static String ROOM_TYPE_CLEANING           = "CLEANING";
    public final static String ROOM_TYPE_USER               = "USER";

    public final static String MEMBER_TYPE_SUPERADMIN       = "S";
    public final static String MEMBER_TYPE_CONSULTANT       = "CT";
    public final static String MEMBER_TYPE_CUSTOMER         = "CU";

    public final static String AUTH_MEMBER                  = "M";
    public final static String AUTH_OBSERVER                = "O";

    public final static String MESSAGE_PLAIN                = "PLAIN";
    public final static String MESSAGE_PHOTO                = "PHOTO";
    public final static String MESSAGE_SYSTEM               = "SYSTEM";

    public final static String SYSTEM_ADMINID               = "SYS_1";


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
                .whereLessThan("members." + mMyId, "Z"); //Z아래라는건 전부다 불러온다는 얘기임.

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
                .whereLessThan("members." + mMyId, "Z").get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int i = 0;
                        i++;
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(!task.isSuccessful()){
                            return;
                        }

                        //채팅방 썸네일 정보 동기화용 맵 두개
                        final Map<String, ChatRoomItem> chatroomsByUserIdMap= new HashMap<>();
                        final Map<ChatRoomItem, String> userIdByChatroomMap= new HashMap<>();
                        //실제 채팅방 목ㄺ
                        final List<ChatRoomItem> chatRoomItems = new ArrayList<>(); //채팅방 정보임.

                        for (DocumentSnapshot document : task.getResult()) {
                            ChatRoomItem chatRoomItem = document.toObject(ChatRoomItem.class);
                            chatRoomItem.id = document.getId();

//                    걍 방만파놓으면 내용이 없는 경우가 있ㅇ므.
                            if(chatRoomItem.lastMessages == null){
                                continue;
                            }

                            //넣고
                            chatRoomItems.add(chatRoomItem);
                            if(chatRoomItem.roomType.equals(ROOM_TYPE_CLEANING)){
                                String customerId = chatRoomItem.cleaningInfo.customerId + "";

                                //타이틀 설정
                                chatRoomItem.roomTitle =  null;
                                chatRoomItem.roomImgUrl = null;

                                //이미지는 동그라미에 성 J이런시긍로
                                chatroomsByUserIdMap.put(customerId, chatRoomItem);
                                userIdByChatroomMap.put(chatRoomItem, customerId);
                            }

                            else if(chatRoomItem.roomType.equals(ROOM_TYPE_USER)){
                                chatRoomItem.roomTitle = "Okhome";
                                chatRoomItem.roomImgUrl = null;
                            }
                        }


                        final Handler handlerFinish = new Handler(){
                            @Override
                            public void dispatchMessage(Message msg) {
                                //정렬
                                Collections.sort(chatRoomItems, new Comparator<ChatRoomItem>() {
                                    @Override
                                    public int compare(ChatRoomItem t1, ChatRoomItem t2) {
                                        return t2.latestChatThumbItem(mMyId).getTimestamp().compareTo(t1.latestChatThumbItem(mMyId).getTimestamp());
                                    }
                                });

                                mChatRoomAdapter.setList(chatRoomItems);
                                mChatRoomAdapter.notifyDataSetChanged();
                                mViewLoading.setVisibility(View.GONE);
                            }
                        };
                        //채팅방에 들어갈 title
                        String[] ids = chatroomsByUserIdMap.keySet().toArray(new String[chatroomsByUserIdMap.size()]);

                        if(ids.length > 0){
                            UserFSController.getUserInfos(ids, new UserFSController.UserInfoCallback() {
                                @Override
                                public void onUserInfoGet(Map<String, Map<String, String>> mapUser) {

                                    //맵과 사용자 정보 매칭
                                    for(Map.Entry<ChatRoomItem, String> entry : userIdByChatroomMap.entrySet()){

                                        ChatRoomItem chatRoomItem = entry.getKey();
                                        String customerId = entry.getValue();

                                        Map<String, String> mapUserInfo =  mapUser.get(customerId);

                                        String userName = mapUserInfo.get("name");
                                        String userPhotoUrl = mapUserInfo.get("photoUrl");

                                        //
                                        //클리닝 타입말고 올 일 없음.
                                        if(chatRoomItem.roomType.equals(ROOM_TYPE_CLEANING)){
                                            chatRoomItem.roomTitle = userName;
                                        }

                                    }

                                    handlerFinish.sendEmptyMessage(0);
                                }
                            });
                        }else{
                            handlerFinish.sendEmptyMessage(0);
                        }
                        //사용자 이름, 사진 정보 가져오기


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

    public final static void plusLastCheckedMessageMills(String chatroomId){
        Long mills = JoSharedPreference.with().get("LAST_CHATROOM_CHECKED_" + chatroomId);
        if(mills == null){
            ;
            JoSharedPreference.with().push("LAST_CHATROOM_CHECKED_" + chatroomId, System.currentTimeMillis());
        }else{
            JoSharedPreference.with().push("LAST_CHATROOM_CHECKED_" + chatroomId, mills + 3000);
        }
    }

    /** 로컬시간 기준으로 마지막에 읽은 시간 업데이트*/
    public final static void updateLastCheckedMessage(String chatroomId, Date date){
        //비교해서 업데이트해야겠음.

        Long mills = JoSharedPreference.with().get("LAST_CHATROOM_CHECKED_" + chatroomId);
        if(mills == null){
            JoSharedPreference.with().push("LAST_CHATROOM_CHECKED_" + chatroomId, date.getTime());
        }else{
            if(mills < date.getTime()){
                JoSharedPreference.with().push("LAST_CHATROOM_CHECKED_" + chatroomId, date.getTime() + 1);
            }
        }
    }

    /**방의 시간ㅇㄹ 넘김 기준으로 체크함.*/
    public final static boolean hasCheckedMessage(String chatroomId, Date date){

        Long lastMessageMills = JoSharedPreference.with().get("LAST_CHATROOM_CHECKED_" + chatroomId);

        if(lastMessageMills == null){
            return false;
        }else{
            if(lastMessageMills > date.getTime()){
                return true;
            }else{
                return false;
            }
        }
    }
}
