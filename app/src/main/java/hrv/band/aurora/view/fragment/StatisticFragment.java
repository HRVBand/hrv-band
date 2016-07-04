package hrv.band.aurora.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hrv.band.aurora.Control.HRVParameters;
import hrv.band.aurora.R;
import hrv.band.aurora.view.adapter.HRVValue;
import hrv.band.aurora.view.adapter.StatisticValueAdapter;

/**
 * Created by Thomas on 27.06.2016.
 */
public class StatisticFragment extends Fragment {
    public static final String ARG_SECTION_VALUE = "sectionValue";
    public static final String ARG_HRV_VALUE = "hrvValue";
    public static final String ARG_DATE_VALUE = "dateValue";
    private StatisticValueAdapter adapter;
    private final String dateFormat = "dd.MMM yyyy";
    private View rootView;

    public StatisticFragment() {
    }

    public static StatisticFragment newInstance(HRVValue type, ArrayList<HRVParameters> parameters,
                                                Date date) {
        StatisticFragment fragment = new StatisticFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SECTION_VALUE, type);
        args.putParcelableArrayList(ARG_HRV_VALUE, parameters);
        args.putSerializable(ARG_DATE_VALUE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_statistic_fragment, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.stats_measure_history);

        List<HRVParameters> parameters = getArguments().getParcelableArrayList(ARG_HRV_VALUE);
        HRVValue hrvType = (HRVValue) getArguments().getSerializable(ARG_SECTION_VALUE);

        TextView date = (TextView) rootView.findViewById(R.id.stats_date);
        TextView desc = (TextView) rootView.findViewById(R.id.stats_value_desc);
        TextView type = (TextView) rootView.findViewById(R.id.stats_type);

        DateFormat df = new DateFormat();
        date.setText(df.format(dateFormat,
                ((Date) getArguments().getSerializable(ARG_DATE_VALUE)).getTime()));

        desc.setText(hrvType.toString());
        type.setText(hrvType.getUnit());


        adapter = new StatisticValueAdapter(getActivity().getApplicationContext(),
                R.layout.statistic_value_item,
                hrvType, parameters);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                //Intent intent = new Intent(getContext(), ValueDescriptionActivity.class);
                //startActivity(intent);
            }

        });
        return rootView;
    }

    private void setDate() {
        DateFormat df = new DateFormat();
        TextView date = (TextView) rootView.findViewById(R.id.stats_date);
        date.setText(df.format(dateFormat,
                ((Date) getArguments().getSerializable(ARG_DATE_VALUE)).getTime()));
    }

    public void updateValues(ArrayList<HRVParameters> parameters, Date date) {
        getArguments().putParcelableArrayList(ARG_HRV_VALUE, parameters);
        getArguments().putSerializable(ARG_DATE_VALUE, date);
        setDate();
        adapter.setDataset(parameters);
    }
}
