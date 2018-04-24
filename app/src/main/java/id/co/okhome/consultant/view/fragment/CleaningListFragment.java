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
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.cleaning.CleaningInfoModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.viewholder.BlankVHolder;
import id.co.okhome.consultant.view.viewholder.cleaning.ConsultantCleaningNextTaskVHolder;
import id.co.okhome.consultant.view.viewholder.cleaning.ConsultantCleaningPrevTaskVHolder;

/**
 * Created by jo on 2018-04-07.
 */

public class CleaningListFragment extends Fragment{

    @BindView(R.id.fragCleaningList_rcv)
    RecyclerView rcv;

    @BindView(R.id.fragCleaningList_vLoading)
    View vLoading;

    JoRecyclerAdapter<CleaningInfoModel> adapter;

    boolean loadNext = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cleaninglist, null);
        ButterKnife.bind(this, v);

        String type = getArguments().getString("TYPE");

        JoRecyclerAdapter.Params joParams = new JoRecyclerAdapter.Params()
                .setRecyclerView(rcv)
                .setFooterViewHolderCls(BlankVHolder.class)
                .setEmptyView(R.id.layerEmpty_vContents)
                .setBottomLoading(getLayoutInflater().inflate(R.layout.layer_footer_loading, null), new FooterLoadingListener() {
                    @Override
                    public void onFooterLoading() {
                        CleaningInfoModel cleaningInfoModel = adapter.getLastItem();
                        int rowNum = cleaningInfoModel.rownum;
                        getListMore(rowNum);
                    }
        });

        if(type.equals("NEXT")){
            joParams.setItemViewHolderCls(ConsultantCleaningNextTaskVHolder.class);
            loadNext = true;
        }else{
            joParams.setItemViewHolderCls(ConsultantCleaningPrevTaskVHolder.class);
            loadNext = false;
        }

        adapter = new JoRecyclerAdapter(joParams);
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


    //get first
    private void loadListFirst(){

        vLoading.setVisibility(View.VISIBLE);

        RetrofitCallback callback = new RetrofitCallback<List<CleaningInfoModel>>() {
            @Override
            public void onSuccess(List<CleaningInfoModel> result) {
                adapter.setListItems(result);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                vLoading.setVisibility(View.GONE);
            }
        };

        if(loadNext){
            OkhomeRestApi.getCleaningTaskClient().getNextCleaningTasks(ConsultantLoggedIn.id(), 0).enqueue(callback);
        }else{
            OkhomeRestApi.getCleaningTaskClient().getPrevCleaningTasks(ConsultantLoggedIn.id(), 0).enqueue(callback);
        }
    }

    // get more
    private void getListMore(int lastRownum){

        final RetrofitCallback callback = new RetrofitCallback<List<CleaningInfoModel>>() {
            @Override
            public void onSuccess(List<CleaningInfoModel> result) {
                if(result.size() > 0){
                    adapter.addListItems(result);
                    adapter.notifyDataSetChanged();
                    adapter.notifyFooterLoadingComplete();
                }else{
                    adapter.setFooterNoMoreResult(true);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                vLoading.setVisibility(View.GONE);
            }
        };

        if(loadNext){
            OkhomeRestApi.getCleaningTaskClient().getNextCleaningTasks(ConsultantLoggedIn.id(), lastRownum).enqueue(callback);
        }else{
            OkhomeRestApi.getCleaningTaskClient().getPrevCleaningTasks(ConsultantLoggedIn.id(), lastRownum).enqueue(callback);
        }

    }



}
