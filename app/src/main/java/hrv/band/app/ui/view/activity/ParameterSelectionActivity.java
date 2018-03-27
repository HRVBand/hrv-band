package hrv.band.app.ui.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import hrv.band.app.R;
import hrv.band.app.ui.view.adapter.ParameterValueAdapter;

import static hrv.band.app.ui.view.activity.HistoryActivity.DATE_VALUE;
import static hrv.band.app.ui.view.activity.HistoryActivity.PARAMETER_VALUE;
import static hrv.band.app.ui.view.activity.HistoryActivity.SELECTION_VALUE;

public class ParameterSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter_selection);
        setupToolbar();

        final long longDate = getIntent().getLongExtra(DATE_VALUE, 0);
        ListView listView = findViewById(R.id.parameter_values);
        listView.setAdapter(new ParameterValueAdapter(this));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setHRVParameterValue(view, i);
                setActivityResult(i);
                finish();
            }

            private void setActivityResult(int i) {
                Intent intent = new Intent();
                intent.putExtra(DATE_VALUE, longDate);
                intent.putExtra(SELECTION_VALUE, i);
                setResult(RESULT_OK, intent);
            }

            private void setHRVParameterValue(View view, int i) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                prefsEditor.putInt(PARAMETER_VALUE, i);
                prefsEditor.apply();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hrv_parameters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }
}
