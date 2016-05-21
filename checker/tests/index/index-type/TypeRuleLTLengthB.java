import org.checkerframework.checker.index.qual.*;

class TypeRuleLTLengthB {
	int[] arr = new int[5];
	int[] arrB = new int[5];
	
	int accessArray (@LTLength("arrB") int i) {
		//:: warning: (array.access.unsafe.low)
		return arr[i];
	}
}
