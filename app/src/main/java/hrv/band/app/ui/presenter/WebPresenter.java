package hrv.band.app.ui.presenter;

import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import hrv.band.app.R;
import hrv.band.app.ui.view.activity.IWebView;

import static hrv.band.app.ui.view.activity.web.WebsiteUrls.GOOGLE_FORMS_HOST_URL;
import static hrv.band.app.ui.view.activity.web.WebsiteUrls.HRVBAND_HOST_URL;
import static hrv.band.app.ui.view.activity.web.WebsiteUrls.SURVEY_URL;
import static hrv.band.app.ui.view.activity.web.WebsiteUrls.WEBSITE_ABOUT_URL;
import static hrv.band.app.ui.view.activity.web.WebsiteUrls.WEBSITE_FAQ_URL;
import static hrv.band.app.ui.view.activity.web.WebsiteUrls.WEBSITE_PARAMETER_URL;
import static hrv.band.app.ui.view.activity.web.WebsiteUrls.WEBSITE_PRIVACY_URL;
import static hrv.band.app.ui.view.activity.web.WebsiteUrls.WEBSITE_URL;

/**
 * Copyright (c) 2017
 * Created by Thomas on 14.04.2017.
 */

public class WebPresenter implements IWebPresenter {

    private IWebView webView;
    private WebViewClient webViewClient;

    public WebPresenter(IWebView webView) {
        this.webView = webView;
        this.webViewClient = new AppWebViewClient();
    }

    @Override
    public void loadUrl(int urlId) {
        switch (urlId) {
           
            case R.id.web_nav_home:
                webView.loadUrl(WEBSITE_URL);
                break;
            case R.id.web_nav_background:
                webView.loadUrl(WEBSITE_PARAMETER_URL);
                break;
            case R.id.web_nav_faq:
                webView.loadUrl(WEBSITE_FAQ_URL);
                break;
            case R.id.web_nav_privacy:
                webView.loadUrl(WEBSITE_PRIVACY_URL);
                break;
            case R.id.web_nav_about:
                webView.loadUrl(WEBSITE_ABOUT_URL);
                break;
            case R.id.web_nav_survey:
                webView.loadUrl(SURVEY_URL);
                break;
            default: webView.loadUrl(WEBSITE_URL);
        }
    }

    @Override
    public WebViewClient getWebViewClient() {
        return webViewClient;
    }

    /**
     * Checks if the url belongs to the app website. If not it asks the user to open his browser
     * app.
     */
    private class AppWebViewClient extends WebViewClient {
        @Override @SuppressWarnings("deprecation")
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(Uri.parse(url).getHost().startsWith(HRVBAND_HOST_URL)) {
                return false;
            }
            if (Uri.parse(url).getHost().startsWith(GOOGLE_FORMS_HOST_URL)) {
                return false;
            }
            webView.openBrowserIntent(url);
            return true;
        }
    }
}
