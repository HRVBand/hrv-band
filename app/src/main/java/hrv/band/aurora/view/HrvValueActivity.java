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
import android.widget.TextView;

import hrv.band.aurora.Control.HRVParameters;
import hrv.band.aurora.R;
import hrv.band.aurora.view.adapter.AbstractValueAdapter;
import hrv.band.aurora.view.adapter.ValueAdapter;
import hrv.band.aurora.view.fragment.MeasuringFragment;

public class HrvValueActivity extends AppCompatActivity {


    private HRVParameters parameter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hrv_value);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        parameter = getIntent().getParcelableExtra(MainActivity.HRV_PARAMETER_ID);
        String date = getIntent().getStringExtra(MainActivity.HRV_DATE);

        GridView gridview = (GridView) findViewById(R.id.hrv_value_list);

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

        TextView dateTxt =(TextView) findViewById(R.id.hrv_date);
        TextView ratingTxt =(TextView) findViewById(R.id.hrv_rating);
        TextView categoryTxt =(TextView) findViewById(R.id.hrv_category);
        TextView commentTxt =(TextView) findViewById(R.id.hrv_comment);

        dateTxt.setText(date);
        ratingTxt.setText("3.5" + "/5");
        categoryTxt.setText("Allgein");
        commentTxt.setText("bla bbla bla");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Delete action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
