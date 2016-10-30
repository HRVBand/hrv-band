package hrv.band.aurora.common;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEnvironmentBasedRule;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCapabilitiesProvider;
import com.github.stkent.amplify.tracking.interfaces.IEventBasedRule;

/**
 * Created by Julian on 30.10.2016.
 */

public class AmplifyOnClickRule implements IEventBasedRule<Integer> {


    @Override
    public boolean shouldAllowFeedbackPromptByDefault() {
        return true;
    }

    @Override
    public boolean shouldAllowFeedbackPrompt(@NonNull Integer cachedEventValue) {
        return true;
    }

    @NonNull
    @Override
    public String getDescription() {
        return "MyEvent";
    }
}
