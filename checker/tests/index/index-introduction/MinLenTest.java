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
	 
	void arr(int @MinLen(2) [] arr) {
		int m = arr[1];
	}
	 void list(@MinLen(2)List<Object> lst, List<Object> l) {
		 Object o = lst.get(0);
		 if (lst.size() > 4) {
			 o = lst.get(4);
		 }
		 if (lst.size() == 4) {
			 o = lst.get(3);
		 }
		 if (l.size() != 0) {
			 o = l.get(0);
		 }
	 }
	 
	 void String(@MinLen(2) String str, String s) {
		 char c = str.charAt(0);
		 if (str.length() > 4) {
			 c = str.charAt(4);
		 }
		 if (str.length() == 4) {
			 c = str.charAt(3);
		 }
		 if (s.length() != 0) {
			 c = s.charAt(0);
		 }
	 }
	 void String2(String s) {
		 if (s.length() != 0) {
			 char c = s.charAt(0);
		 }
	 }
}
