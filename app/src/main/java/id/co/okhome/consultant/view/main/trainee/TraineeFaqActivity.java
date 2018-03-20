package id.co.okhome.consultant.view.main.trainee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
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
 * Created by frizurd on 16/03/2018.
 */

public class TraineeFaqActivity extends OkHomeParentActivity {

    @BindView(R.id.actTraineeFAQ_tvTitle)      TextView tvTitle;
    @BindView(R.id.actTraineeFAQ_list)         ListView listView;
    @BindView(R.id.actTraineeFAQ_vProgress)    ProgressBar progressBar;

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
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            getAllFaq(extras.getInt("FAQ_ID"));
            tvTitle.setText(extras.getString("FAQ_TITLE"));
        } else {
            getAllFaq(0);
        }
    }

    private void getAllFaq(final int faqId) {
        progressBar.setVisibility(View.VISIBLE);
        OkhomeRestApi.getCommonClient().getAllFaqs(faqId).enqueue(new RetrofitCallback<List<FaqModel>>() {

            @Override
            public void onSuccess(final List<FaqModel> faqs) {
                faqAdapter = new FaqListAdapter(TraineeFaqActivity.this, faqs);
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