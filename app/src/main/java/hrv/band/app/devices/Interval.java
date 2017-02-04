package hrv.band.app.devices;

import java.util.Date;

/**
 * Copyright (c) 2017
 * Created by Julian Martin on 03.01.2016.
 */
public class Interval {

    //RR Interval Data in s
    private double[] rrInterval;
    private final Date startTime;

    public Interval(Date startTime, double[] rrInterval) {
        this.startTime = startTime;
        this.rrInterval = rrInterval;
    }

    public Interval(Date startTime) {
        this.startTime = startTime;
    }

    public double[] getRRInterval()
    {
        return rrInterval;
    }

    public void setRRInterval(Double[] rrInterval)
    {
        this.rrInterval = new double[rrInterval.length];
        for (int i = 0; i < rrInterval.length; i++ ) {
            this.rrInterval[i] = rrInterval[i];
        }
    }

    public Date getStartTime()
    {
        return startTime;
    }
}
