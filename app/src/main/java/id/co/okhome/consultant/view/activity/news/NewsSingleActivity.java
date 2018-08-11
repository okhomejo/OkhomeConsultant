package id.co.okhome.consultant.view.activity.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.NewsModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;

/**
 * Created by frizurd on 16/03/2018.
 */

public class NewsSingleActivity extends OkHomeParentActivity {

    @BindView(R.id.actTraineeNewsSingle_tvTitle)     TextView tvTitle;
    @BindView(R.id.actTraineeNewsSingle_webView)     WebView webView;
    @BindView(R.id.actTraineeNewsSingle_vProgress)   View vLoading;

    int newsId;

    public static void start(Context context, int newsId){
        context.startActivity(new Intent(context, NewsSingleActivity.class).putExtra("newsId", newsId));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_news_single);
        OkhomeUtil.setWhiteSystembar(this);

        ButterKnife.bind(this);

        newsId = getIntent().getIntExtra("newsId", -1);

        init();
    }

    private void init() {
        String newsTitle = getIntent().getStringExtra("NEWS_TITLE");
        tvTitle.setText(newsTitle);
        loadWebView();
    }

    private void loadWebView() {
        WebSettings webSettings =  webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                OkhomeUtil.showToast(getBaseContext(), description);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress == 100){
                    vLoading.setVisibility(View.GONE);
                }else{
                    vLoading.setVisibility(View.VISIBLE);
                }
            }
        });

        vLoading.setVisibility(View.VISIBLE);
        OkhomeRestApi.getCommonClient().getNewsDetail(newsId).enqueue(new RetrofitCallback<NewsModel>() {
            @Override
            public void onSuccess(NewsModel news) {
                webView.loadData(news.contents, "text/html", "UTF-8");
            }
        });

    }

    @OnClick(R.id.actTraineeNewsSingle_vbtnX)
    public void closeActivity() {
        finish();
    }
}
