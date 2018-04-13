package id.co.okhome.consultant.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;
import com.mrjodev.jorecyclermanager.footerloading.FooterLoadingListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.fragment_pager.TabFragmentStatusListener;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.MoneyHistoryModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.viewholder.BlankVHolder;
import id.co.okhome.consultant.view.viewholder.ConsultantSalaryVHolder;

/**
 * Created by jo on 2018-04-07.
 */

public class ConsultantSalaryListFragment extends Fragment implements TabFragmentStatusListener {

    @BindView(R.id.fragConsultantSalaryList_rcv)
    RecyclerView rcv;

    @BindView(R.id.fragConsultantSalaryList_vLoading)
    View vLoading;

    JoRecyclerAdapter<MoneyHistoryModel> adapter;
    String loadType = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_consultant_salarylist, null);
        ButterKnife.bind(this, v);

        String type = getArguments().getString("TYPE");

        if(type.equals("COMPLETE")){
            loadType = "paid";
        }else{
            loadType = "waiting";
        }

        adapter = new JoRecyclerAdapter<>(new JoRecyclerAdapter.Params()
                .setRecyclerView(rcv)
                .addParam("TYPE", type)
                .setFooterViewHolderCls(BlankVHolder.class)
                .setItemViewHolderCls(ConsultantSalaryVHolder.class)

                .setEmptyView(R.id.layerEmpty_vContents)
                .setBottomLoading(getActivity().getLayoutInflater().inflate(R.layout.layer_footer_loading, null), new FooterLoadingListener() {
                    @Override
                    public void onFooterLoading() {
                        MoneyHistoryModel moneyHistory = adapter.getLastItem();
                        int rownum = moneyHistory.rownum;
                        getListMore(rownum);
                    }
                })

        );

        adapter.addFooterItem("A");
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadListFirst();
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

    //get first
    private void loadListFirst(){

        vLoading.setVisibility(View.VISIBLE);

        OkhomeRestApi.getConsultantSalaryClient().getTrainingList(ConsultantLoggedIn.id(), loadType, 0).enqueue(new RetrofitCallback<List<MoneyHistoryModel>>() {
            @Override
            public void onSuccess(List<MoneyHistoryModel> result) {
                adapter.setListItems(result);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                vLoading.setVisibility(View.GONE);
            }
        });
    }

    // get more
    private void getListMore(int lastRownum){

        OkhomeRestApi.getConsultantSalaryClient().getTrainingList(ConsultantLoggedIn.id(), loadType, lastRownum).enqueue(new RetrofitCallback<List<MoneyHistoryModel>>() {
            @Override
            public void onSuccess(List<MoneyHistoryModel> result) {
                if(result.size() > 0){
                    adapter.addListItems(result);
                    adapter.notifyDataSetChanged();
                    adapter.notifyFooterLoadingComplete();
                }else{
                    adapter.setFooterNoMoreResult(true);
                }
            }
        });
    }



}
