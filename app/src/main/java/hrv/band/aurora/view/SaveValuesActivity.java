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
import hrv.band.aurora.view.adapter.ValueAdapter;

public class SaveValuesActivity extends AppCompatActivity {

    public static final String HRV_VALUE_ID = "HRV_VALUE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_values);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        HRVParameters parameter = getIntent().getParcelableExtra(MeasureActivity.HRV_PARAMETER_ID);

        final GridView gridview = (GridView) findViewById(R.id.measure_value_list);

        //HashMap<String, String> meMap=new HashMap<>();
        /*List<String> labels = new ArrayList<>();
        List<String> values = new ArrayList<>();
        labels.add("SD1");
        values.add();
        labels.add("SD2");
        values.add(String.valueOf(parameter.getSd2()));
        labels.add("LFHF");
        values.add(String.valueOf(parameter.getLfhfRatio()));
        labels.add("SDNN");
        values.add(String.valueOf(parameter.getSdnn()));
        labels.add("RMSSD");
        values.add(String.valueOf(parameter.getRmssd()));
        labels.add("Baevsky");
        values.add(String.valueOf(parameter.getBaevsky()));*/

        final ValueAdapter adapter = new ValueAdapter(this,
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

}
