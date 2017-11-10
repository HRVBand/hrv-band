package hrv.band.app.ui.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import hrv.HRVLibFacade;
import hrv.RRData;
import hrv.band.app.R;
import hrv.band.app.model.Measurement;
import hrv.band.app.ui.presenter.IMeasurementDetailsPresenter;
import hrv.band.app.ui.presenter.IRRPresenter;
import hrv.band.app.ui.presenter.MeasuredRRPresenter;
import hrv.band.app.ui.presenter.MeasurementDetailsPresenter;
import hrv.band.app.ui.view.fragment.MeasuredParameterFragment;
import hrv.calc.Histogram;
import hrv.calc.manipulator.HRVDataManipulator;
import hrv.calc.manipulator.HRVFilterOutlier;
import hrv.calc.manipulator.window.NoWindow;
import hrv.calc.parameter.HRVParameter;
import hrv.calc.psd.PowerSpectrum;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import units.TimeUnit;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * This adapter holds the hrv parameters to show in the {@link MeasuredParameterFragment}.
 */
public class MeasurementResultListAdapter extends BaseAdapter {

    /**
     * The context of activity holding the adapter.
     **/
    private final Context context;

    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private int selectedFilterIndex = 0;

    private Dictionary<Integer, HRVDataManipulator> filters = new Hashtable<>();

    private Activity activity;

    private IRRPresenter presenter;

    public MeasurementResultListAdapter(Context context, Activity activity, Measurement measurement) {
        this.context = context;
        this.activity = activity;
        this.presenter = new MeasuredRRPresenter(measurement);

        HRVDataManipulator noFilter = new NoWindow();

        HRVFilterOutlier weakFilter = new HRVFilterOutlier();
        weakFilter.setStrength(0.5);
        weakFilter.setTestRange(2);

        HRVFilterOutlier strongFilter = new HRVFilterOutlier();
        strongFilter.setStrength(0.3);
        strongFilter.setTestRange(6);

        filters.put(0, noFilter);
        filters.put(1, weakFilter);
        filters.put(2, strongFilter);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Create rr info item
        if(position == 0) {
            convertView = createFilterItem(parent);
        } else if (position == 1) {
            convertView = createRRInfoItem(parent);
        } else if(position == 2) {
            convertView = createStressInfoItem(parent);
        } else if(position == 3) {
            convertView = createFrequencyInfoItem(parent);
        } else if(position == 4) {
            convertView = createSDInfoItem(parent);
        } else if(position == 5) {
            convertView = createAdditionalInfoItem(parent);
        }
        return convertView;
    }

    private View createAdditionalInfoItem(ViewGroup parent) {
        View convertView;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(
                R.layout.measurement_result_list_item_additional_info, parent, false);

        IMeasurementDetailsPresenter presenter = new MeasurementDetailsPresenter(
                this.presenter.getMeasurement(), activity);

        AdditionalInfoViewHolder holder = new AdditionalInfoViewHolder();
        holder.date = convertView.findViewById(R.id.hrv_date);
        holder.rating = convertView.findViewById(R.id.hrv_rating);
        holder.category= convertView.findViewById(R.id.hrv_category);
        holder.categoryImg = convertView.findViewById(R.id.hrv_category_icon);
        holder.comment = convertView.findViewById(R.id.hrv_comment);
        convertView.setTag(holder);

        holder.date.setText(presenter.getDate());
        holder.rating.setText(presenter.getRating());
        holder.category.setText(presenter.getCategory());
        holder.categoryImg.setImageDrawable(presenter.getCategoryIcon());
        holder.comment.setText(presenter.getNote());

        return convertView;
    }

    private View createFilterItem(ViewGroup parent) {
        View convertView;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.measurement_result_list_item_filter, parent, false);

        final Button[] buttons = new Button[] { convertView.findViewById(R.id.no_filter_button),
            convertView.findViewById(R.id.weak_filter_button),
            convertView.findViewById(R.id.strong_filter_button)};

        changeSelectedFilterButton(buttons);
        presenter.setFilter(filters.get(selectedFilterIndex));
        presenter.calculateRRStatistic();

        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilterIndex = 0;
                presenter.setFilter(filters.get(selectedFilterIndex));
                presenter.calculateRRStatistic();

                changeSelectedFilterButton(buttons);
                notifyDataSetChanged();
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilterIndex = 1;
                presenter.setFilter(filters.get(selectedFilterIndex));
                presenter.calculateRRStatistic();
                changeSelectedFilterButton(buttons);
                notifyDataSetChanged();
            }
        });

        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilterIndex = 2;
                presenter.setFilter(filters.get(selectedFilterIndex));
                presenter.calculateRRStatistic();
                changeSelectedFilterButton(buttons);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    private void changeSelectedFilterButton(Button[] buttons) {

        buttons[selectedFilterIndex].setBackground(context.getResources().getDrawable(R.drawable.round_corner_button_background_pressed));

        for(int i = 0; i < 3; i++) {
            if(i != selectedFilterIndex) {
                buttons[i].setBackground(context.getResources().getDrawable(R.drawable.round_corner_button_background_not_pressed));
            }
        }
    }

    @Override
    public Object getItem(int i) {
        return presenter.getParameters().get(i);
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Creates a view for the measurement result list containing information about the rr-interval-
     * data
     * @param parent parent view
     * @return view containing information about the rr-interval-data
     */
    private View createRRInfoItem(ViewGroup parent) {
        View convertView;
        RRResultViewHolder holder;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.measurement_result_list_item_rr, parent, false);

        holder = new RRResultViewHolder();
        holder.rrData = convertView.findViewById(R.id.rr_chart_list);
        holder.min = convertView.findViewById(R.id.rr_min);
        holder.max = convertView.findViewById(R.id.rr_max);
        holder.average = convertView.findViewById(R.id.rr_average);
        convertView.setTag(holder);


        List<Line> lines = new ArrayList<>();
        List<PointValue> points = new ArrayList<>();



        //Filter data
        double[] filteredValues = presenter.getFilteredData();

        double sum = 0;
        for (double item : filteredValues) {
            points.add(new PointValue((float) sum, (float) item));
            sum += item;
        }

        Line rrValueLine = new Line(points);
        rrValueLine.setColor(R.color.colorAccent);
        rrValueLine.setHasPoints(false);
        rrValueLine.setHasLabels(false);


        lines.add(rrValueLine);

        LineChartData dataLine = new LineChartData(lines);
        setChartAxis(dataLine);

        holder.rrData.setZoomEnabled(false);
        holder.rrData.setLineChartData(dataLine);
        holder.max.setText(presenter.getRRMax());
        holder.min.setText(presenter.getRRMin());
        holder.average.setText(presenter.getRRAverage());

        return convertView;
    }

    private void setChartAxis(LineChartData data) {
        Axis axisX = new Axis();

        axisX.setLineColor(Color.argb(255,255,0,0));
        axisX.setTextColor(Color.argb(255,255,0,255));
        //data.setAxisXBottom(axisX);

        Axis axisY = new Axis();
        axisY.setLineColor(Color.argb(255,255,0,0));
        axisY.setTextColor(Color.argb(255,255,0,255));
        //data.setAxisYLeft(axisY);
    }

    private View createStressInfoItem(ViewGroup parent) {
        View convertView;
        StressViewHolder holder;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.measurement_result_list_item_stress, parent, false);

        holder = new StressViewHolder();
        holder.histogram = convertView.findViewById(R.id.measurement_result_item_list_histogram);
        holder.stressIndex = convertView.findViewById(R.id.measurement_result_item_list_baevsky_stress_index);
        convertView.setTag(holder);

        List<Column> columns = new ArrayList<>();
        Column column = new Column();

        List<SubcolumnValue> subcolumnValues = new ArrayList<>();

        Histogram hist = new Histogram(presenter.getFilteredData());
        hist.setBinSize(0.05);

        for(int i = 0; i < hist.getBins().length; i++) {
            subcolumnValues.add(new SubcolumnValue(hist.getBins()[i], R.color.colorAccent));
        }

        //If less than 9 bins, add bins up to 9 and shift the mode to the middle
        if(hist.getBins().length < 9) {
            int newBinsLeft = hist.getAmplitudeMode() < 5 ? 5 - hist.getAmplitudeMode() : 0;
            int newBinsRight = 9 - newBinsLeft - hist.getBins().length;

            for(int i = 0; i < newBinsLeft; i++) {
                subcolumnValues.add(new SubcolumnValue(0, R.color.colorAccent));
            }

            for(int i = 0; i < newBinsRight; i++) {
                subcolumnValues.add(new SubcolumnValue(0, R.color.colorAccent));
            }
        }

        column.setValues(subcolumnValues);
        columns.add(column);

        ColumnChartData data = new ColumnChartData(columns);
        holder.histogram.setColumnChartData(data);
        holder.stressIndex.setText(decimalFormat.format(getParamByName("BAEVSKY").getValue()));

        return convertView;
    }

    /**
     * Creates a view for the measurement result list containing information about the rr-interval-
     * data
     * @param parent parent view
     * @return view containing information about the rr-interval-data
     */
    private View createFrequencyInfoItem(ViewGroup parent) {
        View convertView;
        FrequencyViewHolder holder;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.measurement_result_list_item_frequency, parent, false);

        holder = new FrequencyViewHolder();
        holder.frequencyChart = convertView.findViewById(R.id.frequency_chart);
        holder.lf = convertView.findViewById(R.id.measurement_result_item_lf);
        holder.lfUnit = convertView.findViewById(R.id.measurement_result_unit_lf);
        holder.hf = convertView.findViewById(R.id.measurement_result_item_hf);
        holder.hfUnit = convertView.findViewById(R.id.measurement_result_unit_hf);
        holder.lfhf = convertView.findViewById(R.id.measurement_result_item_lfhf);
        holder.lfhfUnit = convertView.findViewById(R.id.measurement_result_unit_lfhf);
        convertView.setTag(holder);


        List<Line> lines = new ArrayList<>();
        List<PointValue> points = new ArrayList<>();
        HRVLibFacade hrvCalc = new HRVLibFacade(RRData.createFromRRInterval(
                presenter.getMeasurement().getRRIntervals(), units.TimeUnit.SECOND));

        PowerSpectrum ps = hrvCalc.getPowerSpectrum(RRData.createFromRRInterval(
                presenter.getFilteredData(), TimeUnit.SECOND));

        for (int i = 0; i < ps.getFrequency().length / 2; i++) {
            points.add(new PointValue((float) ps.getFrequency()[i], (float) ps.getPower()[i]));
        }

        Line frequencyValueLine = new Line(points);
        frequencyValueLine.setColor(R.color.colorAccent);
        frequencyValueLine.setHasPoints(false);
        frequencyValueLine.setHasLabels(false);


        lines.add(frequencyValueLine);

        LineChartData dataLine = new LineChartData(lines);
        setChartAxis(dataLine);

        holder.frequencyChart.setZoomEnabled(false);
        holder.frequencyChart.setLineChartData(dataLine);
        holder.lf.setText(decimalFormat.format(getParamByName("LF").getValue()));
        holder.lfUnit.setText(getParamByName("LF").getUnit());
        holder.hf.setText(decimalFormat.format(getParamByName("HF").getValue()));
        holder.hfUnit.setText(getParamByName("HF").getUnit());
        holder.lfhf.setText(decimalFormat.format(getParamByName("LFHF").getValue()));
        holder.lfhfUnit.setText(getParamByName("LFHF").getUnit());

        return convertView;
    }

    private View createSDInfoItem(ViewGroup parent) {
        View convertView;
        SDViewHolder holder;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.measurement_result_list_item_sd, parent, false);

        holder = new SDViewHolder();
        holder.sdnn = convertView.findViewById(R.id.measurement_result_item_sdnn);
        holder.sdnnUnit = convertView.findViewById(R.id.measurement_result_item_sdnn_unit);
        holder.sd1 = convertView.findViewById(R.id.measurement_result_item_sd1);
        holder.sd1Unit = convertView.findViewById(R.id.measurement_result_item_sd1_unit);;
        holder.sd2 = convertView.findViewById(R.id.measurement_result_item_sd2);
        holder.sd2Unit = convertView.findViewById(R.id.measurement_result_item_sd2_unit);
        holder.sd1sd2 = convertView.findViewById(R.id.measurement_result_item_sd1sd2);
        holder.sdsd = convertView.findViewById(R.id.measurement_result_item_sdsd);
        convertView.setTag(holder);

        holder.sdnn.setText(decimalFormat.format(getParamByName("SDNN").getValue()));
        holder.sdnnUnit.setText(getParamByName("SDNN").getUnit());
        holder.sd1.setText(decimalFormat.format(getParamByName("SD1").getValue()));
        holder.sd1Unit.setText(getParamByName("SD1").getUnit());
        holder.sd2.setText(decimalFormat.format(getParamByName("SD2").getValue()));
        holder.sd2Unit.setText(getParamByName("SD2").getUnit());

        holder.sd1sd2.setText(decimalFormat.format(getParamByName("SD1SD2").getValue()));
        holder.sdsd.setText(decimalFormat.format(getParamByName("SDSD").getValue()));

        return convertView;
    }

    private HRVParameter getParamByName(String name) {
        for(HRVParameter param : presenter.getParameters()) {
            if(param.getName().equals(name)) {
                return param;
            }
        }

        return presenter.getParameters().get(0);
    }

    private static class RRResultViewHolder {
        private LineChartView rrData;
        private TextView min;
        private TextView max;
        private TextView average;
    }

    private static class StressViewHolder {
        private ColumnChartView histogram;
        private TextView stressIndex;
        private TextView range;
        private TextView mode;
    }

    private static class FrequencyViewHolder {
        private LineChartView frequencyChart;
        private TextView lf;
        private TextView lfUnit;
        private TextView hf;
        private TextView hfUnit;
        private TextView lfhf;
        private TextView lfhfUnit;
    }

    private static class SDViewHolder {
        private TextView sdnn;
        private TextView sdnnUnit;
        private TextView sdsd;
        private TextView sd1;
        private TextView sd1Unit;
        private TextView sd2;
        private TextView sd2Unit;
        private TextView sd1sd2;
    }

    private static class AdditionalInfoViewHolder {
        private TextView date;
        private TextView rating;
        private TextView category;
        private TextView comment;
        private ImageView categoryImg;
    }
}

