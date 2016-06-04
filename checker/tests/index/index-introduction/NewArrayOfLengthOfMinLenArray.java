// Test case for issue #20:
// https://github.com/gyhughes/checker-framework/issues/20

// @skip-test until it is fixed

import org.checkerframework.checker.index.qual.MinLen;

public class NewArrayOfLengthOfMinLenArray {

  int /*@MinLen(1)*/ [] m(int /*@MinLen(1)*/ [] nums) {
    return new int[nums.length];
  }

}
    
