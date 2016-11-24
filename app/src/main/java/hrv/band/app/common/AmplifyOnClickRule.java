package hrv.band.app.common;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEventBasedRule;

class AmplifyOnClickRule implements IEventBasedRule<Integer> {


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
