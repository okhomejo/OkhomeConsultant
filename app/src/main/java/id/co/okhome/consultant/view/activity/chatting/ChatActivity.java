package id.co.okhome.consultant.view.activity.chatting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.chat.OkhomeChatManager;
import id.co.okhome.consultant.view.activity.etc.photochooser.ImageChooserActivity;

public class ChatActivity extends OkHomeParentActivity {
    @BindView(R.id.actChat_rcv)         RecyclerView rcv;
    @BindView(R.id.actChat_vLoading)    View vLoading;
    @BindView(R.id.actChat_etText)      EditText etMessage;

    OkhomeChatManager okhomeChatManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        OkhomeUtil.setWhiteSystembar(this);
        okhomeChatManager = new OkhomeChatManager(rcv, vLoading, "1", "CT_2");
        okhomeChatManager.adaptLatestChatItems();
        rcv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    rcv.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rcv.smoothScrollToPosition(rcv.getAdapter().getItemCount()-1);
                        }
                    }, 100);
                }
            }
        });
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
