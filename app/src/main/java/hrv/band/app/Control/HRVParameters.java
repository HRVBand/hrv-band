package hrv.band.app.Control;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

import hrv.band.app.view.adapter.MeasurementCategoryAdapter;

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
    private ArrayList<Double> rrIntervals;
    private double rating;
    private MeasurementCategoryAdapter.MeasureCategory category = MeasurementCategoryAdapter.MeasureCategory.GENERAL;
    private String note;

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
        out.writeList(rrIntervals);
        out.writeDouble(rating);
        out.writeSerializable(category);
        out.writeString(note);
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

    public HRVParameters() {}

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private HRVParameters(Parcel in) {
        time = (Date) in.readValue(getClass().getClassLoader());
        sdsd = in.readDouble();
        sd1 = in.readDouble();
        sd2 = in.readDouble();
        lf = in.readDouble();
        hf = in.readDouble();
        rmssd = in.readDouble();
        sdnn = in.readDouble();
        baevsky = in.readDouble();
        rrIntervals = new ArrayList<>();
        in.readList(rrIntervals, Double.class.getClassLoader());
        rating = in.readDouble();
        category = (MeasurementCategoryAdapter.MeasureCategory) in.readSerializable();
        note = in.readString();
    }

    public HRVParameters(Date time, double sdsd, double sd1, double sd2, double lf, double hf, double rmssd,
                         double sdnn, double baevsky, ArrayList<Double> rrIntervals/*, double rating,
                         MeasurementCategoryAdapter.MeasureCategory category, String note*/) {
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
        /*this.rating = rating;
        this.category = category;
        this.note = note;*/
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
    public ArrayList<Double> getRRIntervals() {
        return rrIntervals;
    }

    public void setRRIntervals(ArrayList<Double> rrIntervals) {
        this.rrIntervals = rrIntervals;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public MeasurementCategoryAdapter.MeasureCategory getCategory() {
        return category;
    }

    public void setCategory(MeasurementCategoryAdapter.MeasureCategory category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object other)
    {
        if(other == null) return false;
        if(other == this) return true;
        if(!(other instanceof HRVParameters)) return false;

        HRVParameters param = (HRVParameters)other;

        return param.getTime().equals(this.getTime());

    }
}
