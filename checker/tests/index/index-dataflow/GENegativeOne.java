// Test case for issue #16:
// https://github.com/gyhughes/checker-framework/issues/15

// @skip-test suppress until the issue is resolved

import java.util.*;

public class GENegativeOne {

  public static void parse(String args) {

    for (int ii = 0; ii < args.length(); ii++) {

      // ii should be @IndexFor("args") here

      args.charAt(ii);

      // ii should be @IndexFor("args") here

      ii++;

      // ii should be @IndexOrHigh("args") here

      ii++;

      // Two options:
      //   (a)  ii is @NonNegative
      //   (b)  ii is ">= 1"

      ii--;

      // Two options:
      //   (a)  ii is ">= -1"
      //   (b)  ii is @NonNegative
    }
  }

}
