package id.co.okhome.consultant.view.activity.chatting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.firestore_manager.chat.OkhomeChatManager;
import id.co.okhome.consultant.view.activity.etc.photochooser.ImageChooserActivity;

public class ChatActivity extends OkHomeParentActivity {
    @BindView(R.id.actChat_rcv)             RecyclerView rcv;
    @BindView(R.id.actChat_vLoading)        View vLoading;
    @BindView(R.id.actChat_etText)          EditText etMessage;
    @BindView(R.id.actChat_vgCleaningInfo)  ViewGroup vgCleaningInfo;

    OkhomeChatManager okhomeChatManager;
    String mRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        OkhomeUtil.setWhiteSystembar(this);

        mRoomId = getIntent().getStringExtra("roomId");
        if(mRoomId == null){
            finish();
            return;
        }

        okhomeChatManager = new OkhomeChatManager(rcv, vLoading, mRoomId, ConsultantLoggedIn.id());
        okhomeChatManager.adaptLatestChatItems();

        //채팅방 정보 업데이트 받아야함.. observer상태인지아닌지 계속확인.
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
