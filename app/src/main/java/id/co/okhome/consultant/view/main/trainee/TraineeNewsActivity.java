package id.co.okhome.consultant.view.main.trainee;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

    @BindView(R.id.actTraineeNews_list)     ListView listView;

    private NewsListAdapter newsAdapter;
    private List<NewsModel> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_news);

        ButterKnife.bind(this);
        getAllNews();
    }

    private void getAllNews() {
        final ProgressDialog p = ProgressDialog.show(this, null, "Loading news...");
        OkhomeRestApi.getCommonClient().getAllNews().enqueue(new RetrofitCallback<List<NewsModel>>() {

            @Override
            public void onSuccess(List<NewsModel> news) {
                newsList = news;
                newsAdapter = new NewsListAdapter(TraineeNewsActivity.this, news);
                listView.setAdapter(newsAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                        OkhomeUtil.showToast(TraineeNewsActivity.this, "boop");
                    }
                });
            }

            @Override
            public void onFinish() {
                super.onFinish();
                p.dismiss();
            }
        });
    }

    @OnClick(R.id.actTraineeNews_vbtnX)
    public void onClickClose() {
        finish();
    }
}