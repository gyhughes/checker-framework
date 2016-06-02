// Test case for issue #17:
// https://github.com/gyhughes/checker-framework/issues/17

// @skip-test until it is fixed

import org.checkerframework.checker.index.qual.*;

public class IndexOf {

  public static void removeWhitespaceBefore(String arg, String delimiter) {
    int delim_index = arg.indexOf(delimiter);
    @IndexOrLow("arg") int tmp1 = delim_index;
    if (delim_index > -1) {
      @IndexFor("arg") int tmp2 = delim_index;
    }
  }

}
