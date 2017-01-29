package suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import adapter.AbstractStatisticAdapterTest;
import adapter.OverviewAdapterTest;
import adapter.ValueAdapterTest;

/**
 * Created by Thomas on 28.01.2017.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({OverviewAdapterTest.class,
        ValueAdapterTest.class, AbstractStatisticAdapterTest.StatisticAdapterTestSuite.class})

public class AdapterTestSuite {

}
