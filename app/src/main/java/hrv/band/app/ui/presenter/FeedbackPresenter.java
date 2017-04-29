package hrv.band.app.ui.presenter;

import hrv.band.app.ui.view.fragment.IFeedbackDialogView;

/**
 * Copyright (c) 2017
 * Created by Thomas on 29.04.2017.
 */

public class FeedbackPresenter implements IFeedbackPresenter {

    private IFeedbackDialogView view;

    public FeedbackPresenter(IFeedbackDialogView view) {
        this.view = view;
    }

    @Override
    public String getSubject() {
        return "Feedback - [" + view.getSubjectText() + "]";
    }

    @Override
    public String getMessage() {
        return view.getMessageText() + "\n" + getDeviceInformation();
    }

    private String getDeviceInformation() {
        String info = "Debug-infos:";
        info += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        info += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
        info += "\n Device: " + android.os.Build.DEVICE;
        info += "\n Model (and Product): " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")";
        return info;
    }
}
