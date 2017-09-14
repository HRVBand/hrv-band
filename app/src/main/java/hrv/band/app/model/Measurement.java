package hrv.band.app.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.Objects;

import hrv.band.app.ui.view.adapter.MeasurementCategoryAdapter;

/**
 * Class that stores: Calculated HRV-Parameter values, Date of the measurement,
 * the actual rr-data, user note about the measurement, measurement category.
 *
 * Created by Julian on 11.06.2016.
 */
@Entity
public class Measurement implements Parcelable {

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

    @PrimaryKey(autoGenerate = true)
    public int id;

    private Date date;

    public double[] getRrIntervals() {
        return rrIntervals;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setRrIntervals(double[] rrIntervals) {
        this.rrIntervals = rrIntervals;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setCategory(MeasurementCategoryAdapter.MeasureCategory category) {
        this.category = category;
    }

    public void setNote(String note) {
        this.note = note;
    }

    private double[] rrIntervals;
    private double rating;
    private MeasurementCategoryAdapter.MeasureCategory category;
    private String note;

    public void setId(int id) {
        this.id = id;
    }

    @Ignore// example constructor that takes a Parcel and gives you an object populated with it's values
    protected Measurement(Parcel in) {
        this.date = (Date) in.readValue(getClass().getClassLoader());

        int rrIntervalLength= in.readInt();
        this.rrIntervals = new double[rrIntervalLength];
        in.readDoubleArray(rrIntervals);
        this.rating = in.readDouble();
        this.category = (MeasurementCategoryAdapter.MeasureCategory) in.readSerializable();
        this.note = in.readString();
    }

    public Measurement() {

    }

    @Ignore
    public Measurement(MeasurementBuilder builder) {
        this.date = builder.time;
        this.rrIntervals = builder.rrIntervals;
        this.rating = builder.rating;
        this.category = builder.category;
        this.note = builder.note;
    }

    /**
     * Creates a new HRVParameter-Object from a ALLHRVIndiceCalculator object
     * At the date the data unit can not be changed according to the incoming data
     * thats why the data has to be converted to the units given in the HRVValue class
     *
     * @param time Time when the measurement began
     * @param rr   Original RR-Data
     * @return New Measurement object
     */
    public static MeasurementBuilder from(Date time, double[] rr) {

        return new MeasurementBuilder(time, rr);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeValue(date);
        out.writeInt(rrIntervals.length);
        out.writeDoubleArray(rrIntervals);
        out.writeDouble(rating);
        out.writeSerializable(category);
        out.writeString(note);
    }

    public Date getDate() {
        return date;
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

        return param.getDate().equals(this.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getDate());
    }

    /**
     * Class builder for {@link Measurement}.
     */
    public static class MeasurementBuilder {
        private Date time;
        private double[] rrIntervals;
        private double rating;
        private MeasurementCategoryAdapter.MeasureCategory category;
        private String note;

        public MeasurementBuilder(Date time, double[] rrIntervals) {
            this.time = time;
            this.rrIntervals = rrIntervals;
        }

        public MeasurementBuilder(Measurement parameter) {
            this.time = parameter.getDate();
            this.rrIntervals = parameter.getRRIntervals();
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
