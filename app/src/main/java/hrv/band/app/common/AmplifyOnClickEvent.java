package hrv.band.app.common;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEvent;


class AmplifyOnClickEvent implements IEvent {

    @NonNull
    @Override
    public String getTrackingKey() {
        return "MyEvent";
    }
}
