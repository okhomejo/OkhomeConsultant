package id.co.okhome.consultant.lib.firestore_manager.chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkhomeDateTimeFormatUtil;
import id.co.okhome.consultant.lib.firestore_manager.chat.OkhomeChatRoomManager;
import id.co.okhome.consultant.lib.firestore_manager.chat.model.ChatRoomItem;
import id.co.okhome.consultant.lib.firestore_manager.chat.vholder.ChatRoomVHolder;
import id.co.okhome.consultant.view.activity.chatting.ChatActivity;

/**
 * Created by jo on 2018-05-06.
 */

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomVHolder>{

    Context mContext;
    List<ChatRoomItem> mChatRoomItemList;
    Map<String, ChatRoomItem> mChatRoomItemMap;
    String mMyId;

    public ChatRoomAdapter(Context context, String myId) {
        this.mChatRoomItemList = new ArrayList<>();
        this.mChatRoomItemMap = new HashMap<>();
        this.mContext = context;
        this.mMyId = myId;
    }

    public List<ChatRoomItem> getChatItemList() {
        return mChatRoomItemList;
    }

    //add item
    public boolean addItem(int position, ChatRoomItem chatRoomItem){

        //이미 있으면 안넣음.
        if(mChatRoomItemMap.containsKey(chatRoomItem.id)){
            return false;
        }

        mChatRoomItemList.add(position, chatRoomItem);
        mChatRoomItemMap.put(chatRoomItem.id, chatRoomItem);
        return true;
    }

    public boolean addItem(ChatRoomItem chatRoomItem){
        return addItem(mChatRoomItemList.size(), chatRoomItem);
    }

    public void setList(List<ChatRoomItem> chatRoomItems){
        this.mChatRoomItemList = chatRoomItems;
    }


    @Override
    public ChatRoomVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_room, null, false);
        ChatRoomVHolder vHolder = new ChatRoomVHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(ChatRoomVHolder holder, int position) {
        final ChatRoomItem chatRoomItem = mChatRoomItemList.get(position);

        holder.tvName.setText(chatRoomItem.roomTitle);

        if(chatRoomItem.lastMessages == null){
            holder.tvMessage.setText("You has been invited.");
        }
        else if(chatRoomItem.latestChatThumbItem(mMyId).msgType.equals("PHOTO")){
            holder.tvMessage.setText("(Photo)");
        }else{
            String msg = chatRoomItem.latestChatThumbItem(mMyId).msg.replace("#{NEW_CHATROOM}", "You has been invited.");
            holder.tvMessage.setText(msg);
        }

        holder.vgThumb.setVisibility(View.GONE);
        holder.vgThumbPhoto.setVisibility(View.GONE);
        holder.tvCleaningDate.setVisibility(View.GONE);
        //유저타입일 경우
        if(chatRoomItem.roomType.equals(OkhomeChatRoomManager.ROOM_TYPE_USER)){
            holder.vgThumbPhoto.setVisibility(View.VISIBLE);
            holder.ivPhoto.setImageResource(R.drawable.img_face_okhome);
        }

        //클리닝 일경우
        else if(chatRoomItem.roomType.equals(OkhomeChatRoomManager.ROOM_TYPE_CLEANING)){
            holder.tvCleaningDate.setVisibility(View.VISIBLE);
            holder.vgThumb.setVisibility(View.VISIBLE);
            holder.tvThumbChar.setText(chatRoomItem.roomTitle.substring(0, 1).toUpperCase());
            String whenDateTime = chatRoomItem.cleaningInfo.whenDateTime;

            String customerId = chatRoomItem.cleaningInfo.customerId;
            int durationMin = chatRoomItem.cleaningInfo.durationMin;

            //날짜설정
            DateTime dtCleaning = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(whenDateTime);
            DateTime dtCleaningEnd = dtCleaning.plusMinutes(durationMin);

            String whenString = OkhomeDateTimeFormatUtil.printOkhomeType(dtCleaning.getMillis(), "(E) d MMM yy HH:mm");
            String whenStringTail = DateTimeFormat.forPattern("HH:mm").print(dtCleaningEnd);

            whenString += "-" + whenStringTail;


            holder.tvCleaningDate.setText(whenString);
            //클리닝
        }


        dispatchInsertDateTime(holder, chatRoomItem);
        dispatchUnreadDot(holder, chatRoomItem);

        holder.vContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, ChatActivity.class)
                        .putExtra("title", chatRoomItem.roomTitle)
                        .putExtra("roomId", chatRoomItem.id));
            }
        });

    }

    private void dispatchUnreadDot(ChatRoomVHolder holder, ChatRoomItem chatRoomItem){
        if(OkhomeChatRoomManager.hasCheckedMessage(chatRoomItem.id, chatRoomItem.latestChatThumbItem(mMyId).getTimestamp())){
            holder.vRedDot.setVisibility(View.GONE);
        }else{
            holder.vRedDot.setVisibility(View.VISIBLE);
        }

    }

    //입력시간 처리
    private void dispatchInsertDateTime(ChatRoomVHolder holder, ChatRoomItem chatRoomItem){

        //시간 ~전 처리
        DateTime dtInsert = new DateTime(chatRoomItem.latestChatThumbItem(mMyId).getTimestamp().getTime()).withZone(DateTimeZone.getDefault());
        DateTime dtNow = new DateTime(System.currentTimeMillis());

        long diff = dtNow.getMillis() - dtInsert.getMillis();
        int diffSec = (int)(diff / 1000);
        String timeText = "";
        if(diffSec < 60){
            //now
            timeText = "Now";
        }else if(diffSec < 60 * 60){
            //분전
            timeText = (diffSec / 60) + " Minutes ago";
        }else if(diffSec < 60 * 60 * 24){
            //시간전
            timeText = (diffSec / 60 /  60) + " Hours ago";
        }else if(diffSec < 60 * 60 * 24 * 2) {
            //어제
            timeText = "Yesterday";
        }else{
            //날짜
            timeText = DateTimeFormat.forPattern("(E) d MMM yy, hh:mm a").print(dtInsert);
        }

        holder.tvDateTime.setText(timeText);
    }


    @Override
    public int getItemCount() {
        return mChatRoomItemList.size();
    }


}
