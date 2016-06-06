// Test case for issue #21:
// https://github.com/gyhughes/checker-framework/issues/21


import org.checkerframework.checker.index.qual.MinLen;

public class NewArrayOfLengthOfMinLenArray {

  int /*@MinLen(1)*/ [] m(int /*@MinLen(1)*/ [] nums) {
    return new int[nums.length];
  }

}
