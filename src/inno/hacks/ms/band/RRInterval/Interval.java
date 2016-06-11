package inno.hacks.ms.band.RRInterval;

import java.util.Date;

/**
 * Created by Julian on 11.06.2016.
 */
public class Interval {

    private double[] RRInterval;
    private Date startTime;

    public double[] GetRRInterval()
    {
        return RRInterval;
    }

    public void SetRRInterval(double[] rrInterval)
    {
        RRInterval = rrInterval;
    }

    public Date GetStartTime()
    {
        return startTime;
    }

    public void SetStartTime(Date time)
    {
        startTime = time;
    }
}
