package hrv.band.app.storage.SampleDataCreation;

import java.util.ArrayList;

import hrv.band.app.Control.HRVParameters;

/**
 * Created by Julian on 25.06.2016.
 */
public interface ISampleDataFactory {

    ArrayList<HRVParameters> create(int sampleSize);
}
