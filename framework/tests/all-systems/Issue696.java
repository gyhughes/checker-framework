// Testcase for Issue 696
// https://github.com/typetools/checker-framework/issues/696
// @skip-test

import java.util.List;
import java.util.Map.Entry;
import java.util.Map;

public class Issue696 {
    public static void test(final List<? extends Map.Entry<byte[], byte[]>> input) {
    }
}
