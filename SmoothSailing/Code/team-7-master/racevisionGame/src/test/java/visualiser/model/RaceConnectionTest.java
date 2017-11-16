package visualiser.model;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by connortaylorbrown on 3/05/17.
 */
public class RaceConnectionTest {
    private RaceConnection onlineConnection;
    private RaceConnection offlineConnection;

    @Before
    public void setUp() {
        onlineConnection = new RaceConnection("livedata.americascup.com", 4941, null);
        offlineConnection = new RaceConnection("localhost", 4942, null);
    }

    /**
     * Host is not online for CI, ignore when committing.
     */
    @Test
    @Ignore
    public void onlineConnectionStatusReady() {
        assertTrue(onlineConnection.check());
    }


//    @Test
//    public void offlineConnectionStatusOffline() {
//        assertFalse(offlineConnection.check());
//    }
}
