package hrv.band.aurora.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hrv.band.aurora.Control.HRVParameters;
import hrv.band.aurora.R;
import hrv.band.aurora.storage.IStorage;
import hrv.band.aurora.storage.SharedPreferencesController;
import hrv.band.aurora.view.adapter.AbstractValueAdapter;
import hrv.band.aurora.view.adapter.ValueAdapter;

public class SaveValuesActivity extends AppCompatActivity {

    public static final String HRV_VALUE_ID = "HRV_VALUE";
    private HRVParameters parameter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_values);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        parameter = getIntent().getParcelableExtra(MeasureActivity.HRV_PARAMETER_ID);

        GridView gridview = (GridView) findViewById(R.id.measure_value_list);

        AbstractValueAdapter adapter = new ValueAdapter(this,
                R.layout.measure_list_item, parameter);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ValueDescriptionActivity.class);
                intent.putExtra(HRV_VALUE_ID, position);
                startActivity(intent);
            }

        });

       /* final RatingBar ratingbar = (RatingBar) findViewById(R.id.ratingBar);
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                ratingBar.setRating(rating);

            }

        });*/
    }

    public void saveMeasurement(View view) {
        IStorage storage = new SharedPreferencesController();
        storage.saveData(this, parameter);
        Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
        finish();
    }

}
