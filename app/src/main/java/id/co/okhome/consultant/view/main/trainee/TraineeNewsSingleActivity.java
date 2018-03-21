package id.co.okhome.consultant.view.main.trainee;

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

/**
 * Created by frizurd on 16/03/2018.
 */

public class TraineeNewsSingleActivity extends OkHomeParentActivity {

    @BindView(R.id.actTraineeNewsSingle_tvTitle)     TextView tvTitle;
    @BindView(R.id.actTraineeNewsSingle_webView)     WebView webView;
    @BindView(R.id.actTraineeNewsSingle_vProgress)   View vLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_news_single);
        OkhomeUtil.setWhiteSystembar(this);

        ButterKnife.bind(this);
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

        String newsContents = getIntent().getStringExtra("NEWS_CONTENTS");
        if (newsContents != null) {
            webView.loadData(newsContents, "text/html", "UTF-8");
        } else {
            webView.loadData("No information available.", "text/html", "UTF-8");
        }
    }

    @OnClick(R.id.actTraineeNewsSingle_vbtnX)
    public void closeActivity() {
        finish();
    }
}
