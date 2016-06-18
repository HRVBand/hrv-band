package inno.hacks.ms.band.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import inno.hacks.ms.band.Control.HRVParameters;
import inno.hacks.ms.band.rrintervalExample.R;
import inno.hacks.ms.band.storage.IStorage;
import inno.hacks.ms.band.storage.SharedPreferencesController;

public class ChartViewActivity extends Activity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_view);

        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.hrv_values_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.chart_drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chart_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        MenuItem m = menu.findItem(R.id.action_websearch);
        m.setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_websearch:
                // create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putInt(ChartFragment.HRV_VALUE_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class ChartFragment extends Fragment {
        public static final String HRV_VALUE_NUMBER = "hrv_number";

        private static List<ILineDataSet> dataSets;

        public ChartFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.chart_view_fragment, container, false);

            int i = getArguments().getInt(HRV_VALUE_NUMBER);
            String hrvValue = getResources().getStringArray(R.array.hrv_values_array)[i];

            LineChart lineChart = (LineChart) rootView.findViewById(R.id.chart_view);
            IStorage storage = new SharedPreferencesController();
            List<HRVParameters> list = storage.loadData(getActivity(), null);
            dataSets = new ArrayList<>();
            dataSets.add(new LineDataSet(setDiagramLfhfValues(list), "lfhf"));
            dataSets.add(new LineDataSet(setDiagramRmssdValues(list), "rmsdd"));

            LineData data = new LineData(setLabels(list), dataSets.get(i));
            lineChart.setData(data); // set the data and list of lables into chart

            data.setDrawValues(true);


            getActivity().setTitle(hrvValue);
            return rootView;
        }

        private List<ILineDataSet> setDataSets(List<HRVParameters> list) {
            List<ILineDataSet> dataSets = new ArrayList<>();

            LineDataSet lfhfSet = new LineDataSet(setDiagramLfhfValues(list), "lfhf");
            lfhfSet.setLineWidth(2f);
            lfhfSet.setColor(Color.parseColor("#ff00f0"));

            LineDataSet rmssdSet = new LineDataSet(setDiagramRmssdValues(list), "rmssd");
            rmssdSet.setLineWidth(2f);
            rmssdSet.setColor(Color.parseColor("#03a9f4"));

            dataSets.add(lfhfSet);
            dataSets.add(rmssdSet);

            return dataSets;
        }

        private List<String> setLabels(List<HRVParameters> list) {
            ArrayList<String> labels = new ArrayList<String>();
            for (int i = 0; i < list.size(); i++) {
                Date time = list.get(i).getTime();
                labels.add(new SimpleDateFormat("HH:mm").format(time));
                //labels.add(String.valueOf(time.getHours()) + ":" + String.valueOf(time.getMinutes()));
            }
            return labels;
        }

        private List<Entry> setDiagramLfhfValues(List<HRVParameters> hrvParametersList) {
            ArrayList<Entry> entries = new ArrayList<>();
            for (int i = 0; i < hrvParametersList.size(); i++) {
                entries.add(new Entry((float) hrvParametersList.get(i).getLfhfRatio(), i));
            }
            return entries;
        }
        private List<Entry> setDiagramRmssdValues(List<HRVParameters> hrvParametersList) {
            ArrayList<Entry> entries = new ArrayList<>();
            for (int i = 0; i < hrvParametersList.size(); i++) {
                entries.add(new Entry((float) hrvParametersList.get(i).getRmssd(), i));
            }
            return entries;
        }
    }
}