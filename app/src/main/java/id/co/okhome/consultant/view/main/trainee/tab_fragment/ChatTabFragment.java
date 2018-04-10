package id.co.okhome.consultant.view.main.trainee.tab_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.fragment_pager.TabFragmentStatusListener;

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
    }

    @Override
    public void onSelect() {

    }

    @Override
    public void onDeselect() {

    }

}
