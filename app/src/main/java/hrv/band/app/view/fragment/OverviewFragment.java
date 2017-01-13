package hrv.band.app.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import hrv.band.app.R;
import hrv.band.app.view.StatisticActivity;
import hrv.band.app.view.adapter.AbstractValueAdapter;
import hrv.band.app.view.adapter.HRVValue;
import hrv.band.app.view.adapter.OverviewValueAdapter;

/**
 * Created by s_czogal on 23.06.2016.
 */

public class OverviewFragment extends Fragment {

    public static final String valueType = "VALUE_TYPE";

    public OverviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.content_overview, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.overview_value_list);
        final AbstractValueAdapter adapter = new OverviewValueAdapter(getActivity()
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                int count = adapter.getCount();
                if (count > position) {
                    Intent intent = new Intent(getContext(), StatisticActivity.class);
                    intent.putExtra(valueType, HRVValue.values()[position]);
                    startActivity(intent);
                } else if (position == count){
                    Snackbar.make(rootView, "More Values Coming Soon!", Snackbar.LENGTH_LONG).show();
                }
            }

        });

        View footerView =  View.inflate(getActivity(), R.layout.overview_add_items_footer, null);
        listView.addFooterView(footerView);

        return rootView;
    }
}
