package id.co.okhome.consultant.lib.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.co.okhome.consultant.R;

/**
 * Created by jo on 2018-05-06.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatVHolder>{

    Context mContext;
    List<ChatItem> mChatItemList;
    Map<String, ChatItem> mChatItemMap;
    Map<String, ChatItem> mLocalChatItemMap = new HashMap<>();
    String mMyId;
    int mPageSize;

    public ChatAdapter(Context context, String myId, int pageSize) {
        this.mChatItemList = new ArrayList<>();
        this.mChatItemMap = new HashMap<>();
        this.mContext = context;
        this.mMyId = myId;
        this.mPageSize = pageSize;

    }

    public List<ChatItem> getChatItemList() {
        return mChatItemList;
    }

    //add item
    public boolean addItem(int position, ChatItem chatItem){

        //이미 있으면 안넣음.
        if(mChatItemMap.containsKey(chatItem.id())){
            return false;
        }

        mChatItemList.add(position, chatItem);
        mChatItemMap.put(chatItem.id(), chatItem);
        return true;
    }

    public boolean addItem(ChatItem chatItem){
        return addItem(mChatItemList.size(), chatItem);
    }


    @Override
    public ChatVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        ChatVHolder vHolder = new ChatVHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(ChatVHolder holder, int position) {
        //데이터 컴인.
        ImageView ivChatPhoto;
        TextView tvChat, tvTime;
        ViewGroup vgContents, vgPhotoContents;
        ViewGroup vgTextContent;
        View vTopPadding;
        View vBottomPadding;

        ChatItem chatItem = mChatItemList.get(position);
        String myId = mMyId;

        vBottomPadding = holder.vBottomPadding;
        if(chatItem.writerId.equals(myId)){
            holder.vgChatMine.setVisibility(View.VISIBLE);
            holder.vgChatOpposite.setVisibility(View.GONE);

            ivChatPhoto = holder.ivMyChatPhoto;
            tvChat = holder.tvMyChat;
            tvTime = holder.tvMyTime;
            vgContents = holder.vgMyContents;
            vgPhotoContents = holder.vgMyPhotoContent;
            vTopPadding = holder.vMyTopPadding;
            vgTextContent = holder.vgMyTextContent;
        }else{
            holder.vgChatMine.setVisibility(View.GONE);
            holder.vgChatOpposite.setVisibility(View.VISIBLE);

            ivChatPhoto = holder.ivOppositeChatPhoto;
            tvChat = holder.tvOppositeChat;
            tvTime = holder.tvOppositeTime;
            vgContents = holder.vgOppositeContents;
            vgPhotoContents = holder.vgOppositePhotoContent;
            vTopPadding = holder.vOppositeTopPadding;
            vgTextContent = holder.vgOppositeTextContent;
        }

        //------------------날짜 헤더 처리
        boolean showDateHeader = false;
        if(position == 0){
            showDateHeader = true;
        }else{

            ChatItem chatPrevItem = mChatItemList.get(position-1);
            DateTime dtNow = chatItem.dateTime();
            DateTime dtPrev = chatPrevItem.dateTime();

            if(dtNow.getDayOfYear() == dtPrev.getDayOfYear()){
                showDateHeader = false;
            }else{
                showDateHeader = true;
            }
        }
        if(showDateHeader){
            holder.vgHeaderDate.setVisibility(View.VISIBLE);
            String date = DateTimeFormat.forPattern("E d MMM yy").withZone(DateTimeZone.forID("Asia/Jakarta")).withLocale(new Locale("id")).print(new DateTime(chatItem.getTimestamp()));
            holder.tvHeaderDate.setText(date);
        }else{
            holder.vgHeaderDate.setVisibility(View.GONE);
        }


        //-----------------채팅 풍선 앞 날짜 중첩되는지 확인하고 날짜는 중복된 아이템의 마지막에만표시
        boolean showDateTime = true;
        //나, 앞, 뒤체크
        if(position == getItemCount()-1){
            //마지막일경우
            showDateTime = true;
        }else{
            //나머지

            ChatItem chatNext = mChatItemList.get(position+1);

            DateTime dtNow = chatItem.dateTime();
            DateTime dtNext = chatNext.dateTime();

            //지금이랑 다음꺼랑 같으면 일단 지금꺼는 시가없앰
            if(dtNow.getHourOfDay() == dtNext.getHourOfDay() && dtNow.getMinuteOfHour() == dtNext.getMinuteOfHour()){
                showDateTime = false;
            }else{
                showDateTime = true;
            }
        }
        if(showDateTime){
            vBottomPadding.setVisibility(View.VISIBLE);
            tvTime.setVisibility(View.VISIBLE);
        }else{
            vBottomPadding.setVisibility(View.GONE);
            tvTime.setVisibility(View.GONE);
        }


        //------------------기타 내용 넣기

        DateTime writeTime = new DateTime(chatItem.getTimestamp());
        String dateTime = DateTimeFormat.forPattern("aa h:mm").withZone(DateTimeZone.forID("Asia/Jakarta")).print(writeTime);
        tvTime.setText(dateTime);


        //------------------사진 처리
        if(chatItem.msgType.equals("PHOTO")){
            vgTextContent.setVisibility(View.GONE);
            tvChat.setText(chatItem.msg);

            vgPhotoContents.setVisibility(View.VISIBLE);
            ivChatPhoto.setVisibility(View.VISIBLE);

            String sizeString = chatItem.msg.substring(chatItem.msg.lastIndexOf("_")+1, (chatItem.msg.lastIndexOf(".")));
            int width = Integer.parseInt(sizeString.substring(0, sizeString.indexOf("x")));
            int height = Integer.parseInt(sizeString.substring(sizeString.indexOf("x")+1));

            Bitmap returnedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(returnedBitmap);
            canvas.drawColor(Color.parseColor("#00000000"));

            Drawable d = new BitmapDrawable(mContext.getResources(), returnedBitmap);

            Picasso.with(mContext).load(chatItem.msg).placeholder(d).into(ivChatPhoto);
//            Glide.with(mContext).load(chatItem.msg).thumbnail(0.5f).into(ivChatPhoto);

        }
        //-------------------테긋트 처리
        else{
            vgTextContent.setVisibility(View.VISIBLE);
            tvChat.setText(chatItem.msg);
            vgPhotoContents.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return mChatItemList.size();
    }


}
