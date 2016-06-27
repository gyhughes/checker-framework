// Test case for issue #27:
// https://github.com/gyhughes/checker-framework/issues/27

// @skip-test until the issue is resolved

import java.util.List;
import java.util.Arrays;
import org.checkerframework.checker.index.qual.*;

class Issue26 {

  void pre1(String args) {
    int ii = 0;
    while ((ii < args.length()) && (args.charAt(ii) != 'A')) {
      //:: warning: (string.access.unsafe.high)
      args.charAt(++ii);
    }
  }

  void pre2(String args) {
    int ii = 0;
    while ((ii < args.length()) && (args.charAt(ii) != 'A')) {
      ii++;
      //:: warning: (string.access.unsafe.high)
      args.charAt(ii);
    }
  }

  void post1(String args) {
    int ii = 0;
    while ((ii < args.length()) && (args.charAt(ii) != 'A')) {
      args.charAt(ii++);
    }
  }

  void post2(String args) {
    int ii = 0;
    while ((ii < args.length()) && (args.charAt(ii) != 'A')) {
      args.charAt(ii);
      ii++;
    }
  }

}
