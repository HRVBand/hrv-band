package hrv.band.aurora;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.RenamingDelegatingContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import hrv.band.aurora.Control.HRVParameters;
import hrv.band.aurora.storage.SQLite.SQLController;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    SQLController controller;
    RenamingDelegatingContext context;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        context = new RenamingDelegatingContext(getContext(), "test_");
        controller = new SQLController();

    }

    public void testInserts()
    {
        ArrayList<HRVParameters> listOfParams = new ArrayList<>();
        listOfParams.add(new HRVParameters(new Date(1000), 0,0,0,0,0,0,0,0, new ArrayList<Double>()));

        controller.saveData(context, listOfParams.get(0));
        controller.saveData(context, listOfParams);

        HRVParameters param = controller.loadData(context, new Date(1000)).get(0);

        assertTrue(param.getTime().getTime() == 1000);
    }

    public void testDeleteAll() {
        ArrayList<HRVParameters> listOfParams = new ArrayList<>();
        listOfParams.add(new HRVParameters(new Date(1000), 0,0,0,0,0,0,0,0, new ArrayList<Double>()));

        controller.saveData(context, listOfParams.get(0));
        controller.deleteAllData(context);

        assertTrue(controller.loadData(context, new Date(1000)).size() == 0);
    }

    public void testImportExport() {
        ArrayList<HRVParameters> listOfParams = new ArrayList<>();
        listOfParams.add(new HRVParameters(new Date(1000), 0,0,0,0,0,0,0,0, new ArrayList<Double>()));

        controller.saveData(context, listOfParams.get(0));
        try {
            controller.exportDB("test.sql", context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller.deleteAllData(context);

        try {
            controller.importDB("test.sql", context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(controller.loadData(context, new Date(1000)).size() != 0);
    }
}
