package hrv.band.app.view.activity;

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
import android.webkit.WebViewClient;

import hrv.band.app.R;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 04.02.2017
 *
 * This Activity contains a web view and shows the app website.
 */
public class WebActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /** The id of the website extra. **/
    public static final String WEBSITE_URL_ID = "website_url_id";
    /** The home address of the website. **/
    public static final String WEBSITE_URL = "http://hrvband.de";
    /** The privacy address of the website. **/
    public static final String WEBSITE_PRIVACY_URL = WEBSITE_URL + "/privacy";
    /** The address of the website explaining the parameters. **/
    public static final String WEBSITE_PARAMETER_URL = WEBSITE_URL + "/parameters";
    /** The address of the websites faq. **/
    public static final String WEBSITE_FAQ_URL = WEBSITE_URL + "/faq";
    /** The address of the website shows imprint. **/
    public static final String WEBSITE_ABOUT_URL = WEBSITE_URL + "/about";
    /** The host url of the website to identify if an url belongs to the app website. **/
    private static final String HOST_URL = "hrvband.de";

    /** The web view to show the website in. **/
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Navbar
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Setting up web view
        webView = (WebView) findViewById(R.id.webView);

        String url = getIntent().getStringExtra(WEBSITE_URL_ID);
        if (!url.contains(HOST_URL)) {
            url = WEBSITE_URL;
        }

        webView.loadUrl(url);
        webView.setWebViewClient(new WebActivity.AppWebViewClient());
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
        } else if (id == R.id.web_nav_home) {
            webView.loadUrl(WEBSITE_URL);
        } else if (id == R.id.web_nav_background) {
            webView.loadUrl(WEBSITE_PARAMETER_URL);
        } else if (id == R.id.web_nav_faq) {
            webView.loadUrl(WEBSITE_FAQ_URL);
        } else if (id == R.id.web_nav_privacy) {
            webView.loadUrl(WEBSITE_PRIVACY_URL);
        } else if (id == R.id.web_nav_about) {
            webView.loadUrl(WEBSITE_ABOUT_URL);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
                webView.loadUrl(WEBSITE_URL);
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
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }


    /**
     * Checks if the url belongs to the app website. If not it asks the user to open his browser
     * app.
     */
    private class AppWebViewClient extends WebViewClient {
        @Override @SuppressWarnings("deprecation")
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(Uri.parse(url).getHost().startsWith(HOST_URL)) {
                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;
        }
    }
}
