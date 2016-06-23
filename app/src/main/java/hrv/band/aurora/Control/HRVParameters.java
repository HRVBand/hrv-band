package hrv.band.aurora.Control;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Julian on 11.06.2016.
 */
public class HRVParameters implements Parcelable {

    private Date time;
    private double sdsd;
    private double sd1;
    private double sd2;
    private double lf;
    private double hf;
    private double rmssd;
    private double sdnn;
    private double baevsky;
    private double[] rrIntervals;

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeValue(time);
        out.writeDouble(sdsd);
        out.writeDouble(sd1);
        out.writeDouble(sd2);
        out.writeDouble(lf);
        out.writeDouble(hf);
        out.writeDouble(rmssd);
        out.writeDouble(sdnn);
        out.writeDouble(baevsky);
        out.writeDoubleArray(rrIntervals);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<HRVParameters> CREATOR = new Parcelable.Creator<HRVParameters>() {
        public HRVParameters createFromParcel(Parcel in) {
            return new HRVParameters(in);
        }

        public HRVParameters[] newArray(int size) {
            return new HRVParameters[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private HRVParameters(Parcel in) {
        time = (Date) in.readValue(null);
        sdsd = in.readDouble();
        sd1 = in.readDouble();
        sd2 = in.readDouble();
        lf = in.readDouble();
        hf = in.readDouble();
        rmssd = in.readDouble();
        sdnn = in.readDouble();
        baevsky = in.readDouble();
        rrIntervals = in.createDoubleArray();
    }

    public HRVParameters(Date time, double sdsd, double sd1, double sd2, double lf, double hf, double rmssd,
                         double sdnn, double baevsky, double[] rrIntervals) {
        this.time = time;
        this.sdsd = sdsd;
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

    public double getSdSd() {
        return sdsd;
    }

    public void setSdSd(double sdsd) {
        this.sdsd = sdsd;
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
