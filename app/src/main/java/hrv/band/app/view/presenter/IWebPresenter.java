package hrv.band.app.view.presenter;

import android.webkit.WebViewClient;

/**
 * Copyright (c) 2017
 * Created by Thomas on 14.04.2017.
 */

public interface IWebPresenter {
    void loadUrl(int urlId);
    WebViewClient getWebViewClient();
}
