package id.co.okhome.consultant.view.fragment.trainee_tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.firestore_manager.chat.OkhomeChatRoomManager;
import id.co.okhome.consultant.lib.fragment_pager.TabFragmentStatusListener;
import id.co.okhome.consultant.view.activity.chatting.ChatActivity;

/**
 * Created by jo on 2018-01-23.
 */

public class ChatTabFragment extends Fragment implements TabFragmentStatusListener {

    @BindView(R.id.fragTabChatRoom_vLoading)    View vLoading;
    @BindView(R.id.fragTabChatRoom_rcv)         RecyclerView rcv;

    OkhomeChatRoomManager okhomeChatRoomManager = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_chat_f_trainee, null);
        ButterKnife.bind(this, v);
        okhomeChatRoomManager = new OkhomeChatRoomManager(rcv, vLoading, ConsultantLoggedIn.id());
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ChatActivity.class));
            }
        });

        okhomeChatRoomManager.adapt();
    }


    @Override
    public void onSelect() {
    }

    @Override
    public void onSelectWithData(Map<String, Object> param) {

    }

    @Override
    public void onDeselect() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        okhomeChatRoomManager.finish();
    }
}
