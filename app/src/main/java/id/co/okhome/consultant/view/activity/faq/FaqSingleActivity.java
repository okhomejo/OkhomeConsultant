package id.co.okhome.consultant.view.activity.faq;

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
import id.co.okhome.consultant.model.FaqModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;

/**
 * Created by frizurd on 16/03/2018.
 */

public class FaqSingleActivity extends OkHomeParentActivity {

    @BindView(R.id.actTraineeFaqSingle_tvTitle)     TextView tvTitle;
    @BindView(R.id.actTraineeFaqSingle_webView)     WebView webView;
    @BindView(R.id.actTraineeFaqSingle_vProgress)   View vLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_faq_single);
        OkhomeUtil.setWhiteSystembar(this);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getFaq(getIntent().getExtras().getInt("FAQ_ID"));
    }

    private void getFaq(int faqId) {
        vLoading.setVisibility(View.VISIBLE);
        OkhomeRestApi.getCommonClient().getFaq(faqId).enqueue(new RetrofitCallback<FaqModel>() {

            @Override
            public void onSuccess(final FaqModel faq) {
                tvTitle.setText(faq.subject);
                loadWebView(faq.contents);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                vLoading.setVisibility(View.GONE);
            }
        });
    }

    private void loadWebView(String contents) {
        WebSettings webSettings =  webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);

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

        if (contents != null) {
            webView.loadData(contents, "text/html", "UTF-8");
        } else {
            webView.loadData("No information available.", "text/html", "UTF-8");
        }
    }

    @OnClick(R.id.actTraineeFaqSingle_vbtnX)
    public void closeActivity() {
        finish();
    }
}
