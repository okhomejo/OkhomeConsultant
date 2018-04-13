package id.co.okhome.consultant.view.activity.cleaning;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;
import com.mrjodev.jorecyclermanager.footerloading.FooterLoadingListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.RecyclerViewPositionManager;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.MoneyHistoryModel;
import id.co.okhome.consultant.model.cleaning.CleaningInfoModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.viewholder.BlankVHolder;
import id.co.okhome.consultant.view.viewholder.ConsultantCleaningTaskVHolder;

public class PreviousCleaningsActivity extends OkHomeParentActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.actPrevCleanings_tvTitle)            TextView tvTitle;
    @BindView(R.id.actPrevCleanings_rcv)                RecyclerView rcv;
    @BindView(R.id.actPrevCleanings_swipe)              SwipeRefreshLayout swipeLayout;
    @BindView(R.id.actPrevCleanings_vProgress)          ProgressBar progressBar;

    private JoRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_cleanings);
        OkhomeUtil.setWhiteSystembar(this);

        ButterKnife.bind(this);
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RecyclerViewPositionManager.clear(rcv);
    }

    @Override
    public void onPause() {
        super.onPause();
        RecyclerViewPositionManager.save(rcv);
    }

    private void init() {
        initRecyclerView();
        loadFirstList();
        swipeLayout.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        adapter = new JoRecyclerAdapter(new JoRecyclerAdapter.Params()
                .setRecyclerView(rcv)
                .setItemViewHolderCls(ConsultantCleaningTaskVHolder.class)
                .setFooterViewHolderCls(BlankVHolder.class)

                .setEmptyView(R.id.layerEmpty_vContents)
                .setBottomLoading(getLayoutInflater().inflate(R.layout.layer_footer_loading, null), new FooterLoadingListener() {
                    @Override
                    public void onFooterLoading() {
                        CleaningInfoModel cleaningInfoModel = (CleaningInfoModel) adapter.getLastItem();
                        int rowNum = cleaningInfoModel.rownum;
                        getListMore(rowNum);
                    }
                })
        );
        adapter.addFooterItem("A");
    }

    private void loadFirstList() {
        swipeLayout.setRefreshing(true);
        OkhomeRestApi.getCleaningTaskClient().getPrevCleaningTasks(ConsultantLoggedIn.id(), 0).enqueue(new RetrofitCallback<List<CleaningInfoModel>>() {
            @Override
            public void onSuccess(List<CleaningInfoModel> result) {
                adapter.setListItems(result);
                RecyclerViewPositionManager.restore(rcv);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                adapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
            }
        });
    }

    private void getListMore(int lastRowNum){
        OkhomeRestApi.getCleaningTaskClient().getPrevCleaningTasks(ConsultantLoggedIn.id(), lastRowNum).enqueue(new RetrofitCallback<List<CleaningInfoModel>>() {
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
        });
    }

    @Override
    public void onRefresh() {
        loadFirstList();
    }

    @OnClick(R.id.actPrevCleanings_vbtnX)
    public void onBackButtonClicked() {
        finish();
    }
}