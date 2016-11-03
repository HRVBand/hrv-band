package hrv.band.aurora.Control;

import java.util.ArrayList;
import java.util.Date;

import hrv.band.aurora.view.adapter.CategorySpinnerAdapter;

/**
 * Created by Julian on 11.06.2016.
 */
public class HRVParameters {

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
    private CategorySpinnerAdapter.MeasureCategory category = CategorySpinnerAdapter.MeasureCategory.GENERAL;
    private String note;

    public HRVParameters() {}

    public HRVParameters(Date time, double sdsd, double sd1, double sd2, double lf, double hf, double rmssd,
                         double sdnn, double baevsky, ArrayList<Double> rrIntervals/*, double rating,
                         CategorySpinnerAdapter.MeasureCategory category, String note*/) {
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

    public CategorySpinnerAdapter.MeasureCategory getCategory() {
        return category;
    }

    public void setCategory(CategorySpinnerAdapter.MeasureCategory category) {
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
