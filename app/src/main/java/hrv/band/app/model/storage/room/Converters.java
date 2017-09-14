package hrv.band.app.model.storage.room;

import android.arch.persistence.room.TypeConverter;

import java.util.Arrays;
import java.util.Date;

import hrv.band.app.ui.view.adapter.MeasurementCategoryAdapter;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 14.09.2017
 */


public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static double[] fromStringToDoubleArray(String value) {
        String[] values = value.split(",");
        double[] result = new double[values.length];
        for (int i = 0; i < values.length; i++) {
             result[i] = Double.valueOf(values[i]);
        }
        return result;
    }

    @TypeConverter
    public static String doublesToString(double[] numbers) {
        return Arrays.toString(numbers);
    }

    @TypeConverter
    public static MeasurementCategoryAdapter.MeasureCategory fromStringToCategory(String value) {
        return MeasurementCategoryAdapter.MeasureCategory.valueOf(value);
    }

    @TypeConverter
    public static String categoryToString(MeasurementCategoryAdapter.MeasureCategory category) {
        return category.toString();
    }

}
