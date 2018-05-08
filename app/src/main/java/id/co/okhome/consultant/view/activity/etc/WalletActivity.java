package id.co.okhome.consultant.view.activity.etc;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.config.OkhomeConstant;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;

public class WalletActivity extends OkHomeParentActivity {

    @BindView(R.id.actCleaningInfo_webview)     WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        OkhomeUtil.setWhiteSystembar(this);
        ButterKnife.bind(this);
        initWebView();
    }

    private void initWebView() {
        webView.loadUrl(OkhomeConstant.DOKU_WEB_VIEW);
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                OkhomeUtil.showToast(getBaseContext(), description);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack() && !webView.getUrl().contains("signin#/home") && !webView.getUrl().contains("signin#/login") && !webView.getUrl().equals("https://staging.doku.com/webapps/signin#/")) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.actWallet_vbtnX)
    public void onClickCloseActivity() {
        finish();
    }
}