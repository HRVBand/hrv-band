package hrv.band.app;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.RenamingDelegatingContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import hrv.band.app.Control.HRVParameters;
import hrv.band.app.storage.SQLite.SQLController;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    private SQLController controller;
    private RenamingDelegatingContext context;

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
        String saveDir = context.getFilesDir() + "/test_test.sql";
        try {


            controller.exportDB("test.sql", context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller.deleteAllData(context);

        try {
            controller.importDB(saveDir, context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(controller.loadData(context, new Date(1000)).size() != 0);
    }
}
