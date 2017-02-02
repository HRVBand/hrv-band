package hrv.band.app.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import hrv.band.app.R;

import static hrv.band.app.view.MainActivity.WEBSITE_URL;
/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * This Activity shows a description of the HRV values.
 */
public class ValueDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_more);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(WEBSITE_URL));
                startActivity(browserIntent);
            }
        });
    }
}
