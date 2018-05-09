package id.co.okhome.consultant.lib.firestore_manager.chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.okhome.consultant.R;
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

        holder.tvName.setText(chatRoomItem.lastWriterName);

        if(chatRoomItem.lastMessage.msgType.equals("PHOTO")){
            holder.tvMessage.setText("(Photo)");
        }else{
            holder.tvMessage.setText(chatRoomItem.lastMessage.msg);
        }

        Glide.with(mContext).load(chatRoomItem.lastWriterPhotoUrl).thumbnail(0.5f).into(holder.ivPhoto);

        holder.vContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, ChatActivity.class).putExtra("roomId", chatRoomItem.id));
            }
        });

    }


    @Override
    public int getItemCount() {
        return mChatRoomItemList.size();
    }


}
