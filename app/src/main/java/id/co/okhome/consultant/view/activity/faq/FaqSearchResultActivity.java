package id.co.okhome.consultant.view.activity.faq;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.RecyclerViewPositionManager;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.FaqModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.viewholder.BlankVHolder;
import id.co.okhome.consultant.view.viewholder.faq.FaqVHolder;

/**
 * Created by frizurd on 22/03/2018.
 */

public class FaqSearchResultActivity extends OkHomeParentActivity {

    @BindView(R.id.actTraineeFAQ_tvTitle)       TextView tvTitle;
    @BindView(R.id.actTraineeFAQ_vgNoResult)    FrameLayout tvNoResults;
    @BindView(R.id.actTraineeFAQ_vbtnSearch)    LinearLayout searchLayout;
    @BindView(R.id.actTraineeFAQ_vProgress)     ProgressBar progressBar;
    @BindView(R.id.actTraineeFAQ_rcv)           RecyclerView rcv;

    private JoRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_faq);
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
        initFaqParams();
        initRecyclerView();
    }

    private void initFaqParams(){
        searchLayout.setVisibility(View.GONE);

        if (getIntent().getExtras() != null) {
            getFaqById(getIntent().getStringExtra("FAQ_SEARCH_IDS"));
            if (getIntent().getStringExtra("FAQ_SEARCH_TITLE") == null) {
                tvTitle.setText("FAQ");
            } else {
                tvTitle.setText(String.format("FAQ : %s", getIntent().getStringExtra("FAQ_SEARCH_TITLE")));
            }
        } else {
            tvNoResults.setVisibility(View.VISIBLE);
        }
    }

    //init recycler view and adapter
    private void initRecyclerView(){
        adapter = new JoRecyclerAdapter(new JoRecyclerAdapter.Params()
                .setRecyclerView(rcv)
                .setItemViewHolderCls(FaqVHolder.class)
                .setFooterViewHolderCls(BlankVHolder.class)
        );
        adapter.addFooterItem("");
    }

    private void getFaqById(final String faqId) {
        progressBar.setVisibility(View.VISIBLE);
        OkhomeRestApi.getCommonClient().getFaqById(faqId).enqueue(new RetrofitCallback<List<FaqModel>>() {

            @Override
            public void onSuccess(final List<FaqModel> faqs) {
                adapter.setListItems(faqs);
                RecyclerViewPositionManager.restore(rcv);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R.id.actTraineeFAQ_vbtnX)
    public void onClickCloseActivity() {
        finish();
    }
}
