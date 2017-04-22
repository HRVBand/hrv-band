package hrv.band.app.ui.view.activity.history.chartstrategy;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hrv.band.app.model.Measurement;
import hrv.band.app.ui.view.adapter.HRVValue;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 07.03.2017
 * <p>
 * Strategy to draw parameters into chart.
 */
public abstract class AbstractChartDrawStrategy {
    protected Context context;
    Column[] columns;

    /**
     * Initialized the chart and showing the given parameters.
     *
     * @param measurements the parameters to show in the chart.
     */
    public void drawChart(List<Measurement> measurements, ColumnChartView mChart,
                          HRVValue hrvValue, Context context) {

        this.context = context;
        initChartValues();
        setChartValues(measurements, hrvValue);
        setAxis(mChart, hrvValue);
    }

    /**
     * Sets the properties of the X and Y axis of the chart.
     */
    private void setAxis(ColumnChartView mChart, HRVValue hrvValue) {
        ColumnChartData data = new ColumnChartData(new ArrayList<>(Arrays.asList(columns)));

        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName(getXAxisLabel());
        axisX.setValues(getXAxisValues());
        axisY.setName(hrvValue.getUnit());
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        mChart.setZoomEnabled(false);
        mChart.setColumnChartData(data);
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
    protected abstract void setChartValues(List<Measurement> parameters, HRVValue hrvValue);


}
