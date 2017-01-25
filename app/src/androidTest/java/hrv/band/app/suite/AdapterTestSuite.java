package hrv.band.app.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import hrv.band.app.adapter.OverviewAdapterTest;
import hrv.band.app.adapter.AbstractStatisticAdapterTest;
import hrv.band.app.adapter.ValueAdapterTest;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 25.01.2017
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({OverviewAdapterTest.class,
        ValueAdapterTest.class, AbstractStatisticAdapterTest.StatisticAdapterTestSuite.class})

public class AdapterTestSuite { }
