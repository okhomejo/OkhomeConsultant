package id.co.okhome.consultant.view.main.trainee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.FaqModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.viewholder.BlankHolder;
import id.co.okhome.consultant.view.viewholder.FaqVHolder;

/**
 * Created by frizurd on 22/03/2018.
 */

public class TraineeFaqSearchResultActivity extends OkHomeParentActivity {

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

    private void init() {
        initFaqParams();
        initRecyclerView();
    }

    private void initFaqParams(){
        searchLayout.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tvTitle.setText(String.format("FAQ : %s", extras.getString("FAQ_SEARCH_TITLE")));
            getFaqById(extras.getString("FAQ_SEARCH_IDS"));

        } else {
            tvNoResults.setVisibility(View.VISIBLE);
        }
    }

    //init recycler view and adapter
    private void initRecyclerView(){
        adapter = new JoRecyclerAdapter(new JoRecyclerAdapter.Params()
                .setRecyclerView(rcv)
                .setItemViewHolderCls(FaqVHolder.class)
                .setFooterViewHolderCls(BlankHolder.class)
        );
        adapter.addFooterItem("");
    }

    private void getFaqById(final String faqId) {
        progressBar.setVisibility(View.VISIBLE);
        OkhomeRestApi.getCommonClient().getFaqById(faqId).enqueue(new RetrofitCallback<List<FaqModel>>() {

            @Override
            public void onSuccess(final List<FaqModel> faqs) {
                adapter.setListItems(faqs);
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
