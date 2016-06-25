package hrv.band.aurora.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import hrv.band.aurora.R;
import hrv.band.aurora.view.ValueDescriptionActivity;
import hrv.band.aurora.view.adapter.AbstractValueAdapter;
import hrv.band.aurora.view.adapter.OverviewValueAdapter;
import hrv.band.aurora.view.adapter.ValueAdapter;

/**
 * Created by s_czogal on 23.06.2016.
 */

public class OverviewFragment extends Fragment {


    public OverviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_overview, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.overview_value_list);
        AbstractValueAdapter adapter = new OverviewValueAdapter(getActivity(),
                R.layout.overview_list_item);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                //Intent intent = new Intent(getContext(), ValueDescriptionActivity.class);
                //startActivity(intent);
            }

        });
        return rootView;
    }

   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        GridView gridView = (GridView) getView().findViewById(R.id.overview_value_list);
        ValueAdapter adapter = new ValueAdapter(getActivity(),
                R.layout.measure_list_item);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                //Intent intent = new Intent(getContext(), ValueDescriptionActivity.class);
                //startActivity(intent);
            }

        });
    }*/
}
