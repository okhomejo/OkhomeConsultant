package id.co.okhome.consultant.view.news;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.NewsModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.viewholder.BlankVHolder;
import id.co.okhome.consultant.view.viewholder.NewsVHolder;

/**
 * Created by frizurd on 15/03/2018.
 */

public class NewsActivity extends OkHomeParentActivity {

    @BindView(R.id.actTraineeNews_rcv)          RecyclerView rcv;
    @BindView(R.id.actTraineeNews_vProgress)    ProgressBar progressBar;

    private JoRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_news);
        OkhomeUtil.setWhiteSystembar(this);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        adapter = new JoRecyclerAdapter(new JoRecyclerAdapter.Params()
                .setRecyclerView(rcv)
                .setItemViewHolderCls(NewsVHolder.class)
                .setFooterViewHolderCls(BlankVHolder.class)
        );
        adapter.addFooterItem("");
        getAllNews();
    }

    private void getAllNews() {
        progressBar.setVisibility(View.VISIBLE);
        OkhomeRestApi.getCommonClient().getAllNews().enqueue(new RetrofitCallback<List<NewsModel>>() {

            @Override
            public void onSuccess(final List<NewsModel> news) {
                adapter.setListItems(news);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R.id.actTraineeNews_vbtnX)
    public void onClickClose() {
        finish();
    }
}