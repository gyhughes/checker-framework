// Test case for issue #660: https://github.com/typetools/checker-framework/issues/660

// The test is skipped until the issue is fixed:
// @skip-test

import java.util.*;
import org.checkerframework.checker.tainting.qual.*;

public class TaintingDiamondInference {

  private @Untainted Set<@Untainted String> s;

  public TaintingDiamondInference() {
    s = new @Untainted TreeSet<>();
    s = new @Untainted TreeSet<@Untainted String>();
  }
}
