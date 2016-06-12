//Copyright (c) Microsoft Corporation All rights reserved.  
// 
//MIT License: 
// 
//Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
//documentation files (the  "Software"), to deal in the Software without restriction, including without limitation
//the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
//to permit persons to whom the Software is furnished to do so, subject to the following conditions: 
// 
//The above copyright notice and this permission notice shall be included in all copies or substantial portions of
//the Software. 
// 
//THE SOFTWARE IS PROVIDED ""AS IS"", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
//TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
//THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
//CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
//IN THE SOFTWARE.
package inno.hacks.ms.band.view;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.UserConsent;

import inno.hacks.ms.band.Control.Calculation;
import inno.hacks.ms.band.Control.HRVParameters;
import inno.hacks.ms.band.Control.SharedPreferencesController;
import inno.hacks.ms.band.Fourier.FastFourierTransform;
import inno.hacks.ms.band.Interpolation.CubicSplineInterpolation;
import inno.hacks.ms.band.RRInterval.Interval;
import inno.hacks.ms.band.rrintervalExample.R;

import com.microsoft.band.sensors.BandRRIntervalEvent;
import com.microsoft.band.sensors.BandRRIntervalEventListener;
import com.microsoft.band.sensors.HeartRateConsentListener;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BandRRIntervalAppActivity extends Activity {

	private BandClient client = null;
	private Button btnStart, btnConsent;
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
	private List rr;//stores the actual measurement-rrIntervals
	static int duration = 60000;//60 seconds will be measured

	/**
	 *Handels when a new RRInterval is incoming
	 */
	private BandRRIntervalEventListener mRRIntervalEventListener = new BandRRIntervalEventListener() {
        @Override
        public void onBandRRIntervalChanged(final BandRRIntervalEvent event) {
            if (event != null) {
				double help = event.getInterval();
            	//appendToUI(String.format("RR Interval = %.3f s\n", help));
				rr.add(help);//add the actual rrInterval

            }
        }
    };

	private RRIntervalSubscriptionTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

		//initialize
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
		txtTimer = (TextView) findViewById(R.id.txt_timer);
        btnStart = (Button) findViewById(R.id.btnStart);
		btnConsent = (Button) findViewById(R.id.btnConsent);

		lfhfTxt = (TextView) findViewById(R.id.textLFHFRatio);
		sdnnTxt= (TextView) findViewById(R.id.textSDNN);
		rmssdTxt= (TextView) findViewById(R.id.textRMSSD);
		sd1Txt= (TextView) findViewById(R.id.textSD1);
		sd2Txt= (TextView) findViewById(R.id.textSD2);
		sd1RatioSd2Txt= (TextView) findViewById(R.id.textSD1SD2Ratio);
		baevskyTxt= (TextView) findViewById(R.id.textBaevsky);

		final WeakReference<Activity> reference = new WeakReference<Activity>(this);

		//start measurement
        btnStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//initialize
				rr = new ArrayList<Double>();
				ival.SetStartTime(new Date());
				task = new RRIntervalSubscriptionTask();

				//txtStatus.setText("");// for debugging purposes

				task.execute();// run assync task for meassurements

				//counter, that stops measurements after duration seconds, and displays the remaining time
				CountDownTimer timer = new CountDownTimer(duration, 1000) {
					//display each second on UI
					@Override
					public void onTick(long millisUntilFinished) {
						long sec = millisUntilFinished / 1000 + 1;
						appendToUI(String.valueOf(sec), btnStart);
					}

					//stop eventhandler, that measures rrIntervals and start calculation
					@Override
					public void onFinish() {
						try {
							client.getSensorManager().unregisterRRIntervalEventListener(mRRIntervalEventListener);
						} catch (BandIOException e) {
							e.printStackTrace();
						}
						ival.SetRRInterval((Double[]) rr.toArray(new Double[rr.size()]));// convert to double and store everything in the ival-object for further analysis
						//String s = ival.printRR(); //for debugging only

						//display status
						appendToUI("", txtTimer);

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
				}.start();

			}
		});

		//get user Consent on reading heart rate bevore actual measuring
        btnConsent.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("unchecked")
            @Override
			public void onClick(View v) {
				new HeartRateConsentTask().execute(reference);
			}
		});
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
		if (client != null) {
			try {
				client.getSensorManager().unregisterRRIntervalEventListener(mRRIntervalEventListener);
			} catch (BandIOException e) {
				appendToUI(e.getMessage(), txtStatus);
			}
		}
	}
	
    @Override
    protected void onDestroy() {
        if (client != null) {
            try {
                client.disconnect().await();
            } catch (InterruptedException e) {
                // Do nothing as this is happening during destroy
            } catch (BandException e) {
                // Do nothing as this is happening during destroy
            }
        }
        super.onDestroy();
    }

    /**
	 * Class that meassures the RRInterval (get results via Eventhandler...)
	 */
	private class RRIntervalSubscriptionTask extends AsyncTask<Void, Void, Void> {
		//register eventhandler, so we can recieve the rrIntervals
		@Override
		protected Void doInBackground(Void... params) {
			try {
				if (getConnectedBandClient()) {
					appendToUI("", txtStatus);
				    int hardwareVersion = Integer.parseInt(client.getHardwareVersion().await());
				    if (hardwareVersion >= 20) {
    					if (client.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED) {
    						client.getSensorManager().registerRRIntervalEventListener(mRRIntervalEventListener);
    					} else {
    						appendToUI("You have not given this application consent to access heart rate data yet."
    								+ " Please press the Heart Rate Consent button.\n", txtStatus);
    					}
				    } else {
				        appendToUI("The RR Interval sensor is not supported with your Band version. Microsoft Band 2 is required.\n", txtStatus);
                    }
				} else {
					appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n", txtStatus);
				}
			} catch (BandException e) {
				String exceptionMessage="";
				switch (e.getErrorType()) {
				case UNSUPPORTED_SDK_VERSION_ERROR:
					exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
					break;
				case SERVICE_ERROR:
					exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
					break;
				default:
					exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
					break;
				}
				appendToUI(exceptionMessage, txtStatus);

			} catch (Exception e) {
				appendToUI(e.getMessage(), txtStatus);
			}
			return null;
		}
	}

	/**
	 * Class that gets user-permission for measuring the heartrate (and rrIntervals)
	 */
	private class HeartRateConsentTask extends AsyncTask<WeakReference<Activity>, Void, Void> {
		//register eventhandler, so we can recieve wether the user has accepted the measurement
		@Override
		protected Void doInBackground(WeakReference<Activity>... params) {
			try {
				if (getConnectedBandClient()) {
					
					if (params[0].get() != null) {
						client.getSensorManager().requestHeartRateConsent(params[0].get(), new HeartRateConsentListener() {
							@Override
							public void userAccepted(boolean consentGiven) {
							}
					    });
					}
				} else {
					appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n", txtStatus);
				}
			} catch (BandException e) {
				String exceptionMessage="";
				switch (e.getErrorType()) {
				case UNSUPPORTED_SDK_VERSION_ERROR:
					exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
					break;
				case SERVICE_ERROR:
					exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
					break;
				default:
					exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
					break;
				}
				appendToUI(exceptionMessage, txtStatus);

			} catch (Exception e) {
				appendToUI(e.getMessage(), txtStatus);
			}
			return null;
		}
	}

	/**
	 * write data to UI-thread
	 * @param string the text to write
	 * @param txt where to write
     */
	private void appendToUI(final String string, final TextView txt) {
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

    /**
	 * get the Microsoft band client, that handles all the connections and does the actual measurement
	 * @return wether the band is connected
	 * @throws InterruptedException	connection has dropped e.g.
	 * @throws BandException ohter stuff that should not happen
     */
	private boolean getConnectedBandClient() throws InterruptedException, BandException {
		if (client == null) {
			BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
			if (devices.length == 0) {
				appendToUI("Band isn't paired with your phone.\n", txtStatus);
				return false;
			}
			client = BandClientManager.getInstance().create(getBaseContext(), devices[0]);
		} else if (ConnectionState.CONNECTED == client.getConnectionState()) {
			return true;
		}
		
		appendToUI("Band is connecting...\n", txtStatus);
		return ConnectionState.CONNECTED == client.connect().await();
	}

}

