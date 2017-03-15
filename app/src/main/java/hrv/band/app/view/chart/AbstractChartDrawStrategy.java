package hrv.band.app.view.chart;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hrv.band.app.control.Measurement;
import hrv.band.app.view.adapter.HRVValue;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 07.03.2017
 */


public abstract class AbstractChartDrawStrategy {
    protected Column[] columns;
    protected Context context;

    /**
     * Initialized the chart and showing the given parameters.
     * @param parameters the parameters to show in the chart.
     */
    public void drawChart(List<Measurement> parameters, ColumnChartView mChart,
                          HRVValue hrvValue, Context context) {

        this.context = context;
        initChartValues();
        setChartValues(parameters, hrvValue);
        setAxis(mChart, hrvValue);
    }

    /**
     * Sets the properties of the X and Y axis of the chart.
     */
    protected void setAxis(ColumnChartView mChart, HRVValue hrvValue) {
        ColumnChartData data = new ColumnChartData(new ArrayList<>(Arrays.asList(columns)));

        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName(getXAxisLabel());
        axisY.setName(hrvValue.getUnit());
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        mChart.setZoomEnabled(false);
        mChart.setColumnChartData(data);
    }

    /**
     * Initializes the chart values.
     */
    protected void initChartValues() {
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
     * Returns the label of the x axis.
     * @return the label of the x axis.
     */
    protected abstract String getXAxisLabel();

    /**
     * Returns the column count.
     * @return the column count.
     */
    protected abstract int getColumnCount();

    /**
     * Returns the sub column count.
     * @return the sub column count.
     */
    protected abstract int getSubColumnCount();

    /**
     * Sets the values of the chart with the given parameters.
     * @param parameters the parameters to show in the chart.
     */
    protected abstract void setChartValues(List<Measurement> parameters, HRVValue hrvValue);




}
