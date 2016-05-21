import org.checkerframework.checker.index.qual.*;

class TypeRuleLTLength {
	int[] arr = new int[5];
	
	int accessArray (@LTLength("arr") int i) {
		//:: warning: (array.access.unsafe.low)
		return arr[i];
	}
}
