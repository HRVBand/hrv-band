package inno.hacks.ms.band.view;

import java.util.Date;

import inno.hacks.ms.band.Control.Calculation;
import inno.hacks.ms.band.Control.HRVParameters;
import inno.hacks.ms.band.Control.SharedPreferencesController;
import inno.hacks.ms.band.Fourier.FastFourierTransform;
import inno.hacks.ms.band.Interpolation.CubicSplineInterpolation;
import inno.hacks.ms.band.RRInterval.IRRInterval;
import inno.hacks.ms.band.RRInterval.Interval;
import inno.hacks.ms.band.RRInterval.msband.MSBandRRInterval;
import inno.hacks.ms.band.rrintervalExample.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MeasureActivity extends Activity {

	private IRRInterval rrInterval;

	private Button btnStart;
	private TextView txtStatus;
	private TextView txtTimer;
	private TextView lfhfTxt;
	private TextView sdnnTxt;
	private TextView rmssdTxt;
	private TextView sd1Txt;
	private TextView sd2Txt;
	private TextView sd1RatioSd2Txt;
	private TextView baevskyTxt;
	private Interval ival = new Interval();//stores the complete measure --> ~60 seconds of rrIntervals
	//how long will be measured
	static int duration = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

		//initialize
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);



        txtStatus = (TextView) findViewById(R.id.txtStatus);
		txtTimer = (TextView) findViewById(R.id.txt_timer);
        btnStart = (Button) findViewById(R.id.btnStart);

		lfhfTxt = (TextView) findViewById(R.id.textLFHFRatio);
		sdnnTxt= (TextView) findViewById(R.id.textSDNN);
		rmssdTxt= (TextView) findViewById(R.id.textRMSSD);
		sd1Txt= (TextView) findViewById(R.id.textSD1);
		sd2Txt= (TextView) findViewById(R.id.textSD2);
		sd1RatioSd2Txt= (TextView) findViewById(R.id.textSD1SD2Ratio);
		baevskyTxt= (TextView) findViewById(R.id.textBaevsky);

		rrInterval = new MSBandRRInterval(this, txtStatus);

    }

	private void calculate() {
		//start calculation
		CubicSplineInterpolation inter = new CubicSplineInterpolation();
		FastFourierTransform fft = new FastFourierTransform(4096);

		Calculation calc = new Calculation(fft, inter);
		HRVParameters results = calc.Calculate(ival);
		results.setTime(new Date());
		SharedPreferencesController pref = new SharedPreferencesController();
		pref.AddParams(getApplicationContext(), results);
		setResultsUI(results);
	}



	private void setResultsUI(HRVParameters results) {
		String decimal = "%.2f";
		appendToUI(String.format(decimal, results.getLfhfRatio()), lfhfTxt);
		appendToUI(String.format(decimal, results.getSdnn()), sdnnTxt);
		appendToUI(String.format(decimal, results.getRmssd()), rmssdTxt);
		appendToUI(String.format(decimal, results.getSd1()), sd1Txt);
		appendToUI(String.format(decimal, results.getSd2()), sd2Txt);
		appendToUI(String.format(decimal, results.getSd1sd2Ratio()), sd1RatioSd2Txt);
		appendToUI(String.format(decimal, results.getBaevsky()), baevskyTxt);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//reset to default
		txtStatus.setText("");
	}
	
    @Override
	protected void onPause() {
		super.onPause();
		rrInterval.pauseMeasuring();
	}
	
    @Override
    protected void onDestroy() {
        rrInterval.destroy();
        super.onDestroy();
    }

	public void startMeasuring(View view) {
		ival.SetStartTime(new Date());

		rrInterval.startRRIntervalMeasuring();

		//counter, that stops measurements after duration seconds, and displays the remaining time
		new CountDownTimer(duration, 1000) {
			//display each second on UI
			@Override
			public void onTick(long millisUntilFinished) {
				long sec = millisUntilFinished / 1000 + 1;
				appendToUI(String.valueOf(sec), btnStart);
			}

			//stop eventhandler, that measures rrIntervals and start calculation
			@Override
			public void onFinish() {
				rrInterval.stopMeasuring();

				ival.SetRRInterval(rrInterval.getRRIntervals());
				//String s = ival.printRR(); //for debugging only

				//display status
				appendToUI("", txtTimer);

				calculate();
			}
		}.start();
	}

	public void getDevicePermission(View view) {
		rrInterval.getDevicePermission();
	}

	/**
	 * write data to UI-thread
	 * @param string the text to write
     */
	public void appendToUI(final String string, final TextView txt) {
		this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	txt.setText(string);
            }
        });
	}

	/**
	 * write data to UI-thread
	 * @param string the text to write
	 * @param txt where to write
	 */
	private void appendToUI(final String string, final Button txt) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				txt.setText(string);
			}
		});
	}

}

