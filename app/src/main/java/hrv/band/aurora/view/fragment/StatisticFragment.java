package hrv.band.aurora.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import hrv.band.aurora.R;
import hrv.band.aurora.view.adapter.StatisticValueAdapter;

/**
 * Created by Thomas on 27.06.2016.
 */
public class StatisticFragment extends Fragment {

    public StatisticFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_statistic_fragment, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.stats_measure_history);

        String type = getActivity().getIntent().getStringExtra(OverviewFragment.valueType);

        StatisticValueAdapter adapter = new StatisticValueAdapter(getActivity(),
                R.layout.statistic_value_item, type);
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
}
