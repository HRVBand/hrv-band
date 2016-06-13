package inno.hacks.ms.band.Control;

import java.util.Date;

/**
 * Created by Julian on 11.06.2016.
 */
public class HRVParameters {

    private Date time;
    private double sd1;
    private double sd2;
    private double lf;
    private double hf;
    private double rmssd;
    private double sdnn;
    private double baevsky;
    private double[] rrIntervals;

    public HRVParameters(Date time, double sd1, double sd2, double lf, double hf, double rmssd,
                         double sdnn, double baevsky, double[] rrIntervals) {
        this.time = time;
        this.sd1 = sd1;
        this.sd2 = sd2;
        this.lf = lf;
        this.hf = hf;
        this.rmssd = rmssd;
        this.sdnn = sdnn;
        this.baevsky = baevsky;
        this.rrIntervals = rrIntervals;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public double getBaevsky() {
        return baevsky;
    }

    public void setBaevsky(double baevsky) {
        this.baevsky = baevsky;
    }

    public double getSd1() {
        return sd1;
    }

    public void setSd1(double sd1) {
        this.sd1 = sd1;
    }

    public double getSd2() {
        return sd2;
    }

    public void setSd2(double sd2) {
        this.sd2 = sd2;
    }

    public double getSd1sd2Ratio() {
        return sd1 / sd2;
    }

    public double getLf() {
        return lf;
    }

    public void setLf(double lf) {
        this.lf = lf;
    }

    public double getHf() {
        return hf;
    }

    public void setHf(double hf) {
        this.hf = hf;
    }

    public double getLfhfRatio() {
        return lf / hf;
    }

    public double getRmssd() {
        return rmssd;
    }

    public void setRmssd(double rmssd) {
        this.rmssd = rmssd;
    }

    public double getSdnn() {
        return sdnn;
    }

    public void setSdnn(double sdnn) {
        this.sdnn = sdnn;
    }
    public double[] getRRIntervals() {
        return rrIntervals;
    }

    public void setRRIntervals(double[] rrIntervals) {
        this.rrIntervals = rrIntervals;
    }
}
