package ud.group9.moviemanager.utils;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;

public class HashTest {
    // static test attributes
    private static String testString = "teststring";
    private static byte []expectedHash = {
            60, -121, 39, -32, 25, -92, 43, 68, 70, 103, -91,
            -121, -74, 0, 18, 81, -66, -54,-38, -69, -77, 107,
            -2, -40, 8, 122, -110, -63, -120, -126, -47, 17,
    };

    @Rule
    public ContiPerfRule i = new ContiPerfRule();
    @Test
    @PerfTest(invocations = 10_000, threads = 20)   // load test
                                                    // 10_000 times with 20 threads
    @Required(max = 100, average = 10)  // timed test
                                        // maximum time 100ms, avg 10ms
    public void testSha256Hash() {
        Assert.assertTrue("Mismatched hashes",
                Arrays.equals(expectedHash, Hash.sha256Hash(testString)));
    }
}
