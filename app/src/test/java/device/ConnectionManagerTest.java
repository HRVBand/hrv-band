package device;

import android.app.Activity;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import hrv.band.app.device.ConnectionManager;
import hrv.band.app.device.Device;
import hrv.band.app.device.antplus.AntPlusRRDataDevice;
import hrv.band.app.device.msband.MSBandRRIntervalDevice;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by Thomas on 29.04.2017.
 */
@RunWith(RobolectricTestRunner.class)
public class ConnectionManagerTest {

    private ConnectionManager connectionManager;

    @Mock
    private Activity activity;

    @Mock
    private SharedPreferences sharedPreferences;
    @Mock
    private SharedPreferences.Editor editor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(activity.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.putInt(anyString(), anyInt())).thenReturn(editor);

        connectionManager = new ConnectionManager(activity);
    }

    @Test
    public void connectToMsBandTest() {
        when(sharedPreferences.getInt(anyString(), anyInt())).thenReturn(Device.MSBAND.ordinal());

        connectionManager.connectToMSBand();
        assertTrue(connectionManager.getDevice() instanceof MSBandRRIntervalDevice);
    }
    @Test
    public void connectToAntTest() {
        when(sharedPreferences.getInt(anyString(), anyInt())).thenReturn(Device.ANT.ordinal());

        connectionManager.connectToAnt();
        assertTrue(connectionManager.getDevice() instanceof AntPlusRRDataDevice);
    }

    @Test
    public void disconnectTest() {
        when(sharedPreferences.getInt(anyString(), anyInt())).thenReturn(Device.NONE.ordinal());

        connectionManager.disconnectDevices(connectionManager.connectToMSBand());
        assertNull(connectionManager.getDevice());
    }
}
