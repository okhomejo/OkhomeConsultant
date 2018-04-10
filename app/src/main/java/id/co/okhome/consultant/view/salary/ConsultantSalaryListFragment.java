package id.co.okhome.consultant.view.salary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.fragment_pager.TabFragmentStatusListener;
import id.co.okhome.consultant.view.viewholder.BlankVHolder;
import id.co.okhome.consultant.view.viewholder.ConsultantSalaryVHolder;

/**
 * Created by jo on 2018-04-07.
 */

public class ConsultantSalaryListFragment extends Fragment implements TabFragmentStatusListener {

    @BindView(R.id.fragConsultantSalaryList_rcv)
    RecyclerView rcv;

    JoRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_consultant_salarylist, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

        String type = getArguments().getString("TYPE");
        JoRecyclerAdapter.Params params = new JoRecyclerAdapter.Params();
        params.setRecyclerView(rcv)
                .addParam("TYPE", type)
                .setFooterViewHolderCls(BlankVHolder.class)
                .setItemViewHolderCls(ConsultantSalaryVHolder.class);

        adapter = new JoRecyclerAdapter(params);
        adapter.addFooterItem("A");

        loadList();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSelect() {

    }

    @Override
    public void onDeselect() {

    }

    private void loadList(){
        List<String> list = new ArrayList<>();
        for(int i = 0 ; i < 10; i++){
            list.add(i+"");
        }
        adapter.setListItems(list);
    }


}
