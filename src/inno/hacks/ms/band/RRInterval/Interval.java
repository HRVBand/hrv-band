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

    public void SetRRInterval(Double[] rrInterval)
    {
        RRInterval = new double[rrInterval.length];
        for (int i= 0; i < rrInterval.length;i++ ) {
            RRInterval[i] = rrInterval[i];
        }
    }

    public Date GetStartTime()
    {
        return startTime;
    }

    public void SetStartTime(Date time)
    {
        startTime = time;
    }

    public String printRR () {
        String s = new String();
        for (double d: RRInterval
             ) {
            s= s+","+d;
        }
        return s;
    }
}
