import org.checkerframework.checker.index.qual.*;

class MinLenTest {
	
	void foo() {
		@MinLen(124) int[] arr = new int[124];
		int k = arr[100];
	}
}