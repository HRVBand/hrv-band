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
package inno.hacks.ms.band.rrintervalExample;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.UserConsent;

import inno.hacks.ms.band.Control.Calculation;
import inno.hacks.ms.band.Fourier.FastFourierTransform;
import inno.hacks.ms.band.Interpolation.CubicSplineInterpolation;
import inno.hacks.ms.band.RRInterval.Interval;
import inno.hacks.ms.band.rrintervalExample.R;
import inno.hacks.ms.band.view.RRIntervalActivity;

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

import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public class BandRRIntervalAppActivity extends Activity {

	private BandClient client = null;
	private Button btnStart, btnConsent;
	private TextView txtStatus;
	Interval ival = new Interval();
	List rr;
	static int duration = 10000;

	public void GetRRInterval() throws InterruptedException, ExecutionException, TimeoutException {
		Void task =  new RRIntervalSubscriptionTask().get(duration, TimeUnit.MILLISECONDS);//see results in eventlistener
		return;
	}

	private BandRRIntervalEventListener mRRIntervalEventListener = new BandRRIntervalEventListener() {
        @Override
        public void onBandRRIntervalChanged(final BandRRIntervalEvent event) {
            if (event != null) {
				double help = event.getInterval();
            	appendToUI(String.format("RR Interval = %.3f s\n", help));
				rr.add(help);

				//rr.add(help);
				//ival.SetRRInterval((Double[]) rr.toArray(new Double[rr.size()]));
            }
        }
    };

	private RRIntervalSubscriptionTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtStatus = (TextView) findViewById(R.id.txtStatus);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rr = new ArrayList<Double>();
				ival.SetStartTime(new Date());

				//txtStatus.setText("");
				task = new RRIntervalSubscriptionTask();
				task.execute();
				CountDownTimer timer = new CountDownTimer(duration,duration) {
					@Override
					public void onTick(long millisUntilFinished) {

					}

					@Override
					public void onFinish() {
						try {
							client.getSensorManager().unregisterRRIntervalEventListener(mRRIntervalEventListener);
						} catch (BandIOException e) {
							e.printStackTrace();
						}
						ival.SetRRInterval((Double[]) rr.toArray(new Double[rr.size()]));
						String s = ival.printRR();
						appendToUI(ival.printRR());

					}
				}.start();


/*				try {
					//GetRRInterval();
					//new RRIntervalSubscriptionTask().get(duration, TimeUnit.MILLISECONDS);//see results in eventlistener
				} catch (InterruptedException e) {
					ival.SetRRInterval((Double[]) rr.toArray(new Double[rr.size()]));
					ival.printRR();
					e.printStackTrace();
				} catch (ExecutionException e) {
					ival.SetRRInterval((Double[]) rr.toArray(new Double[rr.size()]));
					ival.printRR();
					e.printStackTrace();
				} catch (TimeoutException e) {
					ival.SetRRInterval((Double[]) rr.toArray(new Double[rr.size()]));
					ival.printRR();
					e.printStackTrace();
				}
				ival.SetRRInterval((Double[]) rr.toArray(new Double[rr.size()]));
				ival.printRR();*/
			}
		});
        
        final WeakReference<Activity> reference = new WeakReference<Activity>(this);
        
        btnConsent = (Button) findViewById(R.id.btnConsent);
        btnConsent.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("unchecked")
            @Override
			public void onClick(View v) {
				new HeartRateConsentTask().execute(reference);
			}
		});

		CubicSplineInterpolation inter = new CubicSplineInterpolation();
		FastFourierTransform fft = new FastFourierTransform(2048);

		Double[] rrTest = new Double[10];
		for(int i = 0; i < rrTest.length; i++)
		{
			rrTest[i] = (i % 2) + 1.0;
			//rrTest[i] = 1;
		}


		Interval interval = new Interval();
		interval.SetRRInterval(rrTest);

		Calculation calc = new Calculation(fft, inter);
		calc.Calculate(interval);
    }

	@Override
	protected void onResume() {
		super.onResume();
		txtStatus.setText("");
	}
	
    @Override
	protected void onPause() {
		super.onPause();
		if (client != null) {
			try {
				client.getSensorManager().unregisterRRIntervalEventListener(mRRIntervalEventListener);
			} catch (BandIOException e) {
				appendToUI(e.getMessage());
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
    
	private class RRIntervalSubscriptionTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				if (getConnectedBandClient()) {
				    int hardwareVersion = Integer.parseInt(client.getHardwareVersion().await());
				    if (hardwareVersion >= 20) {
    					if (client.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED) {
    						client.getSensorManager().registerRRIntervalEventListener(mRRIntervalEventListener);
    					} else {
    						appendToUI("You have not given this application consent to access heart rate data yet."
    								+ " Please press the Heart Rate Consent button.\n");
    					}
				    } else {
				        appendToUI("The RR Interval sensor is not supported with your Band version. Microsoft Band 2 is required.\n");
                    }
				} else {
					appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n");
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
				appendToUI(exceptionMessage);

			} catch (Exception e) {
				appendToUI(e.getMessage());
			}
			return null;
		}
	}
	
	private class HeartRateConsentTask extends AsyncTask<WeakReference<Activity>, Void, Void> {
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
					appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n");
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
				appendToUI(exceptionMessage);

			} catch (Exception e) {
				appendToUI(e.getMessage());
			}
			return null;
		}
	}
	
	private void appendToUI(final String string) {
		this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	txtStatus.setText(string);
            }
        });
	}
    
	private boolean getConnectedBandClient() throws InterruptedException, BandException {
		if (client == null) {
			BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
			if (devices.length == 0) {
				appendToUI("Band isn't paired with your phone.\n");
				return false;
			}
			client = BandClientManager.getInstance().create(getBaseContext(), devices[0]);
		} else if (ConnectionState.CONNECTED == client.getConnectionState()) {
			return true;
		}
		
		appendToUI("Band is connecting...\n");
		return ConnectionState.CONNECTED == client.connect().await();
	}

	public void startRRInterval(View view) {
		Intent intent = new Intent(getApplicationContext(), RRIntervalActivity.class);
		startActivity(intent);
	}
}

