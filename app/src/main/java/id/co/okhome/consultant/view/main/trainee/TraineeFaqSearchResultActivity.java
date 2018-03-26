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

    private List<String> faqItems;
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
        searchLayout.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tvTitle.setText(String.format("FAQ : %s", extras.getString("FAQ_SEARCH_TITLE")));
            getFaqById(extras.getString("FAQ_SEARCH_IDS"));
        } else {
            tvNoResults.setVisibility(View.VISIBLE);
        }
    }

    private void getFaqById(final String faqId) {
        progressBar.setVisibility(View.VISIBLE);
        OkhomeRestApi.getCommonClient().getFaqById(faqId).enqueue(new RetrofitCallback<List<FaqModel>>() {

            @Override
            public void onSuccess(final List<FaqModel> faqs) {
                FaqVHolder.ItemClickListener faqClickListener
                        = new FaqVHolder.ItemClickListener() {
                    @Override
                    public void onItemClick(int pos, String value, String tag) {
                        Intent intent = new Intent(getBaseContext(), TraineeFaqSingleActivity.class);
                        intent.putExtra("FAQ_ID", faqs.get(pos).id);
                        startActivity(intent);
                    }
                };

                faqItems = new ArrayList<>();
                for (FaqModel faq : faqs) {
                    faqItems.add(faq.subject);
                }
                JoRecyclerAdapter.Params params = new JoRecyclerAdapter.Params();
                params.setRecyclerView(rcv)
                        .addParam(FaqVHolder.TAG_ITEM_ACT, getBaseContext())
                        .addParam(FaqVHolder.TAG_ITEM_CLICK, faqClickListener)
                        .setItemViewHolderCls(FaqVHolder.class);
                adapter = new JoRecyclerAdapter(params);
                adapter.setListItems(faqItems);
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
