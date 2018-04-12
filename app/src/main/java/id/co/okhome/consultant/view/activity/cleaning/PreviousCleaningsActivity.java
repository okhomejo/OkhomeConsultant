package id.co.okhome.consultant.view.activity.cleaning;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;

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
        getPreviousCleaningTasks();
        swipeLayout.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        adapter = new JoRecyclerAdapter(new JoRecyclerAdapter.Params()
                .setRecyclerView(rcv)
                .setItemViewHolderCls(ConsultantCleaningTaskVHolder.class)
                .setFooterViewHolderCls(BlankVHolder.class)
        );
        adapter.addFooterItem("");
    }

    private void getPreviousCleaningTasks() {
        swipeLayout.setRefreshing(true);
        OkhomeRestApi.getCleaningTaskClient().getPrevCleaningTasks(ConsultantLoggedIn.id()).enqueue(new RetrofitCallback<List<CleaningInfoModel>>() {
            @Override
            public void onSuccess(List<CleaningInfoModel> cleaningList) {
                adapter.setListItems(cleaningList);
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

    @Override
    public void onRefresh() {
        getPreviousCleaningTasks();
    }

    @OnClick(R.id.actPrevCleanings_vbtnX)
    public void onBackButtonClicked() {
        finish();
    }
}
