import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.MinLen;
import org.checkerframework.checker.index.qual.NonNegative;

public class ConstantIndexFor {

  void m(String @MinLen(10) [] arr) {
    for (int i=0; i<10; i++) {
      String dummy = arr[i];
    }
  }

  void m(String @MinLen(10) [] arr, @NonNegative int i) {
    if (i<10) {
      @IndexFor("arr") int j = i;
    }
  }

  void m2(String @MinLen(10) [] arr) {
    @IndexOrHigh("arr") int k = 10;
  }

}
