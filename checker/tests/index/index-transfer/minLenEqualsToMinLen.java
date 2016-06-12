import org.checkerframework.checker.index.qual.*;
import java.util.List;

class minLenEqualsToMinLen {
	void foo(int @MinLen(2)[] arr, int[] arr2, int @MinLen(1)[] arr3) {
		// get a minlen from an array if equals is true
		if (arr2.length == arr.length) {
			int m = arr2[1];
		}
		// get the higher minlen if equals is true
		if (arr3.length == arr.length) {
			// arr3 now has minlen(2)
			int k = arr3[1];
		}
	}
}
