package id.co.okhome.consultant.view.fragment.trainee_tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.fragment_pager.TabFragmentStatusListener;
import id.co.okhome.consultant.view.activity.chatting.ChatActivity;

/**
 * Created by jo on 2018-01-23.
 */

public class ChatTabFragment extends Fragment implements TabFragmentStatusListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_chat_f_trainee, null);
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

}
