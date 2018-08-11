package id.co.okhome.consultant.view.activity.chatting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeDateTimeFormatUtil;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.firestore_manager.chat.OkhomeChatManager;
import id.co.okhome.consultant.lib.firestore_manager.chat.OkhomeChatRoomManager;
import id.co.okhome.consultant.lib.firestore_manager.chat.model.ChatRoomItem;
import id.co.okhome.consultant.view.activity.cleaning.CleaningDetailActivity;
import id.co.okhome.consultant.view.activity.etc.photochooser.ImageChooserActivity;

public class ChatActivity extends OkHomeParentActivity {
    @BindView(R.id.actChat_rcv)             RecyclerView rcv;
    @BindView(R.id.actChat_vLoading)        View vLoading;
    @BindView(R.id.actChat_etText)          EditText etMessage;
    @BindView(R.id.actChat_vgCleaningInfo)  ViewGroup vgCleaningInfo;
    @BindView(R.id.actChat_tvTitle)         TextView tvTitle;
    @BindView(R.id.actChat_tvCleaningDate)  TextView tvCleaningDate;
    @BindView(R.id.actChat_vgNotReadyToJoinChat)    ViewGroup vgNotReadyToJoinChat;
    @BindView(R.id.actChat_vgBottom)        ViewGroup vgBottom;
    @BindView(R.id.actChat_vgChatBox)       ViewGroup vgChatBox;


    OkhomeChatManager okhomeChatManager;
    String mRoomId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        OkhomeUtil.setWhiteSystembar(this);
        tvTitle.setText(getIntent().getStringExtra("title"));

        mRoomId = getIntent().getStringExtra("roomId");
        if(mRoomId == null){
            finish();
            return;
        }

        vgBottom.setVisibility(View.INVISIBLE);
        okhomeChatManager = new OkhomeChatManager(rcv, vLoading, mRoomId, ConsultantLoggedIn.id());
        okhomeChatManager.setOnChatRoomReloadListener(new OkhomeChatManager.OnChatRoomReloadListener() {
            @Override
            public void chatRoomReloaded(ChatRoomItem chatRoomItem) {
                //흠.
                vgBottom.setVisibility(View.VISIBLE);

                if(chatRoomItem.roomType.equals(OkhomeChatRoomManager.ROOM_TYPE_CLEANING)){
                    vgCleaningInfo.setVisibility(View.VISIBLE);
                    //정보 갖고오기..
//                    chatRoomItem.cleaningInfo

                    onCleaningInfoCame(chatRoomItem);
                }else{

                    vgCleaningInfo.setVisibility(View.GONE);
                }

                if(!chatRoomItem.members.get("CT_" + ConsultantLoggedIn.id()).equals("M")){
                    vgNotReadyToJoinChat.setVisibility(View.VISIBLE);
                    vgChatBox.setVisibility(View.GONE);
                }else{
                    vgChatBox.setVisibility(View.VISIBLE);
                    vgNotReadyToJoinChat.setVisibility(View.GONE);
                }
            }
        });
        okhomeChatManager.adaptLatestChatItems();

        //채팅방 정보 업데이트 받아야함.. observer상태인지아닌지 계속확인.
    }


    private void onCleaningInfoCame(final ChatRoomItem chatRoomItem){
        int durationMin = chatRoomItem.cleaningInfo.durationMin;

        //날짜설정
        DateTime dtCleaning = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(chatRoomItem.cleaningInfo.whenDateTime);
        DateTime dtCleaningEnd = dtCleaning.plusMinutes(durationMin);

        String whenString = OkhomeDateTimeFormatUtil.printOkhomeType(dtCleaning.getMillis(), "(E) d MMM yy HH:mm");
        String whenStringTail = DateTimeFormat.forPattern("HH:mm").print(dtCleaningEnd);

        whenString += "-" + whenStringTail;

        tvCleaningDate.setText(whenString);
        vgCleaningInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CleaningDetailActivity.start(ChatActivity.this, chatRoomItem.cleaningInfo.cleaningId);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        okhomeChatManager.finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        okhomeChatManager.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.actChat_vbtnSend)
    public void onSend(View v){
        String message = etMessage.getText().toString();
        okhomeChatManager.write("PLAIN", message);
        etMessage.setText("");
    }

    @OnClick(R.id.actChat_btnAttachment)
    public void onAttachment(View v){
        ImageChooserActivity.start(this);
    }


}
