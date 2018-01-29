package id.co.okhome.consultant.view.main.trainee.fragment;

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
import id.co.okhome.consultant.model.TrainingModel;
import id.co.okhome.consultant.view.viewholder.BlankHolder;
import id.co.okhome.consultant.view.viewholder.TrainingForTraineeVHolder;

/**
 * Created by jo on 2018-01-23.
 */

public class TrainingTabFragment extends Fragment implements TabFragmentStatusListener {

    @BindView(R.id.fragTabTrainingForTrainee_rcv)
    RecyclerView rcv;

    @BindView(R.id.fragTabTrainingForTrainee_vProgress)
    View vLoading;


    JoRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_training_f_trainee, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

        init();

        loadList();
    }

    @Override
    public void onSelect() {

    }

    @Override
    public void onDeselect() {

    }

    private void init(){
        adapter = new JoRecyclerAdapter(
                new JoRecyclerAdapter.Params()
                        .setRecyclerView(rcv)
                        .setItemViewHolderCls(TrainingForTraineeVHolder.class)
//                        .setHeaderViewHolderCls(TrainingHeaderForTraineeVHolder.class)
                        .setFooterViewHolderCls(BlankHolder.class)
        );

//        adapter.addHeaderItem(new String());
        adapter.addFooterItem("");
    }

    //get data and set adapter with data
    private void loadList(){
        vLoading.setVisibility(View.VISIBLE);

//        DelayedWorkRepeator.with("1").setCount(1).setDelay(1000).setJob(new DelayedWorkRepeator.Job() {
//            @Override
//            public void work() {
//
//                List<TrainingModel> list = new ArrayList<>();
//                for(int i = 0; i < 10; i ++){
//                    TrainingModel m = new TrainingModel();
//                    list.add(m);
//                }
//                adapter.setListItems(list);
//
//                vLoading.setVisibility(View.GONE);
//            }
//        }).work();

        List<TrainingModel> list = new ArrayList<>();
        for(int i = 0; i < 8; i ++){
            TrainingModel m = new TrainingModel();
            list.add(m);
        }
        adapter.setListItems(list);

        vLoading.setVisibility(View.GONE);

    }
}
