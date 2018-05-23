package net.gmx.onebyte.ttnmapper;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class LogsTest {
    private static final String CSV = "id, time, nodeaddr, appeui, gwaddr, modulation, datarate, snr, rssi, freq, lat, lon, alt, accuracy, hdop, sats, provider, mqtt_topic, user_agent, user_id\n" +
            "7623176, 2018-05-20 13:04:11, node01, app01, C827E8301AA773F3, LORA, SF7BW125, -3.00, -116.00, 868.300, 16.418070, 3.008527, 695.1, null, 2.0, 11, sats, null, ttn_http_integration_v2, null\n" +
            "7623602, 2018-05-20 13:19:11, node01, app01, C827E8301AA773F3, LORA, SF7BW125, -3.50, -118.00, 868.500, 16.418099, 3.008455, 700.4, null, 0.9, 11, sats, null, ttn_http_integration_v2, null\n" +
            "7623617, 2018-05-20 13:19:47, node01, app01, C827E8301AA773F3, LORA, SF7BW125, -8.00, -120.00, 867.100, 16.418016, 3.008415, 699.2, null, 2.1, 11, sats, null, ttn_http_integration_v2, null\n" +
            "\n" +
            "Number of rows dumped: 3";

    @Test
    public void testParse() throws IOException {
        List<Log> logs = Logs.parse(CSV);
        assertEquals(3, logs.size());
        assertEquals(7623617, logs.get(2).getId());
        assertEquals(16.418016, logs.get(2).getLocation().getLatitude(), 1e-7);
    }
}
