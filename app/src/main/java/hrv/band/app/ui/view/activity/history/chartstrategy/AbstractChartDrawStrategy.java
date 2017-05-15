package hrv.band.app.ui.view.activity.history.chartstrategy;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import hrv.HRVLibFacade;
import hrv.RRData;
import hrv.band.app.R;
import hrv.band.app.model.HRVParameterUnitAdapter;
import hrv.band.app.model.Measurement;
import hrv.calc.parameter.HRVParameterEnum;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import units.TimeUnit;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 07.03.2017
 * <p>
 * Strategy to draw parameters into chart.
 */
public abstract class AbstractChartDrawStrategy {
    private Context context;
    Column[] columns;

    /**
     * Initialized the chart and showing the given parameters.
     *
     * @param measurements the parameters to show in the chart.
     */
    public void drawChart(List<Measurement> measurements, ColumnChartView mChart,
                          HRVParameterEnum hrvValue, Context context) {
        this.context = context;
        initChartValues();
        setChartValues(measurements, hrvValue);
        setAxis(mChart, hrvValue);
    }

    /**
     * Sets the properties of the X and Y axis of the chart.
     */
    private void setAxis(ColumnChartView mChart, HRVParameterEnum hrvValue) {
        ColumnChartData data = new ColumnChartData(new ArrayList<>(Arrays.asList(columns)));

        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName(getXAxisLabel());
        axisX.setValues(getXAxisValues());
        axisY.setName(getUnit(hrvValue));
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        mChart.setZoomEnabled(false);
        mChart.setColumnChartData(data);
    }

    private String getUnit(HRVParameterEnum hrvValue) {
        HRVParameterUnitAdapter unitAdapter = new HRVParameterUnitAdapter();
        return unitAdapter.getUnitOfParameter(hrvValue);
    }

    /**
     * Initializes the chart values.
     */
    private void initChartValues() {
        columns = new Column[getColumnCount()];
        for (int i = 0; i < getColumnCount(); i++) {
            ArrayList<SubcolumnValue> subColumns = new ArrayList<>();
            for (int j = 0; j < getSubColumnCount(); j++) {
                subColumns.add(new SubcolumnValue());
            }
            columns[i] = new Column(subColumns);
        }
    }

    /**
     * Configs the column labels.
     *
     * @param index of the column to config.
     */
    void configColumnLabels(int index) {
        columns[index].setHasLabels(false);
        columns[index].setHasLabelsOnlyForSelected(false);
    }

    int getColor() {
        return ContextCompat.getColor(context, R.color.colorAccent);
    }

    Resources getResources() {
        return context.getResources();
    }

    double getValue(Measurement measurement, HRVParameterEnum hrvValueType) {
        HRVLibFacade hrvCalc = new HRVLibFacade(RRData.createFromRRInterval(measurement.getRRIntervals(), TimeUnit.SECOND));
        if(!hrvCalc.validData()) {
            return -1;
        }

        hrvCalc.setParameters(EnumSet.of(hrvValueType));
        return hrvCalc.calculateParameters().get(0).getValue();
    }

    /**
     * Returns the label of the x axis.
     *
     * @return the label of the x axis.
     */
    protected abstract String getXAxisLabel();

    protected abstract List<AxisValue> getXAxisValues();

    /**
     * Returns the column count.
     *
     * @return the column count.
     */
    protected abstract int getColumnCount();

    /**
     * Returns the sub column count.
     *
     * @return the sub column count.
     */
    protected abstract int getSubColumnCount();

    /**
     * Sets the values of the chart with the given parameters.
     *
     * @param parameters the parameters to show in the chart.
     */
    abstract void setChartValues(List<Measurement> parameters, HRVParameterEnum hrvValue);


}
