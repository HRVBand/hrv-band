package hrv.band.aurora.storage.SampleDataCreation;

import java.util.ArrayList;

import hrv.band.aurora.Control.HRVParameters;

/**
 * Created by Julian on 25.06.2016.
 */
public interface ISampleDataFactory {

    ArrayList<HRVParameters> create(int sampleSize);
}
