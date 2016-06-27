// Test case for issue #26:
// https://github.com/gyhughes/checker-framework/issues/26

// @skip-test until the issue is resolved

import java.util.List;
import java.util.Arrays;
import org.checkerframework.checker.index.qual.*;

class Issue26 {

  void l2a(@MinLen(10) List<String> arg) {
    Object @MinLen(10) [] a1 = arg.toArray();
    String @MinLen(10) [] a2 = arg.toArray(new String[0]);
  }
    
  void a2l(String @MinLen(10) [] arg) {
    @MinLen(10) List<String> lst = Arrays.asList(arg);
  }
    
}
