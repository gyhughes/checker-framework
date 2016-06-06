import org.checkerframework.checker.index.qual.*;

class ArrLength1 {
	void foo(int @MinLen(1)[] arr) {
		int m = arr[arr.length -1];
	}
}
