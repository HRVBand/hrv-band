package presenter;

import android.app.Activity;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import hrv.band.app.model.Measurement;
import hrv.band.app.model.storage.IStorage;
import hrv.band.app.model.storage.sqlite.HRVSQLController;
import hrv.band.app.ui.presenter.HistoryPresenter;
import hrv.band.app.ui.presenter.IHistoryPresenter;

import static junit.framework.Assert.assertFalse;

/**
 * Created by Thomas on 22.04.2017.
 */

public class HistoryPresenterTest {
    private IHistoryPresenter presenter;
    @Mock
    private Activity activity;
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new HistoryPresenter(activity);
    }
    @Test @Ignore
    public void test() {
        IStorage storage = new HRVSQLController();
        storage.saveData(activity, new Measurement.MeasurementBuilder(new Date(1000), new double[]{1, 1, 1, 1, 1}).build());
        storage.saveData(activity, new Measurement.MeasurementBuilder(new Date(1000), new double[]{1, 1, 1, 1, 1}).build());
        storage.saveData(activity, new Measurement.MeasurementBuilder(new Date(1000), new double[]{1, 1, 1, 1, 1}).build());
        assertFalse(presenter.getMeasurements(new Date(1000)).isEmpty());

    }
}
