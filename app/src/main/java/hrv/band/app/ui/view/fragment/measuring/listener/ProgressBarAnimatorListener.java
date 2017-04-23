package hrv.band.app.ui.view.fragment.measuring.listener;

import android.animation.Animator;

import hrv.band.app.device.HRVRRIntervalDevice;
import hrv.band.app.ui.view.fragment.IMeasuringView;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 23.04.2017
 * <p>
 * Animator Listener for the progress bar.
 */
public class ProgressBarAnimatorListener implements Animator.AnimatorListener {

    private IMeasuringView measuringView;

    public ProgressBarAnimatorListener(IMeasuringView measuringView) {
        this.measuringView = measuringView;
    }

    @Override
    public void onAnimationStart(Animator a) {
        measuringView.setConnectionButtonClickable(false);
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        final HRVRRIntervalDevice hrvRrIntervalDevice = measuringView.getHrvRrIntervalDevice();
        if (hrvRrIntervalDevice == null) {
            measuringView.showSnackbar("This should not happen...");
            return;
        }
        hrvRrIntervalDevice.stopMeasuring();

        measuringView.startMeasurementActivity();

        hrvRrIntervalDevice.clearRRIntervals();
        measuringView.resetProgress();
    }


    @Override
    public void onAnimationCancel(Animator animation) {
        animation.removeAllListeners();
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        //is not used at the moment
    }
}
