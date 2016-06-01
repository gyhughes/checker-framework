import org.checkerframework.checker.index.qual.*;

class MinLenTest {
	
	void foo() {
		int @MinLen(124)[] arr = new int[124];
		int k = arr[100];
	}
//	 void foo2() {
//		 int @MinLen(1)[] arr = new int[0];
//		 if (arr.length > 4) {
//			 int m = arr[1];
//		 }
//	 }
}