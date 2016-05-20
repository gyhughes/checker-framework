import org.checkerframework.checker.index.qual.*;

class TypeRuleNonNegative {
	int[] arr = new int[5];
	
	int accessArray (@NonNegative int i) {
		//:: warning: (array.access.unsafe.high)
		return arr[i];
	}
}
