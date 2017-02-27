package hrv.band.app.control;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.Objects;

import hrv.RRData;
import hrv.band.app.view.adapter.MeasurementCategoryAdapter;
import units.TimeUnitConverter;

/**
 * Class that stores: Calculated HRV-Parameter values, Date of the measurement,
 * the actual rr-data, user note about the measurement, measurement category.
 *
 * Created by Julian on 11.06.2016.
 */
public class Measurement implements Parcelable {

    private final Date time;
    private final double sd1;
    private final double sd2;
    private final double lf;
    private final double hf;
    private final double rmssd;
    private final double sdnn;
    private final double baevsky;
    private final double[] rrIntervals;
    private final double rating;
    private final MeasurementCategoryAdapter.MeasureCategory category;
    private final String note;

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Measurement> CREATOR = new Parcelable.Creator<Measurement>() {
        @Override
        public Measurement createFromParcel(Parcel in) {
            return new Measurement(in);
        }
        @Override
        public Measurement[] newArray(int size) {
            return new Measurement[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Measurement(Parcel in) {
        this.time = (Date) in.readValue(getClass().getClassLoader());
        this.sd1 = in.readDouble();
        this.sd2 = in.readDouble();
        this.lf = in.readDouble();
        this.hf = in.readDouble();
        this.rmssd = in.readDouble();
        this.sdnn = in.readDouble();
        this.baevsky = in.readDouble();
        int rrIntervalLength= in.readInt();
        this.rrIntervals = new double[rrIntervalLength];
        in.readDoubleArray(rrIntervals);
        this.rating = in.readDouble();
        this.category = (MeasurementCategoryAdapter.MeasureCategory) in.readSerializable();
        this.note = in.readString();
    }

    public Measurement(MeasurementBuilder builder) {
        this.time = builder.time;
        this.sd1 = builder.sd1;
        this.sd2 = builder.sd2;
        this.lf = builder.lf;
        this.hf = builder.hf;
        this.rmssd = builder.rmssd;
        this.sdnn = builder.sdnn;
        this.baevsky = builder.baevsky;
        this.rrIntervals = builder.rrIntervals;
        this.rating = builder.rating;
        this.category = builder.category;
        this.note = builder.note;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeValue(time);
        out.writeDouble(sd1);
        out.writeDouble(sd2);
        out.writeDouble(lf);
        out.writeDouble(hf);
        out.writeDouble(rmssd);
        out.writeDouble(sdnn);
        out.writeDouble(baevsky);
        out.writeInt(rrIntervals.length);
        out.writeDoubleArray(rrIntervals);
        out.writeDouble(rating);
        out.writeSerializable(category);
        out.writeString(note);
    }

    public Date getTime() {
        return time;
    }

    public double getBaevsky() {
        return baevsky;
    }

    public double getSd1() {
        return sd1;
    }

    public double getSd2() {
        return sd2;
    }

    public double getLf() {
        return lf;
    }

    public double getHf() {
        return hf;
    }

    public double getLfhfRatio() {
        return lf / hf;
    }

    public double getRmssd() {
        return rmssd;
    }

    public double getSdnn() {
        return sdnn;
    }

    public double[] getRRIntervals() {
        return rrIntervals;
    }

    public double getRating() {
        return rating;
    }


    public MeasurementCategoryAdapter.MeasureCategory getCategory() {
        if (category == null) {
            return MeasurementCategoryAdapter.MeasureCategory.GENERAL;
        }
        return category;
    }

    public String getNote() {
        if (note == null) {
            return "";
        }
        return note;
    }

    @Override
    public boolean equals(Object other)
    {
        if(other == null) {
            return false;
        }
        if(other == this) {
            return true;
        }
        if(!(other instanceof Measurement)) {
            return false;
        }

        Measurement param = (Measurement)other;

        return param.getTime().equals(this.getTime());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTime());
    }

    /**
     * Creates a new HRVParameter-Object from a ALLHRVIndiceCalculator object
     * At the time the data unit can not be changed according to the incoming data
     * thats why the data has to be converted to the units given in the HRVValue class
     * @param time Time when the measurement began
     * @param rr Original RR-Data
     * @return New Measurement object
     */
    public static MeasurementBuilder from(Date time, double[] rr) {

        RRData data = RRData.createFromRRInterval(rr, TimeUnitConverter.TimeUnit.SECOND);
        HRVCalculatorController calc = new HRVCalculatorController(data);

        return new MeasurementBuilder(time, rr).
                sd1(calc.getSD1().getValue() * 1000).
                sd2(calc.getSD2().getValue() * 1000). //Convert to ms
                lf(calc.getLF().getValue()).
                hf(calc.getHF().getValue()).
                rmssd(calc.getRMSSD().getValue() * 1000). //Convert to ms
                sdnn(calc.getSDNN().getValue() * 1000). //Convert to ms
                baevsky(calc.getBaevsky().getValue() * 100);
    }

    /**
     * Class builder for {@link Measurement}.
     */
    public static class MeasurementBuilder {
        private Date time;
        private double sd1;
        private double sd2;
        private double lf;
        private double hf;
        private double rmssd;
        private double sdnn;
        private double baevsky;
        private double[] rrIntervals;
        private double rating;
        private MeasurementCategoryAdapter.MeasureCategory category;
        private String note;

        public MeasurementBuilder(Date time, double[] rrIntervals) {
            this.time = time;
            this.rrIntervals = rrIntervals;
        }

        public MeasurementBuilder(Measurement parameter) {
            this.time = parameter.getTime();
            this.sd1 = parameter.getSd1();
            this.sd2 = parameter.getSd2();
            this.lf = parameter.getLf();
            this.hf = parameter.getHf();
            this.rmssd = parameter.getRmssd();
            this.sdnn = parameter.getSdnn();
            this.baevsky = parameter.getBaevsky();
            this.rrIntervals = parameter.getRRIntervals();
        }

        MeasurementBuilder sd1(double sd1) {
            this.sd1 = sd1;
            return this;
        }
        MeasurementBuilder sd2(double sd2) {
            this.sd2 = sd2;
            return this;
        }
        MeasurementBuilder lf(double lf) {
            this.lf = lf;
            return this;
        }
        MeasurementBuilder hf(double hf) {
            this.hf = hf;
            return this;
        }
        MeasurementBuilder rmssd(double rmssd) {
            this.rmssd = rmssd;
            return this;
        }
        MeasurementBuilder sdnn(double sdnn) {
            this.sdnn = sdnn;
            return this;
        }
        MeasurementBuilder baevsky(double baevsky) {
            this.baevsky = baevsky;
            return this;
        }
        public MeasurementBuilder rating(double rating) {
            this.rating = rating;
            return this;
        }
        public MeasurementBuilder category(MeasurementCategoryAdapter.MeasureCategory category) {
            this.category = category;
            return this;
        }
        public MeasurementBuilder note(String note) {
            this.note = note;
            return this;
        }

        public Measurement build() {
            return new Measurement(this);
        }
    }
}
