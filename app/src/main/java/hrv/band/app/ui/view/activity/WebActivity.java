package hrv.band.app.ui.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import hrv.band.app.R;
import hrv.band.app.ui.presenter.IWebPresenter;
import hrv.band.app.ui.presenter.WebPresenter;

import static hrv.band.app.ui.view.activity.web.WebsiteUrls.GOOGLE_FORMS_HOST_URL;
import static hrv.band.app.ui.view.activity.web.WebsiteUrls.HRVBAND_HOST_URL;
import static hrv.band.app.ui.view.activity.web.WebsiteUrls.WEBSITE_URL;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 04.02.2017
 * <p>
 * This Activity contains a web view and shows the app website.
 */
public class WebActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IWebView {

    public static final String WEBSITE_URL_ID = "website_url_id";

    private WebView webView;
    private IWebPresenter webPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        settingUpNavigationBar(toolbar);

        webPresenter = new WebPresenter(this);

        settingUpWebView();
    }

    private void settingUpWebView() {
        webView = (WebView) findViewById(R.id.webView);

        String url = getIntent().getStringExtra(WEBSITE_URL_ID);
        if (!url.contains(HRVBAND_HOST_URL) && !url.contains(GOOGLE_FORMS_HOST_URL) ) {
            url = WEBSITE_URL;
        }

        loadUrl(url);
        webView.setWebViewClient(webPresenter.getWebViewClient());
    }

    private void settingUpNavigationBar(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            navigateBack();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.web_nav_back) {
            finish();
        } else {
            webPresenter.loadUrl(id);
        }
        closeDrawer();
        return true;
    }

    private void closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.web_home:
                loadUrl(WEBSITE_URL);
                return true;

            case R.id.web_open_in_browser:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(webView.getUrl())));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Navigate back through website and return to called activity if website can't go back.
     */
    private void navigateBack() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public void loadUrl(String url) {
        webView.loadUrl(url);
    }

    @Override
    public void openBrowserIntent(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        webView.getContext().startActivity(intent);
    }
}
