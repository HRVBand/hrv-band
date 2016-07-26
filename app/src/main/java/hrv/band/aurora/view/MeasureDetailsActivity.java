package hrv.band.aurora.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import hrv.band.aurora.Control.HRVParameters;
import hrv.band.aurora.R;
import hrv.band.aurora.storage.IStorage;
import hrv.band.aurora.storage.SQLite.SQLController;
import hrv.band.aurora.view.adapter.AbstractValueAdapter;
import hrv.band.aurora.view.adapter.MeasureCategoryAdapter;
import hrv.band.aurora.view.adapter.ValueAdapter;

public class MeasureDetailsActivity extends AppCompatActivity {


    private HRVParameters parameter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_values);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        parameter = getIntent().getParcelableExtra(MainActivity.HRV_PARAMETER_ID);

        GridView gridview = (GridView) findViewById(R.id.measure_value_list);

        AbstractValueAdapter adapter = new ValueAdapter(this,
                R.layout.measure_list_item, parameter);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ValueDescriptionActivity.class);
                intent.putExtra(MainActivity.HRV_VALUE_ID, position);
                startActivity(intent);
            }

        });

        setSpinnerValues();

       /*final RatingBar ratingbar = (RatingBar) findViewById(R.id.ratingBar);
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                Toast.makeText(getApplicationContext(), String.valueOf(rating), Toast.LENGTH_LONG);
                //ratingBar.setRating(rating);

            }

        });*/
    }

    public void saveMeasurement(View view) {
        //IStorage storage = new SharedPreferencesController();
        IStorage storage = new SQLController();
        setMeasurementDetails();
        storage.saveData(getApplicationContext(), parameter);
        Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setMeasurementDetails() {
        parameter.setRating(((RatingBar) findViewById(R.id.measure_rating)).getRating());
        MeasureCategoryAdapter.MeasureCategory category = (MeasureCategoryAdapter.MeasureCategory)
                ((Spinner) findViewById(R.id.measure_categories)).getSelectedItem();
        parameter.setCategory(category);
        parameter.setNote(((TextView) findViewById(R.id.measure_note)).getText().toString());
    }

    private void setSpinnerValues() {
        Spinner spinner = (Spinner) findViewById(R.id.measure_categories);
        MeasureCategoryAdapter spinnerArrayAdapter = new MeasureCategoryAdapter(getApplicationContext());

        spinner.setAdapter(spinnerArrayAdapter);
    }
}
