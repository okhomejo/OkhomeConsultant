package id.co.okhome.consultant.view.main.trainee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.adapter.FaqListAdapter;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.FaqModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;

/**
 * Created by frizurd on 22/03/2018.
 */

public class TraineeFaqSearchResultActivity extends OkHomeParentActivity {

    @BindView(R.id.actTraineeFAQ_vgNoResult)    LinearLayout noResultView;
    @BindView(R.id.actTraineeFAQ_vbtnSearch)    LinearLayout searchLayout;
    @BindView(R.id.actTraineeFAQ_vProgress)     ProgressBar progressBar;
    @BindView(R.id.actTraineeFAQ_tvTitle)       TextView tvTitle;
    @BindView(R.id.actTraineeFAQ_list)          ListView listView;

    private FaqListAdapter faqAdapter;

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
            noResultView.setVisibility(View.VISIBLE);
        }
    }

    private void getFaqById(final String faqId) {
        progressBar.setVisibility(View.VISIBLE);
        OkhomeRestApi.getCommonClient().getFaqById(faqId).enqueue(new RetrofitCallback<List<FaqModel>>() {

            @Override
            public void onSuccess(final List<FaqModel> faqs) {
                faqAdapter = new FaqListAdapter(getBaseContext(), faqs);
                listView.setAdapter(faqAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                        if (faqs.get(pos).childCount == 0) {
                            Intent intent = new Intent(getBaseContext(), TraineeFaqSingleActivity.class);
                            intent.putExtra("FAQ_ID", faqs.get(pos).id);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getBaseContext(), TraineeFaqActivity.class);
                            intent.putExtra("FAQ_ID", faqs.get(pos).id);
                            intent.putExtra("FAQ_TITLE", faqs.get(pos).subject);
                            startActivity(intent);
                        }
                    }
                });
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
