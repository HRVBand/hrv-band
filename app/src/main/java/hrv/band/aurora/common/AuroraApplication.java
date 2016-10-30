package hrv.band.aurora.common;

import android.app.Application;

import com.github.stkent.amplify.tracking.Amplify;

public class AuroraApplication extends Application {

    @Override
    public void onCreate() {

        Amplify.initSharedInstance(this)
                .setFeedbackEmailAddress("fenox@hotmail.com")
                .addTotalEventCountRule(new AmplifyOnClickEvent(), new AmplifyOnClickRule());

        Amplify.getSharedInstance();
    }
}
