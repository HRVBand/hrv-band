package hrv.band.aurora.common;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEvent;

/**
 * Created by Julian on 30.10.2016.
 */

public class AmplyfyOnClickEvent implements IEvent {

    @NonNull
    @Override
    public String getTrackingKey() {
        return "MyEvent";
    }
}
