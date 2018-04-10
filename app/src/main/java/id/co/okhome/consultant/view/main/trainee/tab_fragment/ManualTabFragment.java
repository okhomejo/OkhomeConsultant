package id.co.okhome.consultant.view.main.trainee.tab_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.fragment_pager.TabFragmentStatusListener;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.FaqModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.viewholder.BlankVHolder;
import id.co.okhome.consultant.view.viewholder.ManualParentVHolder;

/**
 * Created by jo on 2018-01-23.
 */

public class ManualTabFragment extends Fragment implements TabFragmentStatusListener {

    @BindView(R.id.fragTabManualForTrainee_rcv)
    RecyclerView rcv;

    @BindView(R.id.fragTabManualForTrainee_vProgress)
    View vLoading;


    JoRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_home_f_manual, null);
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
                        .setItemViewHolderCls(ManualParentVHolder.class)
//                        .setHeaderViewHolderCls(TrainingHeaderForTraineeVHolder.class)
                        .setFooterViewHolderCls(BlankVHolder.class)
        );

//        adapter.addHeaderItem(new String());
        adapter.addFooterItem("");
    }

    //get data and set adapter with data
    private void loadList(){
        vLoading.setVisibility(View.VISIBLE);

        OkhomeRestApi.getCommonClient().getAllFaqs(0, "MANUAL_TRAINEE").enqueue(new RetrofitCallback<List<FaqModel>>() {
            @Override
            public void onSuccess(List<FaqModel> faqs) {
                adapter.setListItems(faqs);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                vLoading.setVisibility(View.GONE);
            }
        });
    }
}
