import org.checkerframework.checker.index.qual.*;
import java.util.List;

class MinLenTest {
	
	void foo() {
		int[] arr = new int[124];
		int k = arr[100];
	}
	 void foo2(int[] param) {
		 int[] arr = param;
		 if (arr.length > 4) {
			 int m = arr[1];
		 }
	 }
	 void foo3(int[] param) {
		 int[] arr = param;
		 if (arr.length != 0) {
			int m = arr[0];
		 }
	 }
	 void foo4(int[] param) {
		 int[] arr = param;
		 if (arr.length == 0) {
			 //:: warning:(array.access.unsafe.literal)
			int m = arr[0];
		 }
		 else {
				int m = arr[0];
		 }
	 }
	 void foo4() {
		 int[] arr = new int[] {1,2,3};
		 int m = arr[1];
	 }
	 
	 void list(@MinLen(2) List<Object> lst) {
		 Object o = lst.get(0);
	 }
	 
	 void String(@MinLen(2) String str) {
		 char c = str.charAt(0);
	 }
}
