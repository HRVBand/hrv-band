package inno.hacks.ms.band.Control;

import java.util.Date;

/**
 * Created by Julian on 11.06.2016.
 */
public class HRVParameters {

    private Date time;
    private double sd1;
    private double sd2;
    private double sd1sd2Ratio;
    private double lf;
    private double hf;
    private double lfhfRatio;
    private double rmssd;
    private double sdnn;
    private double baevsky;

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
        return sd1sd2Ratio;
    }

    public void setSd1sd2Ratio(double sd1sd2Ratio) {
        this.sd1sd2Ratio = sd1sd2Ratio;
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
        return lfhfRatio;
    }

    public void setLfhfRatio(double lfhfRatio) {
        this.lfhfRatio = lfhfRatio;
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
}
