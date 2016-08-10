package hrv.band.aurora.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import hrv.band.aurora.Control.HRVParameters;
import hrv.band.aurora.R;
import hrv.band.aurora.view.StatisticValueActivity;
import hrv.band.aurora.view.MainActivity;
import hrv.band.aurora.view.ValueDescriptionActivity;
import hrv.band.aurora.view.adapter.AbstractValueAdapter;
import hrv.band.aurora.view.adapter.ValueAdapter;

/**
 * Created by Thomas on 10.08.2016.
 */
public class MeasureValueFragment extends Fragment {
    private HRVParameters parameter;

    public MeasureValueFragment() {
    }

    public static MeasureValueFragment newInstance(HRVParameters parameter) {
        MeasureValueFragment fragment = new MeasureValueFragment();
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.HRV_VALUE, parameter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_measure_values, container, false);

        parameter = getArguments().getParcelable(MainActivity.HRV_VALUE);

        ListView listview = (ListView) rootView.findViewById(R.id.hrv_value_list);

        AbstractValueAdapter adapter = new ValueAdapter(this.getActivity(),
                R.layout.measure_list_item, parameter);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(getContext(), ValueDescriptionActivity.class);
                intent.putExtra(MainActivity.HRV_VALUE_ID, position);
                startActivity(intent);
            }

        });

        return rootView;
    }
}
