package inno.hacks.ms.band.view;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import inno.hacks.ms.band.rrintervalExample.R;

public class RRIntervalActivity extends Activity {

    private TextView rrTextView;
    private Button startButton;
    private Button permissionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rrinterval);

        rrTextView = (TextView) findViewById(R.id.rrTextView);
    }
    public void startMeasure(View view) {

    }

    public void getPermission(View view) {

    }

}
