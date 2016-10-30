package hrv.band.aurora.common;

import android.app.Application;

import com.github.stkent.amplify.tracking.Amplify;

/**
 * Created by Julian on 30.10.2016.
 */

public class AuroraApplication extends Application {

    @Override
    public void onCreate() {

        Amplify.initSharedInstance(this)
                .setFeedbackEmailAddress("fenox@hotmail.com")
                .applyAllDefaultRules()
                .addTotalEventCountRule(new AmplyfyOnClickEvent(), new AmplifyOnClickRule());

        Amplify.getSharedInstance();
    }
}
