package id.co.okhome.consultant.view.main.trainee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.adapter.NewsListAdapter;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.NewsModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;

/**
 * Created by frizurd on 15/03/2018.
 */

public class TraineeNewsActivity extends OkHomeParentActivity {

    @BindView(R.id.actTraineeNews_list)         ListView listView;
    @BindView(R.id.actTraineeNews_vProgress)    ProgressBar progressBar;

    private NewsListAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_news);
        OkhomeUtil.setWhiteSystembar(this);
        ButterKnife.bind(this);
        getAllNews();
    }

    private void getAllNews() {
        progressBar.setVisibility(View.VISIBLE);
        OkhomeRestApi.getCommonClient().getAllNews().enqueue(new RetrofitCallback<List<NewsModel>>() {

            @Override
            public void onSuccess(final List<NewsModel> news) {
                newsAdapter = new NewsListAdapter(TraineeNewsActivity.this, news);
                listView.setAdapter(newsAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                        Intent intent = new Intent(getBaseContext(), TraineeNewsSingleActivity.class);
                        intent.putExtra("NEWS_TITLE", news.get(pos).subject);
                        intent.putExtra("NEWS_CONTENTS", news.get(pos).contents);
                        startActivity(intent);
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

    @OnClick(R.id.actTraineeNews_vbtnX)
    public void onClickClose() {
        finish();
    }
}