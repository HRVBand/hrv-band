package hrv.band.aurora.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.apache.commons.lang3.NotImplementedException;

import hrv.band.aurora.R;

public class ImprintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprint);
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
    }

    public void showAmplifyLicense(View view) {
        DialogLicence dialog = new DialogLicence();
        dialog.setLicenceText("Licence Text");
        dialog.show(getFragmentManager(), "Amplify Licence");
    }

    public void showHelloChartsLicence(View view) {
        throw new NotImplementedException("Hellochart Licence not shown");
    }

    public void showApacheCommonsLangLicence(View view) {
        throw new NotImplementedException("Apaceh Commons Lang3 Licence not shown");
    }
}
